package com.print.service;

import java.io.File;



import java.io.FileFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.print.arch.AbstractResourceWatcher;
import com.print.impl.FileListener;
import com.print.util.Emailer;
import com.print.util.Util;

public class DirectoryWatchService extends AbstractResourceWatcher {

	/**
	 * Logger instance
	 * 
	 * 
	 */

	/** The Constant logger. */

	private static final Logger debugLogger = Logger.getLogger("debuglog");
	private static final Logger errorLogger = Logger.getLogger("errorlog");
	private static final Logger perfLogger = Logger.getLogger("perflog");
	private static ExecutorService encryptorexecutorpool = null;

	static {

		encryptorexecutorpool = Executors.newCachedThreadPool();
	}

	public static ExecutorService getEncryptorexecutorpool() {
		return encryptorexecutorpool;
	}

	public static void setEncryptorexecutorpool(ExecutorService encryptorpool) {
		encryptorexecutorpool = encryptorpool;
	}

	// ** used to check if previous run active
	public static volatile boolean previousRunActive = false;

	/**
	 * The current map of files and their timestamps (String fileName => Long
	 * lastMod)
	 */
	private File[] currentFiles = null;

	/**
	 * The directory to watch.
	 */
	private String directory;

	/**
	 * The map of last recorded files and their timestamps (String fileName =>
	 * Long lastMod)
	 */
	// private Map<String, Long> prevFiles = new HashMap<String, Long>();

	/**
	 * Constructor that takes the directory to watch.
	 * 
	 * @param directoryPath
	 *            the directory to watch
	 * @param intervalSeconds
	 *            The interval to use when monitoring this directory. I.e., ever
	 *            x seconds, check this directory to see what has changed.
	 * @throws IllegalArgumentException
	 *             if the argument does not map to a valid directory
	 */
	public DirectoryWatchService(String directoryPath, int intervalSeconds) {

		// Get the common thread interval stuff set up.
		super(intervalSeconds, directoryPath + " interval watcher.");

		// Check that it is indeed a directory.
		/*
		 * File theDirectory = new File(directoryPath);
		 * 
		 * if (theDirectory != null && !theDirectory.isDirectory()) {
		 * 
		 * // This is bad, so let the caller know String message = "The path " +
		 * directory + " does not represent a valid directory."; throw new
		 * IllegalArgumentException(message);
		 * 
		 * }
		 */
		// Else all is well so set this directory and the interval
		this.directory = directoryPath;

	}

	/**
	 * For testing only.
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// log4j conf

		PropertyConfigurator.configure("log4j.properties");

		perfLogger.debug("batch-name              content-name        start-time       end-time       Total (seconds)");
		
		// Monitor WATCH_DIR every 30 seconds
		if (args == null || args.length == 0 || args[0] == null
				|| args[1] == null || args[2] == null || args[3] == null
				|| args[4] == null || args[5] == null || args[6] == null
				|| args[7] == null) {
			debugLogger
					.debug("Please set the environment before starting the application System exiting...");
			errorLogger
					.error("Please set the environment before starting the application");
			System.exit(0);
		}
		
		Util.setupEnv(args[0], args[1], Integer.parseInt(args[2]),
				args[3], args[4], Integer.parseInt(args[5]), args[6],
				args[7], Long.parseLong(args[8]), args[9], args[10],
				args[11], args[12], args[13], args[14], args[15], args[16],
				args[17], args[18]);
		DirectoryWatchService dw = new DirectoryWatchService(
				Util.getWatchDir(), Util.getdelayBetweenIteration());
		dw.addListener(new FileListener());
		debugLogger.debug("starting a thread from main..."
				+ Thread.currentThread().getName());
		dw.start();
		
	}

	/**
	 * Start the monitoring of this directory.
	 */
	@Override
	public void start() throws Exception {

		// Since we're going to start monitoring, we want to take a snapshot of
		// the
		// current directory to we have something to refer to when stuff
		// changes.
		debugLogger.debug("Taking a snapshot of directory");
		// takeSnapshot();

		// And start the thread on the given interval
		super.start();

		// And notify the listeners that monitoring has started
		File theDirectory = new File(directory);
		monitoringStarted(theDirectory);
	}

	/**
	 * Stop the monitoring of this directory.
	 */
	@Override
	public void stop() throws Exception {

		// And start the thread on the given interval
		super.stop();

		// And notify the listeners that monitoring has started
		File theDirectory = new File(directory);
		monitoringStopped(theDirectory);
	}

	/**
	 * Store the file names and the last modified timestamps of all the files
	 * and directories that exist in the directory at this moment.
	 */
	private void takeSnapshot() {

		debugLogger.debug("takesnapshot() .....");

		File dir = new File(directory);
		FileFilter fileFilter = new WildcardFileFilter(
				Util.getBatchFileExtension());
		currentFiles = dir.listFiles(fileFilter);
		if (currentFiles != null && currentFiles.length > 0)
			System.out.println("size" + currentFiles.length);

	}

	/**
	 * Check this directory for any changes and fire the proper events.
	 */
	@Override
	protected void doInterval() throws Exception {

		// Take a snapshot of the current state of the dir for comparisons

		// Thread starting
		if (previousRunActive) {
			debugLogger.debug("previous iteration still active ..."
					+ Thread.currentThread().getName());
			return;
		}
		debugLogger.debug("doInterval() invocation ..."
				+ Thread.currentThread().getName());

		try {

			takeSnapshot();

			// Iterate through the map of current files and compare
			// them for differences etc...

			if (currentFiles != null && currentFiles.length > 0) {

				for (int i = 0; i < currentFiles.length; i++) {

					debugLogger.debug("Adding the resource ..."
							+ currentFiles[i]);

					resourceAdded((currentFiles[i]));

				}
			}
		} catch (Exception e) {

			errorLogger
					.error("DirectoryWatcher: doInterval() : 1 get snap shot of listening directory error."
							+ e.getMessage() + " " + "\n");
			Emailer.sendEMail("Print Server Failure Error - Printer   - "
					+ Util.getPrinterName(), e.getMessage());

			throw e;
		}
	}

}
