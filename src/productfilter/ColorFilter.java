/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

/**
 * Passes if the color of the product is exactly the color this filter checks for.
 */
public class ColorFilter implements Filter<Product> {

    private Color color;

    public ColorFilter(Color color) {
        this.color = color;
    }

    @Override
    public boolean passes(Product item) {
        return color.equals(item.getColor());
    }

}
