package com.heb.gdsn;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import testSupport.LoggingSupportTestRunner;

import java.time.LocalDateTime;

/**
 * Tests VendorSubscription.
 *
 * @author d116773
 * @since 2.3.0
 */
@RunWith(LoggingSupportTestRunner.class)
@ContextConfiguration(locations = "classpath*:testConfig.xml")
public class VendorSubscriptionTest {

    private static final int TEST_SEQUENCE_NUMBER = 1;
    private static final String TEST_GLN = "0037000000001";
    private static final String TEST_PROVIDER_NAME = "Procter and Gamble 1";
    private static final String TEST_SUBSCRIPTION_STATUS = "ADD";
    private static final String TEST_STATUS = "ACK";
    private static LocalDateTime TEST_CREATE_TIME;
    private static LocalDateTime TEST_PROCESS_TIME;
    private static final String TEST_USER_ID = "SYS";
    private static final String TEST_DOCUMENT_ID = "cis.heb.1af90fc0-a7ee-4209-b6a4-c940a7633574";
    private static final String TEST_MESSAGE_ID = "cis.heb.d7578a0e-9a70-4246-b2ac-3b24fde58bbe";

    static {
        VendorSubscriptionTest.TEST_CREATE_TIME = LocalDateTime.of(2017, 2, 12, 16, 40,0);
        VendorSubscriptionTest.TEST_PROCESS_TIME = LocalDateTime.of(2017, 2, 12, 16, 50,0);
    }

    @Autowired
    private VendorSubscriptionRepository repository;

    /*
     * JPA Mapping
     */

