package ru.ineureka.patents.service.office.rupto.maintenance.document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public abstract class MaintenanceFeeDocument {
    private URL template;

    protected MaintenanceFeeDocument() {
    }

    public MaintenanceFeeDocument withTemplate(URL template) {
        this.template = template;
        return this;
    }

    public InputStream getTemplate() throws IOException {
        if (Objects.isNull(template)) return null;

        return template.openStream();
    }

    public URL getTemplateUrl() {
        return template;
    }

    /**
     * Returns MIME type of the document if it's a file.
     */
    public abstract String getTemplateMimeType();
}
