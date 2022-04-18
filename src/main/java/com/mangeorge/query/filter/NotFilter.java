package com.mangeorge.query.filter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;


/**
 * Inverts the child filter.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class NotFilter extends QueryFilterImpl {

    protected final QueryFilter filter;


    public NotFilter(QueryFilter filter) {
        this.filter = filter;
    }

    @Override
    public String getHQLExpression() {
        return "not (" + filter.getHQLExpression() + ")";
    }

    @Override
    public void setHQLParams(Query<?> query) {
        filter.setHQLParams(query);
    }

    @Override
    public void setParameterName(String parameterName) {
        filter.setParameterName(parameterName);
    }

    @Override
    public Criterion getCriteriaExpression() {
        return Restrictions.not(filter.getCriteriaExpression());
    }

    @Override
    public void setAlias(String alias) {
        filter.setAlias(alias);
    }

    @Override
    public String getSolrExpression(boolean withTag) {
        if (StringUtils.isEmpty(filter.getSolrExpression(false))) {
            return "";
        }
        StringBuilder expressionBuilder = new StringBuilder();
        if (withTag) {
            expressionBuilder.append("{!tag=").append(getSolrQueryTag()).append("}");
        }

        String solrExpression = filter.getSolrExpression(false);
        if (!solrExpression.startsWith("-")){
            expressionBuilder.append("-(").append(solrExpression).append(")");
        } else {
            expressionBuilder.append(solrExpression, 2, solrExpression.length()-1);
        }

        return expressionBuilder.toString();
    }

}