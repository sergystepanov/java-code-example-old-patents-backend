package ru.ineureka.patents.dto.patent;

import java.util.List;
import java.util.Objects;

public final class PropertyOwnerDto {
    private final long id;
    private final String name;
    private final String entity_status;
    private List<Long> used_in;

    public PropertyOwnerDto(long id, String name, String entity_status, List<Long> used_in) {
        this.id = id;
        this.name = name;
        this.entity_status = entity_status;
        this.used_in = used_in;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEntity_status() {
        return this.entity_status;
    }

    public List<Long> getUsed_in() {
        return this.used_in;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyOwnerDto that = (PropertyOwnerDto) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(entity_status, that.entity_status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, entity_status);
    }
}
