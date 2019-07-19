package com.heb.util.sercurity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.heb.sadc.utils.AntiVirus;
import com.heb.sadc.utils.AntiVirusException;
import com.heb.sadc.utils.ClamAvAntiVirus95;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Virus scan class, this class requires two properties to be able to function
 * clamav.servername: the address where the antivirus server is located
 * clamav.port: the port number to connect to communicate with the antivirus server
 * @author s753601
 * @version 2.13.0
 */
@Service
public class AntiVirusUtil {
	private static final Logger logger = LoggerFactory.getLogger(AntiVirusUtil.class);
	private static final String BEGIN_SCANNING="checking for antivirus";
	private static final String VIRUS_DETECTED="Virus detected in uploaded file";

	@Value("${clamav.servername}")
	private String serverAddress;

	@Value("${clamav.port}")
	private int serverPort ;

	private AntiVirus scanner;
	/**
	 * This method will check if a file contains a virus, if the file contains no virus the method will return nothing
	 * else the method will throw a AntiVirusException
	 * @param file byte[] The file to be scanned
	 */
	public void virusCheck(byte[] file) throws AntiVirusException{
		AntiVirusUtil.logger.info(BEGIN_SCANNING);
		//Checks if the scanner has been initialized, if not initialize it else do nothing
		if(scanner == null) {
			scanner = new ClamAvAntiVirus95(serverAddress, Integer.toString(serverPort));
		}
		if (scanner.isVirus(file)) {
			AntiVirusUtil.logger.error(VIRUS_DETECTED);
			throw new AntiVirusException(VIRUS_DETECTED);
		}
	}
}

