package com.data_management;

import java.io.IOException;
import java.io.InputStream;

public interface DataReader {
    /**
     * Reads data from a stream and stores it in the data storage.
     * 
     * @param inputStream the stream containing patient data
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(InputStream inputStream, DataStorage dataStorage) throws IOException;
}
