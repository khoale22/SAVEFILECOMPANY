package com.heb.util.ws;

import com.heb.util.ws.scaffolding.converterClasses.DestinationClass;
import com.heb.util.ws.scaffolding.converterClasses.SourceClass;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by d116773 on 4/26/2016.
 */
public class ObjectConverterImplTest {

	private ObjectConverterImpl objectConverter = new ObjectConverterImpl();
	private SourceClass source = new SourceClass();

	@Test
	public void testProcessFieldBigIntegerToInteger() {
		DestinationClass destination = new DestinationClass();

		try {
			this.objectConverter.processField(destination.getClass().getDeclaredField("integerFromBigInteger"),
				destination, this.source);
			Assert.assertEquals("BigInteger to int failed", this.source.getIntBigInteger().intValue(),
					destination.getIntegerFromBigInteger());
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'integerFromBigInteger'");
		} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFieldBigIntegerToLong() {
		DestinationClass destination = new DestinationClass();

		try {
			this.objectConverter.processField(destination.getClass().getDeclaredField("longFromBigInteger"),
					destination, this.source);
			Assert.assertEquals("BigInteger to long failed", this.source.getLongBigInteger().longValue(),
					destination.getLongFromBigInteger());
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'longFromBigInteger'");
		} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFieldStringToInteger() {
		DestinationClass destination = new DestinationClass();

		try {
			this.objectConverter.processField(destination.getClass().getDeclaredField("integerFromString"),
					destination, this.source);
			Assert.assertEquals("String to int failed", Integer.parseInt(this.source.getNumericString()),
					destination.getIntegerFromString());
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'integerFromString'");
		} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFieldStringToLong() {
		DestinationClass destination = new DestinationClass();

		try {
			this.objectConverter.processField(destination.getClass().getDeclaredField("longFromString"),
					destination, this.source);
			Assert.assertEquals("String to long failed", Integer.parseInt(this.source.getNumericString()),
					destination.getLongFromString());
		} catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'longFromString'");
		} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFieldStringToString() {
		DestinationClass destination = new DestinationClass();

		try {
			this.objectConverter.processField(destination.getClass().getDeclaredField("stringFromString"),
					destination, this.source);
			Assert.assertEquals("String to String failed", this.source.getRegularString(), destination.getStringFromString());
		}  catch (NoSuchFieldException e) {
			Assert.fail("Make sure this class has a field called 'stringFromString'");
		} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testGetSourcePropertyValueWorks() {
		DestinationClass destination = new DestinationClass();

		try {
			Object o = this.objectConverter.getSourcePropertyValue(
					destination.getClass().getDeclaredField("stringFromString").getAnnotation(MessageField.class),
					this.source);
			Assert.assertEquals("Did not get source property", this.source.getRegularString(), o);
		} catch (NoSuchFieldException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testProcessCollectionWorks() {
		DestinationClass destination = new DestinationClass();

		try {
			this.objectConverter.processCollection(
					destination.getClass().getDeclaredField("stringList"),
					destination,
					destination.getClass().getDeclaredField("stringList").getAnnotation(MessageField.class),
					this.source
			);

			Assert.assertTrue("Inner list not copied correctly", destination.compareLists(this.source));
		} catch (NoSuchFieldException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testProcessClassWorks() {
		DestinationClass destination = new DestinationClass();

		this.objectConverter.processClass(this.source, destination);
		Assert.assertTrue("Class not processed.", destination.compareTo(this.source));
	}
}
