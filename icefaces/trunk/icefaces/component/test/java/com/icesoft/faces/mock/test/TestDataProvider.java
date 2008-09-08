/*
 *
 */
package com.icesoft.faces.mock.test;

import com.icesoft.faces.mock.test.data.MockDataList;
import com.icesoft.faces.mock.test.data.MockDataObject;
import com.sun.faces.mock.MockMethodBinding;
import java.util.HashMap;

/**
 *
 * @author fye
 */
public class TestDataProvider {

    private final static String[] keyStrings = new String[]{
        "com.icesoft.faces.mock.test.data.MockDataObject",
        "java.util.List",
        "java.lang.Double",
        "java.lang.String",
        "boolean",
        "com.icesoft.faces.context.effects.Effect",
        "java.lang.Object",
        "javax.faces.el.MethodBinding",
        "int",
        "java.lang.Integer",
        "java.util.Date",
        "java.io.File",
        "java.util.Map",
        "javax.faces.convert.Converter",
        "java.lang.Boolean",
        "javax.faces.component.UIComponent",
        "com.icesoft.faces.utils.UpdatableProperty"
    };       
    private final static Object[] valueObjects = new Object[]{
        new MockDataObject("empty"),
        new MockDataList(),
        1.009d,
        "test1",
        Boolean.FALSE,
        new com.icesoft.faces.context.effects.Move(),
        new MockDataObject("objectValue"),        
        new MockMethodBinding(),
        9,
        new Integer(99),
        new java.util.Date(System.currentTimeMillis()),
        new java.io.File("com/icesoft/faces/mock/test"),
        new java.util.HashMap(),
        "javax.faces.convert.Converter",
        new Boolean(false),
        new com.icesoft.faces.component.ext.HtmlInputText(),
        new com.icesoft.faces.utils.UpdatableProperty("test")
    };
    private static HashMap<String, Object> propMap = new HashMap<String, Object>(keyStrings.length);


    static {
        for (int i = 0; i < keyStrings.length; i++) {
            propMap.put(keyStrings[i], valueObjects[i]);
        }
    }

    public Object getSimpleTestObject(String key) {
        return propMap.get(key);
    }

    public int getMatchClass(String matchName) {
//            "java.util.List", //1
//            "java.lang.Object", //2
//            "java.lang.String", //3
//            "boolean", //4
//            "com.icesoft.faces.context.effects.Effect", //5
//            "java.lang.Object", //6
//            "javax.faces.el.MethodBinding",//7
//            "int",//8
//            "java.lang.Integer",//9
//            "java.util.Date", //10
//            "java.io.File", //11
//            "java.util.Map", //12
//            "javax.faces.convert.Converter", //13
//            "java.lang.Boolean", //14
//            "javax.faces.component.UIComponent", //15
//            "com.icesoft.faces.utils.UpdatableProperty" //16

        for (int i = 0; i < keyStrings.length; i++) {
            if (matchName.equals(keyStrings[i])) {
                return i;
            }
        }
        return -1;
    }
    
    
}
