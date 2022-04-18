package com.mangeorge.query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.query.Query;


/**
 * Interface for sending an arbitrary query to the database
 *
 * @author George Beliy on 10-01-2020
 */
public interface EntityQuery {

    /**
     * @return return entity class
     */
    Class<?> getRootClass();

    /**
     * @return query based on criteria
     */
    DetachedCriteria getDetachedCriteria();

    /**
     * @return query based on hql
     */
    String getHQLQuery();

    /**
     * Set parameters to HQL Query
     */
    void setHQLParams(Query<?> query);

    /**
     * @return the beginning of the position with which you want to select objects (numbering from scratch)
     */
    Integer getFirst();

    /**
     * @return number of objects to select
     */
    Integer getPageSize();

    /**
     * @return <code>false</code> if not use cache in query. The cache is used by default.
     */
    Boolean getUseCache();

    /**
     * @return <code>true</code> if the query to get the number of rows
     */
    boolean isCountQuery();

    /**
     * force the query to get the number of rows
     */
    void setCountQuery(boolean countQuery);

    /**
     * @return <code>true</code> if you need use query based on criteria
     */
    boolean isUseCriteriaQuery();

    /**
     * @return <code>true</code> if only different values are needed
     */
    boolean isUseDistinctResult();
}
