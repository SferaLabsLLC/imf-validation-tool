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

package com.sferalabs.imf.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.sferalabs.imf.exception.ImfXmlException;
import com.sferalabs.imf.model.assetmap.Asset;
import com.sferalabs.imf.model.assetmap.AssetMap;
import com.sferalabs.imf.model.imfpackage.ImfPackage;
import com.sferalabs.imf.model.packinglist.PackingList;
import com.sferalabs.imf.util.ImfLogger;
import com.sferalabs.imf.validation.AssetMapValidator;
import com.sferalabs.imf.validation.CompositionPlaylistValidator;
import com.sferalabs.imf.validation.PackingListValidator;

public class CplValidatorTest {
	
	//@Test
	public void testValidCpl() {
		URL folderPath = getClass().getResource
				("/com/sferalabs/imf/test/resources/cpl/ValidCpl");
		try {
			performValidityTest(folderPath);
			if (ImfLogger.getLogger().hasAnyErrors()) {
				fail("Unexpected error");
			} else {
				assertTrue(true);
			}
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}
	
	@Test
	public void testMalformedCpl() {
		URL folderPath = getClass().getResource
				("/com/sferalabs/imf/test/resources/cpl/MalformedCpl");
		try {
			performValidityTest(folderPath);
			fail("Unable to detect malformed xml file");
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	//@Test
	public void testDuplicatedSequenceIdCpl() {
		URL folderPath = getClass().getResource
				("/com/sferalabs/imf/test/resources/cpl/DuplicatedSequenceIdCpl");
		try {
			performValidityTest(folderPath);
			if (ImfLogger.getLogger().hasAnyErrors()) {
				assertTrue(true);
			} else {
				fail("Unable to detect duplicated Sequence Id in CPL");
			}
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}
	
	//@Test
	public void testInvalidTrackFileIdCpl() {
		URL folderPath = getClass().getResource
				("/com/sferalabs/imf/test/resources/cpl/InvalidTrackFileIdCpl");
		try {
			performValidityTest(folderPath);
			if (ImfLogger.getLogger().hasAnyErrors()) {
				assertTrue(true);
			} else {
				fail("Unable to detect invalid Track File Id in CPL");
			}
		} catch (Exception e) {
			fail("Unexpected error: " + e.getMessage());
		}
	}
	
	private void performValidityTest(URL folderPath)
			throws SAXException, IOException, ParserConfigurationException,
			ImfXmlException, URISyntaxException {
		ImfLogger.getLogger().clearErrors();
		File folder = new File(folderPath.toURI());
		ImfPackage pack = new ImfPackage(folder.getAbsolutePath());
		AssetMapValidator assetValidator = new AssetMapValidator(pack);
		assetValidator.parse();
		assetValidator.validate();
		
		//PackingList
		AssetMap assetMap = assetValidator.getAssetMap();
		List<Asset> packingListAssets = assetMap.getPackingListAssets();
		Asset asset = packingListAssets.get(0);
		String packingListPath = assetMap.getAssetResourcePath(asset.getUuid());
		PackingListValidator packingListValidator = new PackingListValidator(packingListPath);
		packingListValidator.parse();
		packingListValidator.setAssetMap(assetMap);
		packingListValidator.validate();
		PackingList packingList = packingListValidator.getPackingList();
		
		//CPL
		List<Asset> cplAssets = assetMap.getCPLAssets();
		for (Asset cpl:cplAssets) {
			String cplFilePath = assetMap.getAssetResourcePath(cpl.getUuid());
			CompositionPlaylistValidator cplValidator = new CompositionPlaylistValidator(cplFilePath);
			cplValidator.setAssetMap(assetMap);
			cplValidator.setPackingList(packingList);
			cplValidator.parse();
			cplValidator.validate();
		}
	}
}
