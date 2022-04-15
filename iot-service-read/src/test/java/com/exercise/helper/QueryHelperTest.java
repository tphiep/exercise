package com.exercise.helper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

public class QueryHelperTest {

    QueryHelper queryHelper = new QueryHelper();

    @Test
    void shouldReturnQuery_withReplacedParameters() {
        String deviceId = UUID.randomUUID().toString();
        String fromDate = DateTimeHelper.formatDateTime(LocalDateTime.now().minusDays(1));
        String toDate = DateTimeHelper.formatDateTime(LocalDateTime.now().plusDays(1));
        String query = queryHelper.buildGetDeviceDataQuery(deviceId, fromDate, toDate);
        Assertions.assertThat(query).contains(deviceId);
        Assertions.assertThat(query).contains(fromDate);
        Assertions.assertThat(query).contains(toDate);
    }
}
