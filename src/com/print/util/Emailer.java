// File:         [Emailer.java]
// Created:      [Dec 16, 2009 creation date]
// Last Changed: [Dec 16, 2009 date last changed]
// Author:       [Ram]
//
// This code is copyright (c) 2009 Allied Vaughn
// 
// History:
// 
//

package com.print.util;

import java.io.File;
import java.io.FileFilter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.StringTokenizer;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

/**
 * The Class Emailer.
 */
public class Emailer {

	/** The Constant logger. */

	/** The mail server config. */
	//private static Properties fMailServerConfig = new Properties();

	/**
	 * Simple demonstration of using the javax.mail API.
	 * 
	 * Run from the command line. Please edit the implementation to use correct
	 * email addresses and host name.
	 */

	private static final Logger errorLogger = Logger.getLogger("errorLog");

	/*
	 * static { Emailer.fetchConfig(); }
	 */

	/**
	 * The main method.
	 * 
	 * @param aArguments
	 *            the arguments
	 */
	public static void main(String... aArguments) throws Exception {

		// the domains of these email addresses should be valid,
		// or the example will fail:
		// Emailer.sendEmail("fromblah@blah.com", "toblah@blah.com",
		// "Testing 1-2-3", "blah blah blah");
		// Emailer.testEMail("testing", "teter");
		String test = "60";
		int t = Integer.parseInt(test);
		System.out.println("batch-name                content-name                   start-time      end-time       Total (seconds)");;
		LocalTime st = LocalTime.parse(Util.getCurrentLocalDateTimeStamp());
		LocalTime et = LocalTime.parse(Util.getCurrentLocalDateTimeStamp());
		System.out.println("pjm-20030661609-SKU"+"       "+"pjm-20030661609-SKU-SEP.pdf"+"     "+st + "        " + et + "        "+ ChronoUnit.SECONDS.between(st,et));
		
	}

	public static void getList(String fileDir) {

		File dir = new File(fileDir);
		FileFilter fileFilter = new WildcardFileFilter("*.txt");
		File[] files = dir.listFiles(fileFilter);
		if (files != null && files.length > 0)
			System.out.println("size" + files.length);

	}

	// PRIVATE //

	public static void sendEMail(String subject, String body) {

		try {
			Email email = new SimpleEmail();
			email.setHostName(Util.getHost());
			email.setSmtpPort(Integer.parseInt(Util.getPort()));
			email.setAuthenticator(new DefaultAuthenticator(Util.getUser(),
					Util.getPwd()));
			email.setSSLOnConnect(true);
			email.setFrom(Util.getFrom());
			email.setSubject(subject);
			email.setMsg(body);
			StringTokenizer st = new StringTokenizer(Util.getTo(), ",");
			while (st.hasMoreElements()) {
				String next = (String) st.nextElement();
				System.out.println(next);
				email.addTo(next);
			}
			email.send();

		} catch (final Exception ex) {
			errorLogger.error(" Emailer : sendEmail() : Email sending error "
					+ ex.getMessage());
		}

	}

}
