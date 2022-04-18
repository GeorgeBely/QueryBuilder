package com.mangeorge.query.filter.single;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
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
@SuppressWarnings("unused")
public class LikeFilter extends SingleFieldFilterImpl<String> {

    static final char ESCAPE_CHAR = '~';
    public static final char ANY_SYMBOLS = '%';
    public static final char ANY_SYMBOL = '_';
    public static final char SOLR_ANY_SYMBOLS = '*';


    private final MatchMode matchMode;


    /**
     * @param value the value that should be in the text fieldPath of the object
     */
    public LikeFilter(String fieldPath, String value, MatchMode matchMode) {
        this((String) null, fieldPath, value, matchMode);
    }

    public LikeFilter(Field field, String value, MatchMode matchMode) {
        this(null, field, value, matchMode);
    }

    public <E> LikeFilter(Class<E> classAlias, String fieldPath, String value, MatchMode matchMode) {
        this(StringUtils.uncapitalize(classAlias.getSimpleName()), fieldPath, value, matchMode);
    }

    public LikeFilter(String alias, Field field, String value, MatchMode matchMode) {
        this(alias, field.getName(), value, matchMode);
    }

    public LikeFilter(String alias, String fieldPath, String value, MatchMode matchMode) {
        super(fieldPath, value);
        setAlias(alias);
        this.matchMode = matchMode;
    }

    @Override
    public String getHQLExpression() {
        return "(lower(" + getField() + ") like :" + parameterName + " escape '" + ESCAPE_CHAR + "')";
    }

    @Override
    public Criterion getCriteriaExpression() {
        return Restrictions.like(getField(), toLikeValue(value, false), matchMode).ignoreCase();
    }

    @Override
    public void setHQLParams(Query<?> query) {
        query.setParameter(parameterName, toLikeValue(value, true));
    }

    @Override
    protected String getSolrExpression() {
        return getFieldName() + ":" + toLikeValue(value, true).replace(ANY_SYMBOLS, SOLR_ANY_SYMBOLS);
    }

    /**
     * Converts a value {@code value} in a form suitable for the LIKE operator.
     *
     * @param value initial form of the value
     * @param addAnySymbol if "true" then add ANY_SYMBOLS
     * @return form suitable for the LIKE operator
     */
    private String toLikeValue(String value, boolean addAnySymbol) {
        String val = value.replaceAll(String.valueOf(ANY_SYMBOLS), ESCAPE_CHAR + "" + ANY_SYMBOLS);
        return (addAnySymbol ? matchMode.toMatchString(val) : val).toLowerCase();
    }

}