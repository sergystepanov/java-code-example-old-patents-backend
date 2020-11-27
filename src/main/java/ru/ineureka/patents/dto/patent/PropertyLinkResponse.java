package ru.ineureka.patents.dto.patent;

public final class PropertyLinkResponse {

    private final String link;

    public PropertyLinkResponse(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
