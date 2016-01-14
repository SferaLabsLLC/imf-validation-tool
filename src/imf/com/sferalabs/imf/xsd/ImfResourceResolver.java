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

import java.io.InputStream;
import java.util.HashMap;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;

/**
 * ImfResourceResolver class for xsd caching
 */
public class ImfResourceResolver implements LSResourceResolver {
	
	private static ImfResourceResolver instance;
	private static Object mutex = new Object();
	
	public static final String XMLDSIG_CORE_SYSTEMID = "http://www.w3.org/TR/2002/REC-xmldsig-core-20020212/xmldsig-core-schema.xsd";
	public static final String XMLDSIG_CORE_SCHEMA = "/com/sferalabs/imf/xsd/schema/xmldsig-core-schema.xsd";
	public static final String XML_SYSTEMID = "http://www.w3.org/2001/03/xml.xsd";
	public static final String XML_SCHEMA = "/com/sferalabs/imf/xsd/schema/xml.xsd";
	public static final String XML_SCHEMA_DTD_SYSTEMID = "XMLSchema.dtd";
	public static final String XML_SCHEMA_DTD_SYSTEMID_2 = "http://www.w3.org/2001/XMLSchema.dtd";
	public static final String XML_SCHEMA_DTD = "/com/sferalabs/imf/xsd/schema/XMLSchema.dtd";
	public static final String XML_DATATYPES_DTD_SYSTEMID = "datatypes.dtd";
	public static final String XML_DATATYPES_DTD = "/com/sferalabs/imf/xsd/schema/datatypes.dtd";
	public static final String DCMLTYPES_XSD_SYSTEMID = "dcmlTypes.xsd";
	public static final String DCMLTYPES_XSD = "/com/sferalabs/imf/xsd/schema/dcmlTypes.xsd";
	public static final String COMPOSITION_PLAYLIST_XSD_SYSTEMID = "compositionPlaylist.xsd";
	public static final String COMPOSITION_PLAYLIST_XSD = "/com/sferalabs/imf/xsd/schema/compositionPlaylist.xsd";
	public static final String CORE_CONSTRAINTS_XSD_SYSTEMID = "coreConstraints.xsd";
	public static final String CORE_CONSTRAINTS_XSD = "/com/sferalabs/imf/xsd/schema/coreConstraints.xsd";

	private HashMap<String,String> nmap;
	
	protected ImfResourceResolver() {
		nmap = new HashMap<String,String>();
		nmap.put(XMLDSIG_CORE_SYSTEMID, XMLDSIG_CORE_SCHEMA);
		nmap.put(XML_SYSTEMID, XML_SCHEMA);
		nmap.put(XML_SCHEMA_DTD_SYSTEMID, XML_SCHEMA_DTD);
		nmap.put(XML_SCHEMA_DTD_SYSTEMID_2, XML_SCHEMA_DTD);
		nmap.put(XML_DATATYPES_DTD_SYSTEMID, XML_DATATYPES_DTD);
		nmap.put(DCMLTYPES_XSD_SYSTEMID, DCMLTYPES_XSD);
		nmap.put(COMPOSITION_PLAYLIST_XSD_SYSTEMID, COMPOSITION_PLAYLIST_XSD);
		nmap.put(CORE_CONSTRAINTS_XSD_SYSTEMID, CORE_CONSTRAINTS_XSD);
	}

	public static ImfResourceResolver getInstance() {
		if (instance == null) {
			synchronized (mutex) {
				if(instance == null) {
					instance = new ImfResourceResolver();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Set cached resource of schemaLocation
	 * @param schemaLocation
	 * @param cachedResource
	 */
	public void setResource(String schemaLocation, String cachedResource) {
		nmap.put(schemaLocation, cachedResource);
	}
	
	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId,
			String systemId, String baseURI) {
		LSInput input = new DOMInputImpl();
		String resoureUrl = nmap.get(systemId);
		if (resoureUrl != null) {
			InputStream resourceAsStream = this.getClass().getResourceAsStream(resoureUrl);
			input.setByteStream(resourceAsStream);
			return input;
		}
		return null;
	}

}
