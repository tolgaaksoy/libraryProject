package com.troia.libraryproject.mapper;

import com.troia.libraryproject.dto.user.SingUpRequestDto;
import com.troia.libraryproject.dto.user.SingUpResponseDto;
import com.troia.libraryproject.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    User singUpRequestDtoToUser(SingUpRequestDto singupRequestDto);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "roles", target = "roles")
    SingUpResponseDto userToSingUpResponseDto(User user);
}
