package ru.ineureka.patents.office.mapper;

import ru.ineureka.patents.office.dto.PatentExtendedDto;

public interface PropertyMapper {
    PatentExtendedDto map(Object root);
}
