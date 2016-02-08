package com.sferalabs.imf.validation;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.netflix.imflibrary.IMFConstraints;
import com.netflix.imflibrary.IMFErrorLogger;
import com.netflix.imflibrary.IMFErrorLoggerImpl;
import com.netflix.imflibrary.KLVPacket;
import com.netflix.imflibrary.MXFOperationalPattern1A;
import com.netflix.imflibrary.exceptions.MXFException;
import com.netflix.imflibrary.st0377.HeaderPartition;
import com.netflix.imflibrary.st0377.PartitionPack;
import com.netflix.imflibrary.st0377.RandomIndexPack;
import com.netflix.imflibrary.st0377.header.EssenceContainerData;
import com.netflix.imflibrary.utils.ResourceByteRangeProvider;
import com.sferalabs.imf.util.MXFFileDataProvider;

public class MXFValidation {

	private final File workingDirectory;
	private final ResourceByteRangeProvider resourceByteRangeProvider;
	private RandomIndexPack randomIndexPack;
	private List<PartitionPack> partitionPacks;
	private List<PartitionPack> referencedPartitionPacks;
	private IMFConstraints.HeaderPartitionIMF headerPartition;
	private IMFErrorLogger imfErrorLogger;

	/**
	 * Lazily creates a model instance corresponding to a st2067-5 compliant MXF file
	 * @param workingDirectory the working directory
	 * @param resourceByteRangeProvider the MXF file represented as a {@link imflibrary.utils.ResourceByteRangeProvider}
	 */
	public MXFValidation(File workingDirectory, ResourceByteRangeProvider resourceByteRangeProvider)
	{
		this.workingDirectory = workingDirectory;
		this.resourceByteRangeProvider = resourceByteRangeProvider;
		this.imfErrorLogger = new IMFErrorLoggerImpl();
	}

    private IMFConstraints.HeaderPartitionIMF getHeaderPartitionIMF(IMFErrorLogger imfErrorLogger) throws IOException
    {
        if (this.headerPartition == null)
        {
            RandomIndexPack randomIndexPack = getRandomIndexPack();
            List<Long> allPartitionByteOffsets = randomIndexPack.getAllPartitionByteOffsets();
            setHeaderPartitionIMF(allPartitionByteOffsets.get(0), allPartitionByteOffsets.get(1) - 1, imfErrorLogger);
        }

        return this.headerPartition;
    }

    
    private HeaderPartition getHeaderPartition(IMFErrorLogger imfErrorLogger) throws IOException
    {
        IMFConstraints.HeaderPartitionIMF headerPartitionIMF = getHeaderPartitionIMF(imfErrorLogger);
        return headerPartitionIMF.getHeaderPartitionOP1A().getHeaderPartition();
    }

    private void setHeaderPartitionIMF(long inclusiveRangeStart, long inclusiveRangeEnd, IMFErrorLogger imfErrorLogger) throws IOException
    {
        File fileWithHeaderPartition = this.resourceByteRangeProvider.getByteRange(inclusiveRangeStart, inclusiveRangeEnd, this.workingDirectory);
        MXFFileDataProvider byteProvider = this.getByteProvider(fileWithHeaderPartition);
        try {
        	HeaderPartition headerPartition = new HeaderPartition(byteProvider, inclusiveRangeStart, inclusiveRangeEnd - inclusiveRangeStart + 1, imfErrorLogger);
        	//validate header partition
        	MXFOperationalPattern1A.HeaderPartitionOP1A headerPartitionOP1A = MXFOperationalPattern1A.checkOperationalPattern1ACompliance(headerPartition);
        	IMFConstraints.HeaderPartitionIMF headerPartitionIMF = IMFConstraints.checkIMFCompliance(headerPartitionOP1A);

        	//add reference to header partition object
        	this.headerPartition = headerPartitionIMF;
        } finally {
        	byteProvider.close();
        }
    }

    
    private List<PartitionPack> getReferencedPartitionPacks(IMFErrorLogger imfErrorLogger) throws IOException
    {
        if (this.referencedPartitionPacks == null)
        {
            setReferencedPartitionPacks(imfErrorLogger);
        }

        return this.referencedPartitionPacks;
    }

    
    private void setReferencedPartitionPacks(IMFErrorLogger imfErrorLogger) throws IOException
    {
        List<PartitionPack> allPartitionPacks = getPartitionPacks();
        HeaderPartition headerPartition = getHeaderPartition(imfErrorLogger);

        Set<Long> indexSIDs = new HashSet<>();
        Set<Long> bodySIDs = new HashSet<>();

        for (EssenceContainerData essenceContainerData : headerPartition.getPreface().getContentStorage().getEssenceContainerDataList())
        {
            indexSIDs.add(essenceContainerData.getIndexSID());
            bodySIDs.add(essenceContainerData.getBodySID());
        }

        List<PartitionPack> referencedPartitionPacks = new ArrayList<>();
        for (PartitionPack partitionPack : allPartitionPacks)
        {
            if (partitionPack.hasEssenceContainer())
            {
                if (bodySIDs.contains(partitionPack.getBodySID()))
                {
                    referencedPartitionPacks.add(partitionPack);
                }
            }
            else if (partitionPack.hasIndexTableSegments())
            {
                if (indexSIDs.contains(partitionPack.getIndexSID()))
                {
                    referencedPartitionPacks.add(partitionPack);
                }
            }
            else if(partitionPack.getPartitionPackType() == PartitionPack.PartitionPackType.HeaderPartitionPack
                    || partitionPack.getPartitionPackType() == PartitionPack.PartitionPackType.FooterPartitionPack)
            {//Either of these partitions are important although they might not contain EssenceContainer or IndexTable data
                referencedPartitionPacks.add(partitionPack);
            }
        }

        this.referencedPartitionPacks = referencedPartitionPacks;
    }

    private List<PartitionPack> getPartitionPacks() throws IOException
    {
        if (this.partitionPacks == null)
        {
            setPartitionPacks();
        }
        return Collections.unmodifiableList(this.partitionPacks);

    }

    private void setPartitionPacks() throws IOException
    {
        RandomIndexPack randomIndexPack = getRandomIndexPack();

        List<PartitionPack> partitionPacks = new ArrayList<>();
        List<Long> allPartitionByteOffsets = randomIndexPack.getAllPartitionByteOffsets();
        for (long offset : allPartitionByteOffsets)
        {
            partitionPacks.add(getPartitionPack(offset));
        }

        //validate partition packs
        MXFOperationalPattern1A.checkOperationalPattern1ACompliance(partitionPacks);
        IMFConstraints.checkIMFCompliance(partitionPacks);

        //add reference to list of partition packs
        this.partitionPacks  = Collections.unmodifiableList(partitionPacks);
    }
    
    private PartitionPack getPartitionPack(long resourceOffset) throws IOException
    {
        long archiveFileSize = this.resourceByteRangeProvider.getResourceSize();
        KLVPacket.Header header;
        {//logic to provide as an input stream the portion of the archive that contains PartitionPack KLVPacker Header
            long rangeStart = resourceOffset;
            long rangeEnd = resourceOffset +
                    (KLVPacket.KEY_FIELD_SIZE + KLVPacket.LENGTH_FIELD_SUFFIX_MAX_SIZE) -1;
            rangeEnd = rangeEnd < (archiveFileSize - 1) ? rangeEnd : (archiveFileSize - 1);

            File fileWithPartitionPackKLVPacketHeader = this.resourceByteRangeProvider.getByteRange(rangeStart, rangeEnd, this.workingDirectory);
            MXFFileDataProvider byteProvider = this.getByteProvider(fileWithPartitionPackKLVPacketHeader);
            try {
            	header = new KLVPacket.Header(byteProvider, resourceOffset);
            } finally {
            	byteProvider.close();
            }
        }

        PartitionPack partitionPack;
        {//logic to provide as an input stream the portion of the archive that contains a PartitionPack and next KLV header

            long rangeStart = resourceOffset;
            long rangeEnd = resourceOffset +
                    (KLVPacket.KEY_FIELD_SIZE + header.getLSize() + header.getVSize()) +
                    (KLVPacket.KEY_FIELD_SIZE + KLVPacket.LENGTH_FIELD_SUFFIX_MAX_SIZE) +
                    -1;
            rangeEnd = rangeEnd < (archiveFileSize - 1) ? rangeEnd : (archiveFileSize - 1);

            File fileWithPartitionPack = this.resourceByteRangeProvider.getByteRange(rangeStart, rangeEnd, this.workingDirectory);
            MXFFileDataProvider byteProvider = this.getByteProvider(fileWithPartitionPack);
            try {
            	partitionPack = new PartitionPack(byteProvider, resourceOffset, true);
            } finally {
            	byteProvider.close();
            }

        }

        return partitionPack;
    }

    /**
     * Returns a model instance corresponding to the RandomIndexPack section of the MXF file
     * @return a {@link imflibrary.st0377.RandomIndexPack} representation of the random index pack section
     * @throws IOException
     */
    public RandomIndexPack getRandomIndexPack() throws IOException
    {
        if (this.randomIndexPack == null)
        {
            setRandomIndexPack();
        }
        return this.randomIndexPack;
    }

    private void setRandomIndexPack() throws IOException
    {

        long archiveFileSize = this.resourceByteRangeProvider.getResourceSize();
        long randomIndexPackSize;
        {//logic to provide as an input stream the portion of the archive that contains randomIndexPack size
            long rangeEnd = archiveFileSize - 1;
            long rangeStart = archiveFileSize - 4;

            File fileWithRandomIndexPackSize = this.resourceByteRangeProvider.getByteRange(rangeStart, rangeEnd, this.workingDirectory);
            byte[] bytes = Files.readAllBytes(Paths.get(fileWithRandomIndexPackSize.toURI()));
            randomIndexPackSize = (long)(ByteBuffer.wrap(bytes).getInt());
            if (randomIndexPackSize <= 0)
            {
                throw new MXFException(String.format("randomIndexPackSize = %d obtained from last 4 bytes of the MXF file is negative, implying that this file does not contain a RandomIndexPack",
                        randomIndexPackSize, archiveFileSize));
            }
        }

        RandomIndexPack randomIndexPack;
        {//logic to provide as an input stream the portion of the archive that contains randomIndexPack
            long rangeEnd = archiveFileSize - 1;
            long rangeStart = archiveFileSize - randomIndexPackSize;
            if (rangeStart < 0)
            {
                throw new MXFException(String.format("randomIndexPackSize = %d obtained from last 4 bytes of the MXF file is larger than archiveFile size = %d, implying that this file does not contain a RandomIndexPack",
                        randomIndexPackSize, archiveFileSize));
            }

            File fileWithRandomIndexPack = this.resourceByteRangeProvider.getByteRange(rangeStart, rangeEnd, this.workingDirectory);
            MXFFileDataProvider byteProvider = this.getByteProvider(fileWithRandomIndexPack);
            try {
            	randomIndexPack = new RandomIndexPack(byteProvider, rangeStart, randomIndexPackSize);
            } finally {
            	byteProvider.close();
            }
        }

        this.randomIndexPack =  randomIndexPack;

    }

    private MXFFileDataProvider getByteProvider(File file) throws IOException {
    	MXFFileDataProvider byteProvider;
        Long size = file.length();
        if(size <= 0){
            throw new IOException(String.format("Range of bytes (%d) has to be +ve and non-zero", size));
        }
        byteProvider = new MXFFileDataProvider(file);
        return byteProvider;
    }

    /**
     * Parse the MFX File and perform validation
     * @throws MXFException if there's any error
     */
	public void parse() throws MXFException
	{
		try {
			getRandomIndexPack();
			getPartitionPacks();
			getReferencedPartitionPacks(imfErrorLogger);
			getHeaderPartitionIMF(imfErrorLogger);
		} catch(IOException e) {
			throw new MXFException(e);
		}
	}
}
