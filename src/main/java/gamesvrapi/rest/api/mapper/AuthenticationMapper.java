package gamesvrapi.rest.api.mapper;


import org.mapstruct.Mapper;

import gamesvrapi.rest.api.dto.TokenDTO;
import gamesvrapi.rest.api.web.response.AuthenticationResponse;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(unmappedTargetPolicy = IGNORE)
public interface AuthenticationMapper {

    AuthenticationResponse toResponse(TokenDTO token);
}