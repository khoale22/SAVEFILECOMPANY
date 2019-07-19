package com.heb.pm.product;

import com.heb.pm.entity.ProductMaster;
import com.heb.pm.repository.ProductInfoRepositoryWithCounts;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import testSupport.ValidatingCallChecker;

/**
 * Tests ProductInfoService
 *
 * @author s573181
 * @since 2.0.1
 */
public class ProductInfoServiceTest {

    /*
	 * This class really just farms out it's work to other objects. The test class will test that it is farmed out
	 * correctly, not that the other classes work correctly. Those checks will be done in the tests for those classes.
	 */

    /*
     * findProductInfoByProdId
     */

    /**
     * Tests findProductInfoByProdId with null passed in as productId.
     */
    @Test(expected = IllegalArgumentException.class)
    public void findProductInfoByProdIdNull(){
		ProductInfoService service = new ProductInfoService();
        service.findProductInfoByProdId(null);
    }

    /**
     * Tests findProductInfoByProdId with results found.
     */
    @Test
    public void findProductInfoByProdIdFound() {

		ProductMaster pm = new ProductMaster();
		ValidatingCallChecker<Long, ProductMaster> callChecker = new ValidatingCallChecker<>(555L, pm);
		ProductInfoRepositoryWithCounts repository = Mockito.mock(ProductInfoRepositoryWithCounts.class);
		Mockito.doAnswer(callChecker).when(repository).findOne(Mockito.anyLong());

		ProductInfoService service = new ProductInfoService();
		service.setProductInfoRepositoryWithCounts(repository);

        ProductMaster productMaster = service.findProductInfoByProdId(555L);
		Assert.assertEquals(pm, productMaster);
		Assert.assertTrue(callChecker.isMethodCalled());
    }
}
