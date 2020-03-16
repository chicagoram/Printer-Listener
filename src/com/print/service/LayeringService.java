package com.print.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.Random;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.print.util.Util;

public class LayeringService implements Callable<String> {

	private String destFile = null;

	public String getDestFile() {
		return destFile;
	}

	public void setDestFile(String destFile) {
		this.destFile = destFile;
	}

	private String sourceFile = null;
	private String overlayFile = null;
	private String page_indicator1 = null;
	private String density = null;
	private String geometry1 = null;
	private String page_indicator2 = null;
	private String geometry2 = null;

	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public String getOverlayFile() {
		return overlayFile;
	}

	public void setOverlayFile(String overlayFile) {
		this.overlayFile = overlayFile;
	}

	public String getPage_indicator1() {
		return page_indicator1;
	}

	public void setPage_indicator1(String page_indicator1) {
		this.page_indicator1 = page_indicator1;
	}

	public String getGeometry1() {
		return geometry1;
	}

	public void setGeometry1(String geometry1) {
		this.geometry1 = geometry1;
	}

	public String getPage_indicator2() {
		return page_indicator2;
	}

	public void setPage_indicator2(String page_indicator2) {
		this.page_indicator2 = page_indicator2;
	}

	public String getGeometry2() {
		return geometry2;
	}

	public void setGeometry2(String geometry2) {
		this.geometry2 = geometry2;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	/** The Constant logger. */

	private static final Logger debugLogger = Logger.getLogger("debuglog");
	private static final Logger errorLogger = Logger.getLogger("errorlog");

	@Override
	public String call() throws Exception {

		switch (this.getPage_indicator1()) {

		case "0":
			layerSingleSide();
			break;

		case "1":
			layerDoubleSide_Samepage();
			break;

		case "2":
			layerDoubleSide_Differentpage_case1();
			break;

		case "3":
			layerDoubleSide_Differentpage_case2();
			break;
	
		default:
			this.setDestFile(null);
			break;

		}

		return destFile;
	}

	private void layerSingleSide() {

		try {

			//T:\4513447\41\191091584620_RSGerman_ins.tif,C:\41059H,pjm-18061848119-SKU-OVL.pdf,0,300,-200+180,-000-000

			String[] list = { Util.getConvert_process_single(),
					this.getSourceFile(), this.getDensity(),
					this.getOverlayFile(), this.getGeometry1(),
					this.getDestFile() };

			ProcessBuilder builder = new ProcessBuilder(list);
			File log = new File(Util.getArt_conv_status_log());
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
				debugLogger.debug("status of converting file "
						+ getSourceFile() + " " + line);
			}
			int returncode = p.waitFor();
			debugLogger.debug(" Return code after converting file "
					+ getSourceFile() + "=" + returncode);

		} catch (Exception err) {
			errorLogger.error("Preprocessor Error  art file " + "@ layering single side" +  getSourceFile()
					+ " " + err.getMessage());
		}

	}
	
