package com.exercise.web;

import com.exercise.domain.Device;
import com.exercise.domain.DeviceData;
import com.exercise.domain.DeviceDataResult;
import com.exercise.service.DeviceDataService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(QueryController.class)
public class QueryControllerTest {

    @MockBean
    private DeviceDataService deviceDataService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldReturnStatusNotFound_ForUnregisteredDevice() throws Exception {
        String deviceId = "unRegisteredDevice";
        String fromDate = "2022-04-12T17:35:03.120Z";
        String toDate = "2022-04-15T17:35:03.120Z";
        String errorMessage = "{\"status\":\"NOT_FOUND\",\"message\":\"Device Not Found id='unRegisteredDevice'\"}";
        Mockito.when(deviceDataService.find(deviceId)).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(buildQuery(deviceId, fromDate, toDate))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        Mockito.verify(deviceDataService, Mockito.times(0)).findBy(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(errorMessage);
    }

    @Test
    public void givenValidDeviceData_ShouldAddItToResponse() throws Exception {
        String deviceId = "27d02c3e-666e-42eb-bbc0-28ffd7ac928c";
        String fromDate = "2022-04-12T17:35:03.120Z";
        String toDate = "2022-04-11T17:35:03.120Z";
        DeviceData deviceData = new DeviceData();
        List<DeviceData> data = new ArrayList<>();
        String data1 = "{\"humidity\":14,\"temperature\":{\"unit\":\"C\",\"value\":\"23.3\"},\"timestamp\":\"2022-04-23T19:01:42.869Z\"}";
        String data2 = "{\"humidity\":44,\"temperature\":{\"unit\":\"C\",\"value\":\"25.3\"},\"timestamp\":\"2022-04-23T19:01:45.869Z\"}";
//        String data1 = "{\"data\":{\"humidity\":14,\"temperature\":{\"unit\":\"C\",\"value\":\"23.3\"},\"timestamp\":\"2022-04-23T19:01:42.869Z\"}}";
//        String data2 = "{\"data\":{\"humidity\":44,\"temperature\":{\"unit\":\"C\",\"value\":\"25.3\"},\"timestamp\":\"2022-04-23T19:01:45.869Z\"}}";
        deviceDataFromJson(data, "{\"data\":" + data1 + "}");
        deviceDataFromJson(data, "{\"data\":" + data2 + "}");

        Device device = Device.builder().deviceId(deviceId).longitude(123).latitude(31).build();
        Mockito.when(deviceDataService.find(deviceId)).thenReturn(Optional.of(device));
        Mockito.when(deviceDataService.findBy(deviceId, fromDate, toDate)).thenReturn(data);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(buildQuery(deviceId, fromDate, toDate))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        DeviceDataResult deviceDataResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), DeviceDataResult.class);
        assertThat(deviceDataResult.getDeviceId()).isEqualTo(deviceId);
        assertThat(deviceDataResult.getLatitude()).isEqualTo(device.getLatitude());
        assertThat(deviceDataResult.getLongitude()).isEqualTo(device.getLongitude());
        assertThat(deviceDataResult.getData().size()).isEqualTo(data.size());
        List<String> resultData = deviceDataResult.getData().stream().map(this::toJson).collect(Collectors.toList());
        assertThat(resultData).contains(data1, data2);
        Mockito.verify(deviceDataService, Mockito.times(1)).find(deviceId);
        Mockito.verify(deviceDataService, Mockito.times(1)).findBy(deviceId, fromDate, toDate);
    }


    @Test
    public void shouldReturnEmptyListOfData_IfConditionNotMatch() throws Exception {
        String deviceId = "27d02c3e-666e-42eb-bbc0-28ffd7ac928c";
        String fromDate = "2022-04-12T17:35:03.120Z";
        String toDate = "2022-04-11T17:35:03.120Z";
        Device device = Device.builder().deviceId(deviceId).longitude(123).latitude(31).build();
        Mockito.when(deviceDataService.find(deviceId)).thenReturn(Optional.of(device));
        Mockito.when(deviceDataService.findBy(deviceId, fromDate, toDate)).thenReturn(Collections.EMPTY_LIST);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get(buildQuery(deviceId, fromDate, toDate))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        DeviceDataResult deviceDataResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), DeviceDataResult.class);
        assertThat(deviceDataResult.getDeviceId()).isEqualTo(deviceId);
        assertThat(deviceDataResult.getLatitude()).isEqualTo(device.getLatitude());
        assertThat(deviceDataResult.getLongitude()).isEqualTo(device.getLongitude());
        assertThat(deviceDataResult.getData().size()).isEqualTo(0);
    }

    protected void deviceDataFromJson(List<DeviceData> data, String json) throws IOException {
        data.add(objectMapper.readValue(json, DeviceData.class));
    }

    protected String toJson(Map<String, Object> data) {
        String result = "";
        try {
            result = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {

        }
        return result.trim();
    }

    private String buildQuery(String deviceId, String fromDate, String toDate) {
        String queryTemplate = "/api/devices/%s?fromDate=%s&toDate=%s";
        return String.format(queryTemplate, deviceId, fromDate, toDate);
    }
}
