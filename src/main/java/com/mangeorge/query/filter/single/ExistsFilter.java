package com.mangeorge.query.filter.single;


import com.mangeorge.query.QueryBuilder;
import org.hibernate.query.Query;


/**
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings("unused")
public class ExistsFilter extends SingleFieldFilterImpl<Object> {

    private final QueryBuilder queryBuilder;


    public ExistsFilter(QueryBuilder queryBuilder) {
        super(null);
        this.queryBuilder = queryBuilder;
    }


    @Override
    public void setParameterName(String parameterName) {
        queryBuilder.getQueryFilter().setParameterName(parameterName);
    }

    @Override
    public void setAlias(String alias) {
       queryBuilder.addAliasPrefix(alias);
    }

    @Override
    public String getHQLExpression() {
        return "exists (" + queryBuilder.getHQLQueryWithoutOrderBy() + ")";
    }

    @Override
    public void setHQLParams(Query<?> query) {
        queryBuilder.getQueryFilter().setHQLParams(query);
    }

    @Override
    protected String getSolrExpression() {
        throw new RuntimeException("Solr not support exists filter");
    }
}
