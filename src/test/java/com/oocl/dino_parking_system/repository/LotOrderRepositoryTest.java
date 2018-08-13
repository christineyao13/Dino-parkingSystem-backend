package com.oocl.dino_parking_system.repository;

import com.oocl.dino_parking_system.entitie.LotOrder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LotOrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager manager;

    @After
    public void tearDown() {
        manager.clear();
    }

    @Test
    public void should_add_Order_successfully() {
        //given
        LotOrder lotOrder = new LotOrder("park", "a1234", "parking", "qwerasdf1234");

        //when
        LotOrder lotOrder1 = orderRepository.save(lotOrder);

        //then
        assertThat(lotOrder1.getPlateNumber(), is("a1234"));
    }

    @Test
    public void should_return_order_list_with_status_is_noRob(){
        //given
        LotOrder order = new LotOrder("津JO9527","3000");
        order.setStatus("waitPark");
        manager.persist(new LotOrder("京JO9527","3306"));
        manager.persist(new LotOrder("津JO9527","8080"));
        manager.persist(order);
        //when
        List<LotOrder> orders = orderRepository.findByStatus("noRob");
        List<LotOrder> orders1 = orderRepository.findByStatus("waitPark");
        //then
        assertThat(orders.size(),is(2));
        assertThat(orders1.size(),is(1));
    }
}