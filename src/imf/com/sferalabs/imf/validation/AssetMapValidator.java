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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.sferalabs.imf.exception.ImfXmlException;
import com.sferalabs.imf.model.assetmap.Asset;
import com.sferalabs.imf.model.assetmap.AssetMap;
import com.sferalabs.imf.model.imfpackage.ImfPackage;
import com.sferalabs.imf.saxhandler.SaxAssetMapHandler;
import com.sferalabs.imf.util.ImfHelper;
import com.sferalabs.imf.util.ImfLogger;
import com.sferalabs.imf.xsd.ImfXsds;

/**
 * AssetMapValidator class to validate the AssetMap xml file
 */
public class AssetMapValidator extends XmlResourceValidator {

	private static class Messages {
		private static final String CHECK_ELEMENT_UNIQUENESS_MSG = "Checking uniqueness of uuid elements";
		private static final String CHECKING_PRESENCE_MSG = "Checking physical presence of each asset from AssetList";
		private static final String ASSET_NOT_FOUND_FMT = "Asset %s's path is not found in package";
		private static final String ASSET_FOUND_FMT = "Asset %s exists in package";
	}

	private static final String ASSETMAP_FILENAME = "ASSETMAP.xml";

	private AssetMap assetMap;
	private ImfPackage imfPackage;
	
	public AssetMapValidator(ImfPackage imfPackage) {
		super(imfPackage.getPackagePath() + File.separator + ASSETMAP_FILENAME);
		resourcePath = ImfXsds.ASSET_MAP_XSD;
		this.imfPackage = imfPackage;
	}

	@Override
	public ValidationResult parse() throws IOException, ParserConfigurationException, SAXException {
		ValidationResult result = validateXsd();
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		SaxAssetMapHandler handler = new SaxAssetMapHandler();
		parser.parse(new FileInputStream(filePath), handler);
		assetMap = handler.getAssetMap();
		assetMap.setImfPackage(imfPackage);
		return result;
	}

	@Override
	/**
	 * Validate the assetMap xml file to check if it follows IMF constraints
	 * - Check if uuid of each asset in assetMap is unique
	 * - Check if asset physically exists in the file system
	 */
	public ValidationResultList validate() throws ImfXmlException {
		// check if uuid is unique
		ImfLogger.getLogger().logDebug(Messages.CHECK_ELEMENT_UNIQUENESS_MSG);
		ValidationResultList result = ImfHelper.getInstance().checkUuidsUnique(assetMap.getAllUuids());

		// check if paths exist
		ImfLogger.getLogger().logDebug(Messages.CHECKING_PRESENCE_MSG);
		for (Asset asset : assetMap.getAssetList()) {
			if (assetMap.getAssetResourcePath(asset.getUuid())==null) {
				result.add(new ValidationResult(true, String.format(Messages.ASSET_NOT_FOUND_FMT, asset.getUuid())));
				ImfLogger.getLogger().logError(String.format(Messages.ASSET_NOT_FOUND_FMT, asset.getUuid()));
			} else {
				ImfLogger.getLogger().logDebug(String.format(Messages.ASSET_FOUND_FMT, asset.getUuid()));
			}
		}
		return result;
	}

	/**
	 * Get assetMap
	 * 
	 * @return assetMap
	 */
	public AssetMap getAssetMap() {
		return assetMap;
	}
}
