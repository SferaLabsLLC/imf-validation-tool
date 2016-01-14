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

import com.sferalabs.imf.model.assetmap.Asset;
import com.sferalabs.imf.model.assetmap.AssetMap;
import com.sferalabs.imf.model.assetmap.Chunk;

/**
 * SaxAssetMapHandler class to parse AssetMap xml file
 */
public class SaxAssetMapHandler extends DefaultHandler {

	enum AssetXmlNode {
		ROOT, ASSETMAP, ASSET, CHUNK
	}

	private AssetXmlNode node = AssetXmlNode.ROOT;
	private boolean isAssetMapInitialized;
	private AssetMap assetMap;
	private List<Asset> assetList;
	private List<Chunk> chunkList;
	private Asset asset;
	private Chunk chunk;
	private String content;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		qName = qName.toLowerCase();
		
		//Check that there's only one AssetMap node in the xml document
		if (isAssetMapInitialized) {
			throw new SAXException("Invalid xml file, only one AssetMap should be presented in the document");
		}
		
		if (node == AssetXmlNode.ROOT) {
			//first element of schema should be AssetMap
			if (!qName.equals("assetmap")) {
				throw new SAXException("Invalid xml file, AssetMap should be the first node");
			}
		}
		
		switch (qName) {
			case "assetmap":
				assetMap = new AssetMap();
				node = AssetXmlNode.ASSETMAP;
				break;
			case "assetlist":
				assetList = new ArrayList<Asset>();
				break;
			case "asset":
				asset = new Asset();
				node = AssetXmlNode.ASSET;
				break;
			case "chunklist":
				chunkList = new ArrayList<Chunk>();
				break;
			case "chunk":
				chunk = new Chunk();
				node = AssetXmlNode.CHUNK;
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
			case "assetmap":
				isAssetMapInitialized = true;
				break;
			case "assetlist":
				if (assetList != null)
					assetMap.setAssetList(assetList);
				assetList = null;
				break;
			case "asset":
				if (asset != null)
					assetList.add(asset);
				asset = null;
				break;
			case "chunklist":
				if (chunkList != null)
					asset.setChunkList(chunkList);
				chunkList = null;
				break;
			case "chunk":
				if (chunk != null)
					chunkList.add(chunk);
				chunk = null;
				break;
			default:
				switch (node) {
					case ASSETMAP:
						switch (qName) {
							case "id":
								assetMap.setUuid(content);
								break;
							case "annotationtext":
								assetMap.setAnnotation(content);
								break;
							case "creator":
								assetMap.setCreator(content);
								break;
							case "volumecount":
								assetMap.setVolumnCount(Long.valueOf(content));
								break;
							case "issuedate":
								assetMap.setIssueDate(content);
								break;
							case "issuer":
								assetMap.setIssuer(content);
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
							case "packinglist":
								asset.setPackingList(Boolean.valueOf(content));
								break;
							default:
								break;
						}
						break;
					case CHUNK:
						switch (qName) {
							case "path":
								chunk.setPath(content);
								break;
							case "volumeindex":
								chunk.setVolumeIndex(Long.valueOf(content));
								break;
							case "offset":
								chunk.setOffset(Long.valueOf(content));
								break;
							case "length":
								chunk.setLength(Long.valueOf(content));
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
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
	
	/**
	 * Get assetMap
	 * @return assetMap
	 */
	public AssetMap getAssetMap() {
		return assetMap;
	}
}
