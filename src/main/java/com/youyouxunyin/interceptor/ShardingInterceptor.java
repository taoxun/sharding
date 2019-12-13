package com.youyouxunyin.interceptor;

import com.youyouxunyin.algorithm.Algorithm;
import com.youyouxunyin.algorithm.DateAlgorithm;
import com.youyouxunyin.algorithm.HashAlgorithm;
import com.youyouxunyin.annotations.Sharding;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {
        Connection.class,Integer.class})})
public class ShardingInterceptor implements Interceptor {

    private Map<String, Algorithm> algorithm = new ConcurrentHashMap();
    SelectSqlParser selectSqlParser = new SelectSqlParser();

    @PostConstruct
    public void init(){
        algorithm.put("date",new DateAlgorithm());
        algorithm.put("hash",new HashAlgorithm());
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statement = (StatementHandler) invocation.getTarget();
        Field delegate_field = getField(statement, "delegate");
        StatementHandler prepared = (StatementHandler)delegate_field.get(statement);
        Field ms_field = getField(prepared, "mappedStatement");
        MappedStatement ms = (MappedStatement) ms_field.get(prepared);

        BoundSql boundSql = prepared.getBoundSql();
        Object parameter = boundSql.getParameterObject();
        String command = ms.getSqlCommandType().name();

        Sharding annotation = AnnotationUtils.findAnnotation(parameter.getClass(), Sharding.class);
        if (annotation!=null){
            before(prepared,ms,boundSql,annotation,command,parameter);
        }else {
            if (parameter instanceof Map){
                Object obj = ((Map) parameter).get("sharding");
                if (obj!=null){
                    Sharding sharding = AnnotationUtils.findAnnotation(obj.getClass(), Sharding.class);
                    if (sharding!=null){
                        before(prepared,ms,boundSql,sharding,command,parameter);
                    }
                }
            }
        }
        return invocation.proceed();
    }

    private void before(StatementHandler prepared ,MappedStatement ms,BoundSql boundSql, Sharding annotation, String command, Object parameter) throws JSQLParserException, IllegalAccessException, NoSuchFieldException {
        String tableName = annotation.tableName();
        String field = annotation.field();
        String mode = annotation.mode();
        String sql = boundSql.getSql();
        Statement statement = CCJSqlParserUtil.parse(sql);
        Object value = this.getFieldValue(parameter,field);
        Algorithm algorithm = this.algorithm.get(mode);

        if (StringUtils.isEmpty(value)){
            if (!SqlCommandType.SELECT.name().equals(command)){
                return;
            }
        }
        if (SqlCommandType.INSERT.name().equals(command)){
            Insert insert = (Insert) statement;
            Table table = insert.getTable();
            String newName = algorithm.doSharding(table.getName(),value);
            table.setName(newName);
            modifySql(boundSql,insert.toString());
        }else if (SqlCommandType.UPDATE.name().equals(command)){
            Update update = (Update) statement;
            List<Table> tables = update.getTables();
            int index = -1;
            for (int i=0;i<tables.size();i++){
                String name = tables.get(i).getName();
                if (tableName.equals(name)){
                    index = i;
                    break;
                }
            }
            if (index>=0){
                String newName = algorithm.doSharding(tables.get(index).getName(),value);
                tables.get(index).setName(newName);
                modifySql(boundSql,update.toString());
            }
        }else if(SqlCommandType.SELECT.name().equals(command)){

            Map<String,Object> map = new HashMap<>();
            map.put("tableName",tableName);
            map.put("algorithm",algorithm);
            map.put("value",value);
            ShardingContext.set(map);

            if (value==null || value.toString().length()==1){
                if ("hash".equals(mode)){
                    noKey(prepared,ms,boundSql,statement,tableName,parameter);
                }
            }else {
                selectSqlParser.processSelect(statement,false);
                modifySql(boundSql,statement.toString());
            }
            ShardingContext.remove();
        }
    }

    private void noKey(StatementHandler prepared ,MappedStatement ms,BoundSql boundSql, Statement statement,String tableName,Object parameter) throws IllegalAccessException {
        String old_sql = statement.toString();
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<16;i++){
            builder.append(old_sql.replaceAll(tableName,tableName+"_"+i));
            if (i!=16-1){
                builder.append(" union all ");
            }
        }
        List<ParameterMapping> mappings = boundSql.getParameterMappings();
        for (int i = 0;i<16-1;i++){
            mappings.add(mappings.get(0));
        }
        BoundSql countBoundSql = new BoundSql(ms.getConfiguration(), builder.toString(), boundSql.getParameterMappings(), parameter);
        Field boundsql_field = getField(prepared, "boundSql");
        boundsql_field.setAccessible(true);
        boundsql_field.set(prepared,countBoundSql);
    }

    private void modifySql(BoundSql boundSql, String sql) throws IllegalAccessException {
        Field sql_field = getField(boundSql, "sql");
        sql_field.setAccessible(true);
        sql_field.set(boundSql,sql);
    }

    private Field getField(Object obj, String name) {
        Field field = ReflectionUtils.findField(obj.getClass(), name);
        field.setAccessible(true);
        return field;
    }

    private Object getFieldValue(Object object,String key) throws IllegalAccessException {
        if (object instanceof Map){
            Object o = ((Map) object).get(key);
            if (o!=null){
                return o;
            }else {
                Object sharding = ((Map) object).get("sharding");
                return getFieldValue(sharding,key);
            }
        }
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f:fields) {
            f.setAccessible(true);
            if (f.getName().equals(key)){
                return f.get(object);
            }
        }
        return null;
    }
}
