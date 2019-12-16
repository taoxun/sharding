package com.youyouxunyin.interceptor;


import com.youyouxunyin.algorithm.Algorithm;
import com.youyouxunyin.algorithm.AlgorithmHandler;
import com.youyouxunyin.util.ComparatorUtil;
import com.youyouxunyin.util.ShardingContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class SelectSqlParser {

    @Autowired
    AlgorithmHandler handler;

    public void processSelect(Statement statement,int index) throws NoSuchFieldException, IllegalAccessException {
        Select select = (Select) statement;
        SelectBody body = select.getSelectBody();
        processSelectBody(body,index);
    }

    public void sort(Statement statement,List list){

        String order = null;
        boolean asc = true;

        Select select = (Select) statement;
        SelectBody body = select.getSelectBody();
        if (body instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) body;
            List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
            if (orderByElements!=null && orderByElements.size()>0){
                Expression expression = orderByElements.get(0).getExpression();
                if (expression instanceof Column){
                    order = ((Column) expression).getColumnName();
                }
                asc = orderByElements.get(0).isAsc();
            }
        }
        Collections.sort(list,new ComparatorUtil(order,asc));
    }

    public void processSelectBody(SelectBody selectBody,int index) throws NoSuchFieldException, IllegalAccessException {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect)selectBody,index);
        }else if (selectBody instanceof SetOperationList){
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                List<SelectBody> plainSelects = operationList.getSelects();
                Iterator var4 = plainSelects.iterator();
                while(var4.hasNext()) {
                    SelectBody plainSelect = (SelectBody)var4.next();
                    this.processSelectBody(plainSelect,index);
                }
            }
        }
    }
    public void processPlainSelect(PlainSelect plainSelect,int index) throws NoSuchFieldException, IllegalAccessException {
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem(),index);
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join:joins) {
                if (join.getRightItem() != null) {
                    this.processFromItem(join.getRightItem(),index);
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
                       processSelectBody(body,index);
                   }
               }
            }
        }
    }

    public void processFromItem(FromItem fromItem,int index) throws NoSuchFieldException, IllegalAccessException {
        if (fromItem instanceof Table){
            Table table = (Table) fromItem;
            if (table!=null){
                modifyTable(table,index);
            }
        }else if (fromItem instanceof SubSelect){
            SubSelect subSelect = (SubSelect) fromItem;
            SelectBody selectBody = subSelect.getSelectBody();
            if (selectBody!=null){
                processSelectBody(selectBody,index);
            }
        }else if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin)fromItem;
            if (subJoin.getJoinList() != null && subJoin.getJoinList().size() > 0) {
                Iterator var3 = subJoin.getJoinList().iterator();
                while(var3.hasNext()) {
                    Join join = (Join)var3.next();
                    if (join.getRightItem() != null) {
                        this.processFromItem(join.getRightItem(),index);
                    }
                }
            }
            if (subJoin.getLeft() != null) {
                this.processFromItem(subJoin.getLeft(),index);
            }
        }
    }

    private void modifyTable(Table table,int index) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> map = ShardingContext.get();

        String tableName = (String) map.get("tableName");
        Object value = map.get("value");
        String mode = (String) map.get("mode");
        int length = (int) map.get("length");

        Field f = table.getClass().getDeclaredField("partItems");
        f.setAccessible(true);
        if (List.class.isAssignableFrom(f.getType())){
            List list = (List) f.get(table);
            for (int i = 0; i< list.size(); i++){
                if (list.get(i).equals(tableName)){
                    if (index==-1){
                        list.set(i,handler.handler(mode,tableName,value,length));
                    }else{
                        list.set(i,tableName+"_"+index);
                    }
                }
            }
        }
    }
}
