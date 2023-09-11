package com.konovalov;

import com.konovalov.dao.CompanyRepository;
import com.konovalov.dao.UserRepository;
import com.konovalov.dto.UserCreateDto;
import com.konovalov.interceptor.TransactionInterceptor;
import com.konovalov.mapper.CompanyReadMapper;
import com.konovalov.mapper.UserCreateMapper;
import com.konovalov.mapper.UserReadMapper;
import com.konovalov.service.UserService;
import com.konovalov.utils.HibUtil;
import jakarta.transaction.Transactional;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class Main {
    @Transactional
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try (SessionFactory factory = HibUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class}, ((proxy, method, args1) -> method.invoke(factory.getCurrentSession(), args1)));

            CompanyReadMapper companyReadMapper = new CompanyReadMapper();
            UserReadMapper userReadMapper = new UserReadMapper(companyReadMapper);
            CompanyRepository companyRepository = new CompanyRepository(session);
            UserCreateMapper userCreateMapper = new UserCreateMapper(companyRepository);
            var userRep = new UserRepository(session);

            TransactionInterceptor transactionInterceptor = new TransactionInterceptor(factory);
            UserService userService = new ByteBuddy().subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded().getDeclaredConstructor(UserReadMapper.class, UserRepository.class, UserCreateMapper.class)
                    .newInstance(userReadMapper, userRep, userCreateMapper);

//            userService.findById(1L).ifPresent(System.out::println);
            UserCreateDto usCrDto = UserCreateDto.builder()
//                    .name("Diman4ik")
                    .companyId(1L)
                    .build();
            userService.create(usCrDto);
        }
    }
}