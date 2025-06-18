package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.mapper.CountyMapper;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CityServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityService cityService;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;
    State state;
    County county;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateRepository.deleteAll();
        countryRepository.deleteAll();

        country = countryRepository.save(Country.builder().countryName("Test Country").countryCode("CN123")
                .currencyCode("USD").currencySymbol("$").build());
        state = stateRepository.save(State.builder().stateName("Test State").stateCode("ST123")
                .country(country).build());
        county = countyRepository.save(County.builder().countyName("Test County")
                .countyCode("COU123").state(state).build());
    }

    @DisplayName("Test Create City")
    @Test
    public void testCreateCity() {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .county(CountyMapper.INSTANCE.toDto(county))
                .cityCode("CITY123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();

        // Act
        CityDto actualCityDto = cityService.createCity(cityDto);

        // Assert
        assertNotNull(actualCityDto);
        assertEquals(cityDto.getCityName(), actualCityDto.getCityName());
        assertEquals(cityDto.getLocale(), actualCityDto.getLocale());
        assertEquals(cityDto.isLocalePreferred(), actualCityDto.isLocalePreferred());
        assertEquals(cityDto.getCityCode(), actualCityDto.getCityCode());
        assertEquals(cityDto.getCityGeoCode(), actualCityDto.getCityGeoCode());
        assertEquals(cityDto.getCityPhoneCode(), actualCityDto.getCityPhoneCode());
        assertNotNull(actualCityDto.getCreatedAt());
        assertNotNull(actualCityDto.getCreatedBy());
    }

    @DisplayName("Test Update City")
    @Test
    public void testUpdateCity() {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .county(CountyMapper.INSTANCE.toDto(county))
                .cityCode("CITY123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();

        // Act
        CityDto newCityDto = cityService.createCity(cityDto);
        newCityDto.setCityName("Updated City");
        newCityDto.setLocale("fr_FR");
        newCityDto.setLocalePreferred(false);
        newCityDto.setCityCode("CITY456");
        newCityDto.setCityGeoCode("GEO456");
        newCityDto.setCityPhoneCode(567);
        newCityDto.setLastModifiedBy(1L);
        CityDto actualCityDto = cityService.updateCity(newCityDto.getCityId(), newCityDto);

        // Assert
        assertNotNull(actualCityDto);
        assertEquals("Updated City", actualCityDto.getCityName());
        assertEquals("fr_FR", actualCityDto.getLocale());
        assertFalse(actualCityDto.isLocalePreferred());
        assertEquals("CITY456", actualCityDto.getCityCode());
        assertEquals("GEO456", actualCityDto.getCityGeoCode());
        assertEquals(567, actualCityDto.getCityPhoneCode());
        assertNotNull(actualCityDto.getLastModifiedAt());
        assertNotNull(actualCityDto.getLastModifiedBy());
    }

    @DisplayName("Test Update Non Existing City")
    @Test
    public void testUpdateNonExistingCity() {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .cityId(1)
                .cityName("Test City")
                .county(CountyMapper.INSTANCE.toDto(county))
                .locale("en_US")
                .localePreferred(true)
                .cityCode("CITY123")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cityService.updateCity(cityDto.getCityId(), cityDto));
    }

    @DisplayName("Test Get City")
    @Test
    public void testGetCity() {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .county(CountyMapper.INSTANCE.toDto(county))
                .cityCode("CITY123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();

        // Act
        CityDto newCityDto = cityService.createCity(cityDto);
        CityDto actualCityDto = cityService.getCity(newCityDto.getCityId());

        // Assert
        assertNotNull(actualCityDto);
        assertEquals(newCityDto.getCityName(), actualCityDto.getCityName());
        assertEquals(newCityDto.getLocale(), actualCityDto.getLocale());
        assertEquals(newCityDto.isLocalePreferred(), actualCityDto.isLocalePreferred());
        assertEquals(newCityDto.getCityCode(), actualCityDto.getCityCode());
        assertEquals(newCityDto.getCityGeoCode(), actualCityDto.getCityGeoCode());
        assertEquals(newCityDto.getCityPhoneCode(), actualCityDto.getCityPhoneCode());
    }

    @DisplayName("Test Get All Cities")
    @Test
    public void testGetAllCities() {
        // Arrange
        CityDto.CityDtoBuilder cityDtoBuilder = CityDto.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .county(CountyMapper.INSTANCE.toDto(county))
                .cityCode("CITY123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234);

        CityDto cityDto = cityDtoBuilder.build();

        // Act
        cityService.createCity(cityDto);
        cityDto.setCityName("Test City 2");
        cityService.createCity(cityDtoBuilder.cityName("Test City0092").cityCode("CTY98756").build());
        List<CityDto> actualCityDtos = cityService.getAllCities();

        // Assert
        assertNotNull(actualCityDtos);
        assertEquals(2, actualCityDtos.size());
    }

    @DisplayName("Test Delete City")
    @Test
    public void testDeleteCity() {
        // Arrange
        CityDto cityDto = CityDto.builder()
                .cityName("Test City")
                .locale("en_US")
                .localePreferred(true)
                .county(CountyMapper.INSTANCE.toDto(county))
                .cityCode("CITY443123")
                .cityGeoCode("GEO123")
                .cityPhoneCode(234)
                .build();

        // Act
        CityDto newCityDto = cityService.createCity(cityDto);
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        cityService.deleteCity(newCityDto.getCityId(), request);

        // Assert
        CityDto actualCityDto = cityService.getCity(newCityDto.getCityId());
        assertTrue(actualCityDto.getVoided());
    }

    @DisplayName("Test Void Non Existing City")
    @Test
    public void testVoidNonExistingCity() {
        // Arrange
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cityService.deleteCity(1, request));
    }
} 