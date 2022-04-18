package com.mangeorge.query.filter.single;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.lang.reflect.Field;


/**
 * A filter that compares the value of a field with the specified value.
 *
 * @author George Beliy on 10-01-2020
 */
public class EqualsFilter<T> extends SingleFieldFilterImpl<T> {

    /**
     * @param value the value with which the value of the fieldPath should be compared
     */
    public EqualsFilter(String fieldPath, T value) {
        this((String) null, fieldPath, value);
    }

    public EqualsFilter(Field field, T value) {
        this(null, field, value);
    }

    public <E> EqualsFilter(Class<E> classAlias, String field, T value) {
        this( StringUtils.uncapitalize(classAlias.getSimpleName()), field, value);
    }

    public EqualsFilter(String alias, Field field, T value) {
        this(alias, field.getName(), value);
    }

    public EqualsFilter(String alias, String field, T value) {
        super(field, value);
        setAlias(alias);
    }

    @Override
    public String getHQLExpression() {
        return getField() + " = :" + parameterName;
    }

    @Override
    public Criterion getCriteriaExpression() {
        return Restrictions.eq(getField(), value);
    }

    @Override
    public void setHQLParams(Query<?> query) {
        query.setParameter(parameterName, value);
    }

    public String getSolrExpression() {
        return getFieldName() + ":\"" + value + "\"";
    }
}