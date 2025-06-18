package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class CommunityRepositoryIntegrationTest extends AbstractionContainerBaseTest {

    private Community.CommunityBuilder communityBuilder = Community.builder();

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

        communityBuilder
                .communityName("Test Community")
                .locale("en_US")
                .localePreferred(true)
                .communityCode("COM123")
                .communityGeoCode("GEO123")
                .communityPhoneCode(234);
    }

    @Test
    @DisplayName("Test Create Community")
    public void testCreateCommunity() {
        // Arrange
        Community community = communityBuilder.build();
        community.setCreatedBy(1L);
        community.setCreatedAt(LocalDateTime.now());
        community.setCommunityCode("COM124");
        community.setCity(city);

        // Act
        Community actualCommunity = communityRepository.save(community);

        // Assert
        assertNotNull(actualCommunity);
        assertNotNull(actualCommunity.getCommunityId());
        assertEquals("Test Community", actualCommunity.getCommunityName());
    }

    @Test
    @DisplayName("Test findAllByVoidedIsFalse returns only non-voided communities")
    public void testFindAllByVoidedIsFalse() {
        // Arrange
        Community community1 = communityBuilder.communityName("Community 1").build();
        community1.setCreatedBy(1L);
        community1.setCreatedAt(LocalDateTime.now());
        community1.setCommunityCode("COM125");
        community1.setVoided(false);
        community1.setCity(city);

        Community community2 = communityBuilder.communityName("Community 2").build();
        community2.setCreatedBy(1L);
        community2.setCreatedAt(LocalDateTime.now());
        community2.setCommunityCode("COM124");
        community2.setVoided(true);
        community2.setCity(city);

        communityRepository.save(community1);
        communityRepository.save(community2);

        // Act
        List<Community> nonVoidedCommunities = communityRepository.findAllByVoidedIsFalse();

        // Assert
        assertNotNull(nonVoidedCommunities);
        assertEquals(1, nonVoidedCommunities.size());
        assertEquals("Community 1", nonVoidedCommunities.get(0).getCommunityName());
    }
} 