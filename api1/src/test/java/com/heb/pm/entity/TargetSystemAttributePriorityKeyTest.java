package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests all of the getters and setters for the Target System Attribute Priority entity
 *
 * @author s753601
 * @since 2.5.0
 */
public class TargetSystemAttributePriorityKeyTest {

    /**
     * Tests the getDataSourceSystemId Method of the Target System Priority Key class
     */
    @Test
    public void getDataSourceSystemIdTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals(12, test.getDataSourceSystemId());

    }

    /**
     * Tests the setDataSourceSystemId Method of the Target System Priority Key class
     */
    @Test
    public void setDataSourceSystemIdTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals(12, test.getDataSourceSystemId());
        test.setDataSourceSystemId(14);
        Assert.assertEquals(14, test.getDataSourceSystemId());
    }

    /**
     * Tests the getLogicalAttributeId Method of the Target System Priority Key class
     */
    @Test
    public void getLogicalAttributeIdTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals(123, test.getLogicalAttributeId());
    }

    /**
     * Tests the setLogicalAttributeId Method of the Target System Priority Key class
     */
    @Test
    public void setLogicalAttributeIdTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals(123, test.getLogicalAttributeId());
        test.setLogicalAttributeId(145);
        Assert.assertEquals(145, test.getLogicalAttributeId());
    }

    /**
     * Tests the getPhysicalAttributeId Method of the Target System Priority Key class
     */
    @Test
    public void getPhysicalAttributeIdTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals(123, test.getPhysicalAttributeId());
    }

    /**
     * Tests the setPhysicalAttributeId Method of the Target System Priority Key class
     */
    @Test
    public void setPhysicalAttributeIdTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals(123, test.getPhysicalAttributeId());
        test.setPhysicalAttributeId(456);
        Assert.assertEquals(456, test.getPhysicalAttributeId());
    }

    /**
     * Tests the getRelationshipGroupTypeCode Method of the Target System Priority Key class
     */
    @Test
    public void getRelationshipGroupTypeCodeTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals("Attr", test.getRelationshipGroupTypeCode());
    }

    /**
     * Tests the setRelationshipGroupTypeCode Method of the Target System Priority Key class
     */
    @Test
    public void setRelationshipGroupTypeCodeTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals("Attr", test.getRelationshipGroupTypeCode());
        test.setRelationshipGroupTypeCode("Grp ");
        Assert.assertEquals("Grp ", test.getRelationshipGroupTypeCode());
    }

    /**
     * Tests the getTargetSystemId Method of the Target System Priority Key class
     */
    @Test
    public void getTargetSystemIdTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals(13, test.getTargetSystemId());
    }

    /**
     * Tests the setTargetSystemId Method of the Target System Priority Key class
     */
    @Test
    public void setTargetSystemIdTest() {
        TargetSystemAttributePriorityKey test = getTargetSystemAttributePriorityKey();
        Assert.assertEquals(13, test.getTargetSystemId());
        test.setTargetSystemId(45);
        Assert.assertEquals(45, test.getTargetSystemId());
    }

    /**
     * Generates a generic key for the Target System Attribute priority
     * @return a generic key for the Target System Attribute priority
     */
    private TargetSystemAttributePriorityKey getTargetSystemAttributePriorityKey(){
        TargetSystemAttributePriorityKey targetSystemAttributePriorityKey = new TargetSystemAttributePriorityKey();
        targetSystemAttributePriorityKey.setRelationshipGroupTypeCode("Attr");
        targetSystemAttributePriorityKey.setDataSourceSystemId(12);
        targetSystemAttributePriorityKey.setLogicalAttributeId(123);
        targetSystemAttributePriorityKey.setPhysicalAttributeId(123);
        targetSystemAttributePriorityKey.setTargetSystemId(13);
        return targetSystemAttributePriorityKey;
    }
}
