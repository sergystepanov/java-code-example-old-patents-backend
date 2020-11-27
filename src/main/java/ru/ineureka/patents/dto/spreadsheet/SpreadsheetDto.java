package ru.ineureka.patents.dto.spreadsheet;

import ru.ineureka.patents.service.cases.client.ClientCaseDto;

public final class SpreadsheetDto {
    private final String fileName;
    private final String checksum;
    private final ClientCaseDto clientCase;

    public SpreadsheetDto(String fileName, String checksum, ClientCaseDto clientCase) {
        this.fileName = fileName;
        this.checksum = checksum;
        this.clientCase = clientCase;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public ClientCaseDto getClientCase() {
        return this.clientCase;
    }

    public String toString() {
        return "SpreadsheetDto(fileName=" + this.getFileName() + ", checksum=" + this.getChecksum() + ", clientCase=" + this.getClientCase() + ")";
    }
}
