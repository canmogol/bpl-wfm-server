package com.bplsoft.wfm.service;


import com.bplsoft.common.property.Property;
import com.bplsoft.wfm.repository.LocationQueryRepository;
import com.bplsoft.wfm.model.LocationModel;
import com.bplsoft.wfm.service.model.LocationResponseModel;
import ma.glasnost.orika.MapperFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

public class LocationService {

    private final static Logger logger = LoggerFactory.getLogger(LocationService.class);

    @Inject
    LocationQueryRepository repository;

    @Inject
    @Named("LocationModelEntityMapper")
    MapperFacade mapperFacade;

    @Inject
    @Property("app.name")
    private String applicationName;

    public List<LocationResponseModel> getAllLocationResponseModel() {
        logger.debug("LocationService getAllLocationResponseModel started");
        List<LocationModel> locationModels = repository.findAll();
        List<LocationResponseModel> locationResponseModels = locationModels.stream().map(locationModel -> mapperFacade.map(locationModel, LocationResponseModel.class)).collect(Collectors.toList());
        logger.debug("LocationService getAllLocationResponseModel finished");
        return locationResponseModels;
    }

}
