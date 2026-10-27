package com.vflores.service.impl;

import com.vflores.model.Course;
import com.vflores.repo.ICourseRepo;
import com.vflores.repo.IGenericRepo;
import com.vflores.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl extends CRUDImpl<Course, Integer> implements ICourseService {

    private final ICourseRepo repo;

    @Override
    protected IGenericRepo<Course, Integer> getRepo() {
        return repo;
    }
}
