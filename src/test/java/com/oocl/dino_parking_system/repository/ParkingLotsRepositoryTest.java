package com.oocl.dino_parking_system.repositorys;

import com.oocl.dino_parking_system.entities.ParkingLot;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ParkingLotsRepositoryTest {
    @Autowired
    private ParkingLotsRepository parkingLotsRepository;
    @Autowired
    private TestEntityManager manager;


    @Test
    public void findAll() {
        //given
       manager.persistAndFlush(new ParkingLot("停车场A",20));
        //when
        List<ParkingLot> parkingLotList = parkingLotsRepository.findAll();
        //then
        assertThat(parkingLotList.size(),is(1));
    }

    @Test
    public void should_find_one_parkinglot_when_size_greater_than_10() {
        //given
        manager.persist(new ParkingLot("停车场A",20));
        manager.persist(new ParkingLot("停车场B",5));
        //when
        System.out.println(parkingLotsRepository.findAll());
        List<ParkingLot> parkingLotList = parkingLotsRepository.findAllBySizeGreaterThanEqual(10);
        //then
        assertThat(parkingLotList.size(),is(1));
        assertThat(parkingLotList.get(0).getName(),is("停车场A"));
        assertThat(parkingLotList.get(0).getSize(),is(20));
    }

    @Test
    public void should_find_one_parkinglot_size_five_when_size_greater_than_10() {
        //given
        manager.persist(new ParkingLot("停车场A",20));
        manager.persist(new ParkingLot("停车场B",5));
        //when
        System.out.println(parkingLotsRepository.findAll());
        List<ParkingLot> parkingLotList = parkingLotsRepository.findAllBySizeLessThanEqual(10);
        //then
        assertThat(parkingLotList.size(),is(1));
        assertThat(parkingLotList.get(0).getName(),is("停车场B"));
        assertThat(parkingLotList.get(0).getSize(),is(5));
    }

    @Test
    public void should_find_two_parkinglot_between_5_20() {
        //given
        manager.persist(new ParkingLot("停车场A",15));
        manager.persist(new ParkingLot("停车场B",10));
        manager.persist(new ParkingLot("停车场C",30));
        //when
        System.out.println(parkingLotsRepository.findAll());
        List<ParkingLot> parkingLotList = parkingLotsRepository.findAllBySizeBetween(5,20);
        //then
        assertThat(parkingLotList.size(),is(2));
        assertThat(parkingLotList.get(0).getName(),is("停车场A"));
        assertThat(parkingLotList.get(0).getSize(),is(15));
        assertThat(parkingLotList.get(1).getName(),is("停车场B"));
    }
}