package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.model.dto.metadata.*;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StateMapperTest {
    private final StateMapper mapper = StateMapper.INSTANCE;

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
        State entity = State.builder()
                .stateId(1)
                .stateName("Test State")
                .locale("en_US")
                .country(CountryMapper.INSTANCE.toEntity(country))
                .localePreferred(true)
                .counties(ImmutableSet.of(CountyMapper.INSTANCE.toEntity(county)))
                .stateCode("STA123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();
        StateDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(entity.getStateId(), dto.getStateId());
        assertEquals(entity.getStateName(), dto.getStateName());
        assertEquals(entity.getLocale(), dto.getLocale());
        assertEquals(entity.isLocalePreferred(), dto.isLocalePreferred());
        assertEquals(entity.getStateCode(), dto.getStateCode());
        assertEquals(entity.getStateGeoCode(), dto.getStateGeoCode());
        assertEquals(entity.getStatePhoneCode(), dto.getStatePhoneCode());
    }

    @Test
    void testToEntity() {
        StateDto dto = StateDto.builder()
                .stateId(2)
                .stateName("Another State")
                .locale("fr_FR")
                .country(country)
                .counties(ImmutableSet.of(county))
                .localePreferred(false)
                .stateCode("STA456")
                .stateGeoCode("GEO456")
                .statePhoneCode(123)
                .build();
        State entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getStateId(), entity.getStateId());
        assertEquals(dto.getStateName(), entity.getStateName());
        assertEquals(dto.getLocale(), entity.getLocale());
        assertEquals(dto.isLocalePreferred(), entity.isLocalePreferred());
        assertEquals(dto.getStateCode(), entity.getStateCode());
        assertEquals(dto.getStateGeoCode(), entity.getStateGeoCode());
        assertEquals(dto.getStatePhoneCode(), entity.getStatePhoneCode());
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