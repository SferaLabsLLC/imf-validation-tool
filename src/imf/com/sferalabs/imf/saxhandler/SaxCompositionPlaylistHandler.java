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
import java.util.Arrays;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.sferalabs.imf.model.compositionplaylist.ApplicationIdentification;
import com.sferalabs.imf.model.compositionplaylist.BaseResource;
import com.sferalabs.imf.model.compositionplaylist.CompositionPlaylist;
import com.sferalabs.imf.model.compositionplaylist.CompositionTimecode;
import com.sferalabs.imf.model.compositionplaylist.ContentKind;
import com.sferalabs.imf.model.compositionplaylist.ContentMaturityRating;
import com.sferalabs.imf.model.compositionplaylist.ContentVersion;
import com.sferalabs.imf.model.compositionplaylist.EssenceDescriptor;
import com.sferalabs.imf.model.compositionplaylist.ExtensionProperties;
import com.sferalabs.imf.model.compositionplaylist.Locale;
import com.sferalabs.imf.model.compositionplaylist.Marker;
import com.sferalabs.imf.model.compositionplaylist.MarkerResource;
import com.sferalabs.imf.model.compositionplaylist.Segment;
import com.sferalabs.imf.model.compositionplaylist.Sequence;
import com.sferalabs.imf.model.compositionplaylist.SequenceType;
import com.sferalabs.imf.model.compositionplaylist.StereoImageTrackFileResource;
import com.sferalabs.imf.model.compositionplaylist.TrackFileResource;

/**
 * SaxCompositionPlaylistHandler class
 */
public class SaxCompositionPlaylistHandler extends DefaultHandler {

	enum ComposistionPlaylistXmlNode {
		ROOT, APPLICATIONIDENTIFICATION, COMPOSITIONPLAYLIST,
		CONTENTMATURITYRATING, CONTENTVERSION, COMPOSITIONTIMECODE,
		ESSENCEDESCRIPTOR, EXTENSIONPROPERTIES, LOCALE,
		SEGMENT, SEQUENCE, STEREOIMAGETRACKFILERESOURCE, TRACKFILERESOURCE,
		MARKERRESOURCE, MARKER, LEFTEYERESOURCE, RIGHTEYERESOURCE, BASERESOURCE
	}
	
	private boolean isCompositionPlaylistInitialized;
	private String content;
	private ComposistionPlaylistXmlNode node = ComposistionPlaylistXmlNode.ROOT;
	
	//CompositionPlaylist variables
	private CompositionPlaylist compositionPlaylist;
	private List<ContentVersion> contentVersionList;
	private List<EssenceDescriptor> essenceDescriptorList;
	private CompositionTimecode compositionTimecode;
	private List<Locale> localeList;
	private ExtensionProperties extensionProperties;
	private List<Segment> segmentList;
	
	//ContentKind
	private ContentKind contentKind;
	
	//ContentVersion variables
	private	ContentVersion contentVersion;
		
	//EssenceDescriptor variables
	private EssenceDescriptor essenceDescriptor;
	
	//ExtensionProperties variables
	private ApplicationIdentification applicationIdentification;
	
	//Locale variables
	private Locale locale;
	private List<String> languageList;
	private List<String> regionList;
	private List<ContentMaturityRating> contentMaturityRatingList;
	
	//ContentMaturityRating variables
	private ContentMaturityRating contentMaturityRating;
	
	//Segment variables
	private Segment segment;
	private List<Sequence> sequenceList;
	
	//Sequence variables
	private Sequence sequence;
	private List<BaseResource> resourceList;
	
	//Resource variables
	private BaseResource resource;
	
	//StereoImageTrackFileResource variables
	private TrackFileResource leftEye;
	private TrackFileResource rightEye;
	
	//MarkerResource variables
	private List<Marker> markers;
	private Marker marker;
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		qName = qName.toLowerCase();
		
		//Check that there's only one PackingList node in the xml document
		if (isCompositionPlaylistInitialized) {
			throw new SAXException("Invalid xml file, only one CompositionPlaylist should be presented in the document");
		}
				
		if (node == ComposistionPlaylistXmlNode.ROOT) {
			//first element of schema should be CompositionPlaylist
			if (!qName.equals("compositionplaylist")) {
				throw new SAXException("Invalid xml file, CompositionPlaylist should be the first node");
			}
		}
				
