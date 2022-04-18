package com.mangeorge.query.helper;


import java.lang.reflect.Field;


/**
 * Helper for working with index objects
 */
public class IndexHelper {

    public static String getSolrFieldName(Field field) {
        return field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class).value();
    }

}
