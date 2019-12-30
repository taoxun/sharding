package com.youyouxunyin.interceptor.parser;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.springframework.util.StringUtils;

import java.util.*;

public class CountSqlParser {


    public static final String KEEP_ORDERBY = "/*keep orderby*/";
    private static final Alias TABLE_ALIAS = new Alias("table_count");
    private final Set<String> skipFunctions = Collections.synchronizedSet(new HashSet());
    private final Set<String> falseFunctions = Collections.synchronizedSet(new HashSet());
    private static final Set<String> AGGREGATE_FUNCTIONS = new HashSet(Arrays.asList("APPROX_COUNT_DISTINCT,ARRAY_AGG,AVG,BIT_,BOOL_,CHECKSUM_AGG,COLLECT,CORR,COUNT,COVAR,CUME_DIST,DENSE_RANK,EVERY,FIRST,GROUP,JSON_,LAST,LISTAGG,MAX,MEDIAN,MIN,PERCENT_,RANK,REGR_,SELECTIVITY,STATS_,STD,STRING_AGG,SUM,SYS_OP_ZONE_ID,SYS_XMLAGG,VAR,XMLAGG".split(",")));

    public CountSqlParser() {
    }

    public static void addAggregateFunctions(String functions) {

        if (!StringUtils.isEmpty(functions)) {
            String[] funs = functions.split(",");

            for(int i = 0; i < funs.length; ++i) {
                AGGREGATE_FUNCTIONS.add(funs[i].toUpperCase());
            }
        }

    }

    public String getSmartCountSql(String sql) {
        return this.getSmartCountSql(sql, "0");
    }

    public String getSmartCountSql(String sql, String name) {
        Statement stmt = null;
        if (sql.indexOf("/*keep orderby*/") >= 0) {
            return this.getSimpleCountSql(sql, name);
        } else {
            try {
                stmt = CCJSqlParserUtil.parse(sql);
            } catch (Throwable var8) {
                return this.getSimpleCountSql(sql, name);
            }

            Select select = (Select)stmt;
            SelectBody selectBody = select.getSelectBody();

            try {
                this.processSelectBody(selectBody);
            } catch (Exception var7) {
                return this.getSimpleCountSql(sql, name);
            }

            this.processWithItemsList(select.getWithItemsList());
            this.sqlToCount(select, name);
            String result = select.toString();
            return result;
        }
    }

    public String getSimpleCountSql(String sql) {
        return this.getSimpleCountSql(sql, "0");
    }

    public String getSimpleCountSql(String sql, String name) {
        StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
        stringBuilder.append("select count(");
        stringBuilder.append(name);
        stringBuilder.append(") from (");
        stringBuilder.append(sql);
        stringBuilder.append(") tmp_count");
        return stringBuilder.toString();
    }

    public void sqlToCount(Select select, String name) {
        SelectBody selectBody = select.getSelectBody();
        List<SelectItem> COUNT_ITEM = new ArrayList();
        COUNT_ITEM.add(new SelectExpressionItem(new Column("count(" + name + ")")));
        if (selectBody instanceof PlainSelect && this.isSimpleCount((PlainSelect)selectBody)) {
            ((PlainSelect)selectBody).setSelectItems(COUNT_ITEM);
        } else {
            PlainSelect plainSelect = new PlainSelect();
            SubSelect subSelect = new SubSelect();
            subSelect.setSelectBody(selectBody);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(COUNT_ITEM);
            select.setSelectBody(plainSelect);
        }

    }

    public boolean isSimpleCount(PlainSelect select) {
        if (select.getGroupBy() != null) {
            return false;
        } else if (select.getDistinct() != null) {
            return false;
        } else {
            Iterator var2 = select.getSelectItems().iterator();

            while(true) {
                String NAME;
                do {
                    String name;
                    do {
                        Expression expression;
                        do {
                            SelectItem item;
                            do {
                                if (!var2.hasNext()) {
                                    return true;
                                }

                                item = (SelectItem)var2.next();
                                if (item.toString().contains("?")) {
                                    return false;
                                }
                            } while(!(item instanceof SelectExpressionItem));

                            expression = ((SelectExpressionItem)item).getExpression();
                        } while(!(expression instanceof Function));

                        name = ((Function)expression).getName();
                    } while(name == null);

                    NAME = name.toUpperCase();
                } while(this.skipFunctions.contains(NAME));

                if (this.falseFunctions.contains(NAME)) {
                    return false;
                }

                Iterator var7 = AGGREGATE_FUNCTIONS.iterator();

                while(var7.hasNext()) {
                    String aggregateFunction = (String)var7.next();
                    if (NAME.startsWith(aggregateFunction)) {
                        this.falseFunctions.add(NAME);
                        return false;
                    }
                }

                this.skipFunctions.add(NAME);
            }
        }
    }

    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            this.processPlainSelect((PlainSelect)selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem)selectBody;
            if (withItem.getSelectBody() != null) {
                this.processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList)selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                List<SelectBody> plainSelects = operationList.getSelects();
                Iterator var4 = plainSelects.iterator();

                while(var4.hasNext()) {
                    SelectBody plainSelect = (SelectBody)var4.next();
                    this.processSelectBody(plainSelect);
                }
            }

            if (!this.orderByHashParameters(operationList.getOrderByElements())) {
                operationList.setOrderByElements((List)null);
            }
        }

    }

    public void processPlainSelect(PlainSelect plainSelect) {
        if (!this.orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements((List)null);
        }

        if (plainSelect.getFromItem() != null) {
            this.processFromItem(plainSelect.getFromItem());
        }

        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            Iterator var3 = joins.iterator();

            while(var3.hasNext()) {
                Join join = (Join)var3.next();
                if (join.getRightItem() != null) {
                    this.processFromItem(join.getRightItem());
                }
            }
        }

    }

    public void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList != null && withItemsList.size() > 0) {
            Iterator var2 = withItemsList.iterator();

            while(var2.hasNext()) {
                WithItem item = (WithItem)var2.next();
                this.processSelectBody(item.getSelectBody());
            }
        }

    }

    public void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
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
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect)fromItem;
            if (subSelect.getSelectBody() != null) {
                this.processSelectBody(subSelect.getSelectBody());
            }
        } else if (!(fromItem instanceof ValuesList) && fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect)fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    this.processSelectBody(subSelect.getSelectBody());
                }
            }
        }

    }

    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        } else {
            Iterator var2 = orderByElements.iterator();

            OrderByElement orderByElement;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                orderByElement = (OrderByElement)var2.next();
            } while(!orderByElement.toString().contains("?"));

            return true;
        }
    }

    static {
        TABLE_ALIAS.setUseAs(false);
    }
}
