package com.print.impl;

import java.io.BufferedReader;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.print.arch.BaseListener;
import com.print.arch.IFileListener;
import com.print.exception.FileMoveException;
import com.print.exception.FileORNetworkNotAvailableException;
import com.print.exception.ResourceAddException;
import com.print.service.DirectoryWatchService;
import com.print.service.LayeringService;
import com.print.util.Emailer;
import com.print.util.Util;

public class FileListener extends BaseListener implements IFileListener {

	private static final Logger errorLogger = Logger.getLogger("errorlog");
	private static final Logger debugLogger = Logger.getLogger("debuglog");
	private static final Logger perfLogger = Logger.getLogger("perflog");

	@Override
	public void onStart(Object monitoredResource) {
		// On startup
		if (monitoredResource instanceof File) {
			File resource = (File) monitoredResource;
			if (resource.isDirectory()) {

				debugLogger
						.debug("Start to monitor the resource........................ "
								+ resource.getAbsolutePath());

			}
		}
	}

	/**
	 * Connstructor
	 */
	public FileListener() {
		super();
	}

	@Override
	public void onStop(Object notMonitoredResource) {

	}

	@Override
	public void onAdd(Object newResource) throws Exception {
		debugLogger.debug("adding the resource and triggering File Listener");
		if (newResource instanceof File) {
			File file = (File) newResource;
			if (file.isFile()) {
				boolean fileIsReady = false;
				while (!fileIsReady) {
					try {

						file.canWrite();
						fileIsReady = true;
					} catch (Exception e) {

						errorLogger.error("Failed to add file "
								+ file.getName());
						throw new ResourceAddException(Util.ADD_RESOURCE_ERROR,
								e);

					}
				}

				debugLogger.debug("About to initiate processing the  file");
				Path filepath = Paths.get(file.getAbsolutePath());
				if (file.canRead())
					processFile(filepath);

			}
		}
	}

	private void processFile(final Path fr) throws Exception {

		//boolean hotfolderexists = false;
		String target = null;
		String newConfig = Util.getWatchDir() + File.separator + "folder.cfg";
		String batchWithOutExt = fr.toFile().getName()
				.replaceFirst("[.][^.]+$", "");
		debugLogger.debug("process file in progress " + " path "
				+ fr.toFile().getName());
		
		   

		try (BufferedReader fs = new BufferedReader(new FileReader(fr.toFile()));) {

			String c;
			String[] tokens = null;
			while ((c = fs.readLine()) != null) {
			//    hotfolderexists = false;

				LocalTime starttime = LocalTime.parse(Util.getCurrentLocalDateTimeStamp());
				tokens = c.split(",");

				String fileName = Util.extractFileName(tokens[0]);
				target = tokens[1] + "\\" + fileName;
               
				
                debugLogger
						.debug("target " + target + " token[0] " + tokens[0]);

				Util.setTotRecords(Util.totRecords + 1);

				if (Util.getTotRecords() == 1) {
					debugLogger.info("Batch in progress    : "
							+ fr.toFile().getName());
				}

				String fileNameWithOutExt = new File(tokens[0]).getName()
						.replaceFirst("[.][^.]+$", "");

				if (checkIfTheContentExists(new File(tokens[0]))) {

					File targetFile = new File(target);

					String cfg = "";

					cfg = fileNameWithOutExt + ".cfg";

					File hotFolderCfg = null;

					String hotFolderCfgName = "";
					if (tokens.length == 2) {
						if (fileNameWithOutExt.contains("SEP")) {
							hotFolderCfgName = Util.getWatchDir()
									+ File.separator + cfg;
							hotFolderCfg = new File(hotFolderCfgName);

						} else {
							hotFolderCfgName = Util.getWatchDir()
									+ File.separator + batchWithOutExt + "-"
									+ cfg;
							hotFolderCfg = new File(hotFolderCfgName);

						}
					}

					if (tokens.length > 2) {
						if (fileNameWithOutExt.contains("SEP")) {
							hotFolderCfgName = Util.getWatchDir()
									+ File.separator + cfg;
							hotFolderCfg = new File(hotFolderCfgName);

						} else {
							
							hotFolderCfgName = Util.getWatchDir()
									+ File.separator + batchWithOutExt + "-"
									+ tokens[2];
							hotFolderCfg = new File(hotFolderCfgName);
							debugLogger.debug("Hot folder config " + hotFolderCfgName);

						}
					}

					// copy config folder file over to processed folder

					String inprocess = Util.getProcessDest() + File.separator
							+ hotFolderCfg.getName();

					if (!hotFolderCfg.exists()) {
						errorLogger.error("Config file   --- "
								+ hotFolderCfg.getName()
								+ " --- does not exist");
					}

					if (hotFolderCfg.exists()) {
						//hotfolderexists = true;
						debugLogger.debug(" sendFileToNewDestination hot folder " + hotFolderCfgName +
								" " + " inprocess " + inprocess);
						sendFileToNewDestination(fr,
								new File(hotFolderCfgName), new File(inprocess), starttime);

						// rename config file to folder.cfg on the same location
						// (listening folder)

						hotFolderCfg.renameTo(new File(newConfig));
						Files.deleteIfExists(Paths.get(Util.getCfgFolder()
										+ File.separator + "folder.cfg"));

						debugLogger.debug(" moveFileToNewDestination hot folder " + newConfig);
							
						
						moveFiletoNewDestination(new File(newConfig),
								Util.getCfgFolder());
						
						// Now send Content File
						if (tokens[0].contains("SEP")) {
							sendFileToNewDestination(fr, new File(tokens[0]),
									targetFile, starttime);
							String destFile = Util.getProcessDest()
									+ File.separator
									+ new File(tokens[0]).getName();
							sendFileToNewDestination(fr, new File(tokens[0]),
									new File(destFile), starttime);
							moveFiletoNewDestination(new File(tokens[0]),
									Util.getProcessDest());
							
//							perfLogger.debug(batchWithOutExt+"       "+fileName+"     "+starttime + "        " + endtime + "        "+ SECONDS.between(starttime,endtime));
							
						} else {
							if (tokens.length > 2) {

								String processedFile = preProcess(fr, hotFolderCfgName,tokens);
								sendFileToNewDestination(fr, new File(
										processedFile), new File(tokens[1] + "\\" + new File(processedFile).getName()), starttime);

							} else {


								sendFileToNewDestination(fr,
										new File(tokens[0]), targetFile, starttime);
							}
						}
					}

				}
			}

		} catch (Exception e) {
			errorLogger.error("LOC008:- File processing error "
					+ " Error Message - " + e.getMessage());
			e.printStackTrace();
		} finally {

			moveFiletoNewDestination(fr.toFile(), Util.getProcessDest());
		}

		debugLogger.info("Total Records Processed.............................."
				+ Util.totRecords);
		debugLogger.info("\n");
		debugLogger.info("Processing Ends@.....................................");

		debugLogger.info("\n");
		Util.setTotRecords(0);

	}

