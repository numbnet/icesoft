package org.icefaces.ace.component.animation;

import java.io.Serializable;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;


/**
 *
 */
public class BehaviorStateHolderSaver {

    private String className;
    private Serializable savedState = null;



     public BehaviorStateHolderSaver(FacesContext context, Object toSave) {
        className = toSave.getClass().getName();

        if (toSave instanceof StateHolder) {
            // do not save an attached object that is marked transient.
            if (!((StateHolder) toSave).isTransient()) {
                savedState =
                      (Serializable) ((StateHolder) toSave).saveState(context);
            } else {
                className = null;
            }
        } else if (toSave instanceof Serializable) {
            savedState = (Serializable) toSave;
            className = null;
        }
    }

      public Object restore(FacesContext context) throws IllegalStateException {
        Object result = null;
        Class toRestoreClass;

        // if the Object to save implemented Serializable but not
        // StateHolder
        if (null == className && null != savedState) {
            return savedState;
        }

        // if the Object to save did not implement Serializable or
        // StateHolder
        if (className == null) {
            return null;
        }

        // else the object to save did implement StateHolder

        try {
            toRestoreClass = loadClass(className, this);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        if (null != toRestoreClass) {
            try {
                result = toRestoreClass.newInstance();
            }
            catch (InstantiationException e) {
                throw new IllegalStateException(e);
            }
            catch (IllegalAccessException a) {
                throw new IllegalStateException(a);
            }
        }

        if (null != result && null != savedState &&
            result instanceof StateHolder) {
            // don't need to check transient, since that was done on
            // the saving side.
            ((StateHolder) result).restoreState(context, savedState);
        }
        return result;
    }

      private static Class loadClass(String name,
            Object fallbackClass) throws ClassNotFoundException {
        ClassLoader loader =
            Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return Class.forName(name, false, loader);
    }
}
