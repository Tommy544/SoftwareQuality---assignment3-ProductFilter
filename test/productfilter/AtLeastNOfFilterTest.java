/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

/**
 *
 * @author Vladimir Caniga
 * @author Jakub Smolar
 */
public class AtLeastNOfFilterTest {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void constructorShouldPassTest() {
        
        Filter filter = mock(Filter.class);
        
        // should pass
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(1, filter);        
    }
    
    @Test
    public void constructorNLowerThan0Test() {
        Filter filter = mock(Filter.class);
        
        expectedException.expect(IllegalArgumentException.class);
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(-1, filter);
    }
    
    @Test
    public void constructorNHigherThanFiltersTest() {
        Filter filter = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        Filter filter3 = mock(Filter.class);
        
        expectedException.expect(FilterNeverSucceeds.class);
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(4, filter, filter2, filter3);
    }
    
    @Test
    public void passesExactNChildrenPassTest() {
        Filter filter = mock(Filter.class);
        Mockito.when(filter.passes(Matchers.anyObject())).thenReturn(true);
        Filter filter2 = mock(Filter.class);
        Mockito.when(filter2.passes(Matchers.anyObject())).thenReturn(false);
        
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(1, filter, filter2);
        assertEquals(true, nOfFilter.passes(new String()));
    }
    
    @Test
    public void passesIncorrectNChildrenPassTest() {
        Filter filter = mock(Filter.class);
        Mockito.when(filter.passes(Matchers.anyObject())).thenReturn(true);
        Filter filter2 = mock(Filter.class);
        Mockito.when(filter2.passes(Matchers.anyObject())).thenReturn(false);
        Filter filter3 = mock(Filter.class);
        Mockito.when(filter.passes(Matchers.anyObject())).thenReturn(true);
        
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(3, filter, filter2, filter3);
        assertEquals(false, nOfFilter.passes(new String()));
    }
    
}
