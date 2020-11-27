package ru.ineureka.patents.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ineureka.patents.documents.converter.Converter;
import ru.ineureka.patents.documents.converter.HtmlToPdfConverter;

@Configuration
public class ConverterConfig {

    @Bean
    public Converter converter() {
        return new HtmlToPdfConverter();
    }
}
