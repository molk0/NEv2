package com.maciej.software.nev2.unit.util;

import com.maciej.software.nev2.util.TypeFormatter;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class TypeFormatterTest {

    @Test
    public void emptyStringPassed_shouldReturn0() {
        double weight = TypeFormatter.parseWeight("");
        assertEquals((double)0, weight);
    }

    @Test
    public void weightValuePassed_shouldReturnDouble() {
        String weight = "20";
        double weightDouble = TypeFormatter.parseWeight(weight);
        assertEquals(Double.parseDouble(weight), weightDouble);
    }
}
