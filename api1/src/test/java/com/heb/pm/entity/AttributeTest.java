package com.heb.pm.entity;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests all of the getters and setters for the Attribute entity
 *
 * @author s753601
 * @since 2.5.0
 */
public class AttributeTest {

    /**
     *Test the getAttribute Method in the Attribute class
     */
    @Test
    public void getAttributeIDTest(){
        Attribute test = getDefaultAttribute();
        Assert.assertEquals(Long.valueOf(123), test.getAttributeId());
    }

    /**
     *Tests the setAttribute Method in the Attribute class
     */
    @Test
    public void setAttributeIDTest(){
        Attribute test = getDefaultAttribute();
        Assert.assertEquals(Long.valueOf(123), test.getAttributeId());
        test.setAttributeId(Long.valueOf(456));
        Assert.assertEquals(Long.valueOf(456), test.getAttributeId());

    }

    /**
     *Tests the getAttributeName Method in the Attribute class
     */
    @Test
    public void getAttributeNameTest(){
        Attribute test = getDefaultAttribute();
        Assert.assertEquals("Test", test.getAttributeName());
    }

    /**
     *Tests the setAttributeName Method in the Attribute class
     */
    @Test
    public void setAttributeNameTest(){
        Attribute test = getDefaultAttribute();
        Assert.assertEquals("Test", test.getAttributeName());
        test.setAttributeName("Not Test");
        Assert.assertEquals("Not Test", test.getAttributeName());
    }

    /**
     *Tests the getLogicalPhysicalRelationship Method in the Attribute class
     */
    @Test
    public void getLogicalPhysicalRelationshipTest(){
        Attribute test = getDefaultAttribute();
        Assert.assertNotNull(test.getLogicalPhysicalRelationship());
        Assert.assertEquals(123, test.getLogicalPhysicalRelationship().get(0).getKey().getLogicalAttributeId());
        Assert.assertEquals(456, test.getLogicalPhysicalRelationship().get(0).getKey().getPhysicalAttributeId());

    }

    /**
     *Tests the setLogicalPhysicalRelationship Method in the Attribute class
     */
    @Test
    public void setLogicalPhysicalRelationshipTest(){
        List<LogicalPhysicalRelationship> list = getDefaultLogicalPhysicalRelationshipList();
        list.add(getDefaultLogicalPhysicalRelationship());
        Attribute test = getDefaultAttribute();
        Assert.assertNotNull(test.getLogicalPhysicalRelationship());
        Assert.assertEquals(123, test.getLogicalPhysicalRelationship().get(0).getKey().getLogicalAttributeId());
        Assert.assertEquals(456, test.getLogicalPhysicalRelationship().get(0).getKey().getPhysicalAttributeId());
        test.setLogicalPhysicalRelationshipLists(list);
        Assert.assertEquals(2, test.getLogicalPhysicalRelationship().size());
    }

    /**
     *Tests the toString Method in the Attribute class
     */
    @Test
    public void toStringTest(){
        String expected = "Attribute(" +
                "Attribute ID: " + 123 +
                " Attribute Name: " + "Test" +
                ")";
        Attribute test = getDefaultAttribute();
        Assert.assertEquals(expected, test.toString());

    }

    /**
     *Generates a generic Attribute for testing
     * @return a generic Attribute
     */
    private Attribute getDefaultAttribute(){
        Attribute defaultAttribute = new Attribute();
        defaultAttribute.setAttributeId(Long.valueOf(123));
        defaultAttribute.setAttributeName("Test");
        defaultAttribute.setLogicalPhysicalRelationshipLists(getDefaultLogicalPhysicalRelationshipList());
        return defaultAttribute;
    }

    /**
     *Generates a generic Logical Physical Relationship for testing
     * @return a generic Logical Physical Relationship
     */
    private LogicalPhysicalRelationship getDefaultLogicalPhysicalRelationship(){
        LogicalPhysicalRelationship defaultLogicalPhysicalRelationship = new LogicalPhysicalRelationship();
        LogicalPhysicalRelationshipKey key = new LogicalPhysicalRelationshipKey();
        key.setLogicalAttributeId(123);
        key.setPhysicalAttributeId(456);
        defaultLogicalPhysicalRelationship.setKey(key);
        return defaultLogicalPhysicalRelationship;
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
}
