package com.mangeorge.query.filter.group;

import com.mangeorge.query.filter.QueryFilter;

import java.util.List;


/**
 * Represents a set of filters that must be added to the query in a specific way.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings("unused")
public interface QueryGroupFilter extends QueryFilter {

    /**
     * @param filter filter that need added to the group
     */
    void add(QueryFilter filter);

    /**
     * @param filters filters that need added to the group
     */
    void addAll(List<QueryFilter> filters);

    /**
     * @return filters in group
     */
    List<QueryFilter> getFilters();

    /**
     * If <code>expand</code> is <code>false</code>, then only the first level filters will be returned.
     * Group filters will be presented as is.
     * else, if <code>expand</code> is <code>true</code>, then group filters will be deployed to single filters. The
     * resulting list will not include group filters, but single filters will be included, of which the group consists.
     *
     * @param expand whether it is necessary to deploy group filters
     * @return filters in group
     */
    List<QueryFilter> getFilters(boolean expand);

}