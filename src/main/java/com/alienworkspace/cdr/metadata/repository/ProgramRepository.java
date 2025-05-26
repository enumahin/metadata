package com.alienworkspace.cdr.metadata.repository;

import com.alienworkspace.cdr.metadata.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Program entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
}
