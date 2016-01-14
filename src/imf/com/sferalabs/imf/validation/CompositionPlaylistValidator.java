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
package com.sferalabs.imf.validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.netflix.imflibrary.exceptions.MXFException;
import com.netflix.imflibrary.utils.FileByteRangeProvider;
import com.netflix.imflibrary.utils.ResourceByteRangeProvider;
import com.sferalabs.imf.exception.ImfXmlException;
import com.sferalabs.imf.model.assetmap.Asset;
import com.sferalabs.imf.model.assetmap.AssetMap;
import com.sferalabs.imf.model.compositionplaylist.BaseResource;
import com.sferalabs.imf.model.compositionplaylist.CompositionPlaylist;
import com.sferalabs.imf.model.compositionplaylist.ContentKind;
import com.sferalabs.imf.model.compositionplaylist.Segment;
import com.sferalabs.imf.model.compositionplaylist.Sequence;
import com.sferalabs.imf.model.compositionplaylist.TrackFileResource;
import com.sferalabs.imf.model.packinglist.PackingAsset;
import com.sferalabs.imf.model.packinglist.PackingList;
import com.sferalabs.imf.saxhandler.SaxCompositionPlaylistHandler;
import com.sferalabs.imf.util.ImfHelper;
import com.sferalabs.imf.util.ImfLogger;
import com.sferalabs.imf.xsd.ImfXsds;

/**
 * CompositionPlaylistValidator class
 */
public class CompositionPlaylistValidator extends XmlResourceValidator {

	private static class Messages {
		public static final String ASSET_MAP_UNSET_MSG = "AssetMap was unset for CompositionPlaylist";
		public static final String VALIDATING_UUID_MSG =  "Validating uniqueness of uuid elements";
		public static final String VALIDATING_CPL_UUID_MSG = "Validating existence of CPL uuid in %s";
		public static final String CPL_ID_NOT_FOUND_FMT = "CPL Id %s is not found in %s";
		public static final String CPL_ID_FOUND_FMT = "CPL Id %s is found in %s";
		public static final String VALIDATING_SEQUENCE_UUID_FMT = "Validating uniqueness of Sequence Id %s in a segment";
		public static final String SEQUENCE_NOT_UNIQUE_UUID_FMT = "Sequence Id %s is not unique in a segment";
		public static final String VALIDATING_TRACK_ID_FMT = "Validating uniqueness of Sequence Track Id %s in a segment";
		public static final String TRACK_NOT_UNIQUE_UUID_FMT = "Sequence TrackId %s is not unique in a segment";
		public static final String VALIDATING_CONTENTKING_MSG = "Validating ContentKind";
		public static final String INVALID_CONTENTKIND_FMT = "ContentKind value %s is invalid. The value should be "
				+ "advertisement, feature, psa, rating, short, teaser, test, trailer, transitional, episode, highlights, or event";
		public static final String VALID_CONTENTKIND_FMT = "ContentKind value %s is valid";
		public static final String VALIDATING_SOURCEDURARION_FMT = "Validating SourceDuration of resource %s";
		public static final String INVALID_INTRINSIC_DURATION_FMT = "Resource %s has invalid intrinsic duration value. "
				+ "Intrinsic duration value should be larger than entry point value";
		public static final String NEGATIVE_DURATION_FMT = "Resource %s has negative source duration value";
		public static final String INVALID_DURATION_FMT = "Resource %s has invalid source duration value. "
				+ "Source duration value should be between 0 and IntrinsicDuration - EntryPoint";
		public static final String VALIDATING_TRACK_FILE_FMT = "Validating Track File %s existence in AssetMap";
		public static final String TRACK_FILE_NOT_FOUND_FMT = "Track File %s is not found in %s";
		public static final String TRACK_FILE_FOUND_FMT = "Track File %s is found in %s";
		public static final String VALIDATING_MEDIA_TYPE_FMT = "Validating Track File %s media type";
		public static final String TRACK_FILE_MEDIA_TYPE_FMT = "Track File %s 's media type is %s";
		public static final String TRACK_INVALID_MIME_FMT = "Track File %s has invalid media type (should be application/mxf)";
		public static final String VALIDATING_MXF_FILE_FMT = "Validating MXF File %s format";
		public static final String VALID_MXF_FILE_FMT = "MXF File %s format is valid";
		public static final String INVALID_MXF_FILE_FMT = "MXF File %s format is invalid";
		public static final String VALIDATING_ESSENCE_EXIST = "Validating essence %s exist in IMF package";
		public static final String REFERRENCED_ESSENCE_EXIST = "Essence %s exists in IMF package";
		public static final String REFERRENCED_ESSENCE_NOT_EXIST = "Essence %s does not exist in IMF package";
		public static final String REPEAT_COUNT_VALUE = "RepeatCount is %s";
		public static final String ABSENT_REPEAT_COUNT_VALUE = "RepeatCount is 1 because RepeatCount element is absent";
	}
	private CompositionPlaylist compositionPlaylist;
	private AssetMap assetMap;
	private PackingList packingList;

