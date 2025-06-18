package com.alienworkspace.cdr.metadata.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CountyTest {
    @Test
    void testBuilderAndGetters() {
        County county = County.builder()
                .countyId(1)
                .countyName("Test County")
                .locale("en_US")
                .localePreferred(true)
                .countyCode("COU123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();
        assertEquals(1, county.getCountyId());
        assertEquals("Test County", county.getCountyName());
        assertEquals("en_US", county.getLocale());
        assertTrue(county.isLocalePreferred());
        assertEquals("COU123", county.getCountyCode());
        assertEquals("GEO123", county.getCountyGeoCode());
        assertEquals(234, county.getCountyPhoneCode());
    }

    @Test
    void testSetters() {
        County county = new County();
        county.setCountyId(2);
        county.setCountyName("Another County");
        county.setLocale("fr_FR");
        county.setLocalePreferred(false);
        county.setCountyCode("COU456");
        county.setCountyGeoCode("GEO456");
        county.setCountyPhoneCode(123);
        assertEquals(2, county.getCountyId());
        assertEquals("Another County", county.getCountyName());
        assertEquals("fr_FR", county.getLocale());
        assertFalse(county.isLocalePreferred());
        assertEquals("COU456", county.getCountyCode());
        assertEquals("GEO456", county.getCountyGeoCode());
        assertEquals(123, county.getCountyPhoneCode());
    }

    @Test
    void testEqualsAndHashCode() {
        String uuid = "12345678-1234-1234-1234-1234567890ab";
        County c1 = County.builder().state(State.builder().stateId(1).stateName("A").build())
                .countyId(1).countyName("A").build();
        County c2 = County.builder().state(State.builder().stateId(1).stateName("A").build())
                .countyId(1).countyName("A").build();
        c1.setUuid(uuid);
        c2.setUuid(uuid);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        County county = County.builder().countyId(1).countyName("Test").build();
        assertTrue(county.toString().contains("Test"));
    }
} 