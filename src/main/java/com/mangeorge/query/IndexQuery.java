package com.mangeorge.query;


import org.apache.solr.client.solrj.SolrQuery;

import java.util.List;
import java.util.Set;


/**
 * Interface for sending an arbitrary query to the index application
 *
 * @param <T> index entity type
 * @author George Beliy on 31-01-2020
 */
public interface IndexQuery<T> {

    /**
     * @return return entity class
     */
    Class<T> getEntityClass();

    /**
     * @return query for full text search
     */
    String getQuery();

    /**
     * @return filters for query
     */
    List<String> getFilterQueries();

    /**
     * @return list objects with field and sort order. Sort by sequence.
     */
    List<SolrQuery.SortClause> getSorts();

    /**
     * @return list objects with field and sort order in group. Sort by sequence.
     */
    List<SolrQuery.SortClause> getGroupSorts();

    /**
     * @return facet fields
     */
    List<String> getFacets();

    /**
     * @return facets by multiple fields
     */
    List<List<String>> getPivotFacets();

    /**
     * @return facet queries
     */
    List<String> getFacetQueries();

    /**
     * If you do not specify fields in this method, all fields of the entity will be received
     *
     * @return A list of fields that restricts the information included in response to a request to these fields
     */
    List<String> getFields();

    /**
     * @return the beginning of the position with which you want to select objects (numbering from scratch)
     */
    Integer getFirst();

    /**
     * @return number of objects to select
     */
    Integer getPageSize();

    /**
     * Numeric option indicating the maximum number of facet field counts
     * be included in the response for each field - in descending order of count.
     *
     * @return number facet items to return
     */
    Integer getFacetLimit();

    /**
     * @return <code>true</code> if you need to use highlighting
     */
    boolean isUseHighlight();

    /**
     * @return <code>true</code> if you need to use spellcheck
     */
    boolean isUseSpellcheck();

    /**
     * @return fields to be grouped
     */
    Set<String> getGroupFields();

    /**
     * @return queries to be grouped
     */
    Set<String> getGroupQueries();

    /**
     * @return limit of entities in group. Default is 1
     */
    Integer getGroupLimit();

}
