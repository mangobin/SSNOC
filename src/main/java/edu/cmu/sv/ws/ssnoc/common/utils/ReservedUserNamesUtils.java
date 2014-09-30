package edu.cmu.sv.ws.ssnoc.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.rest.SSNoCAppInitializer;

public class ReservedUserNamesUtils {

	/**
	 * List of invalid names
	 */
	public static Set<String> INVALID_NAMES = new HashSet<String>();
	
	static {
		Log.trace("Loading reserved usernames...");
		try (InputStream input = SSNoCAppInitializer.class.getClassLoader()
				.getResourceAsStream("/reservedUsernames.properties");) {
			if (input != null) {
				// Load a properties file
				Properties prop = new Properties();
				prop.load(input);

				// Load the list of invalid names into a Set
				String invalidNames = prop.getProperty("reservedNames");
				if (invalidNames != null) {
					String[] nameList = invalidNames.split(" ");

					for (int i = 0; i < nameList.length; i++) {
						INVALID_NAMES.add(nameList[i]);
					}
				} else {
					Log.warn("!!! Could not find banned user names in the properties files !!!");
				}
			} else {
				Log.warn("Could not load resevered usernames file.");
			}
		} catch (IOException ex) {
			Log.error("Error when loading reserved usernames file : ", ex);
		}
	}
	
}