	public static final String SMPTE_CONTENTKING_URL = "http://www.smpte-ra.org/schemas/2067-3/2013#content-kind";
	
	public CompositionPlaylistValidator(String path) {
		super(path);
		resourcePath = ImfXsds.CORE_CONSTRAINTS_XSD;
	}

	@Override
	public ValidationResult parse() throws IOException, ParserConfigurationException, SAXException {
		ValidationResult result = validateXsd();
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		SaxCompositionPlaylistHandler handler = new SaxCompositionPlaylistHandler();
		parser.parse(new FileInputStream(filePath), handler);
		compositionPlaylist = handler.getCompositionPlaylist();
		return result;
	}

	@Override
	public ValidationResultList validate() throws ImfXmlException {
		if (assetMap == null) {
			throw new ImfXmlException(Messages.ASSET_MAP_UNSET_MSG);
		}

		//check if uuid is unique
		ImfLogger.getLogger().logDebug(Messages.VALIDATING_UUID_MSG);
		ValidationResultList results = ImfHelper.getInstance().checkUuidsUnique(compositionPlaylist.getAllUuids());
		
		//check if cpl uuid is in assetmap
		ImfLogger.getLogger().logDebug(String.format(Messages.VALIDATING_CPL_UUID_MSG, "AssetMap"));
		if (!assetMap.getAllUuids().contains(compositionPlaylist.getUuid())) {
			String msg = String.format(
					Messages.CPL_ID_NOT_FOUND_FMT, compositionPlaylist.getUuid(), "AssetMap");
			results.add(new ValidationResult(true, msg));
			ImfLogger.getLogger().logError(msg);
		} else {
			ImfLogger.getLogger().logDebug(String.format(
					Messages.CPL_ID_FOUND_FMT, compositionPlaylist.getUuid(), "AssetMap"));
		}
		
		//check if cpl uuid is in packinglist
		if (packingList != null) {
			ImfLogger.getLogger().logDebug(String.format(Messages.VALIDATING_CPL_UUID_MSG, "PackageList"));
			if (!packingList.getAllUuids().contains(compositionPlaylist.getUuid())) {
				String msg = String.format(Messages.CPL_ID_NOT_FOUND_FMT, compositionPlaylist.getUuid(), "PackageList");
				results.add(new ValidationResult(true, msg));
				ImfLogger.getLogger().logError(msg);
			} else {
				ImfLogger.getLogger().logDebug(String.format(
						Messages.CPL_ID_FOUND_FMT, compositionPlaylist.getUuid(), "PackingList"));
			}
		}
		
		//validate ContentKind value
		ValidationResult contentKindValidationResult = validateContentKind();
		if(contentKindValidationResult.hasError()){
			results.add(contentKindValidationResult);
		}
		
		List<Segment> segments = compositionPlaylist.getSegmentList();
		
		if (segments != null) {
			for(Segment segment : segments) {
				List<Sequence> sequences = segment.getSequenceList();
				//validate sequences
				ValidationResultList sequenceValidationResult = validateExtensionSequence(sequences);
				results.addAll(sequenceValidationResult);

				if (sequences != null) {
					for(Sequence sequence : sequences) {
						List<BaseResource> resources = sequence.getResourceList();
						if (resources != null) {
							for (BaseResource resource : resources) {
								//validate SourceDuration
								ValidationResult durationValidation= validateSourceDuration(resource);
								if(durationValidation.hasError()){
									results.add(durationValidation);
								}
								//validate TrackFileResource
								if (resource instanceof TrackFileResource) {
									ValidationResult resourceValidation = validateTrackFileResource((TrackFileResource) resource);
									if(resourceValidation.hasError()){
										results.add(resourceValidation);
									} else {
										//validate Track file have valid MFX format
										String trackFileId = ((TrackFileResource) resource).getTrackFileId();
										ImfLogger.getLogger().logDebug(String.format(Messages.VALIDATING_ESSENCE_EXIST,  trackFileId));
										String filePath = assetMap.getAssetResourcePath(trackFileId);
										//validate that essence referenced by CPL exists
										if (filePath != null) {
											ImfLogger.getLogger().logDebug(String.format(Messages.REFERRENCED_ESSENCE_EXIST,  trackFileId));
											String fileName = assetMap.getAssetByUuid(trackFileId).getChunkList().get(0).getPath();
											ValidationResult mfxValidation = validateMFXResourceFile(fileName, filePath);
											if (mfxValidation.hasError()) {
												results.add(mfxValidation);
											}
										} else {
											String msg = String.format(Messages.REFERRENCED_ESSENCE_NOT_EXIST, trackFileId);
											results.add(new ValidationResult(true, msg));
											ImfLogger.getLogger().logError(msg);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return results;
	}

	private ValidationResultList validateExtensionSequence(List<Sequence> sequences) throws ImfXmlException {
		//validate uniqueness of Id and trackId
		ValidationResultList results = new ValidationResultList();
		for (int i = 0; i < sequences.size(); i++) {
			Sequence seq = sequences.get(i);
			for (int j = i+1; j < sequences.size(); j++) {
				Sequence other_seq = sequences.get(j);
				ImfLogger.getLogger().logDebug(String.format(Messages.VALIDATING_SEQUENCE_UUID_FMT,  seq.getId()));
				if (other_seq.getId().equals(seq.getId())) {
					String msg = String.format(Messages.SEQUENCE_NOT_UNIQUE_UUID_FMT, seq.getId());
					results.add(new ValidationResult(true, msg));
					ImfLogger.getLogger().logError(msg);
				} else {
					ImfLogger.getLogger().logDebug(String.format(Messages.VALIDATING_TRACK_ID_FMT,  seq.getTrackId()));
				}
				
				if (other_seq.getTrackId().equals(seq.getTrackId())) {
					String msg = String.format(Messages.TRACK_NOT_UNIQUE_UUID_FMT, seq.getTrackId());
					results.add(new ValidationResult(true, msg));
					ImfLogger.getLogger().logError(msg);
				}
			}
		}
		return  results;
	}
	
	private ValidationResult validateContentKind() {
		ValidationResult result = new ValidationResult();
		ImfLogger.getLogger().logDebug(Messages.VALIDATING_CONTENTKING_MSG);
		ContentKind contentKind = compositionPlaylist.getContentKind();
		if (contentKind != null) {
			if ((contentKind.getScope() == null) ||
					(contentKind.getScope().equals(SMPTE_CONTENTKING_URL))) {
				String value = contentKind.getValue().toLowerCase();
				if (! ImfHelper.getInstance().isContentKindPermitted(value)) {
					String msg = String.format(Messages.INVALID_CONTENTKIND_FMT, value);
					result.setMessage(msg);
					result.setHasError(true);
					ImfLogger.getLogger().logWarning(msg);
				} else {
					String msg = String.format(Messages.VALID_CONTENTKIND_FMT, value);
					result.setMessage(msg);
					ImfLogger.getLogger().logDebug(msg);
				}
			}
		}
		return  result;
	}
	
	private ValidationResult validateMFXResourceFile(String fileName, String filePath) {
		ValidationResult result = new ValidationResult();
		File inputFile = new File(filePath);
		File workingDirectory = null;
		ImfLogger.getLogger().logDebug(String.format(
				Messages.VALIDATING_MXF_FILE_FMT, fileName));
		try {
			workingDirectory = Files.createTempDirectory("imf_").toFile();
			ResourceByteRangeProvider resourceByteRangeProvider = new FileByteRangeProvider(inputFile);
			MXFValidation imfEssenceComponentReader = new MXFValidation(workingDirectory, resourceByteRangeProvider);
	        imfEssenceComponentReader.parse();
	        ImfLogger.getLogger().logDebug(String.format(
					Messages.VALID_MXF_FILE_FMT, fileName));
		} catch (MXFException e) {
			ImfLogger.getLogger().logError(String.format(
						Messages.INVALID_MXF_FILE_FMT, fileName));
			result.setHasError(true);
			result.setMessage(e.getMessage());
			ImfLogger.getLogger().logError(e.getMessage());
		} catch (IOException e) {
			ImfLogger.getLogger().logError(String.format(
					Messages.INVALID_MXF_FILE_FMT, fileName));
			result.setHasError(true);
			result.setMessage(e.getMessage());
			ImfLogger.getLogger().logError(e.getMessage());
		}
		if (workingDirectory != null) {
			try {
				Path path = FileSystems.getDefault().getPath(workingDirectory.toPath().toString(), "range");
				Files.deleteIfExists(path);
				Files.deleteIfExists(workingDirectory.toPath());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		return result;
	}

	private ValidationResult validateSourceDuration(BaseResource res) {
		ValidationResult result = new ValidationResult();

		ImfLogger.getLogger().logDebug(String.format(Messages.VALIDATING_SOURCEDURARION_FMT, res.getId()));
		long intrinsicDuration = res.getIntrinsicDuration();
		long entryPoint = res.getEntryPoint();
		long sourceDuration = res.getSourceDuration();
		
		if (intrinsicDuration < entryPoint) {
			String msg = String.format(Messages.INVALID_INTRINSIC_DURATION_FMT, res.getId());
			result.setHasError(true);
			result.setMessage(msg);
			ImfLogger.getLogger().logWarning(msg);
		}
		
		if (sourceDuration < 0) {
			String msg = String.format(Messages.NEGATIVE_DURATION_FMT, res.getId());
			result.setHasError(true);
			result.setMessage(msg);
			ImfLogger.getLogger().logWarning(msg);
		}
		
		if (sourceDuration > (intrinsicDuration - entryPoint)) {
			String msg = String.format(Messages.INVALID_DURATION_FMT, res.getId());
			result.setHasError(true);
			result.setMessage(msg);
			ImfLogger.getLogger().logWarning(msg);
		}
		return result;
	}

	private ValidationResult validateTrackFileResource(TrackFileResource res) throws ImfXmlException {
		ValidationResult result = new ValidationResult();
		String trackFileId = res.getTrackFileId();
		ImfLogger.getLogger().logDebug(String.format(Messages.VALIDATING_TRACK_FILE_FMT, trackFileId));
		//validate that trackFileId exists in AssetMap
		Asset asset = assetMap.getAssetByUuid(trackFileId);
		if (asset == null) {
			String msg = String.format(Messages.TRACK_FILE_NOT_FOUND_FMT, trackFileId, "AssetMap");
			result.setHasError(true);
			result.setMessage(msg);
			ImfLogger.getLogger().logError(msg);
			return result;
		} else {
			ImfLogger.getLogger().logDebug(String.format(Messages.TRACK_FILE_FOUND_FMT, trackFileId, "AssetMap"));
		}
		
		//validate that Track file shall be application/mfx
		if (packingList != null) {
			ImfLogger.getLogger().logDebug(String.format(Messages.VALIDATING_MEDIA_TYPE_FMT, trackFileId));
			PackingAsset packingAsset = packingList.getPackingAssetByUuid(trackFileId);
			if (packingAsset == null) {
				String msg = String.format(Messages.TRACK_FILE_NOT_FOUND_FMT, trackFileId, "PKL File");
				result.setHasError(true);
				result.setMessage(msg);
				ImfLogger.getLogger().logError(msg);
				return result;
			}
		
			String mimeType = packingAsset.getType();
			ImfLogger.getLogger().logDebug(String.format(Messages.TRACK_FILE_MEDIA_TYPE_FMT, trackFileId, mimeType));
			if (!mimeType.equalsIgnoreCase("application/mxf")) {
				String msg = String.format(Messages.TRACK_INVALID_MIME_FMT, trackFileId);
				result.setHasError(true);
				result.setMessage(msg);
				ImfLogger.getLogger().logWarning(msg);
			}
		}
		
		//validate repeatCount
		if (res.getRepeatCount() > 0) {
			ImfLogger.getLogger().logDebug(String.format(Messages.REPEAT_COUNT_VALUE, res.getRepeatCount()));
		} else {
			ImfLogger.getLogger().logDebug(String.format(Messages.ABSENT_REPEAT_COUNT_VALUE));
		}
		
		return  result;
	}

	/**
	 * @return compositionPlaylist
	 */
	public CompositionPlaylist getCompositionPlaylist() {
		return compositionPlaylist;
	}
	
	/**
	 * Set assetMap
	 * @param assetMap
	 */
	public void setAssetMap(AssetMap assetMap) {
		this.assetMap = assetMap;
	}
	
	/**
	 * Set packingList
	 * @param packingList
	 */
	public void setPackingList(PackingList packingList) {
		this.packingList = packingList;
	}
}
