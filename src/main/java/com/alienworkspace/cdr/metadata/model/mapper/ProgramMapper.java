package com.alienworkspace.cdr.metadata.model.mapper;

import com.alienworkspace.cdr.metadata.model.Program;
import com.alienworkspace.cdr.metadata.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.metadata.ProgramDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * Mapper for Program entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface ProgramMapper {

    ProgramMapper INSTANCE = Mappers.getMapper(ProgramMapper.class);

    /**
     * Maps a ProgramDto to a Program entity.
     *
     * @param programDto The ProgramDto to map.
     * @return The mapped Program entity.
     */
    default Program toProgram(ProgramDto programDto) {
        Program program = Program.builder()
                .programId(programDto.getProgramId())
                .name(programDto.getName())
                .description(programDto.getDescription())
                .active(programDto.isActive())
                .build();
        AuditTrailMapper.mapFromDto(programDto, program);
        return program;
    }

    /**
     * Maps a Program entity to a ProgramDto.
     *
     * @param program The Program entity to map.
     * @return The mapped ProgramDto.
     */
    default ProgramDto toProgramDto(Program program) {
        ProgramDto programDto = ProgramDto.builder()
                .programId(program.getProgramId())
                .name(program.getName())
                .description(program.getDescription())
                .active(program.isActive())
                .build();
        AuditTrailMapper.mapToDto(program, programDto);
        return programDto;
    }
}
