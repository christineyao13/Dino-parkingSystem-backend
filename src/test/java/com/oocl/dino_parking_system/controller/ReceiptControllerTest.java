package com.oocl.dino_parking_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oocl.dino_parking_system.entitie.LotOrder;
import com.oocl.dino_parking_system.entitie.Receipt;
import com.oocl.dino_parking_system.service.OrderService;
import com.oocl.dino_parking_system.service.ReceiptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static com.oocl.dino_parking_system.constant.Constants.STATUS_NOROB;
import static com.oocl.dino_parking_system.constant.Constants.TYPE_PARKCAR;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(ReceiptController.class)
public class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ReceiptService receiptService;

    @MockBean
    private OrderService orderService;

    @Test
    public void should_return_receipt_id_when_given_car_plate_number() throws Exception {
        //given
        Receipt receipt = new Receipt();
        LotOrder lotOrder = new LotOrder(TYPE_PARKCAR, "a1234", STATUS_NOROB, receipt.getId());
        given(receiptService.generateReceipt()).willReturn(receipt);
        given(orderService.generateOrder(anyString(), anyString())).willReturn(lotOrder);
	    Map<String,String>postData = new HashMap<>();
	    postData.put("plateNum","a1234");
        //when
        ResultActions resultActions = mockMvc.perform(post("/receipts")
		        .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(postData)));

        //then
        resultActions.andExpect(jsonPath("$.id", is(receipt.getId())));
    }
}
