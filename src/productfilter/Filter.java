/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

/**
 * Can be used to filter some collection of items.
 * It is up to client to iterate over the items.
 */
public interface Filter<T> {

    /**
     * @param item decide whether this item passes criteria of the filter
     * @return true if the item passes
     */
    boolean passes(T item);

}
