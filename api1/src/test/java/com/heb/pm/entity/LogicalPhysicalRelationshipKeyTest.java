package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests all of the getters and setters for the Logical Physical Relationship Key entity
 *
 * @author s753601
 * @since 2.5.0
 */
public class LogicalPhysicalRelationshipKeyTest {

    /**
     * Tests the getLogicalAttributeId Method of the LogicalPhysicalRelationshipKey class
     */
    @Test
    public void getLogicalAttributeIdTest() {
        LogicalPhysicalRelationshipKey test = getDefaultKey();
        Assert.assertEquals(123, test.getLogicalAttributeId());
    }

    /**
     * Tests the setLogicalAttribute Method of the LogicalPhysicalRelationshipKey class
     */
    @Test
    public void setLogicalAttributeIdTest() {
        LogicalPhysicalRelationshipKey test = getDefaultKey();
        Assert.assertEquals(123, test.getLogicalAttributeId());
        test.setLogicalAttributeId(456);
        Assert.assertEquals(456, test.getLogicalAttributeId());
    }

    /**
     * Tests the getPhysicalAttribute Method of the LogicalPhysicalRelationshipKey class
     */
    @Test
    public void getPhysicalAttributeIdTest() {
        LogicalPhysicalRelationshipKey test = getDefaultKey();
        Assert.assertEquals(456, test.getPhysicalAttributeId());
    }

    /**
     * Tests the setPhysicalAttributeId Method of the LogicalPhysicalRelationshipKey class
     */
    @Test
    public void setPhysicalAttributeIdTest() {
        LogicalPhysicalRelationshipKey test = getDefaultKey();
        Assert.assertEquals(456, test.getPhysicalAttributeId());
        test.setPhysicalAttributeId(123);
        Assert.assertEquals(123, test.getPhysicalAttributeId());
    }

    /**
     * Tests the getRelationshipGroupTypeCode Method of the LogicalPhysicalRelationshipKey class
     */
    @Test
    public void getRelationshipGroupTypeCodeTest() {
        LogicalPhysicalRelationshipKey test = getDefaultKey();
        Assert.assertEquals("ATTR", test.getRelationshipGroupTypeCode());
    }

    /**
     * Tests the setRelationshipGroupTypeCode Method of the LogicalPhysicalRelationshipKey class
     */
    @Test
    public void setRelationshipGroupTypeCodeTest() {
        LogicalPhysicalRelationshipKey test = getDefaultKey();
        Assert.assertEquals("ATTR", test.getRelationshipGroupTypeCode());
        test.setRelationshipGroupTypeCode("GRP ");
        Assert.assertEquals("GRP ", test.getRelationshipGroupTypeCode());
    }

    /**
     * Generates a generic Logical Physical Relationship key for testing
     * @return a generic Logical Physical relationship key
     */
    private LogicalPhysicalRelationshipKey getDefaultKey(){
        LogicalPhysicalRelationshipKey key = new LogicalPhysicalRelationshipKey();
        key.setLogicalAttributeId(123);
        key.setPhysicalAttributeId(456);
        key.setRelationshipGroupTypeCode("ATTR");
        return key;
    }
}
