package ru.ineureka.patents.dto.patent;

import java.util.List;

public final class PropertyCaseDto {
    private final long id;
    private final String case_name;
    private final long import_file_id;
    private final long client_id;
    private final long user_id;
    private final String date_created;
    private List<PropertyCaseRecordDto> records;

    public PropertyCaseDto(long id, String case_name, long import_file_id, long client_id, long user_id, String date_created) {
        this.id = id;
        this.case_name = case_name;
        this.import_file_id = import_file_id;
        this.client_id = client_id;
        this.user_id = user_id;
        this.date_created = date_created;
    }

    public long getId() {
        return this.id;
    }

    public String getCase_name() {
        return this.case_name;
    }

    public long getImport_file_id() {
        return this.import_file_id;
    }

    public long getClient_id() {
        return this.client_id;
    }

    public long getUser_id() {
        return this.user_id;
    }

    public String getDate_created() {
        return this.date_created;
    }

    public List<PropertyCaseRecordDto> getRecords() {
        return this.records;
    }

    public void setRecords(List<PropertyCaseRecordDto> records) {
        this.records = records;
    }

    public String toString() {
        return "PropertyCaseDto(id=" + this.getId() + ", case_name=" + this.getCase_name() + ", import_file_id=" + this.getImport_file_id() + ", client_id=" + this.getClient_id() + ", user_id=" + this.getUser_id() + ", date_created=" + this.getDate_created() + ", records=" + this.getRecords() + ")";
    }
}
