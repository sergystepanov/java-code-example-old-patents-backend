package ru.ineureka.patents.dto.client;

public final class ClientDto {
    private final long id;
    private final String name;
    private final String original_name;
    private final String code;
    private final boolean pct_as_application;
    private final String date_created;
    private final long import_template_id;

    public ClientDto(long id, String name, String original_name, String code, boolean pct_as_application,
                     String date_created, long import_template_id) {
        this.id = id;
        this.name = name;
        this.original_name = original_name;
        this.code = code;
        this.pct_as_application = pct_as_application;
        this.date_created = date_created;
        this.import_template_id = import_template_id;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getOriginal_name() {
        return this.original_name;
    }

    public String getCode() {
        return this.code;
    }

    public String getDate_created() {
        return this.date_created;
    }

    public boolean isPct_as_application() {
        return pct_as_application;
    }

    public long getImport_template_id() {
        return this.import_template_id;
    }

    @Override
    public String toString() {
        return "ClientDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", original_name='" + original_name + '\'' +
                ", code='" + code + '\'' +
                ", pct_as_application=" + pct_as_application +
                ", date_created='" + date_created + '\'' +
                ", import_template_id=" + import_template_id +
                '}';
    }
}
