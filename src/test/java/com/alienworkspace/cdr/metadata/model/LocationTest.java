package com.alienworkspace.cdr.metadata.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    @Test
    void testBuilderAndGetters() {
        Location location = Location.builder()
                .locationId(1)
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .locationPhoneCode(234)
                .build();
        assertEquals(1, location.getLocationId());
        assertEquals("Test Location", location.getLocationName());
        assertEquals("en_US", location.getLocale());
        assertTrue(location.isLocalePreferred());
        assertEquals("LOC123", location.getLocationCode());
        assertEquals("GEO123", location.getLocationGeoCode());
        assertEquals(234, location.getLocationPhoneCode());
    }

    @Test
    void testSetters() {
        Location location = new Location();
        location.setLocationId(2);
        location.setLocationName("Another Location");
        location.setLocale("fr_FR");
        location.setLocalePreferred(false);
        location.setLocationCode("LOC456");
        location.setLocationGeoCode("GEO456");
        location.setLocationPhoneCode(123);
        assertEquals(2, location.getLocationId());
        assertEquals("Another Location", location.getLocationName());
        assertEquals("fr_FR", location.getLocale());
        assertFalse(location.isLocalePreferred());
        assertEquals("LOC456", location.getLocationCode());
        assertEquals("GEO456", location.getLocationGeoCode());
        assertEquals(123, location.getLocationPhoneCode());
    }

    @Test
    void testEqualsAndHashCode() {
        String uuid = "12345678-1234-1234-1234-1234567890ab";
        Location l1 = Location.builder().community(Community.builder().communityId(1).communityName("A").build())
                .locationId(1).locationName("A").build();
        Location l2 = Location.builder().community(Community.builder().communityId(1).communityName("A").build())
                .locationId(1).locationName("A").build();
        l1.setUuid(uuid);
        l2.setUuid(uuid);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }

    @Test
    void testToString() {
        Location location = Location.builder().locationId(1).locationName("Test").build();
        assertTrue(location.toString().contains("Test"));
    }
} 