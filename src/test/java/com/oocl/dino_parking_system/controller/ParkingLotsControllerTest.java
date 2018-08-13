package com.oocl.dino_parking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.dino_parking_system.entitie.ParkingLot;
import com.oocl.dino_parking_system.service.ParkingLotsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ParkingLotController.class)
public class ParkingLotsControllerTest {
    @Autowired
    ParkingLotController parkingLotsController;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ParkingLotsService parkingLotsService;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void should_create_new_parkinglot_when_given_a_parkinglot() throws Exception{
        //given
        ParkingLot parkingLot = new ParkingLot("parkinglot1",20);
        when(parkingLotsService.createParkingLots(any(ParkingLot.class))).thenReturn(true);
        //when
        ResultActions result = mvc.perform(post("/parkingLots")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(parkingLot)));
        //then
        result.andExpect(status().isCreated()).andDo(print());
    }
}