package ru.ineureka.patents.documents.converter;

import java.nio.file.Path;

public interface Converter {

    /**
     * Returns converted file as Base64-encoded string.
     *
     * @param input A file to convert.
     * @throws ConverterException In case of recoverable exceptions.
     */
    String convert(Path input) throws ConverterException;

    /**
     * Additional step for converters running in the background.
     */
    void shutdown();
}
