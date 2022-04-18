package com.mangeorge.query.helper;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.type.AbstractStandardBasicType;
import org.hibernate.type.StandardBasicTypes;

import java.util.Date;


/**
 * Helper for working with HQL
 *
 * @author George Beliy on 10-01-2020
 */
public class HQLBuilderHelper {

    /**
     * @param clazz class for which you want to generate an alias
     * @return generated alias
     */
    public static String generateAliasForClass(Class<?> clazz) {
        return StringUtils.uncapitalize(clazz.getSimpleName());
    }

    /**
     * Defines hibernate type of the object, depending on the java type.
     * Only the definition of Short, Integer, Float, Double, Long, Boolean, Date and String types is supported.
     * If the class is of some other type, then Hibernate.STRING type is returned.
     * Hibernate.STRING is also returned if the <code>null</code> clazz.
     * If the passed class is an array, then the method returns the class of its elements.
     *
     * @param clazz class for which you want to define hibernate type
     * @return hibernate type of object
     */
    public static AbstractStandardBasicType<?> getHibernateType(Class<?> clazz) {
        if (clazz != null) {
            if (Long.class.equals(clazz))
                return StandardBasicTypes.LONG;
            if (Double.class.equals(clazz))
                return StandardBasicTypes.DOUBLE;
            if (Float.class.equals(clazz))
                return StandardBasicTypes.FLOAT;
            if (Integer.class.equals(clazz))
                return StandardBasicTypes.INTEGER;
            if (Short.class.equals(clazz))
                return StandardBasicTypes.SHORT;
            if (Boolean.class.equals(clazz))
                return StandardBasicTypes.BOOLEAN;
            if (String.class.equals(clazz))
                return StandardBasicTypes.STRING;
            if (Date.class.equals(clazz))
                return StandardBasicTypes.DATE;
            if (clazz.isArray()) {
                return getHibernateType(clazz.getComponentType());
            }
        }
        return StandardBasicTypes.STRING;
    }

}