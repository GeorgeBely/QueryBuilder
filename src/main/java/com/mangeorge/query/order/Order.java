package com.mangeorge.query.order;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;

import java.lang.reflect.Field;


/**
 * Sort object. Used to add sorting to a database query.
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Order {

    public static final String ORDER_DIRECT = "asc";
    public static final String ORDER_REVERSE = "desc";


    private String field;
    private boolean reverse;
    private String alias;
    private Order additionalOrder;


    public Order(String field) {
        this(field, null, false, null);
    }

    public Order(String field, Order additionalOrder) {
        this(field, null, false, additionalOrder);
    }

    public Order(String field, boolean reverse, Order additionalOrder) {
        this(field, null, reverse, additionalOrder);
    }

    public Order(String field, boolean reverse) {
        this(field, null, reverse, null);
    }

    public Order(String field, String alias) {
        this(field, alias, false, null);
    }

    public Order(Field field) {
        this(field, null, false, null);
    }

    public Order(Field field, boolean reverse) {
        this(field, null, reverse, null);
    }

    public Order(Field field, boolean reverse, Order additionalOrder) {
        this(field, null, reverse, additionalOrder);
    }

    public Order(Field field, Order additionalOrder) {
        this(field, null, false, additionalOrder);
    }

    public Order(Field field, String alias) {
        this(field, alias,  false, null);
    }

    public Order(String field, String alias, boolean reverse) {
        this(field, alias,  reverse, null);
    }

    public Order(Field field, String alias, boolean reverse, Order additionalOrder) {
        this(field.getName(), alias, reverse, additionalOrder);
    }

    /**
     * @param fieldPath       fieldPath for sorting
     * @param alias           alias for fieldPath
     * @param reverse         if is <code>true</code> then the {@value ORDER_REVERSE} sort will be used
     *                        else {@value ORDER_DIRECT} sort will be used
     * @param additionalOrder sort that must be added after this
     */
    public Order(String fieldPath, String alias, boolean reverse, Order additionalOrder) {
        this.field = fieldPath;
        this.alias = alias;
        this.reverse = reverse;
        this.additionalOrder = additionalOrder;
    }


    /**
     * @return last additional sorting
     */
    private Order getLastAdditionalOrder() {
        if (additionalOrder != null)
            return additionalOrder.getLastAdditionalOrder();

        return this;
    }

    /**
     * @param alias alias for fields that do not have their own
     * @return expression for 'order by' as the HQL string.
     */
    public String getExpression(String alias) {
        StringBuilder expression = new StringBuilder();
        if (this.alias != null) {
            expression.append(this.alias).append(".");
        } else if (alias != null)
            expression.append(alias).append(".");

        expression.append(field).append(" ").append(reverse ? ORDER_REVERSE : ORDER_DIRECT);
        if (additionalOrder != null)
            expression.append(", ").append(additionalOrder.getExpression(alias));

        return expression.toString();
    }

    /**
     * Add orders to criteria
     *
     * @param criteria criteria with added sorts
     */
    public void addToCriteria(DetachedCriteria criteria) {
        String fieldPath = (alias != null ? alias + "." : "") + field;
        criteria.addOrder(reverse ? org.hibernate.criterion.Order.desc(fieldPath)
                : org.hibernate.criterion.Order.asc(fieldPath));

        if (additionalOrder != null) {
            additionalOrder.addToCriteria(criteria);
        }
    }

    /**
     * Adds an additional sort order
     *
     * @param order sort object
     */
    public void addAdditionalOrder(Order order) {
        getLastAdditionalOrder().additionalOrder = order;
    }

    /**
     * Add prefix for alias current order and all additional orders.
     *
     * @param prefix prefix for alias name
     */
    public void addAliasPrefix(String prefix) {
        if (StringUtils.isNotBlank(alias)) {
            setAlias(prefix + alias);
        }
        if (additionalOrder != null) {
            additionalOrder.addAliasPrefix(prefix);
        }
    }

    /**
     * Generate order by fields
     *
     * @return generated {@link Order} object
     */
    public static Order getOrders(boolean reverse, Field... fields) {
        Field mainField = null;
        Order order = null;
        for (Field field : fields) {
            if (mainField == null) {
                mainField = field;
            } else {
                if (order == null) {
                    order = new Order(field);
                } else {
                    order.addAdditionalOrder(new Order(field));
                }
            }
        }
        if (mainField == null)
            return null;
        return new Order(mainField, null, reverse, order);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Order getAdditionalOrder() {
        return additionalOrder;
    }
}