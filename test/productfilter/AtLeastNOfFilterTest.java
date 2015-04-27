/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

/**
 *
 * @author vcaniga
 */
public class AtLeastNOfFilterTest {
    
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void constructorTest() {
        
        Filter filter = mock(Filter.class);
        
        // should pass
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(1, filter);        
    }
    
    @Test
    public void constructorNLowerThan0() {
        Filter filter = mock(Filter.class);
        
        expectedException.expect(IllegalArgumentException.class);
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(-1, filter);
    }
    
    @Test
    public void constructorNHigherThanFilters() {
        Filter filter = mock(Filter.class);
        Filter filter2 = mock(Filter.class);
        Filter filter3 = mock(Filter.class);
        
        expectedException.expect(FilterNeverSucceeds.class);
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(4, filter, filter2, filter3);
    }
    
    @Test
    public void passesCorrectTest() {
        Filter filter = mock(Filter.class);
        Mockito.when(filter.passes(Matchers.anyObject())).thenReturn(true);
        Filter filter2 = mock(Filter.class);
        Mockito.when(filter2.passes(Matchers.anyObject())).thenReturn(false);
        
        AtLeastNOfFilter nOfFilter = new AtLeastNOfFilter(1, filter, filter2);
        assertEquals(true, nOfFilter.passes(new String()));
    }
    
    @Test
    public void passesIncorrectTest() {
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
