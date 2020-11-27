package ru.ineureka.patents.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ineureka.patents.persistence.office.PatentOffice;
import ru.ineureka.patents.persistence.property.PropertyType;
import ru.ineureka.patents.service.office.OfficeService;
import ru.ineureka.patents.service.property.PropertyService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final OfficeService officeService;
    private final PropertyService propertyService;

    public PropertyController(OfficeService officeService, PropertyService propertyService) {
        this.officeService = officeService;
        this.propertyService = propertyService;
    }

    @GetMapping("/offices")
    public List<PatentOffice> getOffices() {
        return officeService.getAllOffices();
    }

    @GetMapping("/types")
    public List<PropertyType> getTypes() {
        return propertyService.getAllTypes();
    }
}
