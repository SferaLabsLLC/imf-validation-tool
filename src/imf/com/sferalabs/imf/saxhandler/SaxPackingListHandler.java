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
package com.sferalabs.imf.saxhandler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sferalabs.imf.model.packinglist.PackingAsset;
import com.sferalabs.imf.model.packinglist.PackingList;

/**
 * SaxPackingListHandler class to parse PackingList xml file
 */
public class SaxPackingListHandler extends DefaultHandler {
	
	enum PackingListXmlNode {
		ROOT, PACKINGLIST, ASSET
	}
	
	private boolean isPackingListInitialized;
	private PackingListXmlNode node = PackingListXmlNode.ROOT;
	private PackingList packingList;
	private List<PackingAsset> assetList;
	private PackingAsset asset;
	
	private String content;
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		qName = qName.toLowerCase();
		
		//Check that there's only one PackingList node in the xml document
		if (isPackingListInitialized) {
			throw new SAXException("Invalid xml file, only one PackingList should be presented in the document");
		}
		
		if (node == PackingListXmlNode.ROOT) {
			//first element of schema should be PackingList
			if (!qName.equals("packinglist")) {
				throw new SAXException("Invalid xml file, PackingList should be the first node");
			}
		}
		
		switch (qName) {
			case "packinglist":
				packingList = new PackingList();
				node = PackingListXmlNode.PACKINGLIST;
				break;
			case "assetlist":
				assetList = new ArrayList<PackingAsset>();
				break;
			case "asset":
				asset = new PackingAsset();
				node = PackingListXmlNode.ASSET;
				break;
			default:
				break;	
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		qName = qName.toLowerCase();
		switch (qName) {
			case "packinglist":
				isPackingListInitialized = true;
				break;
			case "assetlist":
				if (assetList != null)
					packingList.setAssetList(assetList);
				assetList = null;
				break;
			case "asset":
				if (asset != null)
					assetList.add(asset);
				asset = null;
				break;
			default:
				switch (node) {
					case PACKINGLIST:
						switch (qName) {
							case "id":
								packingList.setUuid(content);
								break;
							case "annotationtext":
								packingList.setAnnotation(content);
								break;
							case "iconid":
								packingList.setIconId(content);
								break;
							case "issuedate":
								packingList.setIssueDate(content);
								break;
							case "issuer":
								packingList.setIssuer(content);
								break;
							case "creator":
								packingList.setCreator(content);
								break;
							case "groupid":
								packingList.setGroupId(content);
								break;
							default:
								break;
						}
						break;
					case ASSET:
						switch (qName) {
							case "id":
								asset.setUuid(content);
								break;
							case "annotationtext":
								asset.setAnnotation(content);
								break;
							case "hash":
								asset.setHash(content);
								break;
							case "size":
								asset.setSize(Long.valueOf(content));
								break;
							case "type":
								asset.setType(content);
								break;
							case "originalfileName":
								asset.setFilename(content);
								break;
							default:
								break;
						}
						break;					
					default:
						break;
				}
				break;
		}
	}
	
	/**
	 * Get packingList
	 * @return packingList
	 */
	public PackingList getPackingList() {
		return packingList;
	}
}
