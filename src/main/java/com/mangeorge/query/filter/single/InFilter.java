package com.mangeorge.query.filter.single;

import com.mangeorge.query.helper.HQLBuilderHelper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.lang.reflect.Field;


/**
 * A filter that compares the field value with the specified values.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class InFilter<T> extends SingleFieldFilterImpl<T[]> {

    /**
     * @param values set of possible fieldPath values
     */
    @SafeVarargs
    public InFilter(String fieldPath, T... values) {
        this((String) null, fieldPath, values);
    }

    @SafeVarargs
    public InFilter(Field field, T... values) {
        this(null, field, values);
    }

    @SafeVarargs
    public <E> InFilter(Class<E> classAlias, String fieldPath, T... values) {
        this(StringUtils.uncapitalize(classAlias.getSimpleName()), fieldPath, values);
    }

    @SafeVarargs
    public InFilter(String alias, Field field, T... values) {
        this(alias, field.getName(), values);
    }

    @SafeVarargs
    public InFilter(String alias, String fieldPath, T... values) {
        super(fieldPath, values);
        setAlias(alias);
    }


    @Override
    public String getHQLExpression() {
        return getField() + " in :" + parameterName;
    }

    @Override
    public Criterion getCriteriaExpression() {
        return Restrictions.in(getField(), (Object[]) value);
    }

    @Override
    public void setHQLParams(Query<?> query) {
        query.setParameterList(parameterName, value, HQLBuilderHelper.getHibernateType(value[0].getClass()));
    }

    @Override
    public String getSolrExpression() {
        return getFieldName() +":(\"" + StringUtils.join(value, "\" OR \"") + "\")";
    }
}