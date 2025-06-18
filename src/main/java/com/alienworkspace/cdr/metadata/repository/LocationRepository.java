package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.model.Location;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Location entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    /**
     * Find all locations that are not voided.
     *
     * @return a list of locations
     */
    List<Location> findAllByVoidedIsFalse();
} 