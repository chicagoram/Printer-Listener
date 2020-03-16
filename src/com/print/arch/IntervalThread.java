package com.print.arch;

import org.apache.log4j.Logger;

import com.print.service.DirectoryWatchService;
import com.print.util.Util;

public abstract class IntervalThread implements Runnable {

	/**
	 * Whether or not this thread is active.
	 */
	private boolean active = false;

	/**
	 * The interval in seconds to run this thread
	 */
	private int interval = -1;

	/**
	 * The name of this pool (for loggin/display purposes).
	 */
	private String name;

	/**
	 * This instance's thread
	 */
	private Thread runner;

	private static final Logger debugLogger = Logger.getLogger("debuglog");
	private static final Logger errorLogger = Logger.getLogger("errorlog");

	/**
	 * Construct a new interval thread that will run on the given interval with
	 * the given name.
	 * 
	 * @param intervalSeconds
	 *            the number of seconds to run the thread on
	 * @param name
	 *            the name of the thread
	 */
	public IntervalThread(int intervalSeconds, String name) {

		this.interval = intervalSeconds * 1000;
		this.name = name;
	}

	/**
	 * Start the thread on the specified interval.
	 */
	public void start() throws Exception {

		debugLogger.debug("Starting Interval Thread "
				+ Thread.currentThread().getName());
		active = true;

		// If we don't have a thread yet, make one and start it.
		if (runner == null && interval > 0) {
			debugLogger.debug("Thread Not running "
					+ Thread.currentThread().getName());
			runner = new Thread(this);
			runner.start();
		}
	}

	/**
	 * Stop the interval thread.
	 */
	public void stop() throws Exception {
		debugLogger.debug("Stopping Interval Thread "
				+ Thread.currentThread().getName());
		active = false;
	}

	/**
	 * Not for public use. This thread is automatically started when a new
	 * instance is retrieved with the getInstance method. Use the start() and
	 * stop() methods to control the thread.
	 * 
	 * @see Thread#run()
	 */
	@Override
	public void run() {

		debugLogger.debug("Running Interval Thread   "
				+ Thread.currentThread().getName());

		// Make this a relatively low level thread
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		// Pause this thread for the amount of the interval
		while (active) {
			try {

				Util.setIteration(1);
				debugLogger.debug("Iteration Count " + Util.getIteration());

				doInterval();
				Thread.sleep(interval);
				DirectoryWatchService.previousRunActive = false;

			} catch (Exception e) {
				errorLogger.error("LOC003:- Interval Thread interrupting "
						+ e.getMessage());
				// Ignore
			}
		}
	}

	/**
	 * String representation of this object. Just the name given to it an
	 * instantiation.
	 * 
	 * @return the string name of this pool
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * The interval has expired and now it's time to do something.
	 */
	protected abstract void doInterval() throws Exception;
}
