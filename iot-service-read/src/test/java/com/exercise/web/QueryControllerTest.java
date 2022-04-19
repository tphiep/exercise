package com.exercise.web;

import com.exercise.error.ErrorMessage;
import com.exercise.error.ErrorResponse;
import com.exercise.service.DeviceDataService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(QueryController.class)
public class QueryControllerTest {

    @MockBean
    private DeviceDataService deviceDataService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldReturnNothing_ForUnregisteredDevice() throws Exception {
        String deviceId = "unRegisteredDevice";
        String fromDate = "2022-04-12T17:35:03.120Z";
        String toDate = "2022-04-15T17:35:03.120Z";

        Mockito.when(deviceDataService.find(deviceId, fromDate, toDate)).thenReturn("{}");
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/devices/unRegisteredDevice?fromDate=2022-04-12T17:35:03.120Z&toDate=2022-04-15T17:35:03.120Z")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo("{}");
    }

    @Test
    public void shouldReturnError_ForInvalidFromDate() throws Exception {
        String deviceId = "unRegisteredDevice";
        String fromDate = "2022-04-12T17:35:03.120Z";
        String toDate = "2022-04-11T17:35:03.120Z";

        Mockito.when(deviceDataService.find(deviceId, fromDate, toDate)).thenReturn("{}");
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/devices/unRegisteredDevice?fromDate=2022-014-12T17:35:03.120Z&toDate=2022-04-15T17:35:03.120Z")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        ErrorResponse errorResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);
        assertThat(errorResponse.getMessage()).isEqualTo(String.format(ErrorMessage.TYPE_ERROR, "fromDate", LocalDateTime.class.getName()));
//        assertThat(mvcResult.getResponse().).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Mockito.verify(deviceDataService, Mockito.times(0)).find(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
}
