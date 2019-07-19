package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import testSupport.LoggingSupportTestRunner;

import java.util.*;

/**
 * Created by s573181 on 6/7/2016.
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class ProductMasterTest {

    @Autowired
    private ProductMasterRepositoryTest productMasterRepositoryTest;

    /*
	 * equals
	 */

    /**
     * Test equals on the same object.
     */
    @Test
    public void equalsSameObject() {
        ProductMaster pm = this.getDefaultRecord();
        boolean equals = pm.equals(pm);
        Assert.assertTrue(equals);
    }

    /**
     * Test equals on similar objects.
     */
    @Test
    public void equalsSimilarObject() {
        ProductMaster pm1 = this.getDefaultRecord();
        ProductMaster pm2 = this.getDefaultRecord();
        boolean equals = pm1.equals(pm2);
        Assert.assertTrue(equals);
    }

    /**
     * Tests toString.
     */
    @Test
    public void testToString() {
        ProductMaster pm = this.getDefaultRecord();
        System.out.println(pm.toString());
        Assert.assertEquals("ProductMaster{description='DUMMY PROD FOR MRTS           ', prodId=0}",
                pm.toString());
    }

    @Test
    @Transactional
    public void testRepo() {
        List<ProductMaster> products = this.productMasterRepositoryTest.findAll();
        products.forEach((i) -> System.out.println(i));
    }

    /*
	 * Test JPA mapping.
	 */
    /**
     * Tests that the object has the correct JPA mapping on  productMaster.
     */
    @Test
    @Transactional
    public void testMappingProductMaster() {
        ProductMaster pm = this.productMasterRepositoryTest.findOne(this.getDefaultRecord().getProdId());
        this.fullItemCompare(this.getDefaultRecord(), pm);
    }

    private ProductMaster getDefaultRecord(){
        ProductMaster productMaster = new ProductMaster();
        productMaster.setProdId(0L);
        productMaster.setDescription("DUMMY PROD FOR MRTS           ");
		productMaster.setRetailLink(0L);
        List<ProdItem> prodItems = new ArrayList<>();

        ProdItem prodItem = new ProdItem();
        ProdItemKey prodItemKey = new ProdItemKey();
        prodItemKey.setItemType("ITMCD");
        prodItemKey.setProductId(0);
        prodItemKey.setItemCode(66L);
        prodItem.setKey(prodItemKey);

        ProdItem prodItem2 = new ProdItem();
        ProdItemKey prodItemKey2 = new ProdItemKey();
        prodItemKey2.setItemType("ITMCD");
        prodItemKey2.setProductId(0);
        prodItemKey2.setItemCode(36849L);
        prodItem2.setKey(prodItemKey2);

        prodItems.add(prodItem);
        prodItems.add(prodItem2);
        productMaster.setProdItems(prodItems);
        return productMaster;
    }

    private ProductMaster getAlternateRecord(){
        ProductMaster productMaster = new ProductMaster();
        productMaster.setProdId(57L);
        productMaster.setDescription("WACO TRIBUNE SUNDAY");
        return productMaster;
    }

    private void fullItemCompare(ProductMaster a, ProductMaster b){
        Assert.assertEquals(a.getProdId(),b.getProdId());
        Assert.assertEquals(a.getDescription(), b.getDescription());
		Assert.assertEquals(a.getRetailLink(), b.getRetailLink());

        Assert.assertEquals(a.getProdItems().size(), b.getProdItems().size());
        Iterator<ProdItem> aProdItems = a.getProdItems().iterator();
        Iterator<ProdItem> bProdItems = b.getProdItems().iterator();
        for(; aProdItems.hasNext(); ){
            Assert.assertEquals((aProdItems.next()), bProdItems.next());
        }
    }


}
