package com.mangeorge.query.filter.group;

import com.mangeorge.query.filter.QueryFilter;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

import java.util.List;


/**
 * A group filter whose sub-filters are applied to a query using logic 'AND'.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"unused"})
public class AndFilter extends LogicGroupFilterImpl {

    public AndFilter() {
        super();
    }

    /**
     * @param alias an alias that will be set as the alias in the filters
     */
    public AndFilter(String alias) {
        super();
        setAlias(alias);
    }

    public AndFilter(List<QueryFilter> filters) {
        super(filters);
    }

    public AndFilter(QueryFilter... filters) {
        super(filters);
    }

    @Override
    public String logicString() {
        return "AND";
    }

    @Override
    public Junction junction() {
        return Restrictions.conjunction();
    }

}
