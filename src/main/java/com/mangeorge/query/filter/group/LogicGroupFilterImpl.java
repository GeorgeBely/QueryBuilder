package com.mangeorge.query.filter.group;

import com.mangeorge.query.filter.QueryFilter;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.function.Function;


/**
 * The basic implementation of a group logical filter.
 * A group logical filter is a filter whose sub-filters are applied to the query using a specific logical operation.
 * Filters based on criteria are supported.
 *
 * @author George Beliy on 10-01-2020
 */
public abstract class LogicGroupFilterImpl extends QueryGroupFilterImpl implements LogicGroupFilter {


    LogicGroupFilterImpl() {
        super();
    }

    LogicGroupFilterImpl(List<QueryFilter> filters) {
        super(filters);
    }

    LogicGroupFilterImpl(QueryFilter... filters) {
        super(filters);
    }


    @Override
    public String getHQLExpression() {
        return getExpression(QueryFilter::getHQLExpression);
    }

    @Override
    public String getSolrExpression(boolean withTag) {
        return getExpression(f -> f.getSolrExpression(false));
    }

    private String getExpression(Function<QueryFilter, String> filterExpression) {
        StringBuilder buf = null;
        int i = 0;
        for (QueryFilter filter : filters) {
            String pName = parameterName + i++;
            filter.setParameterName(pName);
            String hql = filterExpression.apply(filter);
            if (hql != null && !hql.isEmpty()) {
                if (buf == null) {
                    buf = new StringBuilder((this instanceof AndFilter) ? "" : "(");
                } else {
                    buf.append(" ").append(logicString()).append(" ");
                }
                if (filter instanceof AndFilter && this instanceof OrFilter) {
                    buf.append("(").append(hql).append(")");
                } else {
                    buf.append(hql);
                }
            }
        }
        if (buf == null || buf.length() == 0)
            return null;
        if (!(this instanceof AndFilter))
            buf.append(")");
        return buf.toString();
    }

    @Override
    public void setHQLParams(Query<?> query) {
        for (QueryFilter filter : filters) {
            filter.setHQLParams(query);
        }
    }

    @Override
    public Criterion getCriteriaExpression() {
        Junction junction = junction();
        for (QueryFilter filter : filters)
            junction.add(filter.getCriteriaExpression());
        return junction;
    }

    @Override
    public String toString() {
        StringBuilder buf = null;
        for (QueryFilter filter : filters) {
            if (buf == null) {
                buf = new StringBuilder("(");
            } else {
                buf.append(" ").append(logicString()).append(" ");
            }
            buf.append(filter);
        }
        if (buf == null) {
            return "";
        }
        buf.append(")");
        return buf.toString();
    }
}