package com.alienworkspace.cdr.metadata.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CountryTest {
    @Test
    void testBuilderAndGetters() {
        Country country = Country.builder()
                .countryId(1)
                .countryName("Test Country")
                .locale("en_US")
                .localePreferred(true)
                .countryCode("CTR123")
                .currencyCode("USD")
                .currencySymbol("$")
                .currencyName("Dollar")
                .countryGeoCode("GEO123")
                .countryPhoneCode(234)
                .build();
        assertEquals(1, country.getCountryId());
        assertEquals("Test Country", country.getCountryName());
        assertEquals("en_US", country.getLocale());
        assertTrue(country.isLocalePreferred());
        assertEquals("CTR123", country.getCountryCode());
        assertEquals("USD", country.getCurrencyCode());
        assertEquals("$", country.getCurrencySymbol());
        assertEquals("Dollar", country.getCurrencyName());
        assertEquals("GEO123", country.getCountryGeoCode());
        assertEquals(234, country.getCountryPhoneCode());
    }

    @Test
    void testSetters() {
        Country country = new Country();
        country.setCountryId(2);
        country.setCountryName("Another Country");
        country.setLocale("fr_FR");
        country.setLocalePreferred(false);
        country.setCountryCode("CTR456");
        country.setCurrencyCode("EUR");
        country.setCurrencySymbol("€");
        country.setCurrencyName("Euro");
        country.setCountryGeoCode("GEO456");
        country.setCountryPhoneCode(123);
        assertEquals(2, country.getCountryId());
        assertEquals("Another Country", country.getCountryName());
        assertEquals("fr_FR", country.getLocale());
        assertFalse(country.isLocalePreferred());
        assertEquals("CTR456", country.getCountryCode());
        assertEquals("EUR", country.getCurrencyCode());
        assertEquals("€", country.getCurrencySymbol());
        assertEquals("Euro", country.getCurrencyName());
        assertEquals("GEO456", country.getCountryGeoCode());
        assertEquals(123, country.getCountryPhoneCode());
    }

    @Test
    void testEqualsAndHashCode() {
        String uuid = "12345678-1234-1234-1234-1234567890ab";
        Country c1 = Country.builder().countryId(1).countryName("A").build();
        Country c2 = Country.builder().countryId(1).countryName("A").build();
        c1.setUuid(uuid);
        c2.setUuid(uuid);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        Country country = Country.builder().countryId(1).countryName("Test").build();
        assertTrue(country.toString().contains("Test"));
    }
} 