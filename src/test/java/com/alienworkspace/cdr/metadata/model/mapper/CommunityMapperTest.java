package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.Community;
import com.alienworkspace.cdr.model.dto.metadata.*;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommunityMapperTest {
    private final CommunityMapper mapper = CommunityMapper.INSTANCE;

    CountryDto country;

    StateDto state;

    CityDto city;

    CountyDto county;

    CommunityDto community;

    LocationDto location;

    @BeforeEach
    public void setup() {
        country = createCountry();
        state = createState(country);
        county = createCounty(state);
        city = createCity(county);
        community = createCommunity(city);
        location = createLocation(community);
    }

    @Test
    void testToDto() {
        Community entity = Community.builder()
                .communityId(1)
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .city(CityMapper.INSTANCE.toEntity(city))
                .locations(ImmutableSet.of(LocationMapper.INSTANCE.toEntity(location)))
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();
        CommunityDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(entity.getCommunityId(), dto.getCommunityId());
        assertEquals(entity.getCommunityName(), dto.getCommunityName());
        assertEquals(entity.getLocale(), dto.getLocale());
        assertEquals(entity.isLocalePreferred(), dto.isLocalePreferred());
        assertEquals(entity.getCommunityCode(), dto.getCommunityCode());
        assertEquals(entity.getCommunityGeoCode(), dto.getCommunityGeoCode());
        assertEquals(entity.getCommunityPhoneCode(), dto.getCommunityPhoneCode());
        assertEquals(entity.getLocations().size(), dto.getLocations().size());
    }

    @Test
    void testToEntity() {
        CommunityDto dto = CommunityDto.builder()
                .communityId(2)
                .communityName("Another Community")
                .locale("fr_FR")
                .city(city)
                .locations(ImmutableSet.of(location))
                .localePreferred(false)
                .communityCode("COM456")
                .communityGeoCode("GEO456")
                .communityPhoneCode(123)
                .build();
        Community entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getCommunityId(), entity.getCommunityId());
        assertEquals(dto.getCommunityName(), entity.getCommunityName());
        assertEquals(dto.getLocale(), entity.getLocale());
        assertEquals(dto.isLocalePreferred(), entity.isLocalePreferred());
        assertEquals(dto.getCommunityCode(), entity.getCommunityCode());
        assertEquals(dto.getCommunityGeoCode(), entity.getCommunityGeoCode());
        assertEquals(dto.getCommunityPhoneCode(), entity.getCommunityPhoneCode());
    }

    @Test
    void testNullHandling() {
        assertThrows(NullPointerException.class, () -> mapper.toDto(null));
        assertThrows(NullPointerException.class, () -> mapper.toEntity(null));
    }

    private CountryDto createCountry() {
        return CountryDto.builder()
                .countryId(1)
                .countryName("Test Country")
                .countryCode("TC")
                .currencySymbol("$")
                .countryPhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .build();
    }

    private StateDto createState(CountryDto country) {
        return StateDto.builder()
                .stateId(1)
                .stateName("Test State")
                .stateCode("TS")
                .statePhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .country(country)
                .build();
    }

    private CountyDto createCounty(StateDto state) {
        return CountyDto.builder()
                .countyId(1)
                .countyName("Test County")
                .countyCode("TC")
                .countyPhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .state(state)
                .build();
    }

    private CityDto createCity(CountyDto county) {
        return CityDto.builder()
                .cityId(1)
                .cityName("Test City")
                .cityCode("TC")
                .cityPhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .county(county)
                .build();
    }

    private CommunityDto createCommunity(CityDto city) {
        return CommunityDto.builder()
                .communityId(1)
                .communityName("Test Community")
                .communityCode("TC")
                .communityPhoneCode(123)
                .locale("en")
                .localePreferred(true)
                .city(city)
                .build();
    }

    private LocationDto createLocation(CommunityDto community) {
        return LocationDto.builder()
                .locationId(1)
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .community(community)
                .locationPhoneCode(234)
                .build();
    }
} 