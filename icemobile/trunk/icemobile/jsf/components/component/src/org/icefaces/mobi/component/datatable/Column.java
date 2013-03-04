/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobi.component.datatable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.el.ValueExpression;

import org.icemobile.component.IColumn;

public class Column extends ColumnBase implements IColumn{
    
    private static final Logger LOG =
            Logger.getLogger(Column.class.toString());
    
    private Object baseVar;
    private Method[] propertyChainMethods;
    private ValueExpression ve;
    private String format;
    private String simpleExpr;
    
    protected void setBaseVar(Object obj){
        this.baseVar = obj;
    }

    /**
     * Get the column text value from a cached method expression. The baseVar
     * needs to be set by the parent for value resolution. Once the value is
     * resolved the baseVar will be set to null.
     * 
     * @see org.icemobile.component.IColumn#getValueFromCachedExpression()
     */
    @Override
    public String getValueFromCachedExpression() {
        if( ve == null ){
            ve = getValueExpression("value");
            String expr = ve.getExpressionString();
            if( expr != null && expr.indexOf("#{") > -1){
                LOG.warning("expr="+expr);
                simpleExpr = expr.substring(expr.indexOf("#{"),expr.indexOf('}')+1);
                LOG.warning("simpleExpr: " + simpleExpr);
                format = expr.replace(simpleExpr, "%s");
                LOG.warning("format=" + format);
                simpleExpr = simpleExpr.substring(2,simpleExpr.length()-1);
                LOG.warning("simplExpr=" + simpleExpr);
                
                //LOG.warning("expr=" + expr + ", simplExpr=" + simpleExpr);
            }
        }
        if( baseVar == null ){
            return super.getValue();
        }
        else{
            if( propertyChainMethods == null ){
                String[] parts = simpleExpr.split("\\.");
                
                propertyChainMethods = new Method[parts.length-1];
                Class base = baseVar.getClass();
                for( int i = 1 ; i < parts.length ; i++ ){
                    try {
                        
                        String getter = "get" + parts[i].substring(0,1).toUpperCase() 
                                + parts[i].substring(1);
                        //LOG.warning("getter: " + getter);
                        propertyChainMethods[i-1] = base.getMethod(getter);
                        //LOG.warning("method: " + propertyChainMethods[i-1]);
                        base = propertyChainMethods[i-1].getClass();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
            Object obj = baseVar;
            for( Method method : propertyChainMethods ){
                //LOG.warning("resolving method: " + method);
                try {
                    obj = method.invoke(obj);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            baseVar = null;
            return String.format(format, (obj != null ? obj.toString() : ""));
        }
    }


}
