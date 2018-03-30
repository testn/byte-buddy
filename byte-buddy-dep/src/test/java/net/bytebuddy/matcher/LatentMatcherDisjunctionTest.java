package net.bytebuddy.matcher;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.test.utility.MockitoRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class LatentMatcherDisjunctionTest {

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private LatentMatcher<Object> left, right;

    @Mock
    private ElementMatcher<Object> leftResolved, rightResolved;

    @Mock
    private TypeDescription typeDescription;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        when(left.resolve(typeDescription)).thenReturn((ElementMatcher) leftResolved);
        when(right.resolve(typeDescription)).thenReturn((ElementMatcher) rightResolved);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testApplicationBoth() throws Exception {
        Object target = new Object();
        when(leftResolved.matches(target)).thenReturn(false);
        when(rightResolved.matches(target)).thenReturn(false);
        assertThat(new LatentMatcher.Disjunction<Object>(left, right).resolve(typeDescription).matches(target), is(false));
        verify(leftResolved).matches(target);
        verifyNoMoreInteractions(leftResolved);
        verify(rightResolved).matches(target);
        verifyNoMoreInteractions(rightResolved);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testApplicationFirstOnly() throws Exception {
        Object target = new Object();
        when(leftResolved.matches(target)).thenReturn(true);
        assertThat(new LatentMatcher.Disjunction<Object>(left, right).resolve(typeDescription).matches(target), is(true));
        verify(leftResolved).matches(target);
        verifyNoMoreInteractions(leftResolved);
        verifyZeroInteractions(rightResolved);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testApplicationBothPositive() throws Exception {
        Object target = new Object();
        when(leftResolved.matches(target)).thenReturn(false);
        when(rightResolved.matches(target)).thenReturn(true);
        assertThat(new LatentMatcher.Disjunction<Object>(left, right).resolve(typeDescription).matches(target), is(true));
        verify(leftResolved).matches(target);
        verifyNoMoreInteractions(leftResolved);
        verify(rightResolved).matches(target);
        verifyNoMoreInteractions(rightResolved);
    }
}
