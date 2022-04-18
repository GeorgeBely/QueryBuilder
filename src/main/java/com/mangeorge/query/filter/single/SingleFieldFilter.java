package com.mangeorge.query.filter.single;

import com.mangeorge.query.filter.QueryFilter;


/**
 * The interface of the filter applied to one entity field.
 * In the implemented class, the field name and value must be specified.
 *
 * @author George Beliy on 10-01-2020
 */
interface SingleFieldFilter extends QueryFilter {

    /**
     * If a root alias exists, it is added before the field.
     *
     * @return field for which the filter is used
     */
    String getField();

}