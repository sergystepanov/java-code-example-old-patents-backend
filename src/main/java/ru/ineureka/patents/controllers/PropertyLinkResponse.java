package ru.ineureka.patents.controllers;

public final class PropertyLinkResponse {
    private final String link;

    public PropertyLinkResponse(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }
}
