package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.model.County;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for County entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface CountyRepository extends JpaRepository<County, Integer> {

    /**
     * Find all counties that are not voided.
     *
     * @return a list of counties
     */
    List<County> findAllByVoidedIsFalse();
} 