package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.model.Country;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for Country entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    /**
     * Find all countries that are not voided.
     *
     * @return a list of countries
     */
    List<Country> findAllByVoidedIsFalse();

    /**
     * Find a country by its country code.
     *
     * @param countryCode the country code to search for
     * @return the country with the given code, or null if not found
     */
    @Query("SELECT DISTINCT c FROM Country c WHERE c.countryCode = ?1")
    Optional<Country> findByCountryCode(String countryCode);
}
