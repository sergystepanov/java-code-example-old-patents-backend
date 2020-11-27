package ru.ineureka.patents.persistence.client;

import javax.persistence.*;

@Entity(name = "CLIENT_IMPORT_TEMPLATE")
public class ClientImportTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "field_map")
    private String fieldMap;

    @Column(name = "patent_type_map")
    private String patentTypeMap;

    protected ClientImportTemplate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(String fieldMap) {
        this.fieldMap = fieldMap;
    }

    public String getPatentTypeMap() {
        return patentTypeMap;
    }

    public void setPatentTypeMap(String patentTypeMap) {
        this.patentTypeMap = patentTypeMap;
    }
}
