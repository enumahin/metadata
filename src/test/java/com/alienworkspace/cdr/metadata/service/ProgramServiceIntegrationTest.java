package com.alienworkspace.cdr.metadata.service;

import com.alienworkspace.cdr.metadata.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.repository.ProgramRepository;
import com.alienworkspace.cdr.model.dto.metadata.ProgramDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProgramServiceIntegrationTest extends AbstractionContainerBaseTest{

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramService programService;

    @BeforeEach
    public void setUp() {
        programRepository.deleteAll();
    }

    @DisplayName("Test Create Program")
    @Test
    public void testCreateProgram() {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        ProgramDto actualProgramDto = programService.createProgram(programDto);

        // Assert
        assertNotNull(actualProgramDto);
        assertEquals(programDto.getName(), actualProgramDto.getName());
        assertEquals(programDto.getDescription(), actualProgramDto.getDescription());
        assertEquals(programDto.isActive(), actualProgramDto.isActive());
        assertNotNull(actualProgramDto.getCreatedAt());
        assertNotNull(actualProgramDto.getCreatedBy());
    }

    @DisplayName("Test Update Program")
    @Test
    public void testUpdateProgram() {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        ProgramDto newProgramDto = programService.createProgram(programDto);
        newProgramDto.setName("Updated Program");
        newProgramDto.setDescription("Updated Description");
        newProgramDto.setActive(false);
        newProgramDto.setLastModifiedBy(1L);
        ProgramDto actualProgramDto = programService.updateProgram(newProgramDto.getProgramId(), newProgramDto);

        // Assert
        assertNotNull(actualProgramDto);
        assertFalse(actualProgramDto.isActive());
        assertNotNull(actualProgramDto.getLastModifiedAt());
        assertNotNull(actualProgramDto.getLastModifiedBy());
    }

    @DisplayName("Test Update Non Existing Program")
    @Test
    public void testUpdateNonExistingProgram() {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .programId(1)
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> programService.updateProgram(programDto.getProgramId(), programDto));
    }

    @DisplayName("Test Get Program")
    @Test
    public void testGetProgram() {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        ProgramDto newProgramDto = programService.createProgram(programDto);
        ProgramDto actualProgramDto = programService.getProgram(newProgramDto.getProgramId());

        // Assert
        assertNotNull(actualProgramDto);
        assertEquals(newProgramDto.getName(), actualProgramDto.getName());
        assertEquals(newProgramDto.getDescription(), actualProgramDto.getDescription());
        assertEquals(newProgramDto.isActive(), actualProgramDto.isActive());
    }

    @DisplayName("Test Get All Programs")
    @Test
    public void testGetAllPrograms() {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        programService.createProgram(programDto);
        programDto.setName("Test Program 2");
        programService.createProgram(programDto);
        List<ProgramDto> actualProgramDtos = programService.getAllPrograms();

        // Assert
        assertNotNull(actualProgramDtos);
        assertEquals(2, actualProgramDtos.size());
    }

    @DisplayName("Test Delete Program")
    @Test
    public void testDeleteProgram() {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        ProgramDto newProgramDto = programService.createProgram(programDto);
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();
        programService.deleteProgram(newProgramDto.getProgramId(), request);

        // Assert
        ProgramDto actualProgramDtos = programService.getProgram(newProgramDto.getProgramId());
        assertTrue(actualProgramDtos.getVoided());
    }

    @DisplayName(("Test Void Non Existing Program"))
    @Test
    public void testVoidNonExistingProgram() {
        // Arrange
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test Void Reason")
                .build();

        // Act

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> programService.deleteProgram(1, request));
    }
}
