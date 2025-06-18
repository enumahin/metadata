package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.City;
import com.alienworkspace.cdr.model.dto.metadata.*;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CityMapperTest {
    private final CityMapper mapper = CityMapper.INSTANCE;

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
        City entity = City.builder()
                .cityId(1)
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CIT123")
                .county(CountyMapper.INSTANCE.toEntity(county))
                .communities(ImmutableSet.of(CommunityMapper.INSTANCE.toEntity(community)))
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();
        CityDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(entity.getCityId(), dto.getCityId());
        assertEquals(entity.getCityName(), dto.getCityName());
        assertEquals(entity.getLocale(), dto.getLocale());
        assertEquals(entity.isLocalePreferred(), dto.isLocalePreferred());
        assertEquals(entity.getCityCode(), dto.getCityCode());
        assertEquals(entity.getCityGeoCode(), dto.getCityGeoCode());
        assertEquals(entity.getCityPhoneCode(), dto.getCityPhoneCode());
    }

    @Test
    void testToEntity() {
        CityDto dto = CityDto.builder()
                .cityId(2)
                .cityName("Another City")
                .locale("fr_FR")
                .localePreferred(false)
                .county(county)
                .communities(ImmutableSet.of(community))
                .cityCode("CIT456")
                .cityGeoCode("GEO456")
                .cityPhoneCode(123)
                .build();
        City entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getCityId(), entity.getCityId());
        assertEquals(dto.getCityName(), entity.getCityName());
        assertEquals(dto.getLocale(), entity.getLocale());
        assertEquals(dto.isLocalePreferred(), entity.isLocalePreferred());
        assertEquals(dto.getCityCode(), entity.getCityCode());
        assertEquals(dto.getCityGeoCode(), entity.getCityGeoCode());
        assertEquals(dto.getCityPhoneCode(), entity.getCityPhoneCode());
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