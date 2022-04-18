package com.mangeorge.query.solr;


import java.util.List;


/**
 * @author gbeliy on 26.06.2020
 */
public class PivotFacetResult {

    private String name;
    private List<PivotFacetValue> values;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PivotFacetValue> getValues() {
        return values;
    }

    public void setValues(List<PivotFacetValue> values) {
        this.values = values;
    }
}
