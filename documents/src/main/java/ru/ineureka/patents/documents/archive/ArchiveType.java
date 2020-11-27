package ru.ineureka.patents.documents.archive;

public enum ArchiveType {
    ZIP, GZ;

    public String toExtension() {
        return '.' + this.name().toLowerCase();
    }
}
