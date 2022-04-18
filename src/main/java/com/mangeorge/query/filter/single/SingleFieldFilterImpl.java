package com.mangeorge.query.filter.single;

import com.mangeorge.query.filter.QueryFilterImpl;
import org.apache.commons.lang3.StringUtils;


/**
 * Basic implementation of the single filter.
 *
 * @author George Beliy on 10-01-2020
 */
public abstract class SingleFieldFilterImpl<T> extends QueryFilterImpl implements SingleFieldFilter {

    T value;
    private final String field;


    SingleFieldFilterImpl(String field) {
        this.field = field;
    }

    SingleFieldFilterImpl(String field, T value) {
        if (value == null || (value instanceof Object[] && ((Object[]) value).length == 0)) {
            throw new IllegalArgumentException("You must specify a value");
        }
        this.value = value;
        this.field = field;
    }

    @Override
    public String getSolrExpression(boolean withTag) {
        String solrExpression = getSolrExpression();
        if (StringUtils.isBlank(solrExpression)) {
            return "";
        }
        StringBuilder expressionBuilder = new StringBuilder();
        if (withTag) {
            expressionBuilder.append("{!tag=").append(getSolrQueryTag()).append("}");
        }
        expressionBuilder.append("(").append(solrExpression).append(")");

        return expressionBuilder.toString();
    }

    protected abstract String getSolrExpression();

    @Override
    public String getField() {
        if (StringUtils.isBlank(getAlias()))
            return field;
        return getAlias() + "." + field;
    }

    public String getFieldName() {
        return field;
    }

}