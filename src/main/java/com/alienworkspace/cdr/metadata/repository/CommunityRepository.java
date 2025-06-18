package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.model.Community;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Repository for Community entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface CommunityRepository extends JpaRepository<Community, Integer> {

    /**
     * Find all communities that are not voided.
     *
     * @return a list of communities
     */
    @Query("SELECT c FROM Community c WHERE c.voided = false")
    List<Community> findAllByVoidedIsFalse();
} 