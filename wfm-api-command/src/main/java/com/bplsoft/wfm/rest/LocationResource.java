package com.bplsoft.wfm.rest;

import com.bplsoft.wfm.rest.dto.AddLocationResponseDTO;
import com.bplsoft.wfm.rest.dto.LocationResponseDTO;
import com.bplsoft.wfm.rest.dto.LoginResponseDTO;
import com.bplsoft.wfm.service.LocationService;
import com.bplsoft.wfm.service.model.AddLocationRequestModel;
import com.bplsoft.wfm.service.model.AddLocationResponseModel;
import com.bplsoft.wfm.service.model.LocationResponseModel;
import com.bplsoft.wfm.rest.dto.AddLocationRequestDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.glasnost.orika.MapperFacade;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;


@Path("/location")
@Api(value = "/location", tags = "location")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class LocationResource {

    @Inject
    LocationService service;

    @Inject
    @Named("LocationDTOModelMapper")
    MapperFacade mapperFacade;


    @ApiOperation(
        value = "add new location",
        notes = "return new location id.",
        response = LoginResponseDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 400, message = "Location already added.")
        }
    )
    @POST
    public AddLocationResponseDTO addLocation(AddLocationRequestDTO addLocationRequestDTO) {
        AddLocationRequestModel addLocationRequestModel = mapperFacade.map(addLocationRequestDTO, AddLocationRequestModel.class);
        AddLocationResponseModel addLocationResponseModel = service.addLocation(addLocationRequestModel);
        AddLocationResponseDTO addLocationResponseDTO = mapperFacade.map(addLocationResponseModel, AddLocationResponseDTO.class);
        return addLocationResponseDTO;
    }


    @ApiOperation(
        value = "get all locations",
        notes = "return all locations on the system.",
        response = LoginResponseDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 400, message = "Location not found.")
        }
    )
    @GET
    public List<LocationResponseDTO> getAllLocationResponseModel() {
        List<LocationResponseModel> locationModels = service.getAllLocationResponseModel();
        List<LocationResponseDTO> locationResponseModels = locationModels.stream().map(locationModel -> mapperFacade.map(locationModel, LocationResponseDTO.class)).collect(Collectors.toList());
        return locationResponseModels;
    }


    @ApiOperation(
        value = "delete location",
        notes = "recors is deleted using id",
        response = LoginResponseDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(code = 400, message = "id not found.")
        }
    )
    @DELETE
    public void deleteLocation(Integer id) {
        service.deleteLocation(id);
    }

}
