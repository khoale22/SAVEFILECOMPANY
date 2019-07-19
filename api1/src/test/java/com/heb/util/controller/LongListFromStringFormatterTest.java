package com.heb.util.controller;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by s573181 on 5/24/2016.
 */
public class LongListFromStringFormatterTest {

    private static Locale locale;

    @Autowired
    private static LongListFromStringFormatter longListFromStringFormatter = new LongListFromStringFormatter();

    /**
     * Prepares local for testing.
     */
    @BeforeClass
    public static void setup(){ LongListFromStringFormatterTest.locale = Locale.getDefault();}

    /**
     * Tests that a comma delimited string is correctly parsed into a List of Long values.
     */
    @Test
    public void parseCommmaDelimeted() {
        String longString = "9223372036854775807, 10000, 555555";
        List<Long> longList = this.longListFromStringFormatter.parse(longString, LongListFromStringFormatterTest.locale);
        Assert.assertEquals(longList.get(0), Long.valueOf(9223372036854775807L));
        Assert.assertEquals(longList.get(1), Long.valueOf(10000));
        Assert.assertEquals(longList.get(2), Long.valueOf(555555));
    }

    /**
     * Tests that a space delimited string is correctly parsed into a List of Long values.
     */
    @Test
    public void parseSpaceDelimeted() {
        String longString = "9223372036854775807 10000 555555";
        List<Long> longList = this.longListFromStringFormatter.parse(longString, LongListFromStringFormatterTest.locale);
        Assert.assertEquals(longList.get(0), Long.valueOf(9223372036854775807L));
        Assert.assertEquals(longList.get(1), Long.valueOf(10000));
        Assert.assertEquals(longList.get(2), Long.valueOf(555555));
    }

    /**
     * Tests that a Tab delimited string is correctly parsed into a List of Long values.
     */
    @Test
    public void parseTabDelimeted() {
        String longString = "9223372036854775807\t10000\t555555";
        List<Long> longList = this.longListFromStringFormatter.parse(longString, LongListFromStringFormatterTest.locale);
        Assert.assertEquals(longList.get(0), Long.valueOf(9223372036854775807L));
        Assert.assertEquals(longList.get(1), Long.valueOf(10000));
        Assert.assertEquals(longList.get(2), Long.valueOf(555555));
    }

    /**
     * Tests that a Carriage Return delimited string is correctly parsed into a List of Long values.
     */
    @Test
    public void parseCarriageReturnDelimeted() {
        String longString = "9223372036854775807\n10000\n555555";
        List<Long> longList = this.longListFromStringFormatter.parse(longString, LongListFromStringFormatterTest.locale);
        Assert.assertEquals(longList.get(0), Long.valueOf(9223372036854775807L));
        Assert.assertEquals(longList.get(1), Long.valueOf(10000));
        Assert.assertEquals(longList.get(2), Long.valueOf(555555));
    }

    /**
     * Tests that the parsed Long List isn't null.
     */
    @Test
    public void testParse_NotNull_When_Comma_Seperated(){
        List<Long> longList;
        longList = longListFromStringFormatter.parse("9223372036854775807, 555555", locale );
        Assert.assertNotNull(longList);
    }

    /**
     * Tests that the parse throws an illegal argument exception when passed non Long values.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParse_Throw_NumberFormatException(){
        longListFromStringFormatter.parse("a,b,c", locale);
    }


    /**
     * Tests that the Print method correctly returns the right string value.
     */
    @Test
    public void testPrint() {
        List<Long> list = new ArrayList<Long>();
        list.add(123L);
        list.add(234L);
        String stringToPrint = longListFromStringFormatter.print(list, Locale.getDefault());
        Assert.assertNotNull(stringToPrint);
        Assert.assertEquals("[123, 234]", stringToPrint);
    }

}