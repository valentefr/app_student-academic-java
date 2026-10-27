package com.vflores.controller;

import com.vflores.dto.SchoolTermDTO;
import com.vflores.model.SchoolTerm;
import com.vflores.service.ISchoolTermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/schoolterms")
@RequiredArgsConstructor
public class SchoolTermController {

    private final ISchoolTermService service;
    private final ModelMapper defaultMapper;

    @GetMapping
    public ResponseEntity<List<SchoolTermDTO>> findAll(){
        List<SchoolTermDTO> list = service.findall().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolTermDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(toDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<SchoolTermDTO> save(@Valid @RequestBody SchoolTermDTO dto){
        SchoolTerm obj = service.save(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(obj)); //201 CREATED
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolTermDTO> update(@PathVariable Integer id, @Valid @RequestBody SchoolTermDTO dto) throws Exception {
        SchoolTerm obj = service.update(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build(); //204 NO CONTENT
    }

    private SchoolTermDTO toDTO(SchoolTerm obj){
        return defaultMapper.map(obj, SchoolTermDTO.class);
    }

    private SchoolTerm toEntity(SchoolTermDTO dto) {
        return defaultMapper.map(dto, SchoolTerm.class);
    }
}
