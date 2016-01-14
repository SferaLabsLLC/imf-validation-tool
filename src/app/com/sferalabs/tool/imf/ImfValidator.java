/*
 * Copyright (c) 2015 Sfera Labs LLC. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. All advertising materials mentioning features or use of this software must
 * display the following acknowledgement:
 * This product includes software developed by the SFERA LABS LLC.
 *
 * 4. Neither the name of SFERA LABS LLC nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY SFERA LABS LLC AND ITS CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SFERA LABS LLC OR ITS CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sferalabs.tool.imf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.sferalabs.imf.exception.ImfXmlException;
import com.sferalabs.imf.model.assetmap.Asset;
import com.sferalabs.imf.model.assetmap.AssetMap;
import com.sferalabs.imf.model.imfpackage.ImfPackage;
import com.sferalabs.imf.model.packinglist.PackingList;
import com.sferalabs.imf.util.ImfLogger;
import com.sferalabs.imf.validation.AssetMapValidator;
import com.sferalabs.imf.validation.CompositionPlaylistValidator;
import com.sferalabs.imf.validation.PackingListValidator;
import com.sferalabs.imf.validation.ValidationResult;
import com.sferalabs.imf.validation.ValidationResultList;

public class ImfValidator {
	
	private static String verifyingPackagePath;
	private static List<String> dependentPackagePaths;
	private static boolean isVerboseEnabled;
	private static boolean isHashCheckingSkipped;

	// banner text
	private static final String VERSION = "1.8";
	private static final String TITLE = "IMF package validator v" + VERSION;
	private static final String COPYRIGHT = "(c) 2015 Sfera Labs LLC";
	private static final String BANNER = TITLE + " " + COPYRIGHT;
	
	// XSDs
	private static String assetMapXsdPath;
	private static String packingListXsdPath;
	private static String coreConstraintsXsdPath;
	
	
	private static void usage() {
		System.out.println("Usage:");
		System.out.println("-p,--package    <folder>    package to verify (should be used only one time)");
		System.out.println("-d,--depend     <folder>    dependent package (can be used multiple times to specify multiple dependent packages)");
		System.out.println("--hash-check                perform files hash validation (disabled by default)");
		System.out.println("--assetmap-xsd              specify asset map xsd");
		System.out.println("--packinglist-xsd           specify packinglist xsd");
		System.out.println("--coreconstraints-xsd       specify core constraints xsd");
		System.out.println("-v,--verbose                print debug information during validation");
		System.out.println("-h,--help                   show usage information");
	}
	
	private static boolean parseCmdLine(String[] args) {
		boolean packagePathWasSet = false;
		
		if (args.length < 1) {
			ImfLogger.getLogger().logError("Invalid arguments, specify a package using -p argument");
			usage();
			return false;
		}
		
		if (args.length == 1) {
			//should be --help option
			if (! args[0].equals("-h") && ! args[0].equals("--help")) {
				ImfLogger.getLogger().logError("Invalid arguments, specify a package using -p argument");
			}
			usage();
			return false;
		}
		
		dependentPackagePaths = new ArrayList<String>();
		isVerboseEnabled = false;
		isHashCheckingSkipped = true;
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "-d":
				case "--depend":
					if (i == (args.length - 1)) {
						ImfLogger.getLogger().logError("Invalid arguments");
						usage();
						return false;
					}
					dependentPackagePaths.add(args[++i]);
					break;
				case "-p":
				case "--package":
					if (packagePathWasSet) {
						ImfLogger.getLogger().logError("--package should be used only one time");
						usage();
						return false;
					}
					if (i == (args.length - 1)) {
						ImfLogger.getLogger().logError("Invalid arguments");
						usage();
						return false;
					}
					verifyingPackagePath = args[++i];
					packagePathWasSet = true;
					break;
				case "-v":
				case "--verbose":
					isVerboseEnabled = true;
					break;
				case "--hash-check":
					isHashCheckingSkipped = false;
					break;
				case "--assetmap-xsd":
				case "--packinglist-xsd":
				case "--coreconstraints-xsd":
					if (i == (args.length - 1)) {
						ImfLogger.getLogger().logError("Invalid arguments");
						usage();
						return false;
					}
					
					String filePath = args[i+1];
					if (!Files.exists(new File(filePath).toPath())) {
						ImfLogger.getLogger().logError(String.format("Invalid xsd path: %s", filePath));
						return false;
					}
					
					switch (args[i]) {
						case "--assetmap-xsd":
							assetMapXsdPath = args[++i];
							break;
						case "--packinglist-xsd":
							packingListXsdPath = args[++i];
							break;
						case "--coreconstraints-xsd":
							coreConstraintsXsdPath = args[++i];
							break;
						default:
							break;
					}
					break;
				default:
					ImfLogger.getLogger().logError("Invalid arguments");
					usage();
					return false;
			}
		}
		
		if (!packagePathWasSet) {
			ImfLogger.getLogger().logError("Please specify package using -p or --package argument");
			usage();
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {

		System.out.println(BANNER);

		if (!parseCmdLine(args)) {
			return;
		}
		
		Path verifyingFolderPath = new File(verifyingPackagePath).toPath();
		//check existence of package
		if (!Files.exists(verifyingFolderPath) && Files.isDirectory(verifyingFolderPath)) {
			ImfLogger.getLogger().logError(String.format("Invalid package path: %s", verifyingPackagePath));
			return;
		}
		
		//check existence of dependent packages
		for (String folderPath:dependentPackagePaths) {
			verifyingFolderPath = new File(folderPath).toPath();
			if (!Files.exists(verifyingFolderPath) && Files.isDirectory(verifyingFolderPath)) {
				ImfLogger.getLogger().logError(String.format("Invalid package path: %s", folderPath));
				return;
			}
		}
		
		//Do validation
		try {
			ValidationResultList validationResults = new ValidationResultList();
			ImfPackage imfPackage = new ImfPackage(verifyingPackagePath);
			imfPackage.setDependantPackageList(dependentPackagePaths);
			if (isVerboseEnabled)
				ImfLogger.getLogger().enableDebugLog();
			
			//AssetMap
			ImfLogger.getLogger().logInfo("Validating ASSETMAP.xml ...");
			AssetMapValidator assetValidator = new AssetMapValidator(imfPackage);
			if (assetMapXsdPath != null) {
				assetValidator.setXsdPath(assetMapXsdPath);
			}
			
			validationResults.add(assetValidator.parse());
			validationResults.addAll(assetValidator.validate());
			System.out.println("");
			
			//PackingList
			AssetMap assetMap = assetValidator.getAssetMap();
			List<Asset> packingListAssets = assetMap.getPackingListAssets();
			if (packingListAssets.size() == 0) {
				ImfLogger.getLogger().logError("Unable to find PackingList file in package");
				return;
			}
			
			if (packingListAssets.size() > 1) {
				ImfLogger.getLogger().logError("Package has multiple PackingList files");
				return;
			}
			
			PackingList packingList = null;
			Asset asset = packingListAssets.get(0);
			String packingListPath = assetMap.getAssetResourcePath(asset.getUuid());
			String packingListFilename = new File(packingListPath).getName();
			ImfLogger.getLogger().logInfo("Validating PackingList file: " + packingListFilename + " ...");
			PackingListValidator packingListValidator = new PackingListValidator(packingListPath);
			if (packingListXsdPath != null) {
				packingListValidator.setXsdPath(packingListXsdPath);
			}
			
			try {
				validationResults.add(packingListValidator.parse());
				packingList = packingListValidator.getPackingList();
				packingListValidator.setAssetMap(assetMap);
				packingListValidator.setSkipHashChecking(isHashCheckingSkipped);
				validationResults.addAll(packingListValidator.validate());
			} catch (SAXException e) {
				ValidationResult result = new ValidationResult();
				result.setHasError(true);
				SAXParseException spe = (SAXParseException) e;
				if (spe != null) {
					String message = "Line: " + spe.getLineNumber() + " Col: " + spe.getColumnNumber() + " " + spe.getMessage();
					ImfLogger.getLogger().logError(message);
					result.setMessage(message);
				}
				validationResults.add(result);
			} catch (Exception e) {
				ValidationResult result = new ValidationResult();
				result.setHasError(true);
				ImfLogger.getLogger().logError(e.getMessage());
				result.setMessage(e.getMessage());
				validationResults.add(result);
			}
			
			//CPL
			List<Asset> cplAssets = assetMap.getCPLAssets();
			for (Asset cpl:cplAssets) {
				String cplFilePath = assetMap.getAssetResourcePath(cpl.getUuid());
				String cplFilename = new File(cplFilePath).getName();
				System.out.println();
				ImfLogger.getLogger().logInfo("Validating CPL: " + cplFilename + " ...");
				CompositionPlaylistValidator cplValidator = new CompositionPlaylistValidator(cplFilePath);
				if (coreConstraintsXsdPath != null) {
					cplValidator.setXsdPath(coreConstraintsXsdPath);
				}
				cplValidator.setAssetMap(assetMap);
				cplValidator.setPackingList(packingList);
				validationResults.add(cplValidator.parse());
				validationResults.addAll(cplValidator.validate());
			}
			
			if(validationResults.containsError())
				ImfLogger.getLogger().logError("Finished validation of IMF package with errors " + verifyingPackagePath);
			else {
				ImfLogger.getLogger().logInfo("Finished validation of IMF package " + verifyingPackagePath);
			}
		} catch (SAXException e) {
			SAXParseException spe = (SAXParseException) e;
			if (spe != null)
				ImfLogger.getLogger().logError("Line: " + spe.getLineNumber() + " Col: " + spe.getColumnNumber() + " " + spe.getMessage());
			ImfLogger.getLogger().logError("Finished validation of IMF package with errors " + verifyingPackagePath);
		} catch (IOException e) {
			ImfLogger.getLogger().logError(e.getMessage());
			ImfLogger.getLogger().logError("Finished validation of IMF package with errors " + verifyingPackagePath);
		} catch (ParserConfigurationException e) {
			ImfLogger.getLogger().logError(e.getMessage());
			ImfLogger.getLogger().logError("Finished validation of IMF package with errors " + verifyingPackagePath);
		} catch (ImfXmlException e) {
			ImfLogger.getLogger().logError(e.getMessage());
			ImfLogger.getLogger().logError("Finished validation of IMF package with errors " + verifyingPackagePath);
		}
	}

}
