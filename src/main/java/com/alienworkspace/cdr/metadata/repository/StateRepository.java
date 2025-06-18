package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.model.State;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for State entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface StateRepository extends JpaRepository<State, Integer> {

    /**
     * Find all states that are not voided.
     *
     * @return a list of states
     */
    List<State> findAllByVoidedIsFalse();
} 