	private void layerDoubleSide_Samepage() {

		try {
	//		Random rand = new Random();

			//C:\Temp\SOD>call layer_double_sided.bat comp.tiff 0 300 NEW1_OVL.pdf  -350-2050 comp_inter.tif
			//C:\Temp\SOD>call layer_double_sided.bat comp_inter.tif 1 300 NEW1_OVL.pdf  -200+320  comp_final.tif
	//		String destFileWithOutExt = new File(this.getSourceFile()).getName()
	//				.replaceFirst("[.][^.]+$", "");
			
	//		String intermed = Util.getProcessDest() + "\\" + destFileWithOutExt + "_" + rand.nextInt(10000)+".tif";
			
			
			String[] list1 = { Util.getConvert_process_double(),
					this.getSourceFile(), "0", this.getDensity(),
					this.getOverlayFile(), this.getGeometry1(),
					this.getDestFile() };
			
			printDoubleSide_Samepage(list1);
			
			/* String[] list2 = { Util.getConvert_process_double(),intermed,
					"0", this.getDensity(),
					this.getOverlayFile(), this.getGeometry2(),
					this.getDestFile()};
			
			printDoubleSide_Samepage(list2); */
			
		}
		catch (Exception e){
			
			errorLogger.error("Preprocessor Error  art file " + " @ layerDoubleSide_Samepage() "  + getSourceFile()
					+ " " + e.getMessage());
		}

	}
	
	
	private void printDoubleSide_Samepage(String[] list) {
		
		
		try {

			
			ProcessBuilder builder = new ProcessBuilder(list);
			File log = new File(Util.getArt_conv_status_log());
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
				debugLogger.debug("status of converting file "
						+ getSourceFile() + " " + line);
			}
			int returncode = p.waitFor();
			debugLogger.debug(" Return code after converting file "
					+ getSourceFile() + "=" + returncode);

		} catch (Exception err) {
			errorLogger.error("Preprocessor Error  art file " + " printDoubleSide_Samepage(String[] list) " +  getSourceFile()
					+ " " + err.getMessage());
		}

	}

	private void layerDoubleSide_Differentpage_case1() {

		//C:\Temp\SOD>call layer_double_sided.bat comp.tiff 0 300 NEW1_OVL.pdf  -350-2050 comp_inter.tif
		//C:\Temp\SOD>call layer_double_sided.bat comp_inter.tif 1 300 NEW1_OVL.pdf  -200+320  comp_final.tif
		
		try {
			Random rand = new Random();

			// //C:\Temp\SOD>call layer_single_sided.bat mike_layout.tif 300
			// NEW2_OVL.pdf -200+3590 mike_layout_sample_layered.tif
			String destFileWithOutExt = new File(this.getSourceFile()).getName()
					.replaceFirst("[.][^.]+$", "");
			
			String intermed = Util.getProcessDest() + "\\" + destFileWithOutExt + "_" + rand.nextInt(10000)+".tif";
			String[] list1 = { Util.getConvert_process_double(),
					this.getSourceFile(), "0", this.getDensity(),
					this.getOverlayFile(), this.getGeometry1(),
					intermed };
			
			printDoubleSide_Differentpage_case1(list1);
			
			String[] list2 = { Util.getConvert_process_double(),intermed,
					"1", this.getDensity(),
					this.getOverlayFile(), this.getGeometry2(),
					this.getDestFile()};
			
			printDoubleSide_Differentpage_case1(list2);
			
		}
		catch (Exception e){
			
			errorLogger.error("Preprocessor Error  art file " + " @ layerDoubleSide_Differentpage_case1() "  + getSourceFile()
					+ " " + e.getMessage());
		}

	}
	
	
	private void printDoubleSide_Differentpage_case1(String[] list) {
		
		
		try {

			// //C:\Temp\SOD>call layer_single_sided.bat mike_layout.tif 300
			// NEW2_OVL.pdf -200+3590 mike_layout_sample_layered.tif
			
			ProcessBuilder builder = new ProcessBuilder(list);
			File log = new File(Util.getArt_conv_status_log());
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
				debugLogger.debug("status of converting file "
						+ getSourceFile() + " " + line);
			}
			int returncode = p.waitFor();
			debugLogger.debug(" Return code after converting file "
					+ getSourceFile() + "=" + returncode);

		} catch (Exception err) {
			errorLogger.error("Preprocessor Error  art file " + " @ layerDoubleSide_Differentpage_case1() "  + getSourceFile()
					+ " " + err.getMessage());		
		}
	}

		
		private void layerDoubleSide_Differentpage_case2() {

			//C:\Temp\SOD>call layer_double_sided.bat comp.tiff 0 300 NEW1_OVL.pdf  -350-2050 comp_inter.tif
			//C:\Temp\SOD>call layer_double_sided.bat comp_inter.tif 1 300 NEW1_OVL.pdf  -200+320  comp_final.tif
			
			try {
				Random rand = new Random();

				// //C:\Temp\SOD>call layer_single_sided.bat mike_layout.tif 300
				// NEW2_OVL.pdf -200+3590 mike_layout_sample_layered.tif
				String destFileWithOutExt = new File(this.getSourceFile()).getName()
						.replaceFirst("[.][^.]+$", "");
				
				String intermed = Util.getProcessDest() + "\\" + destFileWithOutExt + "_" + rand.nextInt(10000)+".tif";
				String[] list1 = { Util.getConvert_process_double(),
						this.getSourceFile(), "0", this.getDensity(),
						this.getOverlayFile(), this.getGeometry1(),
						intermed };
				
				printDoubleSide_Differentpage_case2(list1);
				
				String[] list2 = { Util.getConvert_process_double(),intermed,
						"1", this.getDensity(),
						this.getOverlayFile(), this.getGeometry2(),
						this.getDestFile()};
				
				printDoubleSide_Differentpage_case2(list2);
				
			}
			catch (Exception e){
				
				errorLogger.error("Preprocessor Error  art file " + " @ layerDoubleSide_Differentpage_case2() "  + getSourceFile()
						+ " " + e.getMessage());
			}

		}
		
		
		private void printDoubleSide_Differentpage_case2(String[] list) {
			
			
			try {

				// //C:\Temp\SOD>call layer_single_sided.bat mike_layout.tif 300
				// NEW2_OVL.pdf -200+3590 mike_layout_sample_layered.tif
				
				ProcessBuilder builder = new ProcessBuilder(list);
				File log = new File(Util.getArt_conv_status_log());
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
					debugLogger.debug("status of converting file "
							+ getSourceFile() + " " + line);
				}
				int returncode = p.waitFor();
				debugLogger.debug(" Return code after converting file "
						+ getSourceFile() + "=" + returncode);

			} catch (Exception err) {
				errorLogger.error("Preprocessor Error  art file " + " printDoubleSide_Differentpage_case2(String[] list) " +  getSourceFile()
						+ " " + err.getMessage());
			}


	}

}