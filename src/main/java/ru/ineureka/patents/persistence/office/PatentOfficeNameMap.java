package ru.ineureka.patents.persistence.office;

import java.util.Map;

import static ru.ineureka.patents.office.Office.EAPO;
import static ru.ineureka.patents.office.Office.FIPS;

public final class PatentOfficeNameMap {
    public final static Map<Long, String> values = Map.of(1L, FIPS, 2L, EAPO);
}
