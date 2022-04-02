package com.uni;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class FirstTest {
    @Test
    void additionTest() {
        assertThat(new Calculator().intAdd(2, 2)).isEqualTo(4);
    }

}
