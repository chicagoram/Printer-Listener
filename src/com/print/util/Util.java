package com.print.util;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.ProcessBuilder.Redirect;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class Util {

	/** The Constant APPLICATION_PROPERTIES_FILE. */
	public static final String APPLICATION_PROPERTIES_FILE = "ApplicationProperties.xml";
	/** The Application property table. */
	private static Hashtable<String, String> ApplicationPropertyTable = null;
	private static final String mailfrom = "sysadmin-printserver";
	private static final String mailpass = "seD3nv3r1";
	private static final String db_user = "avuser";
	private static final String db_password = "avuser";
	public static final String ADD_RESOURCE_ERROR = "Unable to add the resource";
	public static final String FILE_MOVE_ERROR = "Unable to move the file";
	public static final String FILE_OR_NETWORK_NOT_FOUND = "File not found or Network not accessible";
	private static int hot_folder_wait_cnt = 0;
	private static String printerName = "";
	private static String watchDir = null;
	private static String processDest = null;
	private static int delayBetweenIteration = 0;
	private static long hotfolderBsyChkIntvl = 0;
	private static String hotFolder = null;
	private static String cfgFolder = null;
	public static volatile int totRecords = 0;
	private static String ignoreFiles = "tst-cnnct";
	private static int iteration = 0;
	private static String batchFileExtension = null;
	private static String art_conv_status_log = null;
	private static String convert_process_single = null;
	private static String convert_process_double = null;
	private static String host = null;
	private static String port = null;
	private static String user = null;
	private static String pwd = null;
	private static String from = null;
	private static String to = null;

	public static String getDbUser() {
		return db_user;
	}

	public static String getDbPassword() {
		return db_password;
	}

	public static String getMailfrom() {
		return mailfrom;
	}

	public static String getMailpass() {
		return mailpass;
	}

	private static String hotfolderFileXtn = null;

	public static String getHotfolderFileXtn() {
		return hotfolderFileXtn;
	}

	public static void setHotfolderFileXtn(String hotfolderFileXtn) {
		Util.hotfolderFileXtn = hotfolderFileXtn;
	}

	public static String getBatchFileExtension() {
		return batchFileExtension;
	}

	public void setBatchFileExtension(String batchXtn) {
		batchFileExtension = batchXtn;
	}

	public static String getPrinterName() {
		return printerName;
	}

	public static void setPrinterName(String printerName) {
		Util.printerName = printerName;
	}

	/*
	 * public static String[] getMailto() { return mailTo; }
	 */

	public static int getIteration() {
		return iteration;
	}

	public static void setIteration(int iterCount) {
		iteration = iteration + iterCount;
	}

	/** The Constant logger. */
	/*
	 * private static final Logger logger = Logger .getLogger(Util.class);
	 */
	public static int getTotRecords() {
		return totRecords;
	}

	public static void setTotRecords(int records) {
		totRecords = records;
	}

	public static String getIgnoreFiles() {
		return ignoreFiles;
	}

	public static void setIgnoreFiles(String ignoreFiles) {
		Util.ignoreFiles = ignoreFiles;
	}

	public static String getCurrentLocalDateTimeStamp() throws Exception{
		 				 
		 return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}
	public static void setupEnv(String watch, String processedDest, int delay,
			String hotfolder, String cfgfolder, int hot_folder_wait_counter,
			String printer, String hotfolder_filextension, long waittime,
			String batchFileXtn, String art_status_log,
			String conversion_process_single, String conversion_process_double,
			String mailhost, String mailport, String mailuser, String mailpwd,
			String mailfrom, String mailto) {

		watchDir = watch;
		processDest = processedDest;
		delayBetweenIteration = delay;
		hotfolderBsyChkIntvl = waittime;
		hotFolder = hotfolder;
		cfgFolder = cfgfolder;
		hot_folder_wait_cnt = hot_folder_wait_counter;
		printerName = printer;
		hotfolderFileXtn = hotfolder_filextension;
		batchFileExtension = batchFileXtn;
		art_conv_status_log = art_status_log;
		convert_process_single = conversion_process_single;
		convert_process_double = conversion_process_double;
		host = mailhost;
		port = mailport;
		user = mailuser;
		pwd = mailpwd;
		from = mailfrom;
		to = mailto;

	}

	public static String getConvert_process_double() {
		return convert_process_double;
	}

	public static void setConvert_process_double(String convert_process_double) {
		Util.convert_process_double = convert_process_double;
	}

	public static String getHost() {
		return host;
	}

	public static void setHost(String host) {
		Util.host = host;
	}

	public static String getPort() {
		return port;
	}

	public static void setPort(String port) {
		Util.port = port;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		Util.user = user;
	}

	public static String getPwd() {
		return pwd;
	}

	public static void setPwd(String pwd) {
		Util.pwd = pwd;
	}

	public static String getFrom() {
		return from;
	}

	public static void setFrom(String from) {
		Util.from = from;
	}

	public static String getTo() {
		return to;
	}

	public static void setTo(String to) {
		Util.to = to;
	}

	public static String getConvert_process_single() {
		return convert_process_single;
	}

	public static void setConvert_process_single(String convert_process_single) {
		Util.convert_process_single = convert_process_single;
	}

	public static String getArt_conv_status_log() {
		return art_conv_status_log;
	}

	public static void setArt_conv_status_log(String art_conv_status_log) {
		Util.art_conv_status_log = art_conv_status_log;
	}

	public static int getHot_folder_wait_cnt() {
		return hot_folder_wait_cnt;
	}

	public static void setHot_folder_wait_cnt(int hot_folder_wait_cnt) {
		Util.hot_folder_wait_cnt = hot_folder_wait_cnt;
	}

	public static Hashtable<String, String> getApplicationPropertyTable() {
		return ApplicationPropertyTable;
	}

	public static void setApplicationPropertyTable(
			Hashtable<String, String> applicationPropertyTable) {
		ApplicationPropertyTable = applicationPropertyTable;
	}

	public static long gethotfolderBsyChkIntvl() {
		return hotfolderBsyChkIntvl;
	}

	public static void sethotfolderBsyChkIntvl(long waittime) {
		hotfolderBsyChkIntvl = waittime;
	}

	public static String getHotFolder() {
		return hotFolder;
	}

	public static void setHotFolder(String hotFolder) {
		Util.hotFolder = hotFolder;
	}

	public static String getCfgFolder() {
		return cfgFolder;
	}

	public static void setCfgFolder(String cfgFolder) {
		Util.cfgFolder = cfgFolder;
	}

	public static String getApplicationPropertiesFile() {
		return APPLICATION_PROPERTIES_FILE;
	}

	public static String getWatchDir() {
		return watchDir;
	}

	public static void setWatchDir(String watchDir) {
		Util.watchDir = watchDir;
	}

	public static int getdelayBetweenIteration() {
		return delayBetweenIteration;
	}

	public static void setdelayBetweenIteration(int timeDelay) {
		Util.delayBetweenIteration = timeDelay;
	}

	public static String getProcessDest() {
		return processDest;
	}

	public static void setProcessDest(String processDest) {
		Util.processDest = processDest;
	}

	public static String extractFileName(String filePathName) {
		if (filePathName == null)
			return null;

		int slashPos = filePathName.lastIndexOf('\\');

		if (slashPos == -1)
			return filePathName;

		return filePathName.substring(slashPos + 1, filePathName.length());
	}

	public static void main(String[] args) throws Exception {
	
		System.out.println(Util.getCurrentLocalDateTimeStamp());
	}
	public static void main5(String[] args) throws Exception {
		File file = new File("c:/Temp/final.tiff");
		File targetFile = new File("C:\\Temp1\\final.tiff");
		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		FileInputStream sf = null;
		FileOutputStream tf = null;
		try {
			sf = new FileInputStream(file);
			sourceChannel = sf.getChannel();
			tf = new FileOutputStream(targetFile);
			destChannel = tf.getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

		} catch (Exception e) {
			System.out.println("error 1" + e.getMessage());
			e.printStackTrace();

		} finally {
			sourceChannel.close();
			destChannel.close();
			sf.close();
			tf.close();
			
		}
	}

	public static void main4(String[] args) {

		try {
			List<String> command = new ArrayList<>();
			String text = "\"text " + "270" + "," + "460" + " " + "\'" + "RAM"
					+ "\'" + "\"";
			text = "text 270,460 \'RAM\'";
			command.add("C:\\41056P_new_printer_listener\\convert.bat");
			command.add("c:\\temp1\\test2.jpeg");
			command.add("-");
			command.add("pointsize");
			command.add("100");
			command.add("-fill");
			command.add("yellow");
			command.add("-draw");
			command.add("\"");
			command.add("text");
			command.add("270");
			command.add(",");
			command.add("460");
			command.add("\'");
			command.add("RAM");
			command.add("\'");
			command.add("\"");
			command.add("c:\\temp1\\test3.jpeg");
			String[] list = { "C:\\41056P_new_printer_listener\\convert.bat ",
					"C:\\temp1\\t.jpeg", " -pointsize ", "100", " -fill ",
					" yellow ", " -draw ", text, "C:\\temp1\\t.jpeg" };
			ProcessBuilder builder = new ProcessBuilder(list);
			// ProcessBuilder builder = new
			// ProcessBuilder("C:\\41056P_new_printer_listener\\convert.bat",
			// " c:/temp1/test2.jpeg", " -pointsize ", "100", " -fill ",
			// " yellow ", " -draw ", text, "c:/temp1/test3.jpeg");
			// builder.directory(new File("c:/"));
			File log = new File("c:/log.txt");
			builder.redirectErrorStream(true);
			builder.redirectOutput(Redirect.appendTo(log));
			final Process p = builder.start();

			InputStream stderr = p.getErrorStream(); // or one may use
														// getInputStream to get
														// the actual output of
														// process
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println("status of conversion" + line);
			}
			p.waitFor();

		} catch (Exception err) {
			System.out.println("Error PreProcessing " + err.getMessage());
		}

	}

	public static void main3(String[] args) {

		File dir = new File("C:/41056H");
		FileFilter fileFilter = new WildcardFileFilter("*.*");
		File[] currentFiles = dir.listFiles(fileFilter);
		if (currentFiles != null && currentFiles.length > 0)
			System.out.println("size" + currentFiles.length);

	}

	public static void main1(String[] args) throws Exception {

		String target = null;
		File sourceFile = null;

		String newConfig = "C:/41056H/_EFI_HotFolder_" + File.separator
				+ "folder.cfg";
		File batchfile = new File("C:/41056P/pjm-14042952132-006001.txt");
		System.out.println(batchfile.exists());
		Path fr = Paths.get("C:/41056P/pjm-14042952132-006001.txt");
		String batchWithOutExt = batchfile.getName().replaceFirst("[.][^.]+$",
				"");

		String newloc = moveFiletoNewDestination(fr, "C:/processed_41056P");
		// inProcessFile = moveFiletoNewDestination(fr,
		// Util.getInProcessDest());

		try (BufferedReader fs = new BufferedReader(new FileReader(new File(
				newloc)));) {

			String c;
			String[] tokens = null;
			while ((c = fs.readLine()) != null) {

				tokens = c.split(",");

				String fileName = Util.extractFileName(tokens[0]);
				target = tokens[1] + "\\" + fileName;

				sourceFile = new File(tokens[0]);

				if (sourceFile != null
						&& (sourceFile.isFile() && sourceFile.canRead())) {
					File targetFile = new File(target);
					if (targetFile.exists()) {
						System.out.println("LOC005:- target " + targetFile
								+ " exists");
						continue;

					} else {
						Util.setTotRecords(1);
					}

					// check if hot folder is busy before sending

					System.out.println("Parent is " + targetFile.getParent());
					// if (isHotFolderNotBusy(targetFile.getParent())) {
					// get cfg file over to hot folder

					String fileNameWithOutExt = new File(tokens[0]).getName()
							.replaceFirst("[.][^.]+$", "");

					System.out.println("File Name without extension is "
							+ fileNameWithOutExt);
					String cfg = fileNameWithOutExt + ".cfg";
					// File hotFolderCfg = new File(Util.getWatchDir()+
					// File.separator + cfg);
					File hotFolderCfg;
					if (fileNameWithOutExt.contains("SEP")) {
						hotFolderCfg = new File("C:/41056P" + File.separator
								+ cfg);
					} else {
						hotFolderCfg = new File("C:/41056P" + File.separator
								+ batchWithOutExt + "-" + cfg);
					}
					hotFolderCfg.renameTo(new File(newConfig));
					Path filepath = Paths.get(newConfig);
					// moveFiletoNewDestination(filepath,Util.getCfgFolder());

					moveFiletoNewDestination(filepath,
							"C:/41056H/[_EFI_HotFolder_]/");

					// Now send Content File
					if (tokens[0].contains("SEP")) {
						moveFiletoNewDestination(new File(tokens[0]),
								"C:/processed_41056P");
					} else {
						sendFileToHotFolder(fr, tokens[0], targetFile);
					}
					// send corresponding cfg

				}
				// } else {
				System.out.println("LOC007:- Source File does not exist "
						+ sourceFile + " File processing error");

				// }
			}

		} catch (Exception e) {
			System.out.println("LOC008:- File processing error ");
			throw e;
		}

		moveFiletoNewDestination(new File(newloc), "D:\\processed_41056");

	}

	private static void sendFileToHotFolder(Path batchName, String fileName,
			File targetFile) throws Exception {
		File inputFile = new File(fileName);
		WritableByteChannel outChannel = null;
		try (ReadableByteChannel inChannel = Channels
				.newChannel(new FileInputStream(inputFile))) {
			outChannel = Channels.newChannel(new FileOutputStream(targetFile));

			// TODO make this configurable
			ByteBuffer buffer = ByteBuffer
					.allocate(Integer.parseInt(String.valueOf(inputFile.length())));
			int read;

			while ((read = inChannel.read(buffer)) > 0) {
				buffer.rewind();
				buffer.limit(read);

				while (read > 0) {
					read -= outChannel.write(buffer);
				}

				buffer.clear();
			}

		} catch (Exception e) {
			System.out.println("LOC006:- Error processing file "
					+ e.getMessage() + " File processing error - Batch Name"
					+ batchName.getFileName());
			throw e;

		} finally {

			if (outChannel != null & outChannel.isOpen())
				outChannel.close();
		}
	}

	public static boolean checkIfFilesExistInHotfolder(String folder, String ext) {

		boolean filesExistChk = false;

		GenericExtFilter filter = new Util().new GenericExtFilter(ext);

		File dir = new File(folder);

		if (dir.isDirectory() == false) {

			return filesExistChk;

		}

		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);

		if (list == null || list.length == 0) {

			return filesExistChk;
		} else {

			return !filesExistChk;
		}

	}

	// inner class, generic extension filter

	public static File moveFiletoNewDestination(File afile, String dest)
			throws Exception {

		System.out.println("Moving file" + afile.getName() + "to destination "
				+ dest);

		File bfile = new File(dest + "\\" + afile.getName());

		FileReader fr = new FileReader(afile);

		BufferedReader br = new BufferedReader(fr);
		StringBuilder contents = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			contents.append(line);
			contents.append(System.getProperty("line.separator"));
		}

		// use buffering
		Writer output = new BufferedWriter(new FileWriter(bfile));

		// FileWriter always assumes default encoding is OK!
		output.write(contents.toString());

		br.close();
		output.close();

		// delete the original file
		afile.delete();

		System.out
				.println("File moved successfully and deleted from source folder!");

		return bfile;

	}

	/**
	 * Gets the property.
	 * 
	 * @param propertyName
	 *            the property name
	 * 
	 * @return the property
	 * 
	 * @throws UnRecoverableException
	 *             the un recoverable exception
	 */
	public static String getProperty(String propertyName) throws Exception {
		String propertyValue = "";
		try {
			if (Util.ApplicationPropertyTable == null) {
				Util.ApplicationPropertyTable = ApplicationPropertyDigester
						.getProperties(Util.APPLICATION_PROPERTIES_FILE);

			}
			propertyValue = Util.ApplicationPropertyTable.get(propertyName);
		} catch (final Exception e) {
			throw new Exception(" Error getting property values"
					+ e.getMessage());
		}

		return propertyValue;
	}

	public static void main2(String[] args) throws Exception {

		String inProcessFile = null;
		String target = null;
		File sourceFile = null;
		Path fr = Paths.get("c:/temp/test.txt");

		// move the file to in process folder

		inProcessFile = moveFiletoNewDestination(fr, "c:/processed_41056P");

		try (

		BufferedReader fs = new BufferedReader(new FileReader(inProcessFile));) {

			String c;
			String[] tokens = null;
			while ((c = fs.readLine()) != null) {

				tokens = c.split(",");

				String file = Util.extractFileName(tokens[0]);
				target = tokens[1] + "\\" + file;

				sourceFile = new File(tokens[0]);

				if (sourceFile != null
						&& (sourceFile.isFile() && sourceFile.canRead())) {
					File targetFile = new File(target);
					if (targetFile.exists()) {
						System.out.println("target exists");
						continue;

					} else {
						Util.setTotRecords(1);
					}

					try (ReadableByteChannel inChannel = Channels
							.newChannel(new FileInputStream(sourceFile))) {
						WritableByteChannel outChannel = Channels
								.newChannel(new FileOutputStream(targetFile));

						// TODO make this configurable
						ByteBuffer buffer = ByteBuffer.allocate(Integer.parseInt(String.valueOf(
								sourceFile.length())));
						int read;

						while ((read = inChannel.read(buffer)) > 0) {
							buffer.rewind();
							buffer.limit(read);

							while (read > 0) {
								read -= outChannel.write(buffer);
							}

							buffer.clear();
						}

					} catch (Exception e) {
						throw e;
					}
				} else {
					System.out.println("Source file does not exist");
				}
			}

		} catch (Exception e) {

			throw e;
		}
		moveFiletoNewDestination(Paths.get(inProcessFile), "d:/processed_41056");

	}

	public static String moveFiletoNewDestination(Path afile, String dest)
			throws Exception {

		String filename = afile.getFileName().toString();
		Path target = FileSystems.getDefault().getPath(dest, filename);
		Files.move(afile, target, REPLACE_EXISTING);

		return target.toString();
	}

	public class GenericExtFilter implements FilenameFilter {

		public GenericExtFilter(String extension) {
		}

		@Override
		public boolean accept(File dir, String name) {

			return (name.endsWith("pdf") || name.endsWith("tif"));
		}
	}

}
