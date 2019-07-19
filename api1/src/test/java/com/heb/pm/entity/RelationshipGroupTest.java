package com.heb.pm.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests all of the getters and setters for the Relationship Group entity
 *
 * @author s753601
 * @since 2.5.0
 */
public class RelationshipGroupTest {

    /**
     * Tests the getRelationshipGroupId Method of the RelationshipGroup class
     */
    @Test
    public void getRelationshipGroupIdTest() {
        RelationshipGroup test = getDefaultRelationshipGroup();
        Assert.assertEquals(123, test.getRelationshipGroupId());
    }

    /**
     * Tests the setRelationshipGroupId Method of the RelationshipGroup class
     */
    @Test
    public void setRelationshipGroupIdTest() {
        RelationshipGroup test = getDefaultRelationshipGroup();
        Assert.assertEquals(123, test.getRelationshipGroupId());
        test.setRelationshipGroupId(456);
        Assert.assertEquals(456, test.getRelationshipGroupId());
    }

    /**
     * Tests the getRelationshipGroupDescription Method of the RelationshipGroup class
     */
    @Test
    public void getRelationshipGroupDescriptionTest() {
        RelationshipGroup test = getDefaultRelationshipGroup();
        Assert.assertEquals("Test", test.getRelationshipGroupDescription());
    }

    /**
     * Tests the setRelationshipGroupDescription Method of the RelationshipGroup class
     */
    @Test
    public void setRelationshipGroupDescriptionTest() {
        RelationshipGroup test = getDefaultRelationshipGroup();
        Assert.assertEquals("Test", test.getRelationshipGroupDescription());
        test.setRelationshipGroupDescription("Not Test");
        Assert.assertEquals("Not Test", test.getRelationshipGroupDescription());
    }

    /**
     * Tests the toString Method of the RelationshipGroup class
     * @return
     */
    @Test
    public void toStringTest(){
        String expect = "Relationship Group{" +
                "ID: " + 123 +
                ", Description: '" + "Test" + "'}";
        RelationshipGroup test = getDefaultRelationshipGroup();
        Assert.assertEquals(expect, test.toString());
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