		switch (qName) {
			case "compositionplaylist":
				compositionPlaylist = new CompositionPlaylist();
				node = ComposistionPlaylistXmlNode.COMPOSITIONPLAYLIST;
				break;
			//CompositionPlaylist Child
			case "contentversionlist":
				contentVersionList = new ArrayList<ContentVersion>();
				break;
			case "essencedescriptorlist":
				essenceDescriptorList = new ArrayList<EssenceDescriptor>();
				break;
			case "compositiontimecode":
				compositionTimecode = new CompositionTimecode();
				node = ComposistionPlaylistXmlNode.COMPOSITIONTIMECODE;
				break;
			case "localelist":
				localeList = new ArrayList<Locale>();
				break;
			case "extensionproperties":
				extensionProperties = new ExtensionProperties();
				node = ComposistionPlaylistXmlNode.EXTENSIONPROPERTIES;
				break;
			case "segmentlist":
				segmentList = new ArrayList<Segment>();
				break;
			case "essencedescriptor":
				essenceDescriptor = new EssenceDescriptor();
				node = ComposistionPlaylistXmlNode.ESSENCEDESCRIPTOR;
				break;
			case "contentkind":
				String scope = attributes.getValue("scope");
				contentKind = new ContentKind();
				if (scope != null) {
					contentKind.setScope(scope.toLowerCase());
				}
				break;
			case "contentversion":
				contentVersion = new ContentVersion();
				node = ComposistionPlaylistXmlNode.CONTENTVERSION;
				break;
			case "locale":
				locale = new Locale();
				node = ComposistionPlaylistXmlNode.LOCALE;
				break;
			case "segment":
				segment = new Segment();
				node = ComposistionPlaylistXmlNode.SEGMENT;
				break;
			//ExtensionProperties Child
			case "applicationidentification":
				applicationIdentification = new ApplicationIdentification();
				node = ComposistionPlaylistXmlNode.APPLICATIONIDENTIFICATION;
				break;
			//Locale Child
			case "languagelist":
				languageList = new ArrayList<String>();
				break;
			case "regionlist":
				regionList = new ArrayList<String>();
				break;
			case "contentmaturityratinglist":
				contentMaturityRatingList = new ArrayList<ContentMaturityRating>();
				break;
			case "contentmaturityrating":
				contentMaturityRating = new ContentMaturityRating();
				node = ComposistionPlaylistXmlNode.CONTENTMATURITYRATING;
				break;
			//Segment Child
			case "sequencelist":
				sequenceList = new ArrayList<Sequence>();
				break;
			case "markersequence":
			case "cc:mainimagesequence":
			case "cc:mainaudiosequence":
			case "cc:subtitlessequence":
			case "cc:hearingimpairedcaptionssequence":
			case "cc:visuallyimpairedtextsequence":
			case "cc:commentarysequence":
			case "cc:karaokesequence":
				sequence = new Sequence();
				node = ComposistionPlaylistXmlNode.SEQUENCE;
				break;
			//Sequence Child
			case "resourcelist":
				resourceList = new ArrayList<BaseResource>();
				break;
			case "resource":
				String type = attributes.getValue("xsi:type");
				if (type != null) {
					type = type.toLowerCase();
					switch (type) {
						case "trackfileresourcetype":
							resource = new TrackFileResource();
							node = ComposistionPlaylistXmlNode.TRACKFILERESOURCE;
							break;
						case "markerresourcetype":
							resource = new MarkerResource();
							markers = new ArrayList<Marker>();
							((MarkerResource) resource).setMarkers(markers);
							node = ComposistionPlaylistXmlNode.MARKERRESOURCE;
							break;
						case "cc:stereoimagetrackfileresourcetype":
							resource = new StereoImageTrackFileResource();
							node = ComposistionPlaylistXmlNode.STEREOIMAGETRACKFILERESOURCE;
							break;
						case "lefteye":
							leftEye = new TrackFileResource();
							((StereoImageTrackFileResource) resource).setLeftEye(leftEye);
							node = ComposistionPlaylistXmlNode.LEFTEYERESOURCE;
							break;
						case "righteye":
							rightEye = new TrackFileResource();
							((StereoImageTrackFileResource) resource).setLeftEye(rightEye);
							node = ComposistionPlaylistXmlNode.RIGHTEYERESOURCE;
							break;
						default:
							resource = new BaseResource();
							node = ComposistionPlaylistXmlNode.BASERESOURCE;
							break;
					}
				} else {
					resource = new BaseResource();
					node = ComposistionPlaylistXmlNode.BASERESOURCE;
				}
				break;
			case "marker":
				marker = new Marker();
				node = ComposistionPlaylistXmlNode.MARKER;
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
			case "compositionplaylist":
				isCompositionPlaylistInitialized = true;
				break;
				//CompositionPlaylist Child
			case "contentversionlist":
				compositionPlaylist.setContentVersionList(contentVersionList);
				break;
			case "essencedescriptorlist":
				compositionPlaylist.setEssenceDescriptorList(essenceDescriptorList);
				break;
			case "compositiontimecode":
				compositionPlaylist.setCompositionTimecode(compositionTimecode);
				break;
			case "localelist":
				compositionPlaylist.setLocaleList(localeList);
				break;
			case "extensionproperties":
				compositionPlaylist.setExtensionProperties(extensionProperties);
				break;
			case "segmentlist":
				compositionPlaylist.setSegmentList(segmentList);
				break;
			case "essencedescriptor":
				essenceDescriptorList.add(essenceDescriptor);
				break;
			case "locale":
				localeList.add(locale);
				break;
			case "contentversion":
				contentVersionList.add(contentVersion);
				break;
			case "segment":
				segmentList.add(segment);
				break;
			//ExtensionProperties Child
			case "applicationidentification":
				applicationIdentification.setUriList(new ArrayList<String>(Arrays.asList(content.split(" "))));
				extensionProperties.setApplicationIdentification(applicationIdentification);
				break;
			//Locale Child
			case "languagelist":
				locale.setLanguageList(languageList);
				break;
			case "regionlist":
				locale.setRegionList(regionList);
				break;
			case "contentmaturityratinglist":
				locale.setContentMaturityRatingList(contentMaturityRatingList);
				break;
			case "contentmaturityrating":
				contentMaturityRatingList.add(contentMaturityRating);
				break;
			//Segment Child
			case "sequencelist":
				segment.setSequenceList(sequenceList);
				break;
			case "markersequence":
				sequence.setType(SequenceType.MARKERSEQUENCE);
				sequenceList.add(sequence);
				break;
			case "cc:mainimagesequence":
				sequence.setType(SequenceType.MAINIMAGESEQUENCE);
				sequenceList.add(sequence);
				break;
			case "cc:mainaudiosequence":
				sequence.setType(SequenceType.MAINAUDIOSEQUENCE);
				sequenceList.add(sequence);
				break;
			case "cc:subtitlessequence":
				sequence.setType(SequenceType.SUBTILESSEQUENCE);
				sequenceList.add(sequence);
				break;
			case "cc:hearingimpairedcaptionssequence":
				sequence.setType(SequenceType.HEARINGIMPAIREDCAPTIONSSEQUENCE);
				sequenceList.add(sequence);
				break;
			case "cc:visuallyimpairedtextsequence":
				sequence.setType(SequenceType.VISUALLYIMPAIREDTEXTSEQUENCE);
				sequenceList.add(sequence);
				break;
			case "cc:commentarysequence":
				sequence.setType(SequenceType.COMMENTARYSEQUENCE);
				sequenceList.add(sequence);
				break;
			case "cc:karaokesequence":
				sequence.setType(SequenceType.KARAOKESEQUENCE);
				sequenceList.add(sequence);
				break;
			//Sequence Child
			case "resourcelist":
				sequence.setResourceList(resourceList);
				break;
			case "resource":
				resourceList.add(resource);
				break;
			case "marker":
				markers.add(marker);
				break;
			default:
				switch (node) {
					case COMPOSITIONPLAYLIST:
						setAttributesForCompositionPlaylist(qName);
						break;
					case COMPOSITIONTIMECODE:
						setAttributesForCompositionTimecode(qName);
						break;
					case EXTENSIONPROPERTIES:
						//Nothing to do
						break;
					case ESSENCEDESCRIPTOR:
						setAttributesForEssenceDescriptor(qName);
						break;
					case LOCALE:
						setAttributesForLocale(qName);
						break;
					case CONTENTVERSION:
						setAttributesForContentVersion(qName);
						break;
					case SEGMENT:
						setAttributesForSegment(qName);
						break;
					case APPLICATIONIDENTIFICATION:
						//Nothing to do
						break;
					case CONTENTMATURITYRATING:
						setAttributesForContentMaturityRating(qName);
						break;
					case SEQUENCE:
						setAttributesForSequence(qName);
						break;
					case TRACKFILERESOURCE:
						setAttributesForTrackFileResource(qName, (TrackFileResource) resource);
						break;
					case BASERESOURCE:
					case MARKERRESOURCE:
					case STEREOIMAGETRACKFILERESOURCE:
						setAttributesForBaseResource(qName, resource);
						break;
					case LEFTEYERESOURCE:
						setAttributesForTrackFileResource(qName, leftEye);
						break;
					case RIGHTEYERESOURCE:
						setAttributesForTrackFileResource(qName, rightEye);
						break;
					default:
						break;
				}
				break; 
		}
	}
	
	private void setAttributesForCompositionPlaylist(String qName) {
		switch (qName) {
			case "id":
				compositionPlaylist.setUuid(content);
				break;
			case "annotation":
				compositionPlaylist.setAnnotation(content);
				break;
			case "issuedate":
				compositionPlaylist.setIssueDate(content);
				break;
			case "issuer":
				compositionPlaylist.setIssuer(content);
				break;
			case "creator":
				compositionPlaylist.setCreator(content);
				break;
			case "contentoriginator":
				compositionPlaylist.setContentOriginator(content);
				break;
			case "contenttitle":
				compositionPlaylist.setContentTitle(content);
				break;
			case "contentkind":
				contentKind.setValue(content);
				compositionPlaylist.setContentKind(contentKind);
				break;
			case "editrate":
				compositionPlaylist.setEditRate(content);
				break;
			case "totalrunningtime":
				compositionPlaylist.setTotalRunningTime(content);
				break;
			default:
				break;
		}
	}
	
	private void setAttributesForCompositionTimecode(String qName) {
		switch (qName) {
			case "timecodedropframe":
				compositionTimecode.setTimecodeDropFrame(Boolean.valueOf(content));
				break;
			case "timecoderate":
				compositionTimecode.setTimecodeRate(Long.valueOf(content));
				break;
			case "timecodestartaddress":
				compositionTimecode.setTimecodeStartAddress(content);
				break;
			default:
				break;
		}
	}
	
	private void setAttributesForEssenceDescriptor(String qName) {
		switch (qName) {
			case "id":
				essenceDescriptor.setId(content);
				break;
			default:
				break;
		}
	}
	
	private void setAttributesForLocale(String qName) {
		switch (qName) {
			case "annotation":
				locale.setAnnotation(content);
				break;
			default:
				break;	
		}
	}
	
	private void setAttributesForContentVersion(String qName) {
		switch (qName) {
			case "id":
				contentVersion.setId(content);
				break;
			case "labeltext":
				contentVersion.setLabelText(content);
				break;
			default:
				break;	
		}
	}
	
	private void setAttributesForSegment(String qName) {
		switch (qName) {
			case "id":
				segment.setId(content);
				break;
			case "annotation":
				segment.setAnnotation(content);
				break;
			default:
				break;	
		}
	}
	
	private void setAttributesForContentMaturityRating(String qName) {
		switch (qName) {
			case "agency":
				contentMaturityRating.setAgency(content);
				break;
			case "rating":
				contentMaturityRating.setRating(content);
				break;
			case "audience":
				contentMaturityRating.setAudience(content);
				break;
			default:
				break;
		}
	}
	
	private void setAttributesForSequence(String qName) {
		switch (qName) {
			case "id":
				sequence.setId(content);
				break;
			case "trackid":
				sequence.setTrackId(content);
				break;
			default:
				break;
		}
	}
	
	private void setAttributesForTrackFileResource(String qName, TrackFileResource resource) {
		setAttributesForBaseResource(qName, resource);
		switch (qName) {
			case "sourceencoding":
				resource.setSourceEncoding(content);
				break;
			case "trackfileid":
				resource.setTrackFileId(content);
				break;
			case "keyid":
				resource.setKeyId(content);
				break;
			case "hash":
				resource.setHash(content);
				break;
			default:
				break;
		}
	}
	
	private void setAttributesForBaseResource(String qName, BaseResource resource) {
		switch (qName) {
			case "id":
				resource.setId(content);
				break;
			case "annotation":
				resource.setAnnotation(content);
				break;
			case "editrate":
				resource.setEditRate(content);
				break;
			case "intrinsicduration":
				resource.setIntrinsicDuration(Long.valueOf(content));
				break;
			case "entrypoint":
				resource.setEntryPoint(Long.valueOf(content));
				break;
			case "sourceduration":
				resource.setSourceDuration(Long.valueOf(content));
				break;
			case "repeatcount":
				resource.setRepeatCount(Long.valueOf(content));
				break;
			default:
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
	public CompositionPlaylist getCompositionPlaylist() {
		return compositionPlaylist;
	}
}
