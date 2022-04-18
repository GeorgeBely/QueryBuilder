package com.mangeorge.query.solr;

import java.util.Map;


/**
 * @author gbeliy on 26.06.2020
 */
public class PivotFacetValue {

    private Integer count;
    private Map<String, Object> values;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }
}
