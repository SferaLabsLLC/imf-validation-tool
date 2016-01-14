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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * CompositionPlaylist class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompositionPlaylistType", propOrder = {
    "id",
    "annotation",
    "issueDate",
    "issuer",
    "creator",
    "contentOriginator",
    "contentTitle",
    "contentKind",
    "contentVersionList",
    "essenceDescriptorList",
    "compositionTimecode",
    "editRate",
    "totalRunningTime",
    "localeList",
    "extensionProperties",
    "segmentList",
    "signer",
    "signature"
})
public class CompositionPlaylist {

	@XmlElement(name = "Id", required = true)
	private String uuid;
	@XmlElement(name = "Annotation")
	private String annotation;
	@XmlElement(name = "IssueDate", required = true)
	private String issueDate;
	@XmlElement(name = "Issuer")
	private String issuer;
	@XmlElement(name = "Creator")
	private String creator;
	@XmlElement(name = "ContentOriginator")
	private String contentOriginator;
	@XmlElement(name = "ContentTitle", required = true)
	private String contentTitle;
	@XmlElement(name = "ContentKind")
	private ContentKind contentKind;
	@XmlElement(name = "ContentVersionList")
	private List<ContentVersion> contentVersionList;
	@XmlElement(name = "EssenceDescriptorList")
	private List<EssenceDescriptor> essenceDescriptorList;
	@XmlElement(name = "CompositionTimecode")
	private CompositionTimecode compositionTimecode;
	@XmlElement(name = "EditRate")
	private String editRate;
	@XmlElement(name = "TotalRunningTime")
	private String totalRunningTime;
	@XmlElement(name = "LocaleList")
	private List<Locale> localeList;
	@XmlElement(name = "ExtensionProperties")
	private ExtensionProperties extensionProperties;
	@XmlElement(name = "SegmentList", required = true)
	private List<Segment> segmentList;

	/**
	 * @return the extensionProperties
	 */
	public final ExtensionProperties getExtensionProperties() {
		return extensionProperties;
	}

	/**
	 * @param extensionProperties
	 *            the extensionProperties to set
	 */
	public final void setExtensionProperties(ExtensionProperties extensionProperties) {
		this.extensionProperties = extensionProperties;
	}

	/**
	 * @return the uuid
	 */
	public final String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the uuid to set
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
	 * @param annotation
	 *            the annotation to set
	 */
	public final void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * @return the issueDate
	 */
	public final String getIssueDate() {
		return issueDate;
	}

	/**
	 * @param issueDate
	 *            the issueDate to set
	 */
	public final void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	/**
	 * @return the issuer
	 */
	public final String getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer
	 *            the issuer to set
	 */
	public final void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	/**
	 * @return the creator
	 */
	public final String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public final void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the contentOriginator
	 */
	public final String getContentOriginator() {
		return contentOriginator;
	}

	/**
	 * @param contentOriginator
	 *            the contentOriginator to set
	 */
	public final void setContentOriginator(String contentOriginator) {
		this.contentOriginator = contentOriginator;
	}

	/**
	 * @return the contentTitle
	 */
	public final String getContentTitle() {
		return contentTitle;
	}

	/**
	 * @param contentTitle
	 *            the contentTitle to set
	 */
	public final void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	/**
	 * @return the contentKind
	 */
	public final ContentKind getContentKind() {
		return contentKind;
	}

	/**
	 * @param contentKind
	 *            the contentKind to set
	 */
	public final void setContentKind(ContentKind contentKind) {
		this.contentKind = contentKind;
	}

	/**
	 * @return the contentVersionList
	 */
	public final List<ContentVersion> getContentVersionList() {
		return contentVersionList;
	}

	/**
	 * @param contentVersionList
	 *            the contentVersionList to set
	 */
	public final void setContentVersionList(List<ContentVersion> contentVersionList) {
		this.contentVersionList = contentVersionList;
	}

	/**
	 * @return the essenceDescriptorList
	 */
	public final List<EssenceDescriptor> getEssenceDescriptorList() {
		return essenceDescriptorList;
	}

	/**
	 * @param essenceDescriptorList
	 *            the essenceDescriptorList to set
	 */
	public final void setEssenceDescriptorList(
			List<EssenceDescriptor> essenceDescriptorList) {
		this.essenceDescriptorList = essenceDescriptorList;
	}

	/**
	 * @return the compositionTimecode
	 */
	public final CompositionTimecode getCompositionTimecode() {
		return compositionTimecode;
	}

	/**
	 * @param compositionTimecode
	 *            the compositionTimecode to set
	 */
	public final void setCompositionTimecode(CompositionTimecode compositionTimecode) {
		this.compositionTimecode = compositionTimecode;
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
	 * @return the totalRunningTime
	 */
	public final String getTotalRunningTime() {
		return totalRunningTime;
	}

	/**
	 * @param totalRunningTime
	 *            the totalRunningTime to set
	 */
	public final void setTotalRunningTime(String totalRunningTime) {
		this.totalRunningTime = totalRunningTime;
	}

	/**
	 * @return the localeList
	 */
	public final List<Locale> getLocaleList() {
		return localeList;
	}

	/**
	 * @param localeList
	 *            the localeList to set
	 */
	public final void setLocaleList(List<Locale> localeList) {
		this.localeList = localeList;
	}

	/**
	 * @return the segmentList
	 */
	public final List<Segment> getSegmentList() {
		return segmentList;
	}

	/**
	 * @param segmentList
	 *            the segmentList to set
	 */
	public final void setSegmentList(List<Segment> segmentList) {
		this.segmentList = segmentList;
	}
	
	/**
	 * Get all uuids in CPL file
	 * 
	 * @return list of uuids
	 */
	public final List<String> getAllUuids() {
		List<String> uuids = new ArrayList<String>();
		
		if (uuid != null) {
			uuids.add(uuid);
		}
		
		//EssenceDescriptor
		if (essenceDescriptorList != null) {
			for (EssenceDescriptor item:essenceDescriptorList) {
				if (item.getId() != null)
					uuids.add(item.getId());
			}
		}
		
		//SegmentList
		if (segmentList != null) {
			for (Segment item:segmentList) {
				if (item.getId() != null) {
					uuids.add(item.getId());
				}
				//SequenceList
				if (item.getSequenceList() != null) {
					for (Sequence seq:item.getSequenceList()) {
						if (seq.getId() != null) {
							uuids.add(seq.getId());
						}
						//ResourceList
						if (seq.getResourceList() != null) {
							for (BaseResource res:seq.getResourceList()) {
								if (res.getId() != null) {
									uuids.add(res.getId());
								}
							}
						}
					}
				}
			}
		}
		
		return uuids;
	}
}
