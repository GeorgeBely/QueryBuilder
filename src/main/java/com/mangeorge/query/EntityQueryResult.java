package com.mangeorge.query;

import java.util.List;


/**
 * Result of query execution.
 *
 * @author George Beliy on 10-01-2020
 */
public class EntityQueryResult<T> {

    private final List<T> entities;
    private final Integer count;


    /**
     * @param entities list of entities
     * @param count number of found objects in the database
     */
    EntityQueryResult(List<T> entities, Integer count) {
        this.entities = entities;
        this.count = count;
    }

    public List<T> getEntities() {
        return entities;
    }

    public Integer getCount() {
        return count;
    }
}
