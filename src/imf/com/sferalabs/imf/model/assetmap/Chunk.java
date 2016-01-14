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

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Chunk data structure class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChunkType", propOrder = {
    "path",
    "volumeIndex",
    "offset",
    "length"
})
public class Chunk {
	@XmlElement(name = "Path", required = true)
	private String path;
	@XmlElement(name = "VolumeIndex")
	private long volumeIndex;
	@XmlElement(name = "Offset")
	private long offset;
	@XmlElement(name = "Length")
	private long length;
	
	/**
	 * Get Chunk path
	 * @return path
	 */
	public final String getPath() {
		return path;
	}
	
	/**
	 * Set chunk path
	 * @param path
	 */
	public final void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * Get Volume index
	 * @return volumeIndex
	 */
	public final long getVolumeIndex() {
		return volumeIndex;
	}
	
	/**
	 * Set Volume index
	 * @param volumeIndex
	 */
	public final void setVolumeIndex(long volumeIndex) {
		this.volumeIndex = volumeIndex;
	}
	
	/**
	 * Get chunk offset
	 * @return offset
	 */
	public final long getOffset() {
		return offset;
	}
	
	/**
	 * Set chunk offset
	 * @param offset
	 */
	public final void setOffset(long offset) {
		this.offset = offset;
	}
	
	/**
	 * Get chunk length
	 * @return length
	 */
	public final long getLength() {
		return length;
	}
	
	/**
	 * Set chunk length
	 * @param length
	 */
	public final void setLength(long length) {
		this.length = length;
	}
}
