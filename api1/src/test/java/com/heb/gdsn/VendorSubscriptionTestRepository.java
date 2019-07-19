package com.heb.gdsn;

import org.junit.runner.RunWith;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

/**
 * Test repository for the VendorSubcription class.
 *
 * @author d116773
 * @since 2.3.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public interface VendorSubscriptionTestRepository extends JpaRepository<VendorSubscription, Integer> {
}
