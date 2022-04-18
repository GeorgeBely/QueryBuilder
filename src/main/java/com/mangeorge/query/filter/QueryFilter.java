package com.mangeorge.query.filter;

import org.hibernate.criterion.Criterion;
import org.hibernate.query.Query;


/**
 * Used to build queries to the database.
 * Allows you to build hql-queries or queries based on criteria.
 *
 * @author George Beliy on 10-01-2020
 */
public interface QueryFilter {

    /**
     * @param parameterName The name of the parameter used in the query instead of a specific value
     */
    void setParameterName(String parameterName);

    /**
     * @return expression for whereas the HQL string.
     */
    String getHQLExpression();

    /**
     * Set HQL parameters to query
     *
     * @param query Hibernate query
     */
    void setHQLParams(Query<?> query);

    /**
     * @return filter expression based on criteria
     */
    Criterion getCriteriaExpression();

    /**
     * @return based alias
     */
    String getAlias();

    /**
     * @param alias based alias name
     */
    void setAlias(String alias);

    /**
     * @param prefix prefix for alias name
     */
    void addAliasPrefix(String prefix);

    /**
     * @param withTag indicator of the need to add a tag to the beginning of the expression
     *
     * @return expression to execute a request in Solr
     */
    String getSolrExpression(boolean withTag);

    /**
     * @return tag for Solr expression
     */
    String getSolrQueryTag();

}