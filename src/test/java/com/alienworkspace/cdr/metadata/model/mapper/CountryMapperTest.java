package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.model.dto.metadata.*;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CountryMapperTest {
    private final CountryMapper mapper = CountryMapper.INSTANCE;

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
        Country entity = Country.builder()
                .countryId(1)
                .countryName("Test Country")
                .locale("en_US")
                .states(ImmutableSet.of(StateMapper.INSTANCE.toEntity(state)))
                .localePreferred(true)
                .countryCode("CTR123")
                .currencyCode("USD")
                .currencySymbol("$")
                .currencyName("Dollar")
                .countryGeoCode("GEO123")
                .countryPhoneCode(234)
                .build();
        CountryDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(entity.getCountryId(), dto.getCountryId());
        assertEquals(entity.getCountryName(), dto.getCountryName());
        assertEquals(entity.getLocale(), dto.getLocale());
        assertEquals(entity.isLocalePreferred(), dto.isLocalePreferred());
        assertEquals(entity.getCountryCode(), dto.getCountryCode());
        assertEquals(entity.getCurrencyCode(), dto.getCurrencyCode());
        assertEquals(entity.getCurrencySymbol(), dto.getCurrencySymbol());
        assertEquals(entity.getCurrencyName(), dto.getCurrencyName());
        assertEquals(entity.getCountryGeoCode(), dto.getCountryGeoCode());
        assertEquals(entity.getCountryPhoneCode(), dto.getCountryPhoneCode());
    }

    @Test
    void testToEntity() {
        CountryDto dto = CountryDto.builder()
                .countryId(2)
                .countryName("Another Country")
                .locale("fr_FR")
                .localePreferred(false)
                .countryCode("CTR456")
                .states(ImmutableSet.of(state))
                .currencyCode("EUR")
                .currencySymbol("€")
                .currencyName("Euro")
                .countryGeoCode("GEO456")
                .countryPhoneCode(123)
                .build();
        Country entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getCountryId(), entity.getCountryId());
        assertEquals(dto.getCountryName(), entity.getCountryName());
        assertEquals(dto.getLocale(), entity.getLocale());
        assertEquals(dto.isLocalePreferred(), entity.isLocalePreferred());
        assertEquals(dto.getCountryCode(), entity.getCountryCode());
        assertEquals(dto.getCurrencyCode(), entity.getCurrencyCode());
        assertEquals(dto.getCurrencySymbol(), entity.getCurrencySymbol());
        assertEquals(dto.getCurrencyName(), entity.getCurrencyName());
        assertEquals(dto.getCountryGeoCode(), entity.getCountryGeoCode());
        assertEquals(dto.getCountryPhoneCode(), entity.getCountryPhoneCode());
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