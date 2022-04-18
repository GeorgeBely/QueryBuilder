package com.mangeorge.query.filter.single;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.lang.reflect.Field;


/**
 * A filter that compares the value of a field with the specified value.
 *
 * @author George Beliy on 06-02-2020
 */
@SuppressWarnings("unused")
public class EqualsFilterWithoutParam extends EqualsFilter<String> {

    /**
     * @param value the value with which the value of the fieldPath should be compared
     */
    public EqualsFilterWithoutParam(String fieldPath, String value) {
        this((String) null, fieldPath, value);
    }

    public EqualsFilterWithoutParam(Field field, String value) {
        this(null, field, value);
    }

    public <E> EqualsFilterWithoutParam(Class<E> classAlias, String field, String value) {
        this( StringUtils.uncapitalize(classAlias.getSimpleName()), field, value);
    }

    public EqualsFilterWithoutParam(String alias, Field field, String value) {
        this(alias, field.getName(), value);
    }

    public EqualsFilterWithoutParam(String alias, String field, String value) {
        super(field, value);
        setAlias(alias);
    }

    @Override
    public String getHQLExpression() {
        return getField() + " = " + value;
    }

    @Override
    public Criterion getCriteriaExpression() {
        return Restrictions.eq(getField(), value);
    }

    @Override
    public void setHQLParams(Query<?> query) {
    }

}