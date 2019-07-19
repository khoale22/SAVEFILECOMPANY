package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests all of the getters and setters for the Logical Physical Relationship entity
 *
 * @author s753601
 * @since 2.5.0
 */
public class LogicalPhysicalRelationshipTest {

    /**
     * Tests the getKey Method of the LogicalPhysicalRelationship class
     */
    @Test
    public void getKeyTest() {
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(456, test.getKey().getPhysicalAttributeId());
        Assert.assertEquals(123, test.getKey().getLogicalAttributeId());
    }

    /**
     * Tests the setKey Method of the LogicalPhysicalRelationship class
     */
    @Test
    public void setKeyTest() {
        LogicalPhysicalRelationshipKey testKey = getDefaultKey();
        testKey.setRelationshipGroupTypeCode("GRP ");
        testKey.setPhysicalAttributeId(789);
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(456, test.getKey().getPhysicalAttributeId());
        Assert.assertEquals(123, test.getKey().getLogicalAttributeId());
        test.setKey(testKey);
        Assert.assertEquals(789, test.getKey().getPhysicalAttributeId());
        Assert.assertEquals("GRP ", test.getKey().getRelationshipGroupTypeCode());
    }

    /**
     * Tests the getAttribute Method of the LogicalPhysicalRelationship class
     */
    @Test
    public void getAttributeTest() {
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(Long.valueOf(123), test.getAttribute().getAttributeId());
    }

    /**
     * Tests the setAttribute Method of the LogicalPhysicalRelationship class
     *
     */
    @Test
    public void setAttributeTest() {
        Attribute testAttribute = getDefaultAttribute();
        testAttribute.setAttributeId(Long.valueOf(456));
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(Long.valueOf(123), test.getAttribute().getAttributeId());
        test.setAttribute(testAttribute);
        Assert.assertEquals(Long.valueOf(456), test.getAttribute().getAttributeId());

    }

    /**
     * Tests the getTargetSystemAttributePriorities Method of the LogicalPhysicalRelationship class
     */
    @Test
    public void getTargetSystemAttributePrioritiesTest() {
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(1, test.getTargetSystemAttributePriorities().size());
    }

    /**
     *Tests the setTargetSystemAttributePriorities Method of the LogicalPhysicalRelationship class
     */
    @Test
    public void setTargetSystemAttributePrioritiesTest() {
        List<TargetSystemAttributePriority> testList = getTargetSystemAttributePriorityList();
        testList.add(getDefaultTargetSystemAttributePriority());
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(1, test.getTargetSystemAttributePriorities().size());
        test.setTargetSystemAttributePriorities(testList);
        Assert.assertEquals(2, test.getTargetSystemAttributePriorities().size());
    }

    /**
     *Tests the getRelationshipGroup Method of the LogicalPhysicalRelationship class
     */
    @Test
    public void  getRelationshipGroupTest() {
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(getDefaultRelationshipGroup().getRelationshipGroupId(), test.getRelationshipGroup().getRelationshipGroupId());
    }

    /**
     *Tests the setRelationshipGroup Method of the LogicalPhysicalRelationship class
     */
    @Test
    public void setRelationshipGroupTest() {
        RelationshipGroup newRelationshipGroup = getDefaultRelationshipGroup();
        newRelationshipGroup.setRelationshipGroupId(753);
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(getDefaultRelationshipGroup().getRelationshipGroupId(), test.getRelationshipGroup().getRelationshipGroupId());
        test.setRelationshipGroup(newRelationshipGroup);
        Assert.assertEquals(newRelationshipGroup.getRelationshipGroupId(), test.getRelationshipGroup().getRelationshipGroupId());

    }

    /**
     *Tests the toString Method of the LogicalPhysicalRelationship class
     */
    @Test
    public void toStringTest(){
        String expect = "Logical Physical Relationship{" +
                "Log Attr ID: " + 123 +
                ", Phy Attr ID: " + 456 +
                ", Rlshp Grp Typ Cd: '" + "ATTR" +
                ", Attribute: " + getDefaultAttribute().toString() +
                ", Rlshp Grp: " + getDefaultRelationshipGroup().toString() + "}";
        LogicalPhysicalRelationship test = getDefaultLogicalPhysicalRelationship();
        Assert.assertEquals(expect, test.toString());
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
        defaultLogicalPhysicalRelationship.setTargetSystemAttributePriorities(getTargetSystemAttributePriorityList());
        defaultLogicalPhysicalRelationship.setRelationshipGroup(getDefaultRelationshipGroup());
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
     *Generates a generic List of Logical Relationships for testing
     * @return a generic list for testing.
     */
    private List<LogicalPhysicalRelationship> getDefaultLogicalPhysicalRelationshipList(){
        ArrayList<LogicalPhysicalRelationship> list = new ArrayList<>();
        list.add(getDefaultLogicalPhysicalRelationship());
        return list;
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
     * Generates a generic Target System Attribute Priority object for testing
     * @return a generic Target System Attribute Priority object for testing
     */
    private TargetSystemAttributePriority getDefaultTargetSystemAttributePriority(){
        TargetSystemAttributePriority targetSystemAttributePriority = new TargetSystemAttributePriority();
        targetSystemAttributePriority.setKey(getTargetSystemAttributePriorityKey());
        targetSystemAttributePriority.setAttributePriorityNumber(1);
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
     * Generates a generic a list of Target System Attribute priorities
     * @return a generic a list of Target System Attribute priorities
     */
    private List<TargetSystemAttributePriority> getTargetSystemAttributePriorityList(){
        ArrayList<TargetSystemAttributePriority> list = new ArrayList<>();
        list.add(getDefaultTargetSystemAttributePriority());
        return list;
    }

    /**
     * Generates a generic Relationship group for testing
     * @return a generic Relationship group for testing
     */
    private RelationshipGroup getDefaultRelationshipGroup(){
        RelationshipGroup relationshipGroup = new RelationshipGroup();
        relationshipGroup.setRelationshipGroupDescription("Test");
        relationshipGroup.setRelationshipGroupId(123);
        return relationshipGroup;
    }
}
