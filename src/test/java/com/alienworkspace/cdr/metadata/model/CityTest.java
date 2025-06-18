package com.alienworkspace.cdr.metadata.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityTest {
    @Test
    void testBuilderAndGetters() {
        City city = City.builder()
                .cityId(1)
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CIT123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();
        assertEquals(1, city.getCityId());
        assertEquals("Test City", city.getCityName());
        assertEquals("en_US", city.getLocale());
        assertTrue(city.isLocalePreferred());
        assertEquals("CIT123", city.getCityCode());
        assertEquals("GEO123", city.getCityGeoCode());
        assertEquals(234, city.getCityPhoneCode());
    }

    @Test
    void testSetters() {
        City city = new City();
        city.setCityId(2);
        city.setCityName("Another City");
        city.setLocale("fr_FR");
        city.setLocalePreferred(false);
        city.setCityCode("CIT456");
        city.setCityGeoCode("GEO456");
        city.setCityPhoneCode(123);
        assertEquals(2, city.getCityId());
        assertEquals("Another City", city.getCityName());
        assertEquals("fr_FR", city.getLocale());
        assertFalse(city.isLocalePreferred());
        assertEquals("CIT456", city.getCityCode());
        assertEquals("GEO456", city.getCityGeoCode());
        assertEquals(123, city.getCityPhoneCode());
    }

    @Test
    void testEqualsAndHashCode() {
        String uuid = "12345678-1234-1234-1234-1234567890ab";
        City c1 = City.builder().cityId(1).county(County.builder().countyId(1).build()).cityName("A").build();
        City c2 = City.builder().cityId(1).county(County.builder().countyId(1).build()).cityName("A").build();
        c1.setUuid(uuid);
        c2.setUuid(uuid);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        City city = City.builder().cityId(1).cityName("Test").build();
        assertTrue(city.toString().contains("Test"));
    }
} 