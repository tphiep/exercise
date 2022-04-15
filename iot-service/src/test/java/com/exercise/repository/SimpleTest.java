package com.exercise.repository;

import com.exercise.helper.DateTimeHelper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SimpleTest {

    @Test
    public void test1() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        int sum = numbers.stream().reduce((a, b) -> a + b).get();
        System.out.println(sum);

        LocalDateTime localDate = LocalDateTime.now().minusDays(1);
        System.out.println(DateTimeHelper.formatDateTime(localDate));
    }
}
