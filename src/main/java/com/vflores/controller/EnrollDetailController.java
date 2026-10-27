package com.vflores.controller;

import com.vflores.dto.queries.CourseWithStudentsDTO;
import com.vflores.dto.EnrollDetailDTO;
import com.vflores.model.Course;
import com.vflores.model.Enroll;
import com.vflores.model.EnrollDetail;
import com.vflores.model.EnrollDetailPK;
import com.vflores.model.SchoolTerm;
import com.vflores.service.IEnrollDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/enrolldetails")
@RequiredArgsConstructor
public class EnrollDetailController {

    private final IEnrollDetailService service;
    private final ModelMapper defaultMapper;

    @GetMapping
    public ResponseEntity<List<EnrollDetailDTO>> findAll(){
        List<EnrollDetailDTO> list = service.findall().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/course-students")
    public ResponseEntity<List<CourseWithStudentsDTO>> findCoursesWithStudents(@RequestParam(required = false) Boolean enabled,
                                                                               @RequestParam(required = false) Integer year){
        return ResponseEntity.ok(service.findCoursesWithStudents(enabled, year));
    }

    @GetMapping("/{idSchoolTerm}/{idEnroll}/{idCourse}")
    public ResponseEntity<EnrollDetailDTO> findById(@PathVariable Integer idSchoolTerm,
                                                    @PathVariable Integer idEnroll,
                                                    @PathVariable Integer idCourse){
        return ResponseEntity.ok(toDTO(service.findById(toPK(idSchoolTerm, idEnroll, idCourse))));
    }

    @PostMapping
    public ResponseEntity<EnrollDetailDTO> save(@Valid @RequestBody EnrollDetailDTO dto){
        EnrollDetail obj = service.save(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(obj)); //201 CREATED
    }

    @PutMapping("/{idSchoolTerm}/{idEnroll}/{idCourse}")
    public ResponseEntity<EnrollDetailDTO> update(@PathVariable Integer idSchoolTerm,
                                                  @PathVariable Integer idEnroll,
                                                  @PathVariable Integer idCourse,
                                                  @Valid @RequestBody EnrollDetailDTO dto) throws Exception {
        EnrollDetail obj = service.update(toPK(idSchoolTerm, idEnroll, idCourse), toEntity(dto));
        return ResponseEntity.ok(toDTO(obj));
    }

    @DeleteMapping("/{idSchoolTerm}/{idEnroll}/{idCourse}")
    public ResponseEntity<Void> delete(@PathVariable Integer idSchoolTerm,
                                       @PathVariable Integer idEnroll,
                                       @PathVariable Integer idCourse){
        service.delete(toPK(idSchoolTerm, idEnroll, idCourse));
        return ResponseEntity.noContent().build(); //204 NO CONTENT
    }

    private EnrollDetailPK toPK(Integer idSchoolTerm, Integer idEnroll, Integer idCourse){
        SchoolTerm schoolTerm = new SchoolTerm();
        schoolTerm.setIdSchoolTerm(idSchoolTerm);

        Enroll enroll = new Enroll();
        enroll.setIdEnroll(idEnroll);

        Course course = new Course();
        course.setIdCourse(idCourse);

        return new EnrollDetailPK(schoolTerm, enroll, course);
    }

    private EnrollDetailDTO toDTO(EnrollDetail obj){
        return defaultMapper.map(obj, EnrollDetailDTO.class);
    }

    private EnrollDetail toEntity(EnrollDetailDTO dto) {
        return defaultMapper.map(dto, EnrollDetail.class);
    }
}