	private static String preProcess(Path batchName, String hotFolderCfgName,String[] tokens) {
		String processedContentFile = null;
		try {

			// C:\41056P\191091584620_RSGerman_ins.tif - 0
			// C:\41056H - 1
			// pjm-18052154362-020787-003-OVL.pdf - 2
			// 0 - 3
			// 300 - 4
			// -50-5 - 5
			// -30-200 - 6

			
			processedContentFile = overlayContent(tokens[0],Util.getWatchDir()
					+ File.separator + tokens[3],
					 tokens[4], tokens[5],	tokens[6], tokens[7]);
			if (processedContentFile == null)
				processedContentFile = Util.getProcessDest() + "\\"
						+ new File(tokens[0]).getName();
			moveFiletoNewDestination(new File(Util.getWatchDir() + "\\" + tokens[3]), Util.getProcessDest());
		} catch (Exception e) {
			errorLogger.error("LOC010:- Pre Processing error "
					+ " Error Message - " + e.getMessage());

		}
		return processedContentFile;
	}

	private static String overlayContent(String sourceFile, String overlayFile, String page1, String density, String geometry1,
			String geometry2) throws Exception

	{

		// sourceFile, density, overlayFile, destFile, page, geometry
		
		debugLogger.debug("FILE PREPROCESSOR INPUT = " + " sourcefile=" + sourceFile + " overlayfile=" + overlayFile + " page indicator=" + page1 + " density=" + density + " geometry1=" + geometry1 + " geometry2=" + geometry2);

		String destFileWithOutExt = new File(sourceFile).getName()
				.replaceFirst("[.][^.]+$", "");
		Random rand = new Random();
		
		String destFile = Util.getProcessDest() + "\\" + destFileWithOutExt + "_" + rand.nextInt(10000)
				+ "." + "tif";

		LayeringService ls = new LayeringService();
		ls.setSourceFile(sourceFile);
		ls.setOverlayFile(overlayFile);
		ls.setGeometry1(geometry1);
		ls.setGeometry2(geometry2);
		ls.setPage_indicator1(page1);
		ls.setDensity(density);
		ls.setDestFile(destFile);
		Future<?> future = DirectoryWatchService.getEncryptorexecutorpool()
				.submit(ls);

		debugLogger.info("LAYERING STATUS for source file " + sourceFile + "="
				+ future.get());
		return destFile;
	}

	private static boolean checkIfTheContentExists(File file)
			throws FileORNetworkNotAvailableException {

		boolean contentVerification = false;
		int attempts = 0;

		if ((file != null) && (file.canRead())) {
			return true;
		}
		do {
			attempts += 1;
			try {

				if ((file != null) && (file.canRead())) {
					return true;
				}
			} catch (Exception e) {
				errorLogger.error("LOC009:- File accessing error "
						+ " Error Message - " + e.getMessage());
			}
		} while (attempts < 3);

		if (!contentVerification) {
			String message = "\"FileListener - Either the file \"\r\n"
					+ file.getName()
					+ " "
					+ "\r\n"
					+ "							+ \" does not exist or Network path not found - 3 attempts made and failed - make sure network maps are active ";
			errorLogger.error(message);
			Emailer.sendEMail("Print Server Failure Error - Printer   - "
					+ Util.getPrinterName(), message);

		}
		return contentVerification;
	}

