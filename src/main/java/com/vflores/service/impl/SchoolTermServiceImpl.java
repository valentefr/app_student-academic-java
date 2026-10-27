package com.vflores.service.impl;

import com.vflores.model.SchoolTerm;
import com.vflores.repo.IGenericRepo;
import com.vflores.repo.ISchoolTermRepo;
import com.vflores.service.ISchoolTermService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolTermServiceImpl extends CRUDImpl<SchoolTerm, Integer> implements ISchoolTermService {

    private final ISchoolTermRepo repo;

    @Override
    protected IGenericRepo<SchoolTerm, Integer> getRepo() {
        return repo;
    }
}
