package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.StudentPeriodsDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity StudentPeriods and its DTO StudentPeriodsDTO.
 */
@Mapper(componentModel = "spring", uses = {StudentMapper.class, AcademicPeriodsMapper.class, })
public interface StudentPeriodsMapper extends EntityMapper <StudentPeriodsDTO, StudentPeriods> {

    @Mapping(source = "student.idPartyRole", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")

    @Mapping(source = "semester.idPeriod", target = "semesterId")
    @Mapping(source = "semester.description", target = "semesterDescription")
    StudentPeriodsDTO toDto(StudentPeriods studentPeriods); 

    @Mapping(source = "studentId", target = "student")

    @Mapping(source = "semesterId", target = "semester")
    StudentPeriods toEntity(StudentPeriodsDTO studentPeriodsDTO); 

    default StudentPeriods fromId(UUID id) {
        if (id == null) {
            return null;
        }
        StudentPeriods studentPeriods = new StudentPeriods();
        studentPeriods.setIdStudentPeriod(id);
        return studentPeriods;
    }
}
