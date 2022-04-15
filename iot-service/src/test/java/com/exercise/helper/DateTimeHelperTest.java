package com.exercise.helper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class DateTimeHelperTest {

    @Test
    public void shouldReturnValidFormatNonNullDate() {
        LocalDateTime localDateTime = LocalDateTime.of(2022,12, 22, 14, 22, 22, 123456789);
        String formattedDateTime = DateTimeHelper.formatDateTime(localDateTime);
        Assertions.assertThat(formattedDateTime).isEqualTo("2022-12-22T14:22:22.123Z");
    }

    @Test
    public void givenNullShouldReturnEmptyString() {
        Assertions.assertThat(DateTimeHelper.formatDateTime(null)).isEmpty();
    }
}
