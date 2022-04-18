package com.mangeorge.query.filter.single;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.lang.reflect.Field;


/**
 * Filter for checking the availability of a given value in the text field of the entity using the LIKE operator.
 * At the beginning and at the end, the symbol '%' is automatically appended,
 * and as the escape character is used the symbol {@value ESCAPE_CHAR}.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class LikeFilterWithoutParam extends LikeFilter {

    /**
     * @param value the value that should be in the text fieldPath of the object
     */
    public LikeFilterWithoutParam(String fieldPath, String value) {
        this((String) null, fieldPath, value);
    }

    public LikeFilterWithoutParam(Field field, String value) {
        this(null, field, value);
    }

    public <E> LikeFilterWithoutParam(Class<E> classAlias, String fieldPath, String value) {
        this(StringUtils.uncapitalize(classAlias.getSimpleName()), fieldPath, value);
    }

    public LikeFilterWithoutParam(String alias, Field field, String value) {
        this(alias, field.getName(), value);
    }

    public LikeFilterWithoutParam(String alias, String fieldPath, String value) {
        super(fieldPath, value, null);
        setAlias(alias);
    }

    @Override
    public String getHQLExpression() {
        return "(lower(" + getField() + ") like lower(" + value + ") escape '" + ESCAPE_CHAR + "')";
    }

    @Override
    public Criterion getCriteriaExpression() {
        return Restrictions.like(getField(), value).ignoreCase();
    }

    @Override
    public void setHQLParams(Query<?> query) {
    }

}