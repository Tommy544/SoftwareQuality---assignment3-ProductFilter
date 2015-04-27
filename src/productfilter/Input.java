/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

import java.util.Collection;

/**
 * Can encapsulate any storage of available products.
 * Implementations can be anything from simple Collections to disk-based databases.
 */
public interface Input {

    Collection<Product> obtainProducts() throws ObtainFailedException;

}
