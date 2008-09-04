/*
 * TODO
 */
package com.icesoft.faces.mock.test;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponent;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author fye
 */
public class CompPropsUtils {

    public static void describe_useRefl_field(UIComponent uiComponent, Map fieldMap) {

        Field[] fields = uiComponent.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }

            if (Modifier.isPrivate(field.getModifiers()) || Modifier.isProtected(field.getModifiers()) || Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(true);
                fieldMap.put(field.getName(), field.getType().getName());
            }
        }
    }

    public static Method[] describe_useRef_method(UIComponent uiComponent) {

        List<Method> list = new ArrayList<Method>();
        Method[] methods = uiComponent.getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (Modifier.isPublic(method.getModifiers()) || Modifier.isProtected(method.getModifiers())) {
                list.add(method);
            //(method.getName(), method.getReturnType().getName());
            }
        }
        return list.toArray(new Method[list.size()]);
    }

    public static void describe_useMetaBeanInfo(UIComponent uiComponent, Map classesMap) {
        try {
            Class beanInfoClass = Class.forName(uiComponent.getClass().getName() + "BeanInfo");
            Object object = beanInfoClass.newInstance();

            if (object instanceof SimpleBeanInfo) {
                SimpleBeanInfo simpleBeanInfo = (SimpleBeanInfo) object;
                PropertyDescriptor[] props = simpleBeanInfo.getPropertyDescriptors();
                //String message = uiComponent.getClass().getName() + " Method name=";
                for (int i = 0; i < props.length; i++) {
                    //message += " " + props[i].getName() + " class=" + props[i].getPropertyType() + ", ";
                    classesMap.put(props[i].getName(), props[i].getPropertyType().getName());
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void describe_useBeanUtils(UIComponent uiComponent, Map propsMap) {
        String message = "failed component class=" + uiComponent.getClass().getName() + " ";
        try {

            propsMap = BeanUtils.describe(uiComponent);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            System.exit(1);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
