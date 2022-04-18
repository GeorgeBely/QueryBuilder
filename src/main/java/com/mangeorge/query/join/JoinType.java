package com.mangeorge.query.join;


/**
 * Represents ways to join tables in queries.
 *
 * @author George Beliy on 10-01-2020
 */
public enum JoinType {

    /**
     * Scalar product.
     */
    SCALAR() {
        public String getJoinString() {
            return ",";
        }
    },

    /**
     * Internal connection.
     */
    INNER() {
        public String getJoinString() {
            return ",";
        }
    },

    /**
     * External connection.
     */
    LEFT_OUTER() {
        public String getJoinString() {
            return " left outer join";
        }
    };

    /**
     * @return a string that must be used in the HQL query to apply this type of table connection.
     */
    public abstract String getJoinString();

}