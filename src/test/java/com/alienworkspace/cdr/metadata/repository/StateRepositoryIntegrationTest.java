package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StateRepositoryIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private LocationRepository locationRepository;

    private State.StateBuilder stateBuilder = State.builder();

    @BeforeEach
    public void setUp() {
        locationRepository.deleteAll();
        communityRepository.deleteAll();
        cityRepository.deleteAll();
        countyRepository.deleteAll();
        stateRepository.deleteAll();
        countryRepository.deleteAll();
        stateBuilder
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .stateCode("STA123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234);
    }

    @Test
    @DisplayName("Test Create State")
    public void testCreateState() {
        // Arrange
        State state = stateBuilder.build();
        state.setCreatedBy(1L);
        state.setCreatedAt(LocalDateTime.now());

        // Act
        State actualState = stateRepository.save(state);

        // Assert
        assertNotNull(actualState);
        assertNotNull(actualState.getStateId());
        assertEquals("Test State", actualState.getStateName());
    }

    @Test
    @DisplayName("Test findAllByVoidedIsFalse returns only non-voided states")
    public void testFindAllByVoidedIsFalse() {
        // Arrange
        State state1 = stateBuilder.stateName("State 1").build();
        state1.setCreatedBy(1L);
        state1.setCreatedAt(LocalDateTime.now());
        state1.setVoided(false);

        State state2 = stateBuilder.stateName("State 2").build();
        state2.setCreatedBy(1L);
        state2.setCreatedAt(LocalDateTime.now());
        state2.setVoided(true);

        stateRepository.save(state1);
        stateRepository.save(state2);

        // Act
        List<State> nonVoidedStates = stateRepository.findAllByVoidedIsFalse();

        // Assert
        assertNotNull(nonVoidedStates);
        assertEquals(1, nonVoidedStates.size());
        assertEquals("State 1", nonVoidedStates.get(0).getStateName());
    }
} 