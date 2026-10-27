package com.vflores.service.impl;

import com.vflores.model.Student;
import com.vflores.repo.IGenericRepo;
import com.vflores.repo.IStudentRepo;
import com.vflores.service.IStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl extends CRUDImpl<Student, Integer> implements IStudentService {

    private final IStudentRepo repo ;

    @Override
    protected IGenericRepo<Student, Integer> getRepo() {
        return repo;
    }
}
