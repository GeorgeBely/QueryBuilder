package com.mangeorge.query.filter.single;

import org.hibernate.criterion.Criterion;
import org.hibernate.query.Query;


/**
 * Inverts the child filter.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class NVLFilter<T> extends SingleFieldFilterImpl<T> {

    protected final SingleFieldFilter filter;


    public NVLFilter(SingleFieldFilterImpl<T> filter, T ifNUllValue) {
        super(null, ifNUllValue);
        this.filter = filter;
    }

    @Override
    public String getHQLExpression() {
        filter.setParameterName(parameterName + "0");
        return filter.getHQLExpression().replace(filter.getField() + " ",
                "ISNULL(" + filter.getField() + ", :" + parameterName +  ") ");
    }

    @Override
    public void setHQLParams(Query<?> query) {
        query.setParameter(parameterName, value);
        filter.setHQLParams(query);
    }

    @Override
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
        filter.setParameterName(parameterName + "0");
    }

    @Override
    public Criterion getCriteriaExpression() {
        throw new RuntimeException("Criteria not support");
    }

    @Override
    public void setAlias(String alias) {
        filter.setAlias(alias);
    }

    @Override
    protected String getSolrExpression() {
        throw new RuntimeException("Solr not support nvl filter");
    }
}