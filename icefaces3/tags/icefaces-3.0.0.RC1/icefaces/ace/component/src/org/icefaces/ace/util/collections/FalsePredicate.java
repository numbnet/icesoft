package org.icefaces.ace.util.collections;


import java.io.Serializable;

/**
 * Predicate implementation that always returns false.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Stephen Colebourne
 */
public final class FalsePredicate implements Predicate, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = 7533784454832764388L;

    /** Singleton predicate instance */
    public static final Predicate INSTANCE = new FalsePredicate();

    /**
     * Factory returning the singleton instance.
     *
     * @return the singleton instance
     * @since Commons Collections 3.1
     */
    public static Predicate getInstance() {
        return INSTANCE;
    }

    /**
     * Restricted constructor.
     */
    private FalsePredicate() {
        super();
    }

    /**
     * Evaluates the predicate returning false always.
     *
     * @param object  the input object
     * @return false always
     */
    public boolean evaluate(Object object) {
        return false;
    }

}