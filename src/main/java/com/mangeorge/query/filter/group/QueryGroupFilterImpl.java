package com.mangeorge.query.filter.group;

import com.mangeorge.query.filter.QueryFilter;
import com.mangeorge.query.filter.QueryFilterImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Basic implementation of the group filter.
 * Contains a set of sub-filters and allows you to manage them.
 *
 * @author George Beliy on 10-01-2020
 */
abstract class QueryGroupFilterImpl extends QueryFilterImpl implements QueryGroupFilter {

    final List<QueryFilter> filters;

    QueryGroupFilterImpl() {
        filters = new ArrayList<>(5);
    }

    /**
     * @param filters set of filters
     */
    QueryGroupFilterImpl(List<QueryFilter> filters) {
        this();
        this.filters.addAll(filters.stream().filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    /**
     * @param filters set of filters
     */
    QueryGroupFilterImpl(QueryFilter... filters) {
        this(Arrays.asList(filters));
    }


    @Override
    public void add(QueryFilter filter) {
        if (filter != null) {
            addAliasToFilter(filter);
            filters.add(filter);
        }
    }

    @Override
    @SuppressWarnings("unused")
    public void addAll(List<QueryFilter> filters) {
        List<QueryFilter> tempFilter = filters.stream().filter(Objects::nonNull).collect(Collectors.toList());
        addAliasToFilters(tempFilter);
        this.filters.addAll(tempFilter);
    }

    @Override
    public List<QueryFilter> getFilters() {
        return getFilters(false);
    }

    @Override
    public List<QueryFilter> getFilters(boolean expand) {
        if (!expand)
            return filters;
        List<QueryFilter> allFilters = new ArrayList<>();
        for (QueryFilter filter : filters) {
            if (filter instanceof QueryGroupFilter)
                allFilters.addAll(((QueryGroupFilter) filter).getFilters(true));
            else
                allFilters.add(filter);
        }
        return allFilters;
    }

    @Override
    public void setAlias(String alias) {
        super.setAlias(alias);
        addAliasToFilters(getFilters(true));
    }

    /**
     * Sets the main alias to the filter. If filter is group filter then will add an alias to its child filters.
     *
     * @param filter filter for which want to set the root alias
     */
    private void addAliasToFilter(QueryFilter filter) {
        if (StringUtils.isNotBlank(getAlias()) && StringUtils.isBlank(filter.getAlias())) {
            filter.setAlias(getAlias());
        }
    }

    /**
     * Sets the main alias to the filters.
     *
     * @param filters filters for which want to set the root alias
     */
    private void addAliasToFilters(List<QueryFilter> filters) {
        for (QueryFilter filter : filters) {
            addAliasToFilter(filter);
        }
    }

    @Override
    public void addAliasPrefix(String prefix) {
        for (QueryFilter filter : filters) {
            filter.addAliasPrefix(prefix);
        }
    }

    public boolean isEmpty() {
        return filters.isEmpty();
    }
}