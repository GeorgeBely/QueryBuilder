package com.mangeorge.query.filter.single;


import com.mangeorge.query.helper.HQLBuilderHelper;
import com.mangeorge.query.helper.ReflectionHelper;


/**
 * A filter by id object.
 *
 * @author George Beliy on 10-01-2020
 */
public class IdFilter extends EqualsFilter<Object> {

    @SuppressWarnings("unused")
    public <T> IdFilter(final Class<T> clazz, Object value) {
        this(HQLBuilderHelper.generateAliasForClass(clazz), clazz, value);
    }

    /**
     * @param alias query alias
     * @param clazz class that contains the id, by which you need to add a filter
     * @param value id value
     */
    public <T> IdFilter(String alias, final Class<T> clazz, Object value) {
        super(alias, ReflectionHelper.getIdFieldName(clazz), value);
    }

}
