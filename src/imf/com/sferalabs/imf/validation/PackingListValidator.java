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
package com.sferalabs.imf.validation;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.sferalabs.imf.exception.ImfXmlException;
import com.sferalabs.imf.model.assetmap.Asset;
import com.sferalabs.imf.model.assetmap.AssetMap;
import com.sferalabs.imf.model.packinglist.PackingAsset;
import com.sferalabs.imf.model.packinglist.PackingList;
import com.sferalabs.imf.saxhandler.SaxPackingListHandler;
import com.sferalabs.imf.util.ImfHashAlgorithm;
import com.sferalabs.imf.util.ImfHelper;
import com.sferalabs.imf.util.ImfLogger;
import com.sferalabs.imf.xsd.ImfXsds;

/**
 * PackingListValidator class
 */
public class PackingListValidator extends XmlResourceValidator {

	private static class Messages {
		public final static String UNSET_ASSET_MAP_MSG =		"AssetMap was unset for PackingList";
		public final static String CHECKING_UNIQUENESS_MSG =	"Checking uniqueness of uuid elements";
		public final static String UUID_NOT_FOUND_FMT =			"%s is not found in AssetMap";
		public final static String UUID_FOUND_FMT =				"%s is found in AssetMap";
		public final static String FILE_HASH_VALUE_FMT =		"File %s hash value is %s";
		public final static String HASH_MISMATCH_FMT =			"File %s hash value (%s) does not match expected value (%s)";
		public final static String FILE_SIZE_FMT =				"File %s size is %d";
		public final static String FILE_SIZE_MISMATCH_FMT =		"File %s size value (%s) does not match expected value (%s)";
		public final static String MIME_TYPE_FMT =				"File %s mime type is %s";
		public final static String MIME_TYPE_MISMATCH_FMT =		"File %s mime type (%s) does not match expected value (%s)";
	}
	private PackingList packingList;
	private AssetMap assetMap;
	private ImfHashAlgorithm hashAlgo;
	private boolean skipHashChecking;
	
	public PackingListValidator(String path) {
		super(path);
		resourcePath = ImfXsds.PACKAGING_LIST_XSD;
		hashAlgo = ImfHashAlgorithm.SHA1;
	}

	@Override
	public ValidationResult parse() throws IOException, ParserConfigurationException, SAXException {
		ValidationResult result = validateXsd();
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		SaxPackingListHandler handler = new SaxPackingListHandler();
		parser.parse(new FileInputStream(filePath), handler);
		packingList = handler.getPackingList();
		return result;
	}
	
	@Override
	public ValidationResultList validate() throws ImfXmlException {
		if (assetMap == null) {
			throw new ImfXmlException(Messages.UNSET_ASSET_MAP_MSG);
		}
		
		//check if uuid is unique
		ImfLogger.getLogger().logDebug(Messages.CHECKING_UNIQUENESS_MSG);
		ValidationResultList results = ImfHelper.getInstance().checkUuidsUnique(packingList.getAllUuids());
		
		for (PackingAsset packingAsset : packingList.getAssetList()) {
			if (packingAsset.getUuid()!=null) {
				//check if uuid is found in AssetMap
				Asset asset = assetMap.getAssetByUuid(packingAsset.getUuid());
				if (asset==null) {
					String msg = String.format(Messages.UUID_NOT_FOUND_FMT, packingAsset.getUuid());
					results.add(new ValidationResult(true, msg));
					ImfLogger.getLogger().logError(msg);
				} else {
					ImfLogger.getLogger().logDebug(String.format(Messages.UUID_FOUND_FMT, packingAsset.getUuid()));
					String assetPath = assetMap.getAssetResourcePath(packingAsset.getUuid());
					
					//check if assetPath exists
					if (assetPath != null) {
						//check hash value
						if (!skipHashChecking) {
							String hashValue = ImfHelper.getInstance().getBase64FileHash(assetPath, hashAlgo);
							ImfLogger.getLogger().logDebug(String.format(Messages.FILE_HASH_VALUE_FMT, assetPath, hashValue));
							if (!hashValue.equals(packingAsset.getHash())) {
								String msg = String.format(Messages.HASH_MISMATCH_FMT,
										assetPath, hashValue, packingAsset.getHash());
								results.add(new ValidationResult(true, msg));
								ImfLogger.getLogger().logWarning(msg);
							}
						}

						//check file size
						long size = ImfHelper.getInstance().getFileSize(assetPath);
						ImfLogger.getLogger().logDebug(String.format(Messages.FILE_SIZE_FMT, assetPath, size));
						if (size!=packingAsset.getSize()) {
							String msg = String.format(Messages.FILE_SIZE_MISMATCH_FMT,
									assetPath, size, packingAsset.getSize());
							results.add(new ValidationResult(true, msg));
							ImfLogger.getLogger().logWarning(msg);
						}
						
						//check mime file type
						String mimeType = ImfHelper.getInstance().getMimeFileType(assetPath);
						ImfLogger.getLogger().logDebug(String.format(Messages.MIME_TYPE_FMT, assetPath, mimeType));
						if (!mimeType.equals(packingAsset.getType())) {
							String msg = String.format(Messages.MIME_TYPE_MISMATCH_FMT,
									assetPath, mimeType, packingAsset.getType());
							results.add(new ValidationResult(true, msg));
							ImfLogger.getLogger().logWarning(msg);
						}
					}
				}
			}
		}
		return results;
	}
	
	/**
	 * Set assetMap
	 * @param assetMap
	 */
	public void setAssetMap(AssetMap assetMap) {
		this.assetMap = assetMap;
	}
	
	/**
	 * Get packingList
	 * @return packingList
	 */
	public PackingList getPackingList() {
		return packingList;
	}
	
	/**
	 * Set algorithm to use to hash the file
	 * @param algo
	 */
	public void setHashAlgorithm(ImfHashAlgorithm algo) {
		hashAlgo = algo;
	}

	/**
	 * Check if hask checking is enabled
	 * @return the skipHashChecking
	 */
	public boolean isSkipHashChecking() {
		return skipHashChecking;
	}

	/**
	 * Enable/disable the hash checking
	 * @param skipHashChecking
	 */
	public void setSkipHashChecking(boolean skipHashChecking) {
		this.skipHashChecking = skipHashChecking;
	}
}
