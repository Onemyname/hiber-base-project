package com.konovalov.mapper;

import com.konovalov.dto.UserReadDto;
import com.konovalov.entities.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto mapFrom(User user) {
        return new UserReadDto(
                user.getId(),
                user.getName(),
                Optional.ofNullable(user.getCompany()).map(companyReadMapper::mapFrom)
                        .orElse(null)
                //поэтому необходимо делать Optional либо NotNull constraints
        );
    }
}
