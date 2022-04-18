package com.mangeorge.query.filter;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;


/**
 * Basic implementation of the filter.
 * Does not support criteria-based queries.
 *
 * @author George Beliy on 10-01-2020
 */
public abstract class QueryFilterImpl implements QueryFilter {

    protected String parameterName = "p";

    private String alias;

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public Criterion getCriteriaExpression() {
        throw new RuntimeException("Criteria expression are not supported");
    }

    @Override
    public String toString() {
        return getHQLExpression();
    }

    @Override
    public void addAliasPrefix(String prefix) {
        if (StringUtils.isNotBlank(alias)) {
            setAlias(prefix + alias);
        }
    }

    @Override
    public String getSolrQueryTag() {
        return parameterName;
    }

}