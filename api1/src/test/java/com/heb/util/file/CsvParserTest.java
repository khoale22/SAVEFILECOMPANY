package com.heb.util.file;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test CsvParser
 *
 * @author d116773
 * @since 2.15.0
 */
public class CsvParserTest {

	@Test
	public void parseLine_simple_case() {
		CsvParser csvParser = new CsvParser();

		String line = "10,Test,50";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(3, parsedLine.size());
		Assert.assertEquals("10", parsedLine.get(0));
		Assert.assertEquals("Test", parsedLine.get(1));
		Assert.assertEquals("50", parsedLine.get(2));
	}

	@Test
	public void parseLine_has_double_quotes() {
		CsvParser csvParser = new CsvParser();

		String line = "10,\"Test with quotes\",50";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(3, parsedLine.size());
		Assert.assertEquals("10", parsedLine.get(0));
		Assert.assertEquals("Test with quotes", parsedLine.get(1));
		Assert.assertEquals("50", parsedLine.get(2));
	}

	@Test
	public void parseLine_has_embedded_double_quotes() {
		CsvParser csvParser = new CsvParser();

		String line = "10,\"Test with \"\" quotes\",50";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(3, parsedLine.size());
		Assert.assertEquals("10", parsedLine.get(0));
		Assert.assertEquals("Test with \" quotes", parsedLine.get(1));
		Assert.assertEquals("50", parsedLine.get(2));
	}

	@Test
	public void parseLine_has_embedded_double_double_quotes() {
		CsvParser csvParser = new CsvParser();

		String line = "10,\"Test with \"\"\"\" quotes\",50";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(3, parsedLine.size());
		Assert.assertEquals("10", parsedLine.get(0));
		Assert.assertEquals("Test with \"\" quotes", parsedLine.get(1));
		Assert.assertEquals("50", parsedLine.get(2));
	}

	@Test
	public void parseLine_has_embedded_comma() {
		CsvParser csvParser = new CsvParser();

		String line = "10,\"Test with, quotes\",50";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(3, parsedLine.size());
		Assert.assertEquals("10", parsedLine.get(0));
		Assert.assertEquals("Test with, quotes", parsedLine.get(1));
		Assert.assertEquals("50", parsedLine.get(2));
	}

	@Test
	public void parseLine_starts_with_quote() {
		CsvParser csvParser = new CsvParser();

		String line = "\"10 ,\",\"Test with, quotes\",50";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(3, parsedLine.size());
		Assert.assertEquals("10 ,", parsedLine.get(0));
		Assert.assertEquals("Test with, quotes", parsedLine.get(1));
		Assert.assertEquals("50", parsedLine.get(2));
	}

	@Test
	public void parseLine_ends_with_quote() {
		CsvParser csvParser = new CsvParser();

		String line = "10,\"Test with, quotes\",\"50 ,\"";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(3, parsedLine.size());
		Assert.assertEquals("10", parsedLine.get(0));
		Assert.assertEquals("Test with, quotes", parsedLine.get(1));
		Assert.assertEquals("50 ,", parsedLine.get(2));
	}

	@Test
	public void parseLine_one_field() {
		CsvParser csvParser = new CsvParser();

		String line = "10";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(1, parsedLine.size());
		Assert.assertEquals("10", parsedLine.get(0));
	}

	@Test
	public void parseLine_one_field_with_quotes() {
		CsvParser csvParser = new CsvParser();

		String line = "\"10\"";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(1, parsedLine.size());
		Assert.assertEquals("10", parsedLine.get(0));
	}

	@Test
	public void parseLine_one_field_with_comma() {
		CsvParser csvParser = new CsvParser();

		String line = "\"10,\"";
		List<String> parsedLine = csvParser.parseLine(line);
		Assert.assertEquals(1, parsedLine.size());
		Assert.assertEquals("10,", parsedLine.get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseLine_error_no_close_quote() {
		CsvParser csvParser = new CsvParser();

		String line = "10,\"Test with, quotes,50";
		List<String> parsedLine = csvParser.parseLine(line);
	}

	@Test
	public void parseLine_with_null() {
		CsvParser csvParser = new CsvParser();
		List<String> parsedLine = csvParser.parseLine(null);
		Assert.assertNotNull(parsedLine);
		Assert.assertTrue(parsedLine.isEmpty());
	}

	@Test
	public void parseLine_empty_string() {
		CsvParser csvParser = new CsvParser();
		List<String> parsedLine = csvParser.parseLine("");
		Assert.assertNotNull(parsedLine);
		Assert.assertTrue(parsedLine.isEmpty());
	}
}