// File:         [ApplicationPropertyDigester.java]
// Created:      [Dec 27, 2009 creation date]
// Last Changed: [Dec 27, 2009 date last changed]
// Author:       [Ram]
//
// This code is copyright (c) 2009 Allied Vaughn
// 
// History:
//

package com.print.util;

/* java lang packages */

import java.io.IOException;
import java.util.Hashtable;

import org.apache.commons.digester.Digester;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationPropertyDigester.
 */
public class ApplicationPropertyDigester {
	/** The Constant logger. */
	private static final Logger logger = Logger
			.getLogger(ApplicationPropertyDigester.class.getName());

	// Hashtable to store query tag name and value
	/** The properties. */
	private static Hashtable<String, String> properties = new Hashtable<String, String>();

	/**
	 * Digest application property.
	 * 
	 * @param propertyName
	 *            the property name
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SAXException
	 *             the SAX exception
	 */
	public static void digestApplicationProperty(String propertyName) {
		try {
			ApplicationPropertyDigester.logger
					.debug("ApplicationPropertyDigester:digestApplicationProperty() proparty name = "
							+ propertyName);

			final Digester digester = new Digester();

			digester.push(new ApplicationPropertyDigester());
			digester.addCallMethod("PropertyConfiguration/Properties/Property",
					"addProperties", 2);
			digester.addCallParam(
					"PropertyConfiguration/Properties/Property/name", 0);
			digester.addCallParam(
					"PropertyConfiguration/Properties/Property/value", 1);
			final java.io.InputStream inputStream = ApplicationPropertyDigester.class
					.getClassLoader().getResourceAsStream(
							"ApplicationProperties.xml");

			ApplicationPropertyDigester.logger.debug("Input Stream "
					+ inputStream);
			digester.parse(inputStream);
		} catch (final Exception e) {
			logger.error(ExceptionUtils.getMessage(e));

		}
	}

	/**
	 * Gets the properties.
	 * 
	 * @param propertyName
	 *            the property name
	 * 
	 * @return the properties
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws SAXException
	 *             the SAX exception
	 */
	public static Hashtable<String, String> getProperties(String propertyName) {

		ApplicationPropertyDigester.logger
				.debug("ApplicationPropertyDigester:getProperties() propertyName = "
						+ propertyName);

		if (ApplicationPropertyDigester.properties == null
				|| ApplicationPropertyDigester.properties.isEmpty()) {
			ApplicationPropertyDigester.digestApplicationProperty(propertyName);
		}
		return ApplicationPropertyDigester.properties;

	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {

	}

	/**
	 * Adds the properties.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public void addProperties(String name, String value) {

		ApplicationPropertyDigester.logger
				.debug("ApplicationPropertyDigester:addProperties() proparty name = "
						+ name + " value = " + value);

		ApplicationPropertyDigester.logger.debug("Name =" + name + " "
				+ "value " + value);
		ApplicationPropertyDigester.properties.put(name, value);
	}
}
