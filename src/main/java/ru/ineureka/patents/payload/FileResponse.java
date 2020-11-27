package ru.ineureka.patents.payload;

public class FileResponse {
    private final String file;

    public FileResponse(String file) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }
}
