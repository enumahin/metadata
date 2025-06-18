package com.alienworkspace.cdr.metadata.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityTest {
    @Test
    void testBuilderAndGetters() {
        Community community = Community.builder()
                .communityId(1)
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();
        assertEquals(1, community.getCommunityId());
        assertEquals("Test Community", community.getCommunityName());
        assertEquals("en_US", community.getLocale());
        assertTrue(community.isLocalePreferred());
        assertEquals("COM123", community.getCommunityCode());
        assertEquals("GEO123", community.getCommunityGeoCode());
        assertEquals(234, community.getCommunityPhoneCode());
    }

    @Test
    void testSetters() {
        Community community = new Community();
        community.setCommunityId(2);
        community.setCommunityName("Another Community");
        community.setLocale("fr_FR");
        community.setLocalePreferred(false);
        community.setCommunityCode("COM456");
        community.setCommunityGeoCode("GEO456");
        community.setCommunityPhoneCode(123);
        assertEquals(2, community.getCommunityId());
        assertEquals("Another Community", community.getCommunityName());
        assertEquals("fr_FR", community.getLocale());
        assertFalse(community.isLocalePreferred());
        assertEquals("COM456", community.getCommunityCode());
        assertEquals("GEO456", community.getCommunityGeoCode());
        assertEquals(123, community.getCommunityPhoneCode());
    }

    @Test
    void testEqualsAndHashCode() {
        String uuid = "12345678-1234-1234-1234-1234567890ab";
        Community c1 = Community.builder().city(City.builder().cityId(1).cityName("A").build())
                .communityId(1).communityName("A").build();
        Community c2 = Community.builder().city(City.builder().cityId(1).cityName("A").build())
                .communityId(1).communityName("A").build();
        c1.setUuid(uuid);
        c2.setUuid(uuid);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void testToString() {
        Community community = Community.builder().communityId(1).communityName("Test").build();
        assertTrue(community.toString().contains("Test"));
    }
} 