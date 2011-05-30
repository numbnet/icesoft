/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
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
import java.util.Comparator;
import java.util.Arrays;
import javax.faces.component.UIComponent;
import org.apache.commons.beanutils.BeanUtils;

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
    
    public static Field[] getStateFieldsForComponent(UIComponent comp) {
        Field[] fields = comp.getClass().getDeclaredFields();
        Arrays.sort(fields, FIELD_NAME_COMPARATOR);
        ArrayList fieldsArrayList = new ArrayList(fields.length);
//System.out.println("Collecting all fields");
        for(int i = 0; i < fields.length; i++) {
            Field field = fields[i];
//System.out.println("Potential: " + field.getName());
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) ||
                Modifier.isTransient(modifiers) ||
                Modifier.isFinal(modifiers)) {
                continue;
            }
            if (!Modifier.isPublic(modifiers)) {
                field.setAccessible(true);
            }
//System.out.println("                              USED");
            fieldsArrayList.add(field);
        }
        Field[] relevantFields = new Field[fieldsArrayList.size()];
        fieldsArrayList.toArray(relevantFields);
        return relevantFields;
    }
    
    private static class FieldNameComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            Field f1 = (Field) o1;
            Field f2 = (Field) o2;
            return f1.getName().compareTo(f2.getName());
        }
    }
    private static final Comparator FIELD_NAME_COMPARATOR = new FieldNameComparator();
    


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
