package com.mangeorge.query.filter.single;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.lang.reflect.Field;


/**
 * A filter that checks the occurrence of a field value in a given range.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class BetweenFilter<T> extends SingleFieldFilterImpl<T> {

    private final T valueFrom;
    private final T valueTo;

    public BetweenFilter(String fieldPath, T valueFrom, T valueTo) {
        this((String) null, fieldPath, valueFrom, valueTo);
    }

    public BetweenFilter(Field field, T valueFrom, T valueTo) {
        this(null, field, valueFrom, valueTo);
    }

    public <E> BetweenFilter(Class<E> classAlias, String fieldPath, T valueFrom, T valueTo) {
        this(StringUtils.uncapitalize(classAlias.getSimpleName()), fieldPath, valueFrom, valueTo);
    }

    public BetweenFilter(String alias, Field field, T valueFrom, T valueTo) {
        this(alias, field.getName(), valueFrom, valueTo);
    }

    public BetweenFilter(String alias, String fieldPath, T valueFrom, T valueTo) {
        super(fieldPath);
        setAlias(alias);
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }

    @Override
    public String getHQLExpression() {
        if (valueTo == null) {
            return getField() + " >= :" + parameterName + "b1 ";
        } else if (valueFrom == null) {
            return getField() + " <= :" + parameterName + "b2 ";
        } else {
            return getField() + " between :" + parameterName + "b1 AND :" + parameterName + "b2";
        }
    }

    @Override
    public Criterion getCriteriaExpression() {
        return Restrictions.between(getField(), valueFrom, valueTo);
    }

    @Override
    public void setHQLParams(Query<?> query) {
        if (valueFrom != null) {
            query.setParameter(parameterName + "b1", valueFrom);
        }
        if (valueTo != null) {
            query.setParameter(parameterName + "b2", valueTo);
        }
    }

    @Override
    protected String getSolrExpression() {
        return getFieldName() + ":[" + (valueFrom == null ? '*' : valueFrom) + " TO " + (valueTo == null ? '*' : valueTo) + "]";
    }
}