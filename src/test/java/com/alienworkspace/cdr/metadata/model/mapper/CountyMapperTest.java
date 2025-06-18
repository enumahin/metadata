package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.model.dto.metadata.*;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CountyMapperTest {
    private final CountyMapper mapper = CountyMapper.INSTANCE;

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
        County entity = County.builder()
                .countyId(1)
                .countyName("Test County")
                .locale("en_US")
                .state(StateMapper.INSTANCE.toEntity(state))
                .cities(ImmutableSet.of(CityMapper.INSTANCE.toEntity(city)))
                .localePreferred(true)
                .countyCode("COU123")
                .countyGeoCode("GEO123")
                .countyPhoneCode(234)
                .build();
        CountyDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(entity.getCountyId(), dto.getCountyId());
        assertEquals(entity.getCountyName(), dto.getCountyName());
        assertEquals(entity.getLocale(), dto.getLocale());
        assertEquals(entity.isLocalePreferred(), dto.isLocalePreferred());
        assertEquals(entity.getCountyCode(), dto.getCountyCode());
        assertEquals(entity.getCountyGeoCode(), dto.getCountyGeoCode());
        assertEquals(entity.getCountyPhoneCode(), dto.getCountyPhoneCode());
    }

    @Test
    void testToEntity() {
        CountyDto dto = CountyDto.builder()
                .countyId(2)
                .countyName("Another County")
                .locale("fr_FR")
                .state(state)
                .cities(ImmutableSet.of(city))
                .localePreferred(false)
                .countyCode("COU456")
                .countyGeoCode("GEO456")
                .countyPhoneCode(123)
                .build();
        County entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getCountyId(), entity.getCountyId());
        assertEquals(dto.getCountyName(), entity.getCountyName());
        assertEquals(dto.getLocale(), entity.getLocale());
        assertEquals(dto.isLocalePreferred(), entity.isLocalePreferred());
        assertEquals(dto.getCountyCode(), entity.getCountyCode());
        assertEquals(dto.getCountyGeoCode(), entity.getCountyGeoCode());
        assertEquals(dto.getCountyPhoneCode(), entity.getCountyPhoneCode());
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