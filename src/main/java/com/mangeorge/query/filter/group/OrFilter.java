package com.mangeorge.query.filter.group;

import com.mangeorge.query.filter.QueryFilter;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

import java.util.List;


/**
 * A group filter whose sub-filters are applied to a query using logic 'OR'.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OrFilter extends LogicGroupFilterImpl {

    public OrFilter() {
        super();
    }

    /**
     * @param rootAlias an alias that will be set as the root alias in the filter
     */
    public OrFilter(String rootAlias) {
        super();
        setAlias(rootAlias);
    }

    public OrFilter(List<QueryFilter> filters) {
        super(filters);
    }

    public OrFilter(QueryFilter... filters) {
        super(filters);
    }

    @Override
    public String logicString() {
        return "OR";
    }

    @Override
    public Junction junction() {
        return Restrictions.disjunction();
    }

}