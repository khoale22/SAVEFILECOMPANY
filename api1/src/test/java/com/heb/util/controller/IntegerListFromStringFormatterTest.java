package com.heb.util.controller;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IntegerListFromStringFormatterTest {

    private static Locale locale;

    private IntegerListFromStringFormatter integerListFromStringFormatter = new IntegerListFromStringFormatter();

    @BeforeClass
    public static void setup() {
        IntegerListFromStringFormatterTest.locale = Locale.getDefault();
    }

    /**
     * Test parsing comma delimited string.
     */
    @Test
    public void parseCommaDelimited() {
        String integerString = "123, 456, 890";
        List<Integer> integerList = this.integerListFromStringFormatter.parse(integerString, IntegerListFromStringFormatterTest.locale);

        Assert.assertNotNull(integerList);
        Assert.assertEquals(integerList.get(0), Integer.valueOf(123));
        Assert.assertEquals(integerList.get(1), Integer.valueOf(456));
        Assert.assertEquals(integerList.get(2), Integer.valueOf(890));
    }

    /**
     * Test parsing space delimited string.
     */
    @Test
    public void parseSpaceDelimited() {
        String integerString = "123 456 890";
        List<Integer> integerList = this.integerListFromStringFormatter.parse(integerString, IntegerListFromStringFormatterTest.locale);

        Assert.assertNotNull(integerList);
        Assert.assertEquals(integerList.get(0), Integer.valueOf(123));
        Assert.assertEquals(integerList.get(1), Integer.valueOf(456));
        Assert.assertEquals(integerList.get(2), Integer.valueOf(890));
    }

    /**
     * Test parsing tab delimited string.
     */
    @Test
    public void parseTabDelimited() {
        String integerString = "123\t456\t890";
        List<Integer> integerList = this.integerListFromStringFormatter.parse(integerString, IntegerListFromStringFormatterTest.locale);

        Assert.assertNotNull(integerList);
        Assert.assertEquals(integerList.get(0), Integer.valueOf(123));
        Assert.assertEquals(integerList.get(1), Integer.valueOf(456));
        Assert.assertEquals(integerList.get(2), Integer.valueOf(890));
    }

    /**
     * Test parsing carriage return delimited string.
     */
    @Test
    public void parseCarriageReturnDelimited() {
        String integerString = "123\n456\n890";
        List<Integer> integerList = this.integerListFromStringFormatter.parse(integerString, IntegerListFromStringFormatterTest.locale);

        Assert.assertNotNull(integerList);
        Assert.assertEquals(integerList.get(0), Integer.valueOf(123));
        Assert.assertEquals(integerList.get(1), Integer.valueOf(456));
        Assert.assertEquals(integerList.get(2), Integer.valueOf(890));
    }

    /**
     * Test parsing comma delimited string not null.
     */
    @Test
    public void testParse_NotNull_When_Comma_Seperated() {
        List<Integer> integerList;
        integerList = integerListFromStringFormatter.parse("123, 1234", locale);

        Assert.assertNotNull(integerList);
    }

    /**
     * Test parsing character string throwing exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParse_Throw_NumberFormatException(){
        integerListFromStringFormatter.parse("a,b,c", locale);
    }

    /**
     * Test print.
     */
    @Test
    public void testPrint() {
        List<Integer> list =new ArrayList<Integer>();
        list.add(123);
        list.add(234);
        String stringToPrint = integerListFromStringFormatter.print(list, Locale.getDefault());

        Assert.assertNotNull(stringToPrint);
        Assert.assertEquals("[123, 234]", stringToPrint);
    }

}