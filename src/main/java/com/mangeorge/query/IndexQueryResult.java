package com.mangeorge.query;


import com.mangeorge.query.solr.PivotFacetResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Result of query execution.
 *
 * @author George Beliy on 31.01.2020
 */
public class IndexQueryResult<T> {

    private Long count;

    private List<T> entities;

    private Map<String, Map<String, Long>> facets;

    private Map<String, Long> facetQueries;

    private Map<String, List<String>> highlighting;

    private Map<String, Map<String, List<T>>> groups;

    private List<PivotFacetResult> pivotFacets;

    private String spellcheckSuggestion;


    public void addFacet(String facetName, String value, Long count) {
        if (facets == null)
            facets = new LinkedHashMap<>();
        if (!facets.containsKey(facetName))
            facets.put(facetName, new LinkedHashMap<>());
        facets.get(facetName).put(value, count);
    }

    public void addFacetQuery(String name, Long count) {
        if (facetQueries == null)
            facetQueries = new LinkedHashMap<>();
        facetQueries.put(name, count);
    }

    public void addGroup(String field, String name, List<T> entities) {
        if (groups == null)
            groups = new LinkedHashMap<>();
        if (!groups.containsKey(field)) {
            groups.put(field, new LinkedHashMap<>());
        }
        groups.get(field).put(name, entities);
    }

    public void addPivotFacet(PivotFacetResult pivotFacetResult) {
        if (pivotFacets == null) {
            pivotFacets = new ArrayList<>();
        }
        pivotFacets.add(pivotFacetResult);
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entities) {
        this.entities = entities;
    }

    public Map<String, Map<String, Long>> getFacets() {
        return facets;
    }

    public void setFacets(Map<String, Map<String, Long>> facets) {
        this.facets = facets;
    }

    public Map<String, Long> getFacetQueries() {
        return facetQueries;
    }

    public void setFacetQueries(Map<String, Long> facetQueries) {
        this.facetQueries = facetQueries;
    }

    public Map<String, List<String>> getHighlighting() {
        return highlighting;
    }

    public void setHighlighting(Map<String, List<String>> highlighting) {
        this.highlighting = highlighting;
    }

    public String getSpellcheckSuggestion() {
        return spellcheckSuggestion;
    }

    public void setSpellcheckSuggestion(String spellcheckSuggestion) {
        this.spellcheckSuggestion = spellcheckSuggestion;
    }

    public Map<String, Map<String, List<T>>> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, Map<String, List<T>>> groups) {
        this.groups = groups;
    }

    public List<PivotFacetResult> getPivotFacets() {
        return pivotFacets;
    }

    public void setPivotFacets(List<PivotFacetResult> pivotFacets) {
        this.pivotFacets = pivotFacets;
    }
}
