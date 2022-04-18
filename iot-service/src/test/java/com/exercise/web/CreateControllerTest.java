package com.exercise.web;

import com.exercise.domain.DeviceItem;
import com.exercise.helper.CustomMapper;
import com.exercise.service.DeviceDataService;
import com.exercise.service.SendDataService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@WebMvcTest(CreateController.class)
@Import(CustomMapper.class)
public class CreateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeviceDataService deviceDataService;

    @MockBean
    private SendDataService sendDataService;

    @Captor
    ArgumentCaptor<DeviceItem> deviceItemCaptor;

    @Test
    public void givenInvalidDeviceData_shouldReturnBadRequest_NoMessageSendToQueue() throws Exception {
        final String body = "{\n" +
//                "    \"deviceId\": \"1d1ada31231311\",\n" +
                "    \"latitude\": 1123.1,\n" +
                "    \"longitude\": -1120.333,\n" +
                "    \"data\":\n" +
                "        {\n" +
                "            \"humidity\": 123,\n" +
                "            \"temperature\": {\n" +
                "                \"unit\": \"C\",\n" +
                "                \"value\": \"23.3\"\n" +
                "            }\n" +
                "        }\n" +
                "}";

        Mockito.doNothing().when(sendDataService).send(Mockito.any());
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/devices/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body)).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        verify(sendDataService, Mockito.times(0)).send(Mockito.any());
    }

    @Test
    public void givenValidDeviceData_ThenMassageWillSendToQueue_AcceptedStatusReturn() throws Exception {
        final String body = "{\n" +
                "    \"deviceId\": \"1d1ada31231311\",\n" +
                "    \"latitude\": 13.1,\n" +
                "    \"longitude\": -10.333,\n" +
                "    \"data\":\n" +
                "        {\n" +
                "            \"humidity\": 123,\n" +
                "            \"temperature\": {\n" +
                "                \"unit\": \"C\",\n" +
                "                \"value\": \"23.3\"\n" +
                "            }\n" +
                "        }\n" +
                "}";

        Mockito.doNothing().when(sendDataService).send(Mockito.any());
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/devices/")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body)).andReturn();
        assertThat(mvcResult.getResponse().getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
        verify(sendDataService, Mockito.times(1)).send(Mockito.any());
        verify(sendDataService).send(deviceItemCaptor.capture());
        DeviceItem item = deviceItemCaptor.getValue();
        assertThat(item.getDeviceId()).isEqualTo("1d1ada31231311");
        assertThat(item.getLatitude()).isEqualTo(13.1);
        assertThat(item.getLongitude()).isEqualTo(-10.333);
    }
}
