package com.vflores.controller;

import com.vflores.dto.EnrollDTO;
import com.vflores.model.Enroll;
import com.vflores.service.IEnrollService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/enrolls")
@RequiredArgsConstructor
public class EnrollController {

    private final IEnrollService service;
    private final ModelMapper defaultMapper;

    @GetMapping
    public ResponseEntity<List<EnrollDTO>> findAll(){
        List<EnrollDTO> list = service.findall().stream().map(this::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(toDTO(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<EnrollDTO> save(@Valid @RequestBody EnrollDTO dto){
        Enroll obj = service.save(toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(obj)); //201 CREATED
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollDTO> update(@PathVariable Integer id, @Valid @RequestBody EnrollDTO dto) throws Exception {
        Enroll obj = service.update(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build(); //204 NO CONTENT
    }

    private EnrollDTO toDTO(Enroll obj){
        return defaultMapper.map(obj, EnrollDTO.class);
    }

    private Enroll toEntity(EnrollDTO dto) {
        return defaultMapper.map(dto, Enroll.class);
    }
}
