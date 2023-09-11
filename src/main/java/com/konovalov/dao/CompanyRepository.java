package com.konovalov.dao;

import com.konovalov.entities.Company;
import jakarta.persistence.EntityManager;

public class CompanyRepository extends RepositoryBase<Long, Company> {
    public CompanyRepository(EntityManager entityManager) {
        super(entityManager, Company.class);
    }
}