    /**
     * Tests the JPA mapping of the sequence number.
     */
    @Test
    public void mappingSequenceNumber() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER, vs.getSequenceNumber());
    }

    /**
     * Tests the JPA mapping of the provider GLN.
     */
    @Test
    public void mappingProviderGln() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_GLN, vs.getVendorGln());
    }

    /**
     * Tests the JPA mapping of the provider name.
     */
    @Test
    public void mappingProviderName() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_PROVIDER_NAME, vs.getVendorName());
    }

    /**
     * Tests the JPA mapping of the subscription status.
     */
    @Test
    public void mappingSubscriptionStatus() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_PROVIDER_NAME, vs.getVendorName());
    }

    /**
     * Tests the JPA mapping of the message status.
     */
    @Test
    public void mappingMessageStatus() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_STATUS, vs.getMessageStatus());
    }

    /**
     * Tests the JPA mapping of the record create time.
     */
    @Test
    public void mappingCreateTime() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_CREATE_TIME, vs.getCreateTime());
    }

    /**
     * Tests the JPA mapping of the record process time.
     */
    @Test
    public void mappingProcessTime() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_PROCESS_TIME, vs.getProcessTime());
    }

    /**
     * Tests the JPA mapping of the record create user ID.
     */
    @Test
    public void mappingCreateUserId() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_USER_ID, vs.getCreateUserId());
    }

    /**
     * Tests the JPA mapping of the record document ID.
     */
    @Test
    public void mappingDocumentId() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_DOCUMENT_ID, vs.getDocumentId());
    }

    /**
     * Tests the JPA mapping of the record message ID.
     */
    @Test
    public void mappingMessageId() {
        VendorSubscription vs = this.repository.findOne(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        Assert.assertEquals(VendorSubscriptionTest.TEST_MESSAGE_ID, vs.getMessageId());
    }

    /*
     * Getters
     */

    /**
     * Tests getSequenceNumber.
     */
    @Test
    public void getSequenceNumber() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER, vs.getSequenceNumber());
    }

    /**
     * Tests getProviderGln.
     */
    @Test
    public void getProviderGln() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_GLN, vs.getVendorGln());
    }

    /**
     * Tests getProviderName.
     */
    @Test
    public void getProviderName() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_PROVIDER_NAME, vs.getVendorName());
    }

    /**
     * Tests getSubscriptionStatus.
     */
    @Test
    public void getSubscriptionStatus() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_PROVIDER_NAME, vs.getVendorName());
    }

    /**
     * Tests getMessageStatus.
     */
    @Test
    public void getMessageStatus() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_STATUS, vs.getMessageStatus());
    }

    /**
     * Tests getCreateTime.
     */
    @Test
    public void getCreateTime() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_CREATE_TIME, vs.getCreateTime());
    }

    /**
     * Tests getProcessTime.
     */
    @Test
    public void getProcessTime() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_PROCESS_TIME, vs.getProcessTime());
    }

    /**
     * Tests getCreateUserId.
     */
    @Test
    public void getCreateUserId() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_USER_ID, vs.getCreateUserId());
    }

    /**
     * Tests getDocumentId.
     */
    @Test
    public void getDocumentId() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_DOCUMENT_ID, vs.getDocumentId());
    }

    /**
     * Tests getMessageId.
     */
    @Test
    public void getMessageId() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_MESSAGE_ID, vs.getMessageId());
    }

    /*
     * Equals
     */

    /**
     * Tests equals when the objects are the same.
     */
    @Test
    public void equalsSameObject(){
        VendorSubscription vs1 = this.getTestVendorSubscription();
        boolean eq = vs1.equals(vs1);
        Assert.assertTrue(eq);
    }

    /**
     * Tests equals when the objects are equal.
     */
    @Test
    public void equalsEqualObjects() {
        VendorSubscription vs1 = this.getTestVendorSubscription();
        VendorSubscription vs2 = this.getTestVendorSubscription();
        boolean eq = vs1.equals(vs2);
        Assert.assertTrue(eq);
    }

    /**
     * Tests equals when passed an unequal object.
     */
    @Test
    public void equalsNotEqual() {
        VendorSubscription vs1 = this.getTestVendorSubscription();
        VendorSubscription vs2 = this.getTestVendorSubscription();
        vs2.setSequenceNumber( -1 * vs2.getSequenceNumber());
        boolean eq = vs1.equals(vs2);
        Assert.assertFalse(eq);
    }

    /**
     * Tests equals when passed a null.
     */
    @Test
    public void equalsNull() {
        VendorSubscription vs1 = this.getTestVendorSubscription();
        boolean eq = vs1.equals(null);
        Assert.assertFalse(eq);
    }

    /**
     * Tests equals when passed a differnt kind of object.
     */
    @Test
    public void equalsDifferentType() {
        VendorSubscription vs1 = this.getTestVendorSubscription();
        boolean eq = vs1.equals(Integer.valueOf(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER));
        Assert.assertFalse(eq);
    }

    /*
     * Hash Code
     */
    /**
     * Tests hashCode returns sequence number.
     */
    @Test
    public void testHashCode() {
        VendorSubscription vs = this.getTestVendorSubscription();
        Assert.assertEquals(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER, vs.hashCode());
    }

    /*
     * toString
     */

    /**
     * Tests toString.
     */
    @Test
    public void testToString() {
        Assert.assertEquals("VendorSubscription{sequenceNumber=1, providerGln='0037000000001', providerName='Procter and Gamble 1', subscriptionStatus='ADD', messageStatus='ACK', createTime=2017-02-12T16:40, processTime=2017-02-12T16:50, createUserId='SYS', documentId='cis.heb.1af90fc0-a7ee-4209-b6a4-c940a7633574', messageId='cis.heb.d7578a0e-9a70-4246-b2ac-3b24fde58bbe'}",
                this.getTestVendorSubscription().toString());
    }

    /*
     * Support functions
     */
    /**
     * Returns an object to test with.
     *
     * @return An object to test with.
     */
    private VendorSubscription getTestVendorSubscription() {

        VendorSubscription vs = new VendorSubscription();

        vs.setSequenceNumber(VendorSubscriptionTest.TEST_SEQUENCE_NUMBER);
        vs.setVendorGln(VendorSubscriptionTest.TEST_GLN);
        vs.setVendorGln(VendorSubscriptionTest.TEST_PROVIDER_NAME);
        vs.setSubscriptionStatus(VendorSubscriptionTest.TEST_SUBSCRIPTION_STATUS);
        vs.setMessageStatus(VendorSubscriptionTest.TEST_STATUS);
        vs.setCreateTime(VendorSubscriptionTest.TEST_CREATE_TIME);
        vs.setProcessTime(VendorSubscriptionTest.TEST_PROCESS_TIME);
        vs.setCreateUserId(VendorSubscriptionTest.TEST_USER_ID);
        vs.setDocumentId(VendorSubscriptionTest.TEST_DOCUMENT_ID);
        vs.setMessageId(VendorSubscriptionTest.TEST_MESSAGE_ID);

        return vs;
    }


}
