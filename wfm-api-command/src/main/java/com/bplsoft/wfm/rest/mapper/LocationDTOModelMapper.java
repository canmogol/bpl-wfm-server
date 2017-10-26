package com.bplsoft.wfm.rest.mapper;

import com.bplsoft.wfm.rest.dto.AddLocationResponseDTO;
import com.bplsoft.wfm.rest.dto.LocationResponseDTO;
import com.bplsoft.wfm.service.model.AddLocationRequestModel;
import com.bplsoft.wfm.service.model.AddLocationResponseModel;
import com.bplsoft.wfm.service.model.LocationResponseModel;
import com.bplsoft.wfm.rest.dto.AddLocationRequestDTO;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

import javax.inject.Named;

@Named("LocationDTOModelMapper")
public class LocationDTOModelMapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(AddLocationRequestDTO.class, AddLocationRequestModel.class)
            .byDefault()
            .register();
        factory.classMap(AddLocationResponseModel.class, AddLocationResponseDTO.class)
            .byDefault()
            .register();
        factory.classMap(LocationResponseModel.class, LocationResponseDTO.class)
            .byDefault()
            .register();
    }

}
