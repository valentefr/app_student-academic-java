package com.vflores.controller;

import com.vflores.dto.CourseDTO;
import com.vflores.model.Course;
import com.vflores.service.ICourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService service;
    private final ModelMapper defaultMapper;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> findAll(){
        List<CourseDTO> list = service.findall().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(toDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CourseDTO> save(@Valid @RequestBody CourseDTO dto){
        Course obj = service.save(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(obj)); //201 CREATED
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable Integer id, @Valid @RequestBody CourseDTO dto) throws Exception {
        Course obj = service.update(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build(); //204 NO CONTENT
    }

    private CourseDTO toDTO(Course obj){
        return defaultMapper.map(obj, CourseDTO.class);
    }

    private Course toEntity(CourseDTO dto) {
        return defaultMapper.map(dto, Course.class);
    }
}
