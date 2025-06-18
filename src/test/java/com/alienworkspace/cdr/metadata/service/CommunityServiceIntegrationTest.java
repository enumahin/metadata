package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.City;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.County;
import com.alienworkspace.cdr.metadata.model.State;
import com.alienworkspace.cdr.metadata.model.mapper.CityMapper;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
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
public class CommunityServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;
    State state;
    County county;
    City city;

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
        city = cityRepository.save(City.builder().cityName("Test City").cityCode("CIT123")
                .cityGeoCode("GEO123").county(county).build());
    }

    @DisplayName("Test Create Community")
    @Test
    public void testCreateCommunity() {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .communityName("Test Community")
                .locale("en_US")
                .city(CityMapper.INSTANCE.toDto(city))
                .localePreferred(true)
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();

        // Act
        CommunityDto actualCommunityDto = communityService.createCommunity(communityDto);

        // Assert
        assertNotNull(actualCommunityDto);
        assertEquals(communityDto.getCommunityName(), actualCommunityDto.getCommunityName());
        assertEquals(communityDto.getLocale(), actualCommunityDto.getLocale());
        assertEquals(communityDto.isLocalePreferred(), actualCommunityDto.isLocalePreferred());
        assertEquals(communityDto.getCommunityCode(), actualCommunityDto.getCommunityCode());
        assertEquals(communityDto.getCommunityGeoCode(), actualCommunityDto.getCommunityGeoCode());
        assertEquals(communityDto.getCommunityPhoneCode(), actualCommunityDto.getCommunityPhoneCode());
        assertNotNull(actualCommunityDto.getCreatedAt());
        assertNotNull(actualCommunityDto.getCreatedBy());
    }

    @DisplayName("Test Update Community")
    @Test
    public void testUpdateCommunity() {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .communityName("Test Community")
                .locale("en_US")
                .city(CityMapper.INSTANCE.toDto(city))
                .localePreferred(true)
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();

        // Act
        CommunityDto newCommunityDto = communityService.createCommunity(communityDto);
        newCommunityDto.setCommunityName("Updated Community");
        newCommunityDto.setLocale("fr_FR");
        newCommunityDto.setLocalePreferred(false);
        newCommunityDto.setCommunityCode("COM456");
        newCommunityDto.setCommunityGeoCode("GEO456");
        newCommunityDto.setCommunityPhoneCode(567);
        newCommunityDto.setLastModifiedBy(1L);
        CommunityDto actualCommunityDto = communityService.updateCommunity(newCommunityDto.getCommunityId(), newCommunityDto);

        // Assert
        assertNotNull(actualCommunityDto);
        assertEquals("Updated Community", actualCommunityDto.getCommunityName());
        assertEquals("fr_FR", actualCommunityDto.getLocale());
        assertFalse(actualCommunityDto.isLocalePreferred());
        assertEquals("COM456", actualCommunityDto.getCommunityCode());
        assertEquals("GEO456", actualCommunityDto.getCommunityGeoCode());
        assertEquals(567, actualCommunityDto.getCommunityPhoneCode());
        assertNotNull(actualCommunityDto.getLastModifiedAt());
        assertNotNull(actualCommunityDto.getLastModifiedBy());
    }

    @DisplayName("Test Update Non Existing Community")
    @Test
    public void testUpdateNonExistingCommunity() {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .communityId(1)
                .communityName("Test Community")
                .city(CityMapper.INSTANCE.toDto(city))
                .locale("en_US")
                .localePreferred(true)
                .communityCode("COM123")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> communityService.updateCommunity(communityDto.getCommunityId(), communityDto));
    }

    @DisplayName("Test Get Community")
    @Test
    public void testGetCommunity() {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .city(CityMapper.INSTANCE.toDto(city))
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();

        // Act
        CommunityDto newCommunityDto = communityService.createCommunity(communityDto);
        CommunityDto actualCommunityDto = communityService.getCommunity(newCommunityDto.getCommunityId());

        // Assert
        assertNotNull(actualCommunityDto);
        assertEquals(newCommunityDto.getCommunityName(), actualCommunityDto.getCommunityName());
        assertEquals(newCommunityDto.getLocale(), actualCommunityDto.getLocale());
        assertEquals(newCommunityDto.isLocalePreferred(), actualCommunityDto.isLocalePreferred());
        assertEquals(newCommunityDto.getCommunityCode(), actualCommunityDto.getCommunityCode());
        assertEquals(newCommunityDto.getCommunityGeoCode(), actualCommunityDto.getCommunityGeoCode());
        assertEquals(newCommunityDto.getCommunityPhoneCode(), actualCommunityDto.getCommunityPhoneCode());
    }

    @DisplayName("Test Get All Communities")
    @Test
    public void testGetAllCommunities() {
        // Arrange
        CommunityDto.CommunityDtoBuilder communityDtoBuilder = CommunityDto.builder()
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .city(CityMapper.INSTANCE.toDto(city))
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234);
        CommunityDto communityDto = communityDtoBuilder.build();


        // Act
        communityService.createCommunity(communityDto);
        communityDto.setCommunityName("Test Community 2");
        communityService.createCommunity(communityDtoBuilder.communityName("Test Community-90").communityCode("COM456").build());
        List<CommunityDto> actualCommunityDtos = communityService.getAllCommunities();

        // Assert
        assertNotNull(actualCommunityDtos);
        assertEquals(2, actualCommunityDtos.size());
    }

    @DisplayName("Test Delete Community")
    @Test
    public void testDeleteCommunity() {
        // Arrange
        CommunityDto communityDto = CommunityDto.builder()
                .communityName("Test Community")
                .locale("en_US")
                .city(CityMapper.INSTANCE.toDto(city))
                .localePreferred(true)
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234)
                .build();

        // Act
        CommunityDto newCommunityDto = communityService.createCommunity(communityDto);
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        communityService.deleteCommunity(newCommunityDto.getCommunityId(), request);

        // Assert
        CommunityDto actualCommunityDto = communityService.getCommunity(newCommunityDto.getCommunityId());
        assertTrue(actualCommunityDto.getVoided());
    }

    @DisplayName("Test Void Non Existing Community")
    @Test
    public void testVoidNonExistingCommunity() {
        // Arrange
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> communityService.deleteCommunity(1, request));
    }
} 