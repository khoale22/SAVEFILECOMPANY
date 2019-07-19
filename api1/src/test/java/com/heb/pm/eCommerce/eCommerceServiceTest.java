package com.heb.pm.eCommerce;

import com.heb.pm.ecommerce.EcommerceService;
import com.heb.pm.entity.TargetSystemAttributePriority;
import com.heb.pm.repository.TargetSystemAttributePriorityRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;

/**
 * The interface attribute repository.
 * @author  s753601
 * @since 2.5.0
 */
public class eCommerceServiceTest {

    @InjectMocks
    private EcommerceService ecommerceService;

    @Mock
    private TargetSystemAttributePriorityRepository repository;

    /**
     * Initializes mockitos.
     */
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSourcePriorityTableTest(){
        Mockito.when(this.repository.findSourcePriorities()).thenReturn(getTargetSystemAttributePriorityList());
        List<TargetSystemAttributePriority> allTargetSystemAttributePriority = this.ecommerceService.getSourcePriorityTable();
        Assert.assertEquals(getTargetSystemAttributePriorityList(), allTargetSystemAttributePriority);
    }

    /**
     * Generates a generic List<TargetSystemAttributePriority>
     * @return a list used for testing
     */
    protected List<TargetSystemAttributePriority> getTargetSystemAttributePriorityList(){
        ArrayList<TargetSystemAttributePriority> targetSystemAttributePriorities = new ArrayList<>();
        targetSystemAttributePriorities.add(new TargetSystemAttributePriority());
        return targetSystemAttributePriorities;
    }
}
