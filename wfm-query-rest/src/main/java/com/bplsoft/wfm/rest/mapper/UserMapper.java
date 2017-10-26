package com.bplsoft.wfm.rest.mapper;

import com.bplsoft.wfm.rest.dto.LoginResponseDTO;
import com.bplsoft.wfm.service.model.LoginResponseModel;
import com.bplsoft.wfm.rest.dto.LoginRequestDTO;
import com.bplsoft.wfm.service.model.LoginRequestModel;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

import javax.inject.Named;

@Named("UserMapper")
public class UserMapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(LoginRequestDTO.class, LoginRequestModel.class)
            .byDefault()
            .register();
        factory.classMap(LoginResponseModel.class, LoginResponseDTO.class)
            .byDefault()
            .register();
    }

}
