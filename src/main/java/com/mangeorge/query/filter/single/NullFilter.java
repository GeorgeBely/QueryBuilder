package com.mangeorge.query.filter.single;

import com.mangeorge.query.filter.NotFilter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import java.lang.reflect.Field;


/**
 * The filter that checks that value is null.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"WeakerAccess","unused"})
public class NullFilter extends SingleFieldFilterImpl<Object> {

    public NullFilter(String field) {
        this((String) null, field);
    }

    public NullFilter(Field field) {
        this(null, field);
    }

    public <E> NullFilter(Class<E> classAlias, String field) {
        this( StringUtils.uncapitalize(classAlias.getSimpleName()), field);
    }

    public NullFilter(String alias, Field field) {
        this(alias, field.getName());
    }

    public NullFilter(String alias, String field) {
        super(field);
        setAlias(alias);
    }

    @Override
    public String getHQLExpression() {
        return getField() + " is null";
    }

    @Override
    public void setHQLParams(Query<?> query) {
    }

    @Override
    public Criterion getCriteriaExpression() {
        return Restrictions.isNull(getField());
    }

    @Override
    public String getSolrExpression(boolean withTag) {
        return new NotFilter(new LikeFilter(getFieldName(), "", MatchMode.START)).getSolrExpression(withTag);
    }

    @Override
    protected String getSolrExpression() {
        return null;
    }

}