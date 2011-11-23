package org.icefaces.ace.util.collections;


import java.io.Serializable;

/**
 * Predicate implementation that always returns true.
 *
 * @since Commons Collections 3.0
 * @version $Revision: 646777 $ $Date: 2008-04-10 13:33:15 +0100 (Thu, 10 Apr 2008) $
 *
 * @author Stephen Colebourne
 */
public final class TruePredicate implements  Predicate, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = 3374767158756189740L;

    /** Singleton predicate instance */
    public static final Predicate INSTANCE = new TruePredicate();

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
    private TruePredicate() {
        super ();
    }

    /**
     * Evaluates the predicate returning true always.
     *
     * @param object  the input object
     * @return true always
     */
    public boolean evaluate(Object object) {
        return true;
    }

}