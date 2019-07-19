package com.heb.gdsn;

import com.heb.util.jpa.PageableResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Tests VendorSubscriptionService.
 *
 * @author d116773
 * @since 2.3.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class VendorSubscriptionServiceTest {

    @Autowired
    private VendorSubscriptionRepository repository;

    private VendorSubscriptionService service;

    @Before
    public void setup() {
        this.service = new VendorSubscriptionService();
        this.service.setVendorSubscriptionRepository(this.repository);
    }

    /*
     * findAll
     */

    /**
     * Tests findAll when passed a negative page number.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void findAllNegativePage() {
        this.service.findAll(-1, 1);
    }

    /**
     * Tests findAll when passed negative page size.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void findAllNegativePageSize() {
        this.service.findAll(0, -1);
    }

    @Test
    public void findAllGood() {
        PageableResult<VendorSubscription> vendorSubscriptions = this.service.findAll(0, 10);
        Assert.assertEquals(3, vendorSubscriptions.getPageCount().intValue());

    }
}
