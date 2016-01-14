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
package com.sferalabs.imf.model.assetmap;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Asset data structure class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssetType", propOrder = {
    "id",
    "annotationText",
    "packingList",
    "chunkList"
})
public class Asset {
	
	@XmlElement(name = "Id", required = true)
	private String uuid;
	@XmlElement(name = "AnnotationText")
	private String annotation;
	@XmlElement(name = "PackingList")
	private boolean packingList;
	@XmlElement(name = "ChunkList", required = true)
	private List<Chunk> chunkList;
	
	/**
	 * Set asset uuid
	 * @param uuid asset's uuid
	 */
	public final void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Set asset annotation
	 * @param annotation asset's annotation
	 */
	public final void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	/**
	 * Set asset packing list (true/false)
	 * @param packingList
	 */
	public final void setPackingList(boolean packingList) {
		this.packingList = packingList;
	}
	
	/**
	 * Get asset uuid
	 * @return asset uuid
	 */
	public final String getUuid() {
		return uuid;
	}
	
	/**
	 * Get asset annotation
	 * @return asset annotation
	 */	
	public final String getAnnotation() {
		return annotation;
	}
	
	/**
	 * Get asset packing list
	 * @return packingList
	 */
	public final boolean getPackingList() {
		return packingList;
	}
	
	/**
	 * Set chunk list
	 * @param chunkList
	 */
	public final void setChunkList(List<Chunk> chunkList) {
		this.chunkList = chunkList;
	}
	
	/**
	 * Get chunk list
	 * @return chunkList
	 */
	public final List<Chunk> getChunkList() {
		return chunkList;
	}
}
