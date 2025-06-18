package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.*;
import com.alienworkspace.cdr.model.dto.metadata.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationMapperTest {
    private final LocationMapper mapper = LocationMapper.INSTANCE;

    CountryDto country;

    StateDto state;

    CityDto city;

    CountyDto county;

    CommunityDto community;

    @BeforeEach
    public void setup() {
        country = createCountry();
        state = createState(country);
        county = createCounty(state);
        city = createCity(county);
        community = createCommunity(city);
    }

    @Test
    void testToDto() {
        Location entity = Location.builder()
                .locationId(1)
                .locationName("Test Location")
                .locale("en_US")
                .localePreferred(true)
                .locationCode("LOC123")
                .locationGeoCode("GEO123")
                .community(CommunityMapper.INSTANCE.toEntity(community))
                .locationPhoneCode(234)
                .build();
        LocationDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(entity.getLocationId(), dto.getLocationId());
        assertEquals(entity.getLocationName(), dto.getLocationName());
        assertEquals(entity.getLocale(), dto.getLocale());
        assertEquals(entity.isLocalePreferred(), dto.isLocalePreferred());
        assertEquals(entity.getLocationCode(), dto.getLocationCode());
        assertEquals(entity.getLocationGeoCode(), dto.getLocationGeoCode());
        assertEquals(entity.getLocationPhoneCode(), dto.getLocationPhoneCode());
    }

    @Test
    void testToEntity() {
        LocationDto dto = LocationDto.builder()
                .locationId(2)
                .locationName("Another Location")
                .locale("fr_FR")
                .community(community)
                .localePreferred(false)
                .locationCode("LOC456")
                .locationGeoCode("GEO456")
                .locationPhoneCode(123)
                .build();
        Location entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getLocationId(), entity.getLocationId());
        assertEquals(dto.getLocationName(), entity.getLocationName());
        assertEquals(dto.getLocale(), entity.getLocale());
        assertEquals(dto.isLocalePreferred(), entity.isLocalePreferred());
        assertEquals(dto.getLocationCode(), entity.getLocationCode());
        assertEquals(dto.getLocationGeoCode(), entity.getLocationGeoCode());
        assertEquals(dto.getLocationPhoneCode(), entity.getLocationPhoneCode());
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
} 