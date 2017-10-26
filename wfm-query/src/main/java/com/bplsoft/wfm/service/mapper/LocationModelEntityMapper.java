package com.bplsoft.wfm.service.mapper;

import com.bplsoft.wfm.service.model.AddLocationRequestModel;
import com.bplsoft.wfm.service.model.AddLocationResponseModel;
import com.bplsoft.wfm.service.model.LocationResponseModel;
import com.bplsoft.wfm.model.LocationModel;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

import javax.inject.Named;

@Named("LocationModelEntityMapper")
public class LocationModelEntityMapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(AddLocationRequestModel.class, LocationModel.class)
            .byDefault()
            .register();
        factory.classMap(LocationModel.class, AddLocationResponseModel.class)
            .byDefault()
            .register();
        factory.classMap(LocationModel.class, LocationResponseModel.class)
            .byDefault()
            .register();
    }

}
