/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

import java.util.Collection;

/**
 * Can represent some form into which the selected products are displayed,
 * serialized representation of the collection etc.
 */
public interface Output {

    void postSelectedProducts(Collection<Product> products);

}
