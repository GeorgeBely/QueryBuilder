package com.mangeorge.query.join;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;

import java.lang.reflect.Field;
import java.util.Objects;


/**
 * Alias for the class, used to build the hql or criteria query.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings("unused")
public class QueryJoin {

    /**
     * JoinType type
     */
    private final JoinType joinType;

    /**
     * Alias of the root object
     */
    private String rootAlias;

    /**
     * The field name in the root object for binding
     */
    private final String rootJoinField;

    /**
     * Class name of the adjoining object
     */
    private String adjoiningClassName;

    /**
     * Alias of the adjoining object
     */
    private String adjoiningAlias;

    /**
     * The field name in the adjoining object for binding
     */
    private String adjoiningJoinField;


    /**
     * Create join for classes. For binding, used join type {#joinType}.
     *
     * @param rootAlias          alias for root entity
     * @param rootJoinField      the field name in the root entity for binding
     * @param adjoiningClassName class name of adjoining entity
     * @param adjoiningAlias     alias for adjoining entity
     * @param adjoiningJoinField the field name in the adjoining entity for binding
     * @param joinType           the join type
     */
    public QueryJoin(String rootAlias, String rootJoinField, String adjoiningClassName,
                     String adjoiningAlias, String adjoiningJoinField, JoinType joinType) {
        this.rootAlias = rootAlias;
        this.rootJoinField = rootJoinField;
        this.adjoiningClassName = adjoiningClassName;
        this.adjoiningAlias = adjoiningAlias;
        this.adjoiningJoinField = adjoiningJoinField;
        this.joinType = joinType;
    }

    /**
     * Create join for classes. For binding, used join type {#joinType}.
     *
     * @param rootAlias          alias for root entity
     * @param adjoiningClass     class of adjoining entity
     * @param rootJoinField      the field name in the root entity for binding
     * @param adjoiningJoinField the field name in the adjoining entity for binding
     * @param joinType           the join type
     */
    public QueryJoin(String rootAlias, Class<?> adjoiningClass, String rootJoinField,
                     String adjoiningJoinField, JoinType joinType) {
        this(rootAlias, rootJoinField, adjoiningClass.getSimpleName(),
                StringUtils.uncapitalize(adjoiningClass.getSimpleName()), adjoiningJoinField, joinType);
    }

    /**
     * Create aliases for classes. For binding, used join type {#joinType}.
     *
     * @param rootClass          class of root entity
     * @param adjoiningClass     class of adjoining entity
     * @param rootJoinField      the field name in the root entity for binding
     * @param adjoiningJoinField the field name in the adjoining entity for binding
     * @param joinType           the join type
     */
    public QueryJoin(Class<?> rootClass, Class<?> adjoiningClass, String rootJoinField,
                     String adjoiningJoinField, JoinType joinType) {
        this(StringUtils.uncapitalize(rootClass.getSimpleName()), adjoiningClass, rootJoinField, adjoiningJoinField, joinType);
    }

    /**
     * Create aliases for classes. For binding, used join type {#joinType}.
     *
     * @param rootJoinField      the field of root entity for binding
     * @param adjoiningJoinField the field of adjoining entity for binding
     * @param joinType           the join type
     */
    @SuppressWarnings("unchecked")
    public QueryJoin(Field rootJoinField, Field adjoiningJoinField, JoinType joinType) {
        this((Class<?>) rootJoinField.getDeclaringClass(),
                (Class<?>) adjoiningJoinField.getDeclaringClass(),
                rootJoinField, adjoiningJoinField, joinType);
    }

    /**
     * Create aliases for classes. For binding, used join type {#joinType}.
     * Generates a root alias from the {#rootJoinField} declared class!!!
     * Generates an adjoining alias from the {#adjoiningJoinField} declared class!!!
     *
     * @param rootClass          class of root entity
     * @param adjoiningClass     class of adjoining entity
     * @param rootJoinField      the field of root entity for binding
     * @param adjoiningJoinField the field of adjoining entity for binding
     * @param joinType           the join type
     */
    public QueryJoin(Class<?> rootClass, Class<?> adjoiningClass,
                     Field rootJoinField, Field adjoiningJoinField, JoinType joinType) {
        this(rootClass, adjoiningClass, rootJoinField.getName(), adjoiningJoinField.getName(), joinType);
    }

    /**
     * Create aliases for classes. For binding, used join type {#joinType}.
     * Generates an adjoining alias from the {#adjoiningJoinField} declared class!!!
     *
     * @param rootAlias          alias for root entity
     * @param rootJoinField      the field of root entity for binding
     * @param adjoiningJoinField the field of adjoining entity for binding
     * @param joinType           the join type
     */
    @SuppressWarnings("unchecked")
    public QueryJoin(String rootAlias, Field rootJoinField, Field adjoiningJoinField, JoinType joinType) {
        this(rootAlias, (Class<?>) adjoiningJoinField.getDeclaringClass(),
                rootJoinField.getName(), adjoiningJoinField.getName(), joinType);
    }

    /**
     * Create aliases for classes. For binding, used join type {#joinType}.
     *
     * @param rootAlias          alias for root entity
     * @param adjoiningAlias     alias for adjoining entity
     * @param rootJoinField      the field of root entity for binding
     * @param adjoiningJoinField the field of adjoining entity for binding
     * @param joinType           the join type
     */
    public QueryJoin(String rootAlias, String adjoiningAlias, Field rootJoinField, Field adjoiningJoinField, JoinType joinType) {
        this(rootAlias, rootJoinField.getName(),
                adjoiningJoinField.getDeclaringClass().getSimpleName(),
                adjoiningAlias, adjoiningJoinField.getName(), joinType);
    }

    /**
     * Create join for classes. For binding, used JoinType.INNER.
     *
     * @param rootAlias           alias for root entity
     * @param rootJoinField       the field name in the root entity for binding
     * @param adjoiningClassName  class name of adjoining entity
     * @param adjoiningAlias      alias for adjoining entity
     * @param adjoiningJoinField  the field name in the adjoining entity for binding
     */
    public QueryJoin(String rootAlias, String rootJoinField, String adjoiningClassName,
                     String adjoiningAlias, String adjoiningJoinField) {
        this(rootAlias, rootJoinField, adjoiningClassName, adjoiningAlias, adjoiningJoinField, JoinType.INNER);
    }

    /**
     * Create join for classes. For binding, used JoinType.INNER.
     *
     * @param rootAlias           alias for root entity
     * @param adjoiningClass      class of adjoining entity
     * @param rootJoinField       the field name in the root entity for binding
     * @param adjoiningJoinField  the field name in the adjoining entity for binding
     */
    public QueryJoin(String rootAlias, Class<?> adjoiningClass, String rootJoinField,
                     String adjoiningJoinField) {
        this(rootAlias, adjoiningClass, rootJoinField, adjoiningJoinField, JoinType.INNER);
    }

    /**
     * Create aliases for classes. For binding, used JoinType.INNER.
     *
     * @param rootClass          class of root entity
     * @param adjoiningClass     class of adjoining entity
     * @param rootJoinField      the field name in the root entity for binding
     * @param adjoiningJoinField the field name in the adjoining entity for binding
     */
    public QueryJoin(Class<?> rootClass, Class<?> adjoiningClass, String rootJoinField,
                     String adjoiningJoinField) {
        this(rootClass, adjoiningClass, rootJoinField, adjoiningJoinField, JoinType.INNER);
    }

    /**
     * Create aliases for classes. For binding, used JoinType.INNER.
     *
     * @param rootJoinField       the field of root entity for binding
     * @param adjoiningJoinField  the field of adjoining entity for binding
     */
    public QueryJoin(Field rootJoinField, Field adjoiningJoinField) {
        this(rootJoinField, adjoiningJoinField, JoinType.INNER);
    }

    /**
     * Create aliases for classes. For binding, used JoinType.INNER.
     * Generates a root alias from the {#rootJoinField} declared class!!!
     * Generates an adjoining alias from the {#adjoiningJoinField} declared class!!!
     *
     * @param rootClass           class of root entity
     * @param adjoiningClass      class of adjoining entity
     * @param rootJoinField       the field of root entity for binding
     * @param adjoiningJoinField  the field of adjoining entity for binding
     */
    public QueryJoin(Class<?> rootClass,  Class<?> adjoiningClass,
                     Field rootJoinField, Field adjoiningJoinField) {
        this(rootClass, adjoiningClass, rootJoinField, adjoiningJoinField, JoinType.INNER);
    }

    /**
     * Create aliases for classes. For binding, used JoinType.INNER.
     * Generates an adjoining alias from the {#adjoiningJoinField} declared class!!!
     *
     * @param rootAlias          alias for root entity
     * @param rootJoinField      the field of root entity for binding
     * @param adjoiningJoinField the field of adjoining entity for binding
     */
    public QueryJoin(String rootAlias, Field rootJoinField, Field adjoiningJoinField) {
        this(rootAlias, rootJoinField, adjoiningJoinField, JoinType.INNER);
    }

    /**
     * Create aliases for classes. For binding, used JoinType.INNER.
     *
     * @param rootAlias          alias for root entity
     * @param adjoiningAlias     alias for adjoining entity
     * @param rootJoinField      the field of root entity for binding
     * @param adjoiningJoinField the field of adjoining entity for binding
     */
    public QueryJoin(String rootAlias, String adjoiningAlias, Field rootJoinField, Field adjoiningJoinField) {
        this(rootAlias, adjoiningAlias, rootJoinField, adjoiningJoinField, JoinType.INNER);
    }

    /**
     * Create aliases for classes. For binding, used JoinType.LEFT_OUTER.
     *
     * @param rootAlias       alias for root entity
     * @param rootJoinField   the field name in the root entity for binding
     * @param adjoiningAlias  alias for adjoining entity
     */
    public QueryJoin(String rootAlias, String rootJoinField, String adjoiningAlias) {
        this.rootAlias = rootAlias;
        this.rootJoinField = rootJoinField;
        this.adjoiningAlias = adjoiningAlias;
        this.joinType = JoinType.LEFT_OUTER;
    }

    /**
     * Create aliases for classes. For binding, used JoinType.LEFT_OUTER.
     * Generates an alias from the {#rootJoinField} declared class!!!
     *
     * @param rootJoinField the field of root entity for binding
     */
    public QueryJoin(Field rootJoinField) {
        this(StringUtils.uncapitalize(rootJoinField.getDeclaringClass().getSimpleName()), rootJoinField);
    }

    /**
     * Create aliases for classes. For binding, used JoinType.LEFT_OUTER.
     *
     * @param rootAlias     alias for root entity
     * @param rootJoinField the field of root entity for binding
     */
    public QueryJoin(String rootAlias, Field rootJoinField) {
        this(rootAlias, rootJoinField.getName(),
                StringUtils.uncapitalize(rootJoinField.getType().getSimpleName()));
    }

    /**
     * Create aliases for classes. For binding, used JoinType.LEFT_OUTER.
     *
     * @param rootJoinField  the field of root entity for binding
     * @param adjoiningAlias alias for adjoining entity
     */
    public QueryJoin(Field rootJoinField, String adjoiningAlias) {
        this(StringUtils.uncapitalize(rootJoinField.getDeclaringClass().getSimpleName()),
                rootJoinField.getName(), adjoiningAlias);
    }

    /**
     * @return HQL joinType string
     */
    public String getHQLExpression() {
        if (JoinType.INNER.equals(joinType)) {
            return joinType.getJoinString() + " " + adjoiningClassName + " " + adjoiningAlias;
        } else if (JoinType.LEFT_OUTER.equals(joinType)) {
            return joinType.getJoinString() + " " + rootAlias + "." + rootJoinField + " " + adjoiningAlias;
        }
        return "";
    }

    /**
     * @return HQL where string for joinType
     */
    public String getHQLWhereExpression() {
        if (JoinType.INNER.equals(joinType)) {
            return rootAlias + "." + rootJoinField + " = " + adjoiningAlias + "." + adjoiningJoinField;
        }
        return "";
    }

    /**
     * @return criteria with joinType alias
     */
    public DetachedCriteria addCriteriaAlias(DetachedCriteria criteria) {
        if (JoinType.LEFT_OUTER.equals(joinType)) {
            return criteria.createAlias(rootAlias + "." + rootJoinField, adjoiningAlias);
        }
        return criteria;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public String getRootAlias() {
        return rootAlias;
    }

    public void setRootAlias(String rootAlias) {
        this.rootAlias = rootAlias;
    }

    public String getRootJoinField() {
        return rootJoinField;
    }

    public String getAdjoiningClassName() {
        return adjoiningClassName;
    }

    public String getAdjoiningAlias() {
        return adjoiningAlias;
    }

    public void setAdjoiningAlias(String adjoiningAlias) {
        this.adjoiningAlias = adjoiningAlias;
    }

    public String getAdjoiningJoinField() {
        return adjoiningJoinField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryJoin queryJoin = (QueryJoin) o;
        return joinType == queryJoin.joinType &&
                Objects.equals(rootAlias, queryJoin.rootAlias) &&
                Objects.equals(rootJoinField, queryJoin.rootJoinField) &&
                Objects.equals(adjoiningClassName, queryJoin.adjoiningClassName) &&
                Objects.equals(adjoiningAlias, queryJoin.adjoiningAlias) &&
                Objects.equals(adjoiningJoinField, queryJoin.adjoiningJoinField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joinType, rootAlias, rootJoinField, adjoiningClassName, adjoiningAlias, adjoiningJoinField);
    }
}
