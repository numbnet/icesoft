package org.icefaces.ace.util.collections;


import java.io.Serializable;
import java.util.Collection;

public final class AllPredicate implements  Predicate,
        PredicateDecorator, Serializable {

    /** Serial version UID */
    private static final long serialVersionUID = -3094696765038308799L;

    /** The array of predicates to call */
    private final Predicate[] iPredicates;

    /**
     * Factory to create the predicate.
     * <p>
     * If the array is size zero, the predicate always returns true.
     * If the array is size one, then that predicate is returned.
     *
     * @param predicates  the predicates to check, cloned, not null
     * @return the <code>all</code> predicate
     * @throws IllegalArgumentException if the predicates array is null
     * @throws IllegalArgumentException if any predicate in the array is null
     */
    public static Predicate getInstance(Predicate[] predicates) {
        FunctorUtils.validate(predicates);
        if (predicates.length == 0) {
            return TruePredicate.INSTANCE;
        }
        if (predicates.length == 1) {
            return predicates[0];
        }
        predicates = FunctorUtils.copy(predicates);
        return new AllPredicate(predicates);
    }

    /**
     * Factory to create the predicate.
     * <p>
     * If the collection is size zero, the predicate always returns true.
     * If the collection is size one, then that predicate is returned.
     *
     * @param predicates  the predicates to check, cloned, not null
     * @return the <code>all</code> predicate
     * @throws IllegalArgumentException if the predicates array is null
     * @throws IllegalArgumentException if any predicate in the array is null
     */
    public static Predicate getInstance(Collection predicates) {
        Predicate[] preds = FunctorUtils.validate(predicates);
        if (preds.length == 0) {
            return TruePredicate.INSTANCE;
        }
        if (preds.length == 1) {
            return preds[0];
        }
        return new AllPredicate(preds);
    }

    /**
     * Constructor that performs no validation.
     * Use <code>getInstance</code> if you want that.
     *
     * @param predicates  the predicates to check, not cloned, not null
     */
    public AllPredicate(Predicate[] predicates) {
        super ();
        iPredicates = predicates;
    }

    /**
     * Evaluates the predicate returning true if all predicates return true.
     *
     * @param object  the input object
     * @return true if all decorated predicates return true
     */
    public boolean evaluate(Object object) {
        for (int i = 0; i < iPredicates.length; i++) {
            if (iPredicates[i].evaluate(object) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the predicates, do not modify the array.
     *
     * @return the predicates
     * @since Commons Collections 3.1
     */
    public Predicate[] getPredicates() {
        return iPredicates;
    }

}
