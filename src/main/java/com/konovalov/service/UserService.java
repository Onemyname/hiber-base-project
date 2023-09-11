package com.konovalov.service;

import com.konovalov.dao.UserRepository;
import com.konovalov.dto.UserCreateDto;
import com.konovalov.dto.UserReadDto;
import com.konovalov.entities.User;
import com.konovalov.mapper.UserCreateMapper;
import com.konovalov.mapper.UserReadMapper;
import jakarta.transaction.Transactional;
import jakarta.validation.*;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor

public class UserService {
    private final UserReadMapper mapper;
    private final UserRepository userRepository;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public void create(UserCreateDto userDto) {
        @ Cleanup ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserCreateDto>> violations = validator.validate(userDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        User user = userCreateMapper.mapFrom(userDto);

        userRepository.save(user);
    }


    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        Map<String, Object> properties = Map.of(GraphSemantic.LOAD.getJakartaHintName(), userRepository.getEntityManager().getEntityGraph("withCompany"));
        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<User> maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
        return maybeUser.isPresent();
    }
}
