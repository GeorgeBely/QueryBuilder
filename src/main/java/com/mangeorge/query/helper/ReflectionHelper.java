package com.mangeorge.query.helper;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.AnnotationException;

import javax.management.AttributeNotFoundException;
import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Helper for work with reflection
 *
 * @author George Beliy on 10-01-2020
 */
@SuppressWarnings("WeakerAccess")
public class ReflectionHelper {

    /**
     * Finding a fields with annotation {#annotationClass} in class {#clazz}
     *
     * @param clazz            class in which you want to find a field with an annotation {#annotationClass}
     * @param annotationClass  class of the desired annotation
     * @return list of fields with annotation {#annotationClass}
     */
    public static List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Field> result = new ArrayList<>();
        while (clazz != null) {
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(annotationClass)) {
                    result.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    /**
     * @param clazz class whose id field is to be found
     * @return field name of entity is marked with an annotation {@link Id}
     */
    public static <T> String getIdFieldName(Class<T> clazz) {
        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(clazz, Id.class);
        if (!fields.isEmpty())
            return fields.get(0).getName();
        throw new AnnotationException("Id annotation not present in class " + clazz.getSimpleName());
    }


    /**
     * @return Generated path by fields
     */
    public static String generatePathByFields(Field... fields) {
        return Arrays.stream(fields).map(Field::getName).collect(Collectors.joining("."));
    }

    /**
     * @param fieldName name of the field
     * @param clazz     class whose field is to be found
     * @return {@link Field} object or <code>null</code> if field not be found
     */
    public static Field getFieldByFieldName(String fieldName, Class<?> clazz) {
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    /**
     * Get field value by path
     *
     * @param obj the object whose field you want to retrieve
     * @param fieldPath path to field
     * @return value by field path
     *
     * @throws AttributeNotFoundException If path {#fieldPath} is incorrect
     * @throws InvocationTargetException If the underlying method throws an exception
     */
    public static Object getValueByFieldPath(Object obj, String fieldPath)
            throws AttributeNotFoundException, InvocationTargetException, IllegalAccessException {
        FIELD: for (String dynamicField : fieldPath.split("\\.")) {
            Set<Method> declaredMethods = getAllDeclaredMethods(obj.getClass(), true, false);
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(dynamicField)
                        || declaredMethod.getName().equals("get" + StringUtils.capitalize(dynamicField))
                        || declaredMethod.getName().equals("is" + StringUtils.capitalize(dynamicField))) {
                    obj = declaredMethod.invoke(obj);
                    if (obj == null) {
                        return null;
                    } else {
                        continue FIELD;
                    }
                }
            }
            throw new AttributeNotFoundException("Class " + obj.getClass().getName()
                    + " not have public method with name " + dynamicField + " or get" + StringUtils.capitalize(dynamicField));
        }
        return obj;
    }

    /**
     * @param clazz class, the methods of which must be found
     * @param getSuperclassMethods add superclass methods
     * @param getInterfacesMethods add interfaces methods
     * @return set of all public methods in class {#clazz} and his parent classes
     */
    public static Set<Method> getAllDeclaredMethods(Class<?> clazz, boolean getSuperclassMethods, boolean getInterfacesMethods) {
        Set<Method> methods = new HashSet<>();
        while (clazz != null) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (Modifier.isPublic(method.getModifiers())) {
                    methods.add(method);
                }
            }
            if (getInterfacesMethods) {
                for (Class<?> classInterface : clazz.getInterfaces()) {
                    methods.addAll(getAllDeclaredMethods(classInterface, false, true));
                }
            }
            clazz = getSuperclassMethods ? clazz.getSuperclass() : null;
        }
        return methods;
    }

}
