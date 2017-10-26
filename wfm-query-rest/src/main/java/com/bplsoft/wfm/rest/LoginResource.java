package com.bplsoft.wfm.rest;

import com.bplsoft.wfm.rest.dto.LoginRequestDTO;
import com.bplsoft.wfm.rest.dto.LoginResponseDTO;
import com.bplsoft.wfm.service.LoginService;
import com.bplsoft.wfm.service.model.LoginResponseModel;
import com.bplsoft.wfm.service.model.LoginRequestModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.glasnost.orika.MapperFacade;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "/login", tags = "login")
@Path("/login")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LoginResource {

    @Inject
    LoginService service;

    @Inject
    @Named("UserMapper")
    MapperFacade mapperFacade;

    @ApiOperation(
        value = "logs in a user",
        notes = "returns a model with user's name",
        response = LoginResponseDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 400, message = "Invalid username or password"),
            @ApiResponse(code = 404, message = "Username not found")
        }
    )
    @POST
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        LoginRequestModel loginRequestModel = mapperFacade.map(loginRequestDTO, LoginRequestModel.class);
        LoginResponseModel loginResponseModel = service.login(loginRequestModel);
        LoginResponseDTO loginResponseDTO = mapperFacade.map(loginResponseModel, LoginResponseDTO.class);
        return loginResponseDTO;
    }

}
