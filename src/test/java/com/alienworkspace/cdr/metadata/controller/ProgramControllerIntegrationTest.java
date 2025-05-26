package com.alienworkspace.cdr.metadata.controller;

import com.alienworkspace.cdr.metadata.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.metadata.model.Program;
import com.alienworkspace.cdr.metadata.repository.ProgramRepository;
import com.alienworkspace.cdr.model.dto.metadata.ProgramDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.alienworkspace.cdr.metadata.helpers.Constants.PROGRAM_BASE_URL;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ProgramControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProgramRepository programRepository;

    @BeforeEach
    public void setUp() {
        programRepository.deleteAll();
    }

    @DisplayName("Test Create Program")
    @Test
    public void testCreateProgram() throws Exception {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(PROGRAM_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(programDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.programId").value(greaterThan(0)))
                .andExpect(jsonPath("$.name").value(programDto.getName()))
                .andExpect(jsonPath("$.description").value(programDto.getDescription()))
                .andExpect(jsonPath("$.active").value(programDto.isActive()))
                .andExpect(jsonPath("$.createdAt").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.createdBy").value(CoreMatchers.notNullValue()));
    }

    @DisplayName("Test Create Program with no program name throws exception")
    @Test
    public void testCreateProgramWithNoProgramName() throws Exception {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .description("Test Description")
                .active(true)
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                post(PROGRAM_BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(programDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.errorCode").value(500))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Failed to create")));
    }


    @DisplayName("Test Update Program")
    @Test
    public void testUpdateProgram() throws Exception {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Updated Program")
                .description("Updated Description")
                .active(false)
                .build();

        Program program = Program.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        Program newProgramDto = programRepository.save(program);

        ResultActions result = mockMvc.perform(
                put(PROGRAM_BASE_URL + "/{programId}", newProgramDto.getProgramId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(programDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.programId").value(newProgramDto.getProgramId()))
                .andExpect(jsonPath("$.name").value(programDto.getName()))
                .andExpect(jsonPath("$.description").value(programDto.getDescription()))
                .andExpect(jsonPath("$.active").value(programDto.isActive()))
                .andExpect(jsonPath("$.lastModifiedAt").value(notNullValue()))
                .andExpect(jsonPath("$.lastModifiedAt").value(notNullValue()));
    }

    @DisplayName("Test Update non existing Program")
    @Test
    public void testUpdateNonExistingProgram() throws Exception {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Updated Program")
                .description("Updated Description")
                .active(false)
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                put(PROGRAM_BASE_URL + "/{programId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(programDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Program not found")));
    }

    @DisplayName("Test Get Program")
    @Test
    public void testGetProgram() throws Exception {
        // Arrange
        Program program = Program.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        Program saved = programRepository.save(program);

        ResultActions result = mockMvc.perform(
                get(PROGRAM_BASE_URL + "/{programId}", saved.getProgramId())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.programId").value(saved.getProgramId()))
                .andExpect(jsonPath("$.name").value(program.getName()))
                .andExpect(jsonPath("$.description").value(program.getDescription()))
                .andExpect(jsonPath("$.active").value(program.isActive()));
    }

    @DisplayName("Test Get All Programs")
    @Test
    public void testGetAllPrograms() throws Exception {
        // Arrange
        List<Program> programs = List.of(
                Program.builder()
                        .name("Test Program 1")
                        .description("Test Description 1")
                        .active(true)
                        .build(),
                Program.builder()
                        .name("Test Program 2")
                        .description("Test Description 2")
                        .active(false)
                        .build());

        // Act
        programRepository.saveAll(programs);

        ResultActions result = mockMvc.perform(
                get(PROGRAM_BASE_URL)
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].programId").value(greaterThan(0)))
                .andExpect(jsonPath("$.[0].name").value(programs.get(0).getName()))
                .andExpect(jsonPath("$.[0].description").value(programs.get(0).getDescription()))
                .andExpect(jsonPath("$.[0].active").value(programs.get(0).isActive()))
                .andExpect(jsonPath("$.[1].programId").value(greaterThan(0)))
                .andExpect(jsonPath("$.[1].name").value(programs.get(1).getName()))
                .andExpect(jsonPath("$.[1].description").value(programs.get(1).getDescription()))
                .andExpect(jsonPath("$.[1].active").value(programs.get(1).isActive()));
    }

    @DisplayName("Test Delete Program")
    @Test
    public void testDeleteProgram() throws Exception {
        // Arrange
        Program program = Program.builder()
                .name("Test Program")
                .description("Test Description")
                .active(true)
                .build();

        // Act
        Program saved = programRepository.save(program);

        mockMvc.perform(
                delete(PROGRAM_BASE_URL + "/{programId}", saved.getProgramId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(RecordVoidRequest.builder()
                                .voidReason("Test Void Reason").build()))
        );

        ResultActions result = mockMvc.perform(
                get(PROGRAM_BASE_URL + "/{programId}", saved.getProgramId())
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.programId").value(saved.getProgramId()))
                .andExpect(jsonPath("$.name").value(program.getName()))
                .andExpect(jsonPath("$.description").value(program.getDescription()))
                .andExpect(jsonPath("$.active").value(program.isActive()))
                .andExpect(jsonPath("$.voided").value(true))
                .andExpect(jsonPath("$.voidedAt").value(notNullValue()))
                .andExpect(jsonPath("$.voidedBy").value(notNullValue()))
                .andExpect(jsonPath("$.voidReason").value("Test Void Reason"));
    }


    @DisplayName("Test Delete non existing Program")
    @Test
    public void testDeleteNonExistingProgram() throws Exception {
        // Arrange
        ProgramDto programDto = ProgramDto.builder()
                .name("Updated Program")
                .description("Updated Description")
                .active(false)
                .build();

        // Act
        ResultActions result = mockMvc.perform(
                delete(PROGRAM_BASE_URL + "/{programId}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(programDto))
        );

        // Assert
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage").value(CoreMatchers.containsString("Program not found")));
    }
}
