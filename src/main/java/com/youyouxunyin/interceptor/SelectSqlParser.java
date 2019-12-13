package com.youyouxunyin.interceptor;


import com.youyouxunyin.algorithm.Algorithm;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SelectSqlParser {

    public void processSelect(Statement statement,boolean noKey) throws NoSuchFieldException, IllegalAccessException {
        Select select = (Select) statement;
        SelectBody body = select.getSelectBody();
        processSelectBody(body);

        System.out.println(statement);
    }

    public void processSelectBody(SelectBody selectBody) throws NoSuchFieldException, IllegalAccessException {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect)selectBody);
        }else if (selectBody instanceof SetOperationList){
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                List<SelectBody> plainSelects = operationList.getSelects();
                Iterator var4 = plainSelects.iterator();
                while(var4.hasNext()) {
                    SelectBody plainSelect = (SelectBody)var4.next();
                    this.processSelectBody(plainSelect);
                }
            }
        }
    }
    public void processPlainSelect(PlainSelect plainSelect) throws NoSuchFieldException, IllegalAccessException {
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join:joins) {
                if (join.getRightItem() != null) {
                    this.processFromItem(join.getRightItem());
                }
            }
        }
        if (plainSelect.getSelectItems()!=null && plainSelect.getSelectItems().size()>0){
            List<SelectItem> items = plainSelect.getSelectItems();
            for (SelectItem item:items) {
               if (item instanceof SelectExpressionItem){
                   SelectExpressionItem expressionItem = (SelectExpressionItem) item;
                   Expression expression = expressionItem.getExpression();
                   if (expression instanceof SubSelect){
                       SelectBody body = ((SubSelect) expression).getSelectBody();
                       processSelectBody(body);
                   }
               }
            }
        }
    }

    public void processFromItem(FromItem fromItem) throws NoSuchFieldException, IllegalAccessException {
        if (fromItem instanceof Table){
            Table table = (Table) fromItem;
            if (table!=null){
                modifyTable(table);
            }
        }else if (fromItem instanceof SubSelect){
            SubSelect subSelect = (SubSelect) fromItem;
            SelectBody selectBody = subSelect.getSelectBody();
            if (selectBody!=null){
                processSelectBody(selectBody);
            }
        }else if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin)fromItem;
            if (subJoin.getJoinList() != null && subJoin.getJoinList().size() > 0) {
                Iterator var3 = subJoin.getJoinList().iterator();
                while(var3.hasNext()) {
                    Join join = (Join)var3.next();
                    if (join.getRightItem() != null) {
                        this.processFromItem(join.getRightItem());
                    }
                }
            }
            if (subJoin.getLeft() != null) {
                this.processFromItem(subJoin.getLeft());
            }
        }
    }

    private void modifyTable(Table table) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> map = ShardingContext.get();
        String tableName = (String) map.get("tableName");
        Algorithm algorithm = (Algorithm) map.get("algorithm");
        Object value = map.get("value");

        Field f = table.getClass().getDeclaredField("partItems");
        f.setAccessible(true);
        if (List.class.isAssignableFrom(f.getType())){
            List list = (List) f.get(table);
            for (int i = 0; i< list.size(); i++){
                if (list.get(i).equals(tableName)){
                    list.set(i,algorithm.doSharding(tableName,value));
                }
            }
        }
    }
}
