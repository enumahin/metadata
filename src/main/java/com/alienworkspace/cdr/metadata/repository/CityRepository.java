package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.model.City;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for City entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    /**
     * Find all cities that are not voided.
     *
     * @return a list of cities
     */
    List<City> findAllByVoidedIsFalse();
} 