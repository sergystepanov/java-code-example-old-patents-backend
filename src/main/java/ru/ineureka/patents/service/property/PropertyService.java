package ru.ineureka.patents.service.property;

import org.springframework.stereotype.Service;
import ru.ineureka.patents.persistence.property.Property;
import ru.ineureka.patents.persistence.property.PropertyRepository;
import ru.ineureka.patents.persistence.property.PropertyType;
import ru.ineureka.patents.persistence.property.PropertyTypeRepository;

import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    public PropertyService(PropertyRepository propertyRepository, PropertyTypeRepository propertyTypeRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyTypeRepository = propertyTypeRepository;
    }

    public List<PropertyType> getAllTypes() {
        return propertyTypeRepository.getAllBy();
    }

    /**
     * Checks if a property has an open licence flag by quering the database.
     *
     * @param office    The ID value of property's office in the database.
     * @param type      The ID value of property's type in the database.
     * @param appNumber The value of property's application number.
     * @return The value "1" if the property has open license and "0" if it has not.
     * @since 3.0.0
     */
    public String isOpenLicensed(long office, long type, String appNumber) {
        return propertyRepository.findByPatentOfficeAndTypeAndApplicationNumber(office, type, appNumber)
                .filter(Property::getOpenLicense)
                .map(value -> "1")
                .orElse("0");
    }
}
