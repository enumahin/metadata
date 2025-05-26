package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.model.dto.metadata.ProgramDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;

/**
 * Service interface for Program operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface ProgramService {

    /**
     * Creates a new program.
     *
     * @param programDto the program to create
     * @return the created program
     */
    ProgramDto createProgram(ProgramDto programDto);

    /**
     * Updates an existing program.
     *
     * @param programId the ID of the program to update
     * @param programDto the program to update
     * @return the updated program
     */
    ProgramDto updateProgram(int programId, ProgramDto programDto);

    /**
     * Deletes a program.
     *
     * @param id the ID of the program to delete
     * @param request the request object containing the voided by user
     */
    void deleteProgram(int id, RecordVoidRequest request);

    /**
     * Retrieves a program by its ID.
     *
     * @param id the ID of the program to retrieve
     * @return the program
     */
    ProgramDto getProgram(int id);

    /**
     * Retrieves all programs.
     *
     * @return a list of programs
     */
    List<ProgramDto> getAllPrograms();
}
