package org.icefaces.ace.util.collections;

import java.util.Collection;
import java.util.Iterator;


/**
 * Internal utilities for functors.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Stephen Colebourne
 * @author Matt Benson
 */
class FunctorUtils {

    /**
     * Restricted constructor.
     */
    private FunctorUtils() {
        super ();
    }

    /**
     * Clone the predicates to ensure that the internal reference can't be messed with.
     *
     * @param predicates  the predicates to copy
     * @return the cloned predicates
     */
    static Predicate[] copy(Predicate[] predicates) {
        if (predicates == null) {
            return null;
        }
        return (Predicate[]) predicates.clone();
    }

    /**
     * Validate the predicates to ensure that all is well.
     *
     * @param predicates  the predicates to validate
     */
    static void validate(Predicate[] predicates) {
        if (predicates == null) {
            throw new IllegalArgumentException(
                    "The predicate array must not be null");
        }
        for (int i = 0; i < predicates.length; i++) {
            if (predicates[i] == null) {
                throw new IllegalArgumentException(
                        "The predicate array must not contain a null predicate, index "
                                + i + " was null");
            }
        }
    }

    /**
     * Validate the predicates to ensure that all is well.
     *
     * @param predicates  the predicates to validate
     * @return predicate array
     */
    static Predicate[] validate(Collection predicates) {
        if (predicates == null) {
            throw new IllegalArgumentException(
                    "The predicate collection must not be null");
        }
        // convert to array like this to guarantee iterator() ordering
        Predicate[] preds = new Predicate[predicates.size()];
        int i = 0;
        for (Iterator it = predicates.iterator(); it.hasNext();) {
            preds[i] = (Predicate) it.next();
            if (preds[i] == null) {
                throw new IllegalArgumentException(
                        "The predicate collection must not contain a null predicate, index "
                                + i + " was null");
            }
            i++;
        }
        return preds;
    }
}
