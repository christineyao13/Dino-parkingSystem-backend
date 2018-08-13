package com.oocl.dino_parking_system.service;

import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;


    @Test
    public void should_return_the_right_user_with_the_username_is_haha(){
        //given
        User haha = mock(User.class);
        UserService service = new UserService(userRepository);
        when(userRepository.findByUsername("haha")).thenReturn(haha);
        //when
        UserDetails me = service.loadUserByUsername("haha");
        //then
        assertNotNull(me);
    }

    @Test
    @DisplayName("throws exception")
    public void should_throw_exception(){
        //given
        UserService service = new UserService(userRepository);
        when(userRepository.findByUsername("haha")).thenReturn(null);
        //when
//        UserDetails me = service.loadUserByUsername("haha");
        //then
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("haha"));
    }

    @Test
    public void should_return_true_when_account_is_correct(){
        //given
        UserService service = new UserService(userRepository);
        User haha = mock(User.class);
        given(userRepository.findByUsernameAndPassword(null,null)).willReturn(haha);
        //when
        boolean validate = service.validate(haha);
        //then
        assertThat(validate,is(true));
    }

    @Test
    public void should_return_false_when_account_is_incorrect(){
        //given
        UserService service = new UserService(userRepository);
        User haha = mock(User.class);
        given(haha.getUsername()).willReturn("haha");
        given(haha.getPassword()).willReturn("haha");
        given(userRepository.findByUsernameAndPassword("haha","haha")).willReturn(null);
        //when
        boolean validate = service.validate(haha);
        //then
        assertThat(validate,is(false));
    }

    @Test
    void createUser() {
    }

    @Test
    public void should_return_all_users(){
        //given
        UserService service = new UserService(userRepository);
        List<User> users = Arrays.asList(mock(User.class));
        given(userRepository.findAll()).willReturn(users);
        //when
        List<User> userList = service.getAllUser();
        //then
        assertThat(userList,is(users));
    }

    @Test
    public void should_change_status_successfully_and_return_1(){
        //given
        UserService service = new UserService(userRepository);
        User haha = mock(User.class);
        given(userRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(haha));
        //when
        int state = service.changeUserStatus(1L,true);
        //then
        verify(userRepository).save(haha);
        assertThat(state,is(1));
    }

    @Test
    public void should_change_status_successfully_and_return_0(){
        //given
        UserService service = new UserService(userRepository);
        User haha = mock(User.class);
        given(userRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(null));
        //when
        int state = service.changeUserStatus(1L,true);
        //then
        assertThat(state,is(0));
    }

    @Test
    public void should_return_the_user_with_id_is_1(){
        //given
        UserService service = new UserService(userRepository);
        User haha = mock(User.class);
        given(userRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(haha));
        //when
        User me = service.getUserById(1L);
        //then
        assertThat(me,is(haha));
    }

    @Test
    public void should_update_info_of_user_successfully(){
        //given
        UserService service = new UserService(userRepository);
        User haha = mock(User.class);
        User me = new User("haha","joker","haha@oocl.com","13545325452");
        given(userRepository.findById(1L)).willReturn(java.util.Optional.ofNullable(haha));
        //when
        boolean key = service.updateUser(1L,me);
        //then
        verify(userRepository).save(haha);
        assertThat(key,is(true));
    }

    @Test
    public void should_call_save(){
        //given
        UserService service = new UserService(userRepository);
        User haha = mock(User.class);
        //when
        boolean key = service.save(haha);
        //then
        verify(userRepository).save(haha);
        assertThat(key,is(true));
    }

    @Test
    public void should_delete_user_successfully(){
        //given
        UserService service = new UserService(userRepository);
        //when
        boolean key = service.delete(1L);
        //then
        verify(userRepository).deleteById(1L);
        assertThat(key,is(true));
    }

    @Test
    public void should_return_true_when_the_account_is_correct(){
        //given
        User haha = mock(User.class);
        UserService service = new UserService(userRepository);
        when(haha.getUsername()).thenReturn("haha");
        when(userRepository.findByUsername("haha")).thenReturn(haha);
        //when
        boolean key = service.exits(haha);
        //then
        assertThat(key,is(true));
    }
}