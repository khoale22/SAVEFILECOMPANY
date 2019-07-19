package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests all of the getters and setters for the Target System Attribute Priority entity
 *
 * @author s753601
 * @since 2.5.0
 */
public class TargetSystemAttributePriorityTest {

    /**
     * Tests the getKey method in the Target System Attribute Priority class
     */
    @Test
    public void getKeyTest() {
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(12, test.getKey().getDataSourceSystemId());
        Assert.assertEquals(123, test.getKey().getPhysicalAttributeId());
    }

    /**
     * Tests the setKey method in the Target System Attribute Priority class
     */
    @Test
    public void setKeyTest() {
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(12, test.getKey().getDataSourceSystemId());
        Assert.assertEquals(123, test.getKey().getPhysicalAttributeId());
        TargetSystemAttributePriorityKey newKey = getTargetSystemAttributePriorityKey();
        newKey.setDataSourceSystemId(14);
        newKey.setPhysicalAttributeId(456);
        test.setKey(newKey);
        Assert.assertEquals(14, test.getKey().getDataSourceSystemId());
        Assert.assertEquals(456, test.getKey().getPhysicalAttributeId());
    }

    /**
     * Tests the getAttributePriorityNumber method in the Target System Attribute Priority class
     */
    @Test
    public void getAttributePriorityNumberTest() {
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(1, test.getAttributePriorityNumber());
    }

    /**
     * Tests the setAttributePriorityNumber method in the Target System Attribute Priority class
     */
    @Test
    public void setAttributePriorityNumberTest() {
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(1, test.getAttributePriorityNumber());
        test.setAttributePriorityNumber(5);
        Assert.assertEquals(5, test.getAttributePriorityNumber());

    }

    /**
     * Tests the getLogicalPhysicalRelationship method in the Target System Attribute Priority class
     */
    @Test
    public void getLogicalPhysicalRelationshipTest() {
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(getDefaultLogicalPhysicalRelationship().toString(), test.getLogicalPhysicalRelationship().toString());
    }

    /**
     * Tests the setLogicalPhysicalRelationship method in the Target System Attribute Priority class
     */
    @Test
    public void setLogicalPhysicalRelationshipTest() {
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(getDefaultLogicalPhysicalRelationship().toString(), test.getLogicalPhysicalRelationship().toString());
        LogicalPhysicalRelationship newLogicalPhysicalRelationship = getDefaultLogicalPhysicalRelationship();
        LogicalPhysicalRelationshipKey newKey = getDefaultKey();
        newKey.setPhysicalAttributeId(159);
        newLogicalPhysicalRelationship.setKey(newKey);
        test.setLogicalPhysicalRelationship(newLogicalPhysicalRelationship);
        Assert.assertEquals(newLogicalPhysicalRelationship.toString(), test.getLogicalPhysicalRelationship().toString());
    }

    /**
     * Tests the getSourceSystem method in the Target System Attribute Priority class
     */
    @Test
    public void getSourceSystemTest() {
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(getDefaultSourceSystem().toString(), test.getSourceSystem().toString());

    }

    /**
     * Tests the setSourceSystem method in the Target System Attribute Priority class
     */
    @Test
    public void setSourceSystemTest() {
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(getDefaultSourceSystem().toString(), test.getSourceSystem().toString());
        SourceSystem newSourceSystem = getDefaultSourceSystem();
        newSourceSystem.setId(987);
        test.setSourceSystem(newSourceSystem);
        Assert.assertEquals(newSourceSystem.toString(), test.getSourceSystem().toString());
    }

    /**
     * Tests the getAttribute method in the Target System Attribute Priority class
     */
    @Test
    public void getAttributeTest(){
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(getDefaultAttribute().toString(), test.getAttribute().toString());

    }

    /**
     * Tests the setAttribute method in the Target System Attribute Priority class
     */
    @Test
    public void setAttributeTest(){
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        Assert.assertEquals(getDefaultAttribute().toString(), test.getAttribute().toString());
        Attribute newAttributeSource = new Attribute();
        newAttributeSource.setAttributeName("Not Test");
        test.setAttribute(newAttributeSource);
        Assert.assertEquals(newAttributeSource.toString(), test.getAttribute().toString());
    }

    /**
     * Tests the toString method in the Target System Attribute Priority class
     */
    @Test
    public void toStringTest(){
        TargetSystemAttributePriority test = getDefaultTargetSystemAttributePriority();
        String expect= "Target System Attribute Priority{" +
                "Log Attri ID: " + 123 +
                ", Phys Attr ID: " + 123 +
                ", Rlshp Grp Typ Cd: " + "Attr" +
                ", Dat Src Sys ID: " + 12 +
                ", Trgt Sys ID: " + "Attr" +
                ", Attr Prty Nbr" + 1 +
                ", (Optional) Attr:" + getDefaultAttributeSource().toString() +"}";
        Assert.assertEquals(expect, test.toString());
    }

    /**
     * Generates a generic Target System Attribute Priority object for testing
     * @return a generic Target System Attribute Priority object for testing
     */
    private TargetSystemAttributePriority getDefaultTargetSystemAttributePriority(){
        TargetSystemAttributePriority targetSystemAttributePriority = new TargetSystemAttributePriority();
        targetSystemAttributePriority.setKey(getTargetSystemAttributePriorityKey());
        targetSystemAttributePriority.setAttributePriorityNumber(1);
        targetSystemAttributePriority.setLogicalPhysicalRelationship(getDefaultLogicalPhysicalRelationship());
        targetSystemAttributePriority.setSourceSystem(getDefaultSourceSystem());
        targetSystemAttributePriority.setAttribute(getDefaultAttribute());
        return targetSystemAttributePriority;
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

    /**
     *Generates a generic Attribute for testing
     * @return a generic Attribute
     */
    private Attribute getDefaultAttribute(){
        Attribute defaultAttribute = new Attribute();
        defaultAttribute.setAttributeId(Long.valueOf(123));
        defaultAttribute.setAttributeName("Test");
        return defaultAttribute;
    }

    /**
     *Generates a generic Logical Physical Relationship for testing
     * @return a generic Logical Physical Relationship
     */
    private LogicalPhysicalRelationship getDefaultLogicalPhysicalRelationship(){
        LogicalPhysicalRelationship defaultLogicalPhysicalRelationship = new LogicalPhysicalRelationship();
        LogicalPhysicalRelationshipKey key = getDefaultKey();
        defaultLogicalPhysicalRelationship.setAttribute(getDefaultAttribute());
        defaultLogicalPhysicalRelationship.setKey(key);
        return defaultLogicalPhysicalRelationship;
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

    /**
     * Generates a generic Source System for testing
     * @return a generic Source System
     */
    private SourceSystem getDefaultSourceSystem(){
        SourceSystem sourceSystem = new SourceSystem();
        sourceSystem.setDescription("Test");
        sourceSystem.setId(123L);
        return sourceSystem;
    }

    /**
     * Generates a generic Attribute for testing
     * @return a generic Attribute
     */
    private Attribute getDefaultAttributeSource(){
        Attribute attribute = new Attribute();
        attribute.setAttributeName("Test");
        attribute.setAttributeId(Long.valueOf(123));
        attribute.setSourceSystemId(123L);
        return attribute;
    }
}
