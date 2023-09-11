package com.konovalov.mapper;

import com.konovalov.dto.CompanyReadDto;
import com.konovalov.entities.Company;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {
    @Override
    public CompanyReadDto mapFrom(Company object) {
        return new CompanyReadDto(
                object.getId(),
                object.getName()
        );
    }
}
