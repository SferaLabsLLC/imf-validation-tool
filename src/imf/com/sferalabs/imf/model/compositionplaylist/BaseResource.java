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
package com.sferalabs.imf.model.compositionplaylist;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * BaseResource class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BaseResourceType", propOrder = {
    "id",
    "annotation",
    "editRate",
    "intrinsicDuration",
    "entryPoint",
    "sourceDuration",
    "repeatCount"
})
@XmlSeeAlso({
    TrackFileResource.class,
    MarkerResource.class
})
public class BaseResource {

	@XmlElement(name = "Id", required = true)
	private String id;
	@XmlElement(name = "Annotation")
	private String annotation;
	@XmlElement(name = "EditRate")
	private String editRate;
	@XmlElement(name = "IntrinsicDuration", required = true)
	private long intrinsicDuration;
	@XmlElement(name = "EntryPoint")
	private long entryPoint;
	@XmlElement(name = "SourceDuration")
	private long sourceDuration;
	@XmlElement(name = "RepeatCount")
	private long repeatCount;

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the annotation
	 */
	public final String getAnnotation() {
		return annotation;
	}

	/**
	 * @param annotation
	 *            the annotation to set
	 */
	public final void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * @return the editRate
	 */
	public final String getEditRate() {
		return editRate;
	}

	/**
	 * @param editRate
	 *            the editRate to set
	 */
	public final void setEditRate(String editRate) {
		this.editRate = editRate;
	}

	/**
	 * @return the intrinsicDuration
	 */
	public final long getIntrinsicDuration() {
		return intrinsicDuration;
	}

	/**
	 * @param intrinsicDuration
	 *            the intrinsicDuration to set
	 */
	public final void setIntrinsicDuration(long intrinsicDuration) {
		this.intrinsicDuration = intrinsicDuration;
	}

	/**
	 * @return the entryPoint
	 */
	public final long getEntryPoint() {
		return entryPoint;
	}

	/**
	 * @param entryPoint
	 *            the entryPoint to set
	 */
	public final void setEntryPoint(long entryPoint) {
		this.entryPoint = entryPoint;
	}

	/**
	 * @return the sourceDuration
	 */
	public final long getSourceDuration() {
		return sourceDuration;
	}

	/**
	 * @param sourceDuration
	 *            the sourceDuration to set
	 */
	public final void setSourceDuration(long sourceDuration) {
		this.sourceDuration = sourceDuration;
	}

	/**
	 * @return the repeatCount
	 */
	public final long getRepeatCount() {
		return repeatCount;
	}

	/**
	 * @param repeatCount
	 *            the repeatCount to set
	 */
	public final void setRepeatCount(long repeatCount) {
		this.repeatCount = repeatCount;
	}

}
