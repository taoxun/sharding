package com.youyouxunyin.interceptor.sharding;

import com.youyouxunyin.algorithm.AlgorithmHandler;
import com.youyouxunyin.annotations.Sharding;
import com.youyouxunyin.util.ReflectionUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;

@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {
        Connection.class,Integer.class})})
public class UpdateInterceptor implements Interceptor {

    @Autowired
    AlgorithmHandler handler;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statement = (StatementHandler) invocation.getTarget();
        Field delegate_field = ReflectionUtil.getField(statement, "delegate");
        StatementHandler prepared = (StatementHandler)delegate_field.get(statement);
        Field ms_field = ReflectionUtil.getField(prepared, "mappedStatement");
        MappedStatement ms = (MappedStatement) ms_field.get(prepared);

        BoundSql boundSql = prepared.getBoundSql();
        Object parameter = boundSql.getParameterObject();
        String command = ms.getSqlCommandType().name();

        Sharding sharding = parameter==null?null:AnnotationUtils.findAnnotation(parameter.getClass(), Sharding.class);
        if (sharding!=null){
            this.rewrite(boundSql,sharding,command,parameter);
        }
        return invocation.proceed();
    }

    private void rewrite(BoundSql boundSql, Sharding annotation, String command, Object parameter) throws IllegalAccessException, JSQLParserException {
        String tableName = annotation.tableName();
        String field = annotation.field();
        String mode = annotation.mode();
        int length = annotation.length();
        String sql = boundSql.getSql();
        Statement statement = CCJSqlParserUtil.parse(sql);
        Object value = ReflectionUtil.getFieldValue(parameter,field);

        if (StringUtils.isEmpty(value)){
            return;
        }
        if (SqlCommandType.INSERT.name().equals(command)){
            Insert insert = (Insert) statement;
            Table table = insert.getTable();
            String newName = this.handler.handler(mode, table.getName(), value,length);
            table.setName(newName);
            ReflectionUtil.setField(boundSql,"sql",insert.toString());
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
                String newName = handler.handler(mode,tables.get(index).getName(),value,length);
                tables.get(index).setName(newName);
                ReflectionUtil.setField(boundSql,"sql",update.toString());
            }
        }
    }
}
