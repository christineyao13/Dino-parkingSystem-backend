package com.oocl.dino_parking_system.services;

import com.oocl.dino_parking_system.entities.Role;
import com.oocl.dino_parking_system.repositorys.RoleRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    RoleRepository roleRepository;

    @Test
    void should_save_successfully_when_given_a_role() {
        //given
        RoleService roleService = new RoleService(roleRepository);
        Role role = mock(Role.class);
//        when(role.getName()).thenReturn("parkingboy");
        when(roleRepository.save(role)).thenReturn(role);

        //when
        Role role1 = roleService.save(role);
        //then
        assertThat(role1,is(role));
    }

    @Test
    void setRoleToUser() {
    }
}