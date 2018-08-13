package com.oocl.dino_parking_system.repositorys;

import com.oocl.dino_parking_system.entities.Role;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TestEntityManager manager;

    @Test
    public void findByName() {
        //given
         manager.persist(new Role("parkingboy"));
        //when
        Role role = roleRepository.findByName("parkingboy");
        //then
       assertThat(role.getName(),is("parkingboy"));

    }
}