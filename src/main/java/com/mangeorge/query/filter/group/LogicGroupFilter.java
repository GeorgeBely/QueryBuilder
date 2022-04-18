package com.mangeorge.query.filter.group;

import org.hibernate.criterion.Junction;


/**
 * A group filter whose filters are applied using some logical operation.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"unused"})
public interface LogicGroupFilter extends QueryGroupFilter {

    /**
     * @return a string that must be inserted into the hql request to implement logic
     */
    String logicString();

    /**
     * @return logic of connection of a group of filters for queries based on criteria
     */
    Junction junction();

}
