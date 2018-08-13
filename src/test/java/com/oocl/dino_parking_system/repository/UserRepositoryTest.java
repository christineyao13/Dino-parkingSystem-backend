package com.oocl.dino_parking_system.repository;

import com.oocl.dino_parking_system.entitie.LotOrder;
import com.oocl.dino_parking_system.entitie.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager manager;

    @After
    public void tearDown()throws Exception{
        manager.clear();
    }

    @Test
    public void demotest() {
        User user = new User("a", "111@as.com", "1324564", "aas", "adf");
        LotOrder lotOrder = new LotOrder("asd", "asdf", "adf", "1234");
        lotOrder.setParkingBoy(user);
        orderRepository.save(lotOrder);
        LotOrder o = orderRepository.findById(1L).get();
        user.setLotOrders(Arrays.asList(o));

        employeeRepository.save(user);
        List<User> users = employeeRepository.findAll();
        List<LotOrder> lotOrders = orderRepository.findAll();

        assertThat(lotOrders.get(0).getType(), is("asd"));
        assertThat(users.get(0).getLotOrders().get(0).getType(), is("asd"));

    }

    @Test
    public void should_add_user_successfully(){
        //given
        User user = new User("haha", "haha", "1324564", "haha@oocl.com", "15316452846");
        //when
        employeeRepository.save(user);
        //then
        assertThat(employeeRepository.findAll().size(),is(1));
    }

//    @Test
//    public void should_update_user_successfully(){
//        //given
//        manager.persist(new User("haha", "haha", "1324564", "haha@oocl.com", "15316452846"));
//        //when
//
//        //then
//    }

    @Test
    public void should_find_user_when_find_by_username(){
        //given
        manager.persist(new User("christine", "1333", "123456", "christine@oocl.com", "12345678900"));

        //when
        User user = userRepository.findByUsername("christine");
        //then
        assertThat(user.getUsername(),is("christine"));
        assertThat(user.getNickname(),is("1333"));

    }

    @Test
    public void should_find_user_by_Username_And_Password() {
        //given
        manager.persist(new User("christine", "1333", "123456", "christine@oocl.com", "12345678900"));
        //when
        User user = userRepository.findByUsernameAndPassword("christine","123456");
        //then
        assertThat(user.getUsername(),is("christine"));
        assertThat(user.getNickname(),is("1333"));

    }

}