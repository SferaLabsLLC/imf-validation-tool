package com.sferalabs.imf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.netflix.imflibrary.utils.ByteProvider;

public class MXFFileDataProvider implements ByteProvider {

    private final InputStream inputStream;

    /**
     * Instantiates a new MXF InputStream data provider.
     *
     * @param file the input file
     */
    public MXFFileDataProvider(File file) throws IOException
    {
        this.inputStream = new FileInputStream(file);
    }

    /**
     * Get bytes.
     *
     * @param totalNumBytesToRead the total num bytes to read
     * @return byte[] containing next totalNumBytesToRead
     * @throws java.io.IOException the iO exception
     */
    public synchronized byte[] getBytes(int totalNumBytesToRead) throws IOException
    {
        if(totalNumBytesToRead < 0){
            throw new IOException(String.format("Cannot read %d bytes, should be non-negative and non-zero", totalNumBytesToRead));
        }
        byte[] bytes = new byte[totalNumBytesToRead];
        Integer bytesRead = 0;
        Integer totalBytesRead = 0;
        while(bytesRead != -1
                && bytesRead < totalNumBytesToRead) {
            bytesRead = this.inputStream.read(bytes, totalBytesRead, totalNumBytesToRead);
            if(bytesRead != -1){
                totalBytesRead += bytesRead;
                totalNumBytesToRead -= bytesRead;
            }
        }
        return bytes;
    }

    /**
     * Skip bytes.
     *
     * @param totalNumBytesToSkip the total num bytes to skip
     * @throws java.io.IOException the iO exception
     */
    public synchronized void skipBytes(long totalNumBytesToSkip) throws IOException
    {
        long bytesSkipped = this.inputStream.skip(totalNumBytesToSkip);
        if(bytesSkipped != totalNumBytesToSkip){
            throw new IOException(String.format("Could not skip %d bytes of data, possible truncated data", totalNumBytesToSkip));
        }
    }
    
    public void close() throws IOException {
    	inputStream.close();
    }
}
