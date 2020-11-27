package ru.ineureka.patents.service;

import org.springframework.stereotype.Service;
import ru.ineureka.patents.reader.excel.ExcelReader;
import ru.ineureka.patents.reader.excel.ExcelTable;

import java.io.InputStream;

@Service
public class Reader {

    private final ExcelReader excelReader;

    public Reader() {
        this.excelReader = new ExcelReader();
    }

    public ExcelTable read(InputStream stream) {
        return excelReader.read(stream);
    }
}
