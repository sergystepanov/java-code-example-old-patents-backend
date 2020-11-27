package ru.ineureka.patents.service.converter;

import org.springframework.stereotype.Service;
import ru.ineureka.patents.documents.converter.Converter;
import ru.ineureka.patents.documents.converter.ConverterException;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Service
public class HtmlConverterService {
    private final Converter converter;

    public HtmlConverterService(Converter converter) {
        this.converter = converter;
    }

    public void convertHtml(Path input, Path output) throws ConverterException {
        try {
            Files.write(output, Base64.getDecoder().decode(converter.convert(input)));
        } catch (IOException e) {
            throw new ConverterException("Couldn't write the pdf file");
        }
    }

    @PreDestroy
    void onDestroy() {
        converter.shutdown();
    }
}
