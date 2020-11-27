package ru.ineureka.patents.documents.generator.office;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface DocumentGenerator {
    void generate(InputStream templateStream, OutputStream dest, Map<String, String> what) throws GeneratorException;
}
