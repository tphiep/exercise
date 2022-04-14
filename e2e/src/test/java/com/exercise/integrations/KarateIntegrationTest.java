package com.exercise.integrations;


import com.intuit.karate.junit5.Karate;

public class KarateIntegrationTest {

    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }
}
