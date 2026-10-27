package com.vflores.service.impl;

import com.vflores.model.Enroll;
import com.vflores.repo.IEnrollRepo;
import com.vflores.repo.IGenericRepo;
import com.vflores.service.IEnrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollServiceImpl extends CRUDImpl<Enroll, Integer> implements IEnrollService {

    private final IEnrollRepo repo;

    @Override
    protected IGenericRepo<Enroll, Integer> getRepo() {
        return repo;
    }
}
