package org.icefaces.ace.util.collections;

public interface PredicateDecorator extends Predicate {

    /**
     * Gets the predicates being decorated as an array.
     * <p>
     * The array may be the internal data structure of the predicate and thus
     * should not be altered.
     *
     * @return the predicates being decorated
     */
    Predicate[] getPredicates();

}