package testSupport;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

import java.io.FileNotFoundException;

/**
 * This class adds support for a separate log4j configuration than the one in the src/main directory.
 * To use this, simply extend this class rather than SpringJUnit4ClassRunner.
 *
 * This class was built based on an example found here: http://stackoverflow.com/a/8274428.
 *
 * @author d116773
 * @since 2.0.0
 */
public class LoggingSupportTestRunner extends SpringJUnit4ClassRunner {

	private static final String PROPERTIES_FILE_PATH = "classpath:log4j-test.properties";
	static {
		try {
			Log4jConfigurer.initLogging(LoggingSupportTestRunner.PROPERTIES_FILE_PATH);
		} catch (FileNotFoundException ex) {
			System.err.println("Unable to find log4j configuration");
		}
	}
	/**
	 * Constructs a new {@code SpringJUnit4ClassRunner} and initializes a
	 * TestContextManager to provide Spring testing functionality to
	 * standard JUnit tests.
	 *
	 * @param clazz the test class to be run
	 * @see #createTestContextManager(Class)
	 */
	public LoggingSupportTestRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}
}
