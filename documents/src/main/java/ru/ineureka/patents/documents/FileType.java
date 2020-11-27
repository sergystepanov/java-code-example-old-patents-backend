package ru.ineureka.patents.documents;

public enum FileType {
    HTML("html"),
    PDF("pdf");

    private final String fileType;

    FileType(String fileType) {
        this.fileType = fileType;
    }

    public String get() {
        return fileType;
    }
}
