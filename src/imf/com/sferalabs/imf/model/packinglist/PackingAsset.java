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
package com.sferalabs.imf.model.packinglist;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Packing Asset class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssetType", propOrder = {
    "id",
    "annotationText",
    "hash",
    "size",
    "type",
    "originalFileName"
})
public class PackingAsset {

	@XmlElement(name = "Id", required = true)
	private String uuid;
	@XmlElement(name = "AnnotationText")
	private String annotation;
	@XmlElement(name = "Hash", required = true)
	private String hash;
	@XmlElement(name = "Size", required = true)
	private long size;
	@XmlElement(name = "Type", required = true)
	private String type;
	@XmlElement(name = "OriginalFileName")
	private String filename;
	
	/**
	 * @return the uuid
	 */
	public final String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public final void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * @return the annotation
	 */
	public final String getAnnotation() {
		return annotation;
	}
	/**
	 * @param annotation the annotation to set
	 */
	public final void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	/**
	 * @return the hash
	 */
	public final String getHash() {
		return hash;
	}
	/**
	 * @param hash the hash to set
	 */
	public final void setHash(String hash) {
		this.hash = hash;
	}
	/**
	 * @return the size
	 */
	public final long getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public final void setSize(long size) {
		this.size = size;
	}
	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public final void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the filename
	 */
	public final String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public final void setFilename(String filename) {
		this.filename = filename;
	}
}
