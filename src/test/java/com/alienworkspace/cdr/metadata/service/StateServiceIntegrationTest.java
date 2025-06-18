package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Country;
import com.alienworkspace.cdr.metadata.model.mapper.CountryMapper;
import com.alienworkspace.cdr.metadata.repository.*;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StateServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private StateService stateService;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private LocationRepository locationRepository;

    Country country;

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateRepository.deleteAll();
        countryRepository.deleteAll();

        country = countryRepository.save(Country.builder()
                .states(Set.of())
                .countryName("Test Country").countryCode("CN123")
                .currencyCode("USD").currencySymbol("$").build());
    }

    @DisplayName("Test Create State")
    @Test
    public void testCreateState() {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .stateName("Test State")
                .locale("en_US")
                .country(CountryMapper.INSTANCE.toDto(country))
                .localePreferred(true)
                .stateCode("STATE123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();

        // Act
        StateDto actualStateDto = stateService.createState(stateDto);

        // Assert
        assertNotNull(actualStateDto);
        assertEquals(stateDto.getStateName(), actualStateDto.getStateName());
        assertEquals(stateDto.getLocale(), actualStateDto.getLocale());
        assertEquals(stateDto.isLocalePreferred(), actualStateDto.isLocalePreferred());
        assertEquals(stateDto.getStateCode(), actualStateDto.getStateCode());
        assertEquals(stateDto.getStateGeoCode(), actualStateDto.getStateGeoCode());
        assertEquals(stateDto.getStatePhoneCode(), actualStateDto.getStatePhoneCode());
        assertNotNull(actualStateDto.getCreatedAt());
        assertNotNull(actualStateDto.getCreatedBy());
    }

    @DisplayName("Test Update State")
    @Test
    public void testUpdateState() {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .country(CountryMapper.INSTANCE.toDto(country))
                .stateCode("STATE123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();

        // Act
        StateDto newStateDto = stateService.createState(stateDto);
        newStateDto.setStateName("Updated State");
        newStateDto.setLocale("fr_FR");
        newStateDto.setLocalePreferred(false);
        newStateDto.setStateCode("STATE456");
        newStateDto.setStateGeoCode("GEO456");
        newStateDto.setStatePhoneCode(567);
        newStateDto.setLastModifiedBy(1L);
        StateDto actualStateDto = stateService.updateState(newStateDto.getStateId(), newStateDto);

        // Assert
        assertNotNull(actualStateDto);
        assertEquals("Updated State", actualStateDto.getStateName());
        assertEquals("fr_FR", actualStateDto.getLocale());
        assertFalse(actualStateDto.isLocalePreferred());
        assertEquals("STATE456", actualStateDto.getStateCode());
        assertEquals("GEO456", actualStateDto.getStateGeoCode());
        assertEquals(567, actualStateDto.getStatePhoneCode());
        assertNotNull(actualStateDto.getLastModifiedAt());
        assertNotNull(actualStateDto.getLastModifiedBy());
    }

    @DisplayName("Test Update Non Existing State")
    @Test
    public void testUpdateNonExistingState() {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .stateId(1)
                .stateName("Test State")
                .country(CountryMapper.INSTANCE.toDto(country))
                .locale("en_US")
                .localePreferred(true)
                .stateCode("STATE123")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> stateService.updateState(stateDto.getStateId(), stateDto));
    }

    @DisplayName("Test Get State")
    @Test
    public void testGetState() {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .country(CountryMapper.INSTANCE.toDto(country))
                .stateCode("STATE123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();

        // Act
        StateDto newStateDto = stateService.createState(stateDto);
        StateDto actualStateDto = stateService.getState(newStateDto.getStateId());

        // Assert
        assertNotNull(actualStateDto);
        assertEquals(newStateDto.getStateName(), actualStateDto.getStateName());
        assertEquals(newStateDto.getLocale(), actualStateDto.getLocale());
        assertEquals(newStateDto.isLocalePreferred(), actualStateDto.isLocalePreferred());
        assertEquals(newStateDto.getStateCode(), actualStateDto.getStateCode());
        assertEquals(newStateDto.getStateGeoCode(), actualStateDto.getStateGeoCode());
        assertEquals(newStateDto.getStatePhoneCode(), actualStateDto.getStatePhoneCode());
    }

    @DisplayName("Test Get All States")
    @Test
    public void testGetAllStates() {
        // Arrange
        StateDto.StateDtoBuilder stateDtoBuilder = StateDto.builder()
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .country(CountryMapper.INSTANCE.toDto(country))
                .stateCode("STATE123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234);

        StateDto stateDto = stateDtoBuilder.build();

        // Act
        stateService.createState(stateDto);
        stateDto.setStateName("Test State 2");
        stateService.createState(stateDtoBuilder.stateName("Test State-02").stateCode("STATE9456").build() );
        List<StateDto> actualStateDtos = stateService.getAllStates();

        // Assert
        assertNotNull(actualStateDtos);
        assertEquals(2, actualStateDtos.size());
    }

    @DisplayName("Test Delete State")
    @Test
    public void testDeleteState() {
        // Arrange
        StateDto stateDto = StateDto.builder()
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .country(CountryMapper.INSTANCE.toDto(country))
                .stateCode("STATE123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();

        // Act
        StateDto newStateDto = stateService.createState(stateDto);
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        stateService.deleteState(newStateDto.getStateId(), request);

        // Assert
        StateDto actualStateDto = stateService.getState(newStateDto.getStateId());
        assertTrue(actualStateDto.getVoided());
    }

    @DisplayName("Test Void Non Existing State")
    @Test
    public void testVoidNonExistingState() {
        // Arrange
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> stateService.deleteState(1, request));
    }
} 