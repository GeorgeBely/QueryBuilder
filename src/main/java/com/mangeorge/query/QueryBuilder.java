package com.mangeorge.query;

import com.mangeorge.query.helper.HQLBuilderHelper;
import com.mangeorge.query.filter.QueryFilter;
import com.mangeorge.query.join.JoinType;
import com.mangeorge.query.join.QueryJoin;
import com.mangeorge.query.order.Order;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Builder for generate query based on HQL or Criteria
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings("unused")
public class QueryBuilder implements EntityQuery {

    private final Logger log = LoggerFactory.getLogger(QueryBuilder.class);

    public static final char ALIAS_PREFIX_SYMBOL = '_';


    private final Class<?> rootClass;
    private String rootAlias;

    /**
     * Joins that need to be added to the query
     */
    private Set<QueryJoin> queryJoins = new HashSet<>();

    /**
     * Filter that need to be added to the query
     */
    private QueryFilter queryFilter;

    /**
     * Order that need to be added to the query
     */
    private Order order;

    private Integer first;
    private Integer pageSize;
    private Boolean useCache = true;
    private boolean countQuery;
    private boolean useCriteriaQuery;
    private boolean useDistinctResult;


    public QueryBuilder(Class<?> rootClass) {
        this.rootClass = rootClass;
        rootAlias = HQLBuilderHelper.generateAliasForClass(rootClass);
    }


    @Override
    public DetachedCriteria getDetachedCriteria() {
        DetachedCriteria criteria = DetachedCriteria.forClass(rootClass);
        criteria.add(queryFilter.getCriteriaExpression());
        if (order != null)
            order.addToCriteria(criteria);

        for (QueryJoin alias : queryJoins) {
            criteria = alias.addCriteriaAlias(criteria);
        }
        return criteria;
    }

    public final Class<?> getRootClass() {
        return rootClass;
    }

    @Override
    public String getHQLQuery() {
        String select = getHQLQueryWithoutOrderBy();
        if (countQuery) {
            return select;
        }
        return select + buildOrder();
    }

    public String getHQLQueryWithoutOrderBy() {
        return buildSelect() + buildFrom() + buildJoin() + buildWhere();
    }

    @Override
    public void setHQLParams(Query<?> query) {
        if (queryFilter != null) {
            queryFilter.setHQLParams(query);
        }
    }

    /**
     * @param queryJoin join that need added to the query
     */
    public void addQueryJoin(QueryJoin queryJoin) {
        queryJoins.add(queryJoin);
    }

    /**
     * @param queryJoins joins that need added to the query
     */
    public void addQueryJoins(QueryJoin... queryJoins) {
        this.queryJoins.addAll(Arrays.asList(queryJoins));
    }

    /**
     * Constructs hql expression for the block <code>select</code>.
     *
     * @return select string
     */
    private String buildSelect() {
        String selectAlias = rootAlias;

        if (isUseDistinctResult()) {
            selectAlias = "distinct " + rootAlias;
        }

        if (countQuery) {
            return "select count(" + selectAlias + ")";
        }
        return "select " + selectAlias;
    }

    /**
     * Constructs hql expression for the block <code>from</code>.
     *
     * @return line for specifying the table from which to sample
     */
    private String buildFrom() {
        return " from " + rootClass.getSimpleName() + " " + rootAlias;
    }

    /**
     * Creates an HQL expression for attaching tables to the main table.
     *
     * @return row for joining tables
     */
    private String buildJoin() {
        StringBuilder buf = new StringBuilder();
        if (queryJoins != null) {
            for (QueryJoin alias : queryJoins) {
                buf.append(alias.getHQLExpression());
            }
        }
        log.debug("join expression built: {}", buf);
        return buf.toString();
    }

    /**
     * Constructs hql expression for the block <code>where</code>.
     *
     * @return string with conditions
     */
    private String buildWhere() {
        StringBuilder buf = new StringBuilder();
        if (queryJoins != null) {
            for (QueryJoin alias : queryJoins) {
                if (alias.getJoinType() == JoinType.INNER) {
                    if (!buf.toString().isEmpty()) {
                        buf.append(" AND ");
                    }
                    buf.append(alias.getHQLWhereExpression());
                }
            }
        }
        if (queryFilter != null) {
            if (StringUtils.isBlank(queryFilter.getAlias())) {
                queryFilter.setAlias(rootAlias);
            }
            String whereExpression = queryFilter.getHQLExpression();
            if (StringUtils.isNotBlank(whereExpression)) {
                if (!buf.toString().isEmpty()) {
                    buf.append(" AND ");
                }
                buf.append(whereExpression);
            }
            log.debug("where expression built: {}", buf);
        }
        if (StringUtils.isNotBlank(buf)) {
            return " where " + buf;
        }
        return "";
    }

    /**
     * Constructs hql expression for the block <code>order by</code>.
     *
     * @return line with order expressions
     */
    private String buildOrder() {
        if (order != null) {
            return " order by " + order.getExpression(rootAlias);
        }
        return "";
    }

    /**
     * Add prefix to root alias.
     * Update root alias in {@link #queryJoins} and {@link #queryFilter} and {@link #order}
     *
     * @param aliasPrefix prefix for alias name
     */
    public void addAliasPrefix(String aliasPrefix) {
        String prefix = aliasPrefix + ALIAS_PREFIX_SYMBOL;
        if (!rootAlias.startsWith(prefix)) {
            rootAlias = prefix + getRootAlias();
            for (QueryJoin queryJoin : queryJoins) {
                queryJoin.setAdjoiningAlias(prefix + queryJoin.getAdjoiningAlias());
                queryJoin.setRootAlias(prefix + queryJoin.getRootAlias());
            }
            if (queryFilter != null) {
                queryFilter.addAliasPrefix(prefix);
            }
            if (order != null) {
                order.addAliasPrefix(prefix);
            }
        }
    }

    public String getRootAlias() {
        return rootAlias;
    }

    public void setRootAlias(String rootAlias) {
        this.rootAlias = rootAlias;
    }

    public Set<QueryJoin> getQueryJoins() {
        return queryJoins;
    }

    public void setQueryJoins(Set<QueryJoin> queryJoins) {
        this.queryJoins = queryJoins;
    }

    public QueryFilter getQueryFilter() {
        return queryFilter;
    }

    public void setQueryFilter(QueryFilter queryFilter) {
        this.queryFilter = queryFilter;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    @Override
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Boolean getUseCache() {
        return useCache;
    }

    public void setUseCache(Boolean useCache) {
        this.useCache = useCache;
    }

    @Override
    public boolean isCountQuery() {
        return countQuery;
    }

    @Override
    public void setCountQuery(boolean countQuery) {
        this.countQuery = countQuery;
    }

    @Override
    public boolean isUseCriteriaQuery() {
        return useCriteriaQuery;
    }

    public void setUseCriteriaQuery(boolean useCriteriaQuery) {
        this.useCriteriaQuery = useCriteriaQuery;
    }

    @Override
    public boolean isUseDistinctResult() {
        return useDistinctResult;
    }

    public void setUseDistinctResult(boolean useDistinctResult) {
        this.useDistinctResult = useDistinctResult;
    }
}
