package com.mangeorge.query;

import com.mangeorge.query.helper.IndexHelper;
import com.mangeorge.query.helper.ReflectionHelper;
import com.mangeorge.query.filter.QueryFilter;
import com.mangeorge.query.filter.group.AndFilter;
import com.mangeorge.query.filter.group.QueryGroupFilter;
import com.mangeorge.query.order.Order;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Builder for generate query to Solr
 *
 * @param <T> index entity type
 * @author George Beliy on 31.01.2020
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SolrBuilder<T> implements IndexQuery<T> {


    private final Class<T> rootClass;

    /**
     * Query for full text search
     */
    private String query;

    /**
     * Filter that need to be added to the query
     */
    private QueryFilter queryFilter;

    /**
     * Fields for facet.
     * Key   -> {@link Field} of the index entity
     * Value -> query filter or filters that will be excluded when querying facets
     */
    private Map<Field, QueryFilter> facetsWithExclude;

    /**
     * Queries for facet.
     * Key   -> index facet query, May be entity field or some function
     * Value -> query filter or filters that will be excluded when querying facets
     */
    private Map<String, QueryFilter> facetQueriesWithExclude;

    /**
     * facets by multiple fields
     */
    private List<List<String>> pivotFacets;

    /**
     * Order that need to be added to the query
     */
    private Order order;

    /**
     * Order that need to be added to the query when objects are grouped
     */
    private Order groupOrder;

    /**
     * Entity fields for view
     */
    private Set<Field> fields = new HashSet<>();

    /**
     * limit of entities in group.
     */
    private Integer groupLimit;

    /**
     * Entity fields for grouping
     */
    private Set<String> groupFields = new HashSet<>();

    /**
     * Queries for grouping
     */
    private Set<String> groupQueries = new HashSet<>();

    /**
     * Function fields for view
     */
    private Set<String> functionFields = new HashSet<>();

    private Integer first;
    private Integer pageSize;
    private Integer facetLimit;
    private boolean useHighlight;
    private boolean useSpellcheck;


    public SolrBuilder(Class<T> rootClass) {
        this.rootClass = rootClass;
    }


    private List<QueryFilter> getAllQueryFilters() {
        if (QueryGroupFilter.class.isAssignableFrom(queryFilter.getClass())) {
           return getFilters((QueryGroupFilter) queryFilter);
        }
        return Collections.singletonList(queryFilter);
    }

    private List<QueryFilter> getFilters(QueryGroupFilter queryFilter) {
        List<QueryFilter> allFilters = new ArrayList<>();
        for (QueryFilter filter : queryFilter.getFilters()) {
            if (filter instanceof AndFilter)
                allFilters.addAll(getFilters((QueryGroupFilter) filter));
            else
                allFilters.add(filter);
        }
        return allFilters;
    }

    private List<String> getFacetList(Map<?, QueryFilter> facetsData, Function<Object, String> getFacetString) {
        if (facetsData != null) {
            List<String> facets = new ArrayList<>();
            for (Map.Entry<?, QueryFilter> facetEntry : facetsData.entrySet()) {
                StringBuilder sb = new StringBuilder();
                if (facetEntry.getValue() != null) {
                    sb.append(getAllQueryFilters().stream()
                            .map(QueryFilter::getSolrQueryTag)
                            .collect(Collectors.joining(",", "{!ex=", "}")));
                }
                sb.append(getFacetString.apply(facetEntry.getKey()));
                facets.add(sb.toString());
            }
            return facets;
        }
        return null;
    }

    private String getSolrFieldName(String fieldName) {
        Field field = ReflectionHelper.getFieldByFieldName(fieldName, rootClass);
        if (field == null) {
            return fieldName;
        }
        return IndexHelper.getSolrFieldName(field);
    }

    @Override
    public Class<T> getEntityClass() {
        return rootClass;
    }

    @Override
    public String getQuery() {
        if (StringUtils.isBlank(query))
            return "";
        return query;
    }

    @Override
    public List<String> getFilterQueries() {
        if (queryFilter != null) {
            List<String> filtersForIndex = new ArrayList<>();
            for (QueryFilter qf : getAllQueryFilters()) {
                filtersForIndex.add(qf.getSolrExpression(facetsWithExclude != null));
            }
            return filtersForIndex;
        }
        return null;
    }

    @Override
    public List<SolrQuery.SortClause> getSorts() {
        return getSolrSorts(order);
    }

    @Override
    public List<SolrQuery.SortClause> getGroupSorts() {
        return getSolrSorts(groupOrder);
    }

    private List<SolrQuery.SortClause> getSolrSorts(Order order) {
        if (order != null) {
            List<SolrQuery.SortClause> sortClauses = new ArrayList<>();
            Order tmpOrder = order;
            while (tmpOrder != null) {
                sortClauses.add(new SolrQuery.SortClause(getSolrFieldName(tmpOrder.getField()),
                        tmpOrder.isReverse() ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                tmpOrder = tmpOrder.getAdditionalOrder();
            }
            return sortClauses;
        }
        return null;
    }

    public Order getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(Order groupOrder) {
        this.groupOrder = groupOrder;
    }

    @Override
    public List<String> getFacets() {
        return getFacetList(facetsWithExclude, o -> IndexHelper.getSolrFieldName((Field) o));
    }

    @Override
    public List<List<String>> getPivotFacets() {
        return pivotFacets;
    }

    @Override
    public List<String> getFacetQueries() {
        return getFacetList(facetQueriesWithExclude, Object::toString);
    }

    @Override
    public List<String> getFields() {
        List<String> strFields = new ArrayList<>();
        if (fields != null) {
            strFields.addAll(convertToIndexFields(fields));
        }
        if (functionFields != null) {
            strFields.addAll(functionFields);
        }
        return strFields;
    }

    private List<String> convertToIndexFields(Collection<Field> fields) {
        return fields.stream().map(IndexHelper::getSolrFieldName).collect(Collectors.toList());
    }

    @Override
    public Set<String> getGroupFields() {
        return groupFields;
    }

    @Override
    public Set<String> getGroupQueries() {
        return groupQueries;
    }

    @Override
    public Integer getFirst() {
        return first;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    @Override
    public Integer getFacetLimit() {
        return facetLimit;
    }

    @Override
    public boolean isUseHighlight() {
        return useHighlight;
    }

    @Override
    public boolean isUseSpellcheck() {
        return useSpellcheck;
    }

    public SolrBuilder<T> addQueryFilter(QueryFilter queryFilter) {
        return setQueryFilter(new AndFilter(queryFilter, this.queryFilter));
    }

    public SolrBuilder<T> addFacet(Field field, QueryFilter queryFilter) {
        if (facetsWithExclude == null) {
            facetsWithExclude = new HashMap<>();
        }
        facetsWithExclude.put(field, queryFilter);
        return this;
    }

    public SolrBuilder<T> addPivotFacet(List<Field> fields) {
        if (pivotFacets == null) {
            pivotFacets = new ArrayList<>();
        }
        pivotFacets.add(convertToIndexFields(fields));
        return this;
    }

    public SolrBuilder<T> addPivotFacet(Field... fields) {
        return addPivotFacet(Arrays.asList(fields));
    }

    public SolrBuilder<T> addPivotFacetNative(List<String> nativeFields) {
        if (pivotFacets == null) {
            pivotFacets = new ArrayList<>();
        }
        List<Field> fields = nativeFields.stream()
                .map(f -> ReflectionHelper.getFieldByFieldName(f, getEntityClass())).collect(Collectors.toList());
        pivotFacets.add(convertToIndexFields(fields));
        return this;
    }

    public SolrBuilder<T> addFacetQuery(String facetQuery, QueryFilter queryFilter) {
        if (facetQueriesWithExclude == null) {
            facetQueriesWithExclude = new HashMap<>();
        }
        facetQueriesWithExclude.put(facetQuery, queryFilter);
        return this;
    }

    public SolrBuilder<T> addOrder(Order order) {
        if (this.order == null) {
            return setOrder(order);
        }
        this.order.addAdditionalOrder(order);
        return this;
    }

    public SolrBuilder<T> addField(Field field) {
        fields.add(field);
        return this;
    }

    public SolrBuilder<T> addGroupField(Field field) {
        if (field != null) {
            groupFields.add(IndexHelper.getSolrFieldName(field));
        }
        return this;
    }

    public SolrBuilder<T> addGroupField(String field) {
        if (field != null) {
            groupFields.add(field);
        }
        return this;
    }

    public SolrBuilder<T> addGroupQueries(String query) {
        groupQueries.add(query);
        return this;
    }

    public SolrBuilder<T> addFunctionField(String functionField) {
        functionFields.add(functionField);
        return this;
    }

    public SolrBuilder<T> setQuery(String query) {
        this.query = query;
        return this;
    }

    public SolrBuilder<T> setFirst(Integer first) {
        this.first = first;
        return this;
    }

    public SolrBuilder<T> setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public SolrBuilder<T> setFacetLimit(Integer facetLimit) {
        this.facetLimit = facetLimit;
        return this;
    }

    public SolrBuilder<T> setUseHighlight(boolean useHighlight) {
        this.useHighlight = useHighlight;
        return this;
    }

    public SolrBuilder<T>  setUseSpellcheck(boolean useSpellcheck) {
        this.useSpellcheck = useSpellcheck;
        return this;
    }

    public QueryFilter getQueryFilter() {
        return queryFilter;
    }

    public SolrBuilder<T> setQueryFilter(QueryFilter queryFilter) {
        this.queryFilter = queryFilter;
        return this;
    }

    public Order getOrder() {
        return order;
    }

    public SolrBuilder<T> setOrder(Order order) {
        this.order = order;
        return this;
    }

    public Map<Field, QueryFilter> getFacetsWithExclude() {
        return facetsWithExclude;
    }

    public SolrBuilder<T> setFacetsWithExclude(Map<Field, QueryFilter> facetsWithExclude) {
        this.facetsWithExclude = facetsWithExclude;
        return this;
    }

    public Map<String, QueryFilter> getFacetQueriesWithExclude() {
        return facetQueriesWithExclude;
    }

    public SolrBuilder<T> setFacetQueriesWithExclude(Map<String, QueryFilter> facetQueriesWithExclude) {
        this.facetQueriesWithExclude = facetQueriesWithExclude;
        return this;
    }

    public SolrBuilder<T> setFields(Set<Field> fields) {
        this.fields = fields;
        return this;
    }

    public SolrBuilder<T> setGroupFields(Set<String> groupFields) {
        this.groupFields = groupFields;
        return this;
    }

    public SolrBuilder<T> setGroupQueries(Set<String> groupQueries) {
        this.groupQueries = groupQueries;
        return this;
    }

    @Override
    public Integer getGroupLimit() {
        return groupLimit;
    }

    public SolrBuilder<T> setGroupLimit(Integer groupLimit) {
        this.groupLimit = groupLimit;
        return this;
    }

    public Set<String> getFunctionFields() {
        return functionFields;
    }

    public SolrBuilder<T> setFunctionFields(Set<String> functionFields) {
        this.functionFields = functionFields;
        return this;
    }
}
