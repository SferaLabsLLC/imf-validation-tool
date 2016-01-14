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

package com.sferalabs.imf.xsd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

/**
 * A singleton class declares the XSD resources
 */
public final class ImfXsds {

	public static final String ASSET_MAP_XSD = "/com/sferalabs/imf/xsd/schema/assetMap.xsd";
	public static final String CPL_XSD = "/com/sferalabs/imf/xsd/schema/compositionPlaylist.xsd";
	public static final String DCML_TYPES_XSD = "/com/sferalabs/imf/xsd/schema/dcmlTypes.xsd";
	public static final String OPL_XSD = "/com/sferalabs/imf/xsd/schema/outputProfileList.xsd";
	public static final String PACKAGING_LIST_XSD = "/com/sferalabs/imf/xsd/schema/packingList.xsd";
	public static final String VOL_INDEX_XSD = "/com/sferalabs/imf/xsd/schema/volumnIndex.xsd";
	public static final String CORE_CONSTRAINTS_XSD = "/com/sferalabs/imf/xsd/schema/coreConstraints.xsd";

	private static ImfXsds instance;
	private static Object mutex = new Object();
	
	public static ImfXsds getInstance() {
		if (instance == null) {
			synchronized (mutex) {
				if(instance == null) {
					instance = new ImfXsds();
				}
			}
		}
		return instance;
	}
	
	private ImfXsds() {
	}
	
	/**
	 * Load the specified xsd resource
	 * @param xsd_path path to xsd resource
	 * @return Input Stream of xsd resource, null if invalid xsd resource
	 */
	public InputStream loadXsdResource(String xsd_path) {
		if (xsd_path.startsWith("/com/sferalabs/imf/xsd/schema")) {
			if (getClass().getResource(xsd_path) != null) {
				return getClass().getResourceAsStream(xsd_path);
			}
		} else {
			try {
				return new FileInputStream(new File(xsd_path));
			} catch (FileNotFoundException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Validate given xml file again specified xsd resource
	 * @param xmlFilePath xml file path
	 * @param xsd_resource_path xsd resource
	 * @throws SAXException if xmlFile is not followed xsd
	 * @throws IOException if file path is invalid
	 */
	public void validateXmlAgainsXsdResource(String xmlFilePath, String xsd_resource_path) throws SAXException, IOException {
		Source xmlFile = new StreamSource(new File(xmlFilePath));
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		LSResourceResolver resourceResolver = (LSResourceResolver) ImfResourceResolver.getInstance();
		schemaFactory.setResourceResolver(resourceResolver);
		InputStream xsd_source = loadXsdResource(xsd_resource_path);
		if (xsd_source != null) {
			Schema schema = schemaFactory.newSchema(new StreamSource(xsd_source));
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
		} else {
			throw new IOException(xsd_resource_path + " does not exists");
		}
	}
}
