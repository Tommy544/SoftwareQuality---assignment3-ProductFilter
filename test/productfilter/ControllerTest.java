/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static productfilter.Controller.TAG_CONTROLLER;

/**
 *
 * @author Vladimir Caniga
 * @author Jakub Smolar
 */
public class ControllerTest {

    @Test
    public void sendExactProductsMatchFound() {
        Input input = new InputMock();
        Output output = new OutputMock();
        Logger logger = mock(Logger.class);
        
        Filter filter = mock(Filter.class);
        when(filter.passes((Product) Matchers.argThat(new IsColorRed()))).thenReturn(true);

        Controller controller = new Controller(input, output, logger);
        controller.select(filter);

        OutputMock outputMock = (OutputMock) output;
        assertEquals(1, outputMock.sentProducts.size());
        assertEquals(new Product(1, "FirstProduct", Color.RED, new BigDecimal(100)), outputMock.sentProducts.get(0));
    }
    
    @Test
    public void sendExactProductsMultipleMatchesFound() {
        Input input = new InputMock();
        Output output = new OutputMock();
        Logger logger = mock(Logger.class);
        
        Filter filter = mock(Filter.class);
        when(filter.passes((Product) Matchers.argThat(new IsPriceEqualTo100()))).thenReturn(true);
        
        Controller controller = new Controller(input, output, logger);
        controller.select(filter);
        
        OutputMock outputMock = (OutputMock) output;
        Assert.assertTrue(outputMock.sentProducts.contains(new Product(1, "FirstProduct", Color.RED, new BigDecimal(100))));
        Assert.assertTrue(outputMock.sentProducts.contains(new Product(3, "ThordProduct", Color.BLUE, new BigDecimal(100))));
    }
    
    @Test
    public void sendExactProductsMatchNotFound() {
        Input input = new InputMock();
        Output output = new OutputMock();
        Logger logger = mock(Logger.class);
        
        Filter filter = mock(Filter.class);
        when(filter.passes(new Product(0, "Nonexistent", Color.BLACK, new BigDecimal(0)))).thenReturn(true);
        
        Controller controller = new Controller(input, output, logger);
        controller.select(filter);

        OutputMock outputMock = (OutputMock) output;
        assertEquals(0, outputMock.sentProducts.size());
    }
    
    @Test
    public void logCorrentFormatOnSuccess() {
        Input input = new InputMock();
        Output output = new OutputMock();
        Logger logger = new Loggermock();
        
        Filter filter = mock(Filter.class);
        when(filter.passes((Product) Matchers.argThat(new IsColorRed()))).thenReturn(true);
        
        Controller controller = new Controller(input, output, logger);
        controller.select(filter);
        
        Loggermock loggermock = (Loggermock) logger;
        assertEquals(1, loggermock.levelList.size());
        assertEquals(1, loggermock.tagList.size());
        assertEquals(1, loggermock.messageList.size());
        
        assertEquals("INFO", loggermock.levelList.get(0));
        assertEquals(TAG_CONTROLLER, loggermock.tagList.get(0));
        assertEquals("Successfully selected 1 out of 4 available products.", loggermock.messageList.get(0));
    }
    
    @Test
    public void loggerLogsException() throws ObtainFailedException {
        Input input = mock(Input.class);
        when(input.obtainProducts()).thenThrow(ObtainFailedException.class);
        Output output = new OutputMock();
        Logger logger = new Loggermock();
        
        Filter filter3 = mock(Filter.class);
        when(filter3.passes(Matchers.any(Product.class))).thenReturn(false);
        
        Controller controller = new Controller(input, output, logger);
        controller.select(filter3);
        
        Loggermock loggermock = (Loggermock) logger;
        assertEquals(1, loggermock.levelList.size());
        assertEquals(1, loggermock.tagList.size());
        assertEquals(1, loggermock.messageList.size());
        
        assertEquals("ERROR", loggermock.levelList.get(0));
        assertEquals(TAG_CONTROLLER, loggermock.tagList.get(0));
        assertEquals("Filter procedure failed with exception: productfilter.ObtainFailedException", loggermock.messageList.get(0));
    }
    
    @Test
    public void whenExceptionThenNothingInOutput() throws ObtainFailedException {
        Input input = mock(Input.class);
        when(input.obtainProducts()).thenThrow(ObtainFailedException.class);
        Output output = new OutputMock();
        Logger logger = new Loggermock();
        
        Filter filter3 = mock(Filter.class);
        when(filter3.passes(Matchers.any(Product.class))).thenReturn(false);
        
        Controller controller = new Controller(input, output, logger);
        controller.select(filter3);
        
        OutputMock outputMock = (OutputMock) output;
        assertEquals(0, outputMock.sentProducts.size());
    }
    

    
    class IsColorRed extends ArgumentMatcher<Product> {
        @Override
        public boolean matches(Object argument) {
            Product prod = (Product) argument;
            return prod.getColor() == Color.RED;
        }
    }
    
    class IsPriceEqualTo100 extends ArgumentMatcher<Product> {
        @Override
        public boolean matches(Object argument) {
            Product prod = (Product) argument;
            return prod.getPrice().equals(BigDecimal.valueOf(100));
        }
    }
    
    private static class Loggermock implements Logger {
        
        private List<String> levelList = new ArrayList<>();
        private List<String> messageList = new ArrayList<>();
        private List<String> tagList = new ArrayList<>();

        @Override
        public void setLevel(String level) {
            levelList.add(level);
        }

        @Override
        public void log(String tag, String message) {
            tagList.add(tag);
            messageList.add(message);
        }
    }

    private static class InputMock implements Input {

        @Override
        public Collection<Product> obtainProducts() throws ObtainFailedException {
            Collection<Product> finalProducts = new ArrayList<>();
            finalProducts.add(new Product(1, "FirstProduct", Color.RED, new BigDecimal(100)));
            finalProducts.add(new Product(2, "SecondProduct", Color.GREEN, new BigDecimal(200)));
            finalProducts.add(new Product(3, "ThordProduct", Color.BLUE, new BigDecimal(100)));
            finalProducts.add(new Product(4, "ForthProduct", Color.BLACK, new BigDecimal(400)));

            return finalProducts;
        }
    }

    private static class OutputMock implements Output {

        private List<Product> sentProducts = new ArrayList<>();

        @Override
        public void postSelectedProducts(Collection<Product> products) {
            this.sentProducts = (ArrayList<Product>) products;
        }
    }
}