	private static void sendFileToNewDestination(Path batchName, File file,
			File targetFile, LocalTime starttime) throws Exception {

		FileChannel sourceChannel = null;
		FileChannel destChannel = null;
		FileInputStream sf = null;
		FileOutputStream tf = null;

		try {
			if (file == null || !(file.canRead())) {
				throw new FileORNetworkNotAvailableException(
						"FileListener: sendFileTonewDestination(): 1 ..file "
								+ file.getName()
								+ " does not exist or Network path not found - 3 attempts failed  ");

			}

			hotFolderWait(Util.getHotFolder());
            sf = new FileInputStream(file);
			sourceChannel = sf.getChannel();
			tf = new FileOutputStream(targetFile);
			destChannel = tf.getChannel();
			destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

		} catch (Exception e) {
			errorLogger
					.error("FileListener: sendFileToNewDestination():  2 Error processing file "
							+ e.getMessage()
							+ "  - Batch Name ---"
							+ batchName.getFileName());
			e.printStackTrace();
			Emailer.sendEMail(
					"Print Server Failure Error - Printer   - "
							+ Util.getPrinterName() + " error accessing file "
							+ file.getName() + " ", e.getMessage());
			throw e;

		} finally {
			sourceChannel.close();
			destChannel.close();
			sf.close();
			tf.close();
			LocalTime endtime = LocalTime.parse(Util.getCurrentLocalDateTimeStamp());
			perfLogger.debug(batchName.getFileName()+"       "+FilenameUtils.getExtension(file.getName())+"            "+starttime + "        " + endtime + "        "+ ChronoUnit.SECONDS.between(starttime,endtime));
		}
	}

	private static void moveFiletoNewDestination(File afile, String dest)
			throws Exception {

		Path target = null;

		try {

			debugLogger.debug("Moving file...................... "
					+ afile.toString() + " to destination... " + dest);

			String filename = afile.getName().toString();
			target = FileSystems.getDefault().getPath(dest, filename);
			Files.move(afile.toPath(), target,
					StandardCopyOption.REPLACE_EXISTING);

		}

		catch (Exception e) {
			errorLogger
					.error("FileListener moveFileToNewDestination() 1 - error moving file  "
							+ afile.toString()
							+ " Error Message  "
							+ e.getMessage());
			throw new FileMoveException(
					"FileListener moveFileToNewDestination() 2 - error moving file  "
							+ afile.toString() + " Error Message = "
							+ e.getMessage(), e);

		}

	}

	private static void hotFolderWait(String folder) {

		for (int x = 1; x < Util.getHot_folder_wait_cnt(); x++) {

			int filecount = checkHotfolder(folder);

			if (filecount > 1) {
				debugLogger
						.debug("Hot folder is busy printing   .. Number of files in Hotfolder "
								+ folder + "= " + filecount);
				try {
					TimeUnit.SECONDS.sleep(Util.gethotfolderBsyChkIntvl());
				} catch (Exception e) {
					errorLogger.error("Hotfolder wait too long folder "
							+ folder + "" + e.getMessage(), e);
				}

			} else {
				break;
			}

		}
	}

	private static int checkHotfolder(String folder) {

		File dir = new File(folder);
		FileFilter fileFilter = new WildcardFileFilter("*.*");
		File[] currentFiles = dir.listFiles(fileFilter);
		return currentFiles.length;
	}

	public String getNumCopies(String fileName) {
		// standard for reading an XML file
		String numCopies = "0";

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder;
			Document doc = null;
			XPathExpression expr = null;

			builder = factory.newDocumentBuilder();
			doc = builder.parse(new File(fileName));

			// create an XPathFactory
			XPathFactory xFactory = XPathFactory.newInstance();

			// create an XPath object
			XPath xpath = xFactory.newXPath();

			// compile the XPath expression
			expr = xpath
					.compile("//HotFolder/JobSetting/KEYVALUE[@Key='num copies']");
			// run the query and get a nodeset
			Object result = expr.evaluate(doc, XPathConstants.NODESET);

			// cast the result to a DOM NodeList
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				System.out.println(nodes.item(i).getNodeValue());
				Node node = nodes.item(i);
				// System.out.println(node.getNodeName());
				// System.out.println(node.getNodeValue());
				node.getAttributes();
				Element e = (Element) node;
				String val = e.getAttribute("Value");
				numCopies = val.trim();

			}

		} catch (Exception e) {

			errorLogger.error("Error parsing the file " + fileName
					+ e.getMessage());
		}
		return numCopies;
	}

}
