package com.alienworkspace.cdr.metadata.service.impl;

import static java.lang.String.format;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.helpers.CurrentUser;
import com.alienworkspace.cdr.metadata.model.mapper.ProgramMapper;
import com.alienworkspace.cdr.metadata.repository.ProgramRepository;
import com.alienworkspace.cdr.metadata.service.ProgramService;
import com.alienworkspace.cdr.model.dto.metadata.ProgramDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Service implementation for Program operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Service
@AllArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramServiceImpl.class);

    private final ProgramRepository programRepository;

    private final ProgramMapper programMapper;

    /**
     * Creates a new program.
     *
     * @param programDto The program details.
     * @return The created program.
     */
    @Override
    public ProgramDto createProgram(ProgramDto programDto) {
        try {
            return programMapper.toProgramDto(programRepository.save(programMapper.toProgram(programDto)));
        } catch (Exception e) {
            LOGGER.error("Failed to create program", e);
            throw new RuntimeException(format("Failed to create program", e), e);
        }
    }

    /**
     * Updates an existing program.
     *
     * @param programId  The program ID.
     * @param programDto The program details.
     * @return The updated program.
     */
    @Override
    public ProgramDto updateProgram(int programId, ProgramDto programDto) {
        return programRepository.findById(programId)
                .map(program -> {
                    try {
                        program.setName(programDto.getName());
                        program.setDescription(programDto.getDescription());
                        program.setActive(programDto.isActive());
                        program.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        program.setLastModifiedAt(LocalDateTime.now());
                        return programMapper.toProgramDto(programRepository.save(program));
                    } catch (Exception e) {
                        throw new RuntimeException(format("Failed to update program: %s", e.getMessage()), e);
                    }
                }).orElseThrow(() ->
                        new ResourceNotFoundException("Program not found with id: " + programId));
    }

    /**
     * Deletes a program.
     *
     * @param id        The program ID.
     * @param recordVoidRequest RecordVoidRequest request object.
     */
    @Override
    public void deleteProgram(int id, RecordVoidRequest recordVoidRequest) {
        programRepository.findById(id)
                .map(program -> {
                    try {
                        program.setVoided(true);
                        program.setVoidedAt(LocalDateTime.now());
                        program.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                        program.setVoidReason(recordVoidRequest.getVoidReason());
                        return programMapper.toProgramDto(programRepository.save(program));
                    } catch (Exception e) {
                        throw new RuntimeException(format("Failed to delete program: %s", e.getMessage()), e);
                    }
                }).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Program not found with id: " + id));
    }

    /**
     * Retrieves a program by its ID.
     *
     * @param id The ID of the program to retrieve.
     * @return The program.
     */
    @Override
    public ProgramDto getProgram(int id) {
        return programMapper.toProgramDto(programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found with id: " + id)));
    }

    /**
     * Retrieves all programs.
     *
     * @return A list of programs.
     */
    @Override
    public List<ProgramDto> getAllPrograms() {
        return programRepository.findAll().stream().map(programMapper::toProgramDto).toList();
    }
}
