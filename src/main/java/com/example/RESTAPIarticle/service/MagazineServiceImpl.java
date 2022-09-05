package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.dao.MagazineDAO;
import com.example.RESTAPIarticle.entity.Magazine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MagazineServiceImpl implements MagazineService{
    private final MagazineDAO magazineDAO;

    @Override
    public Magazine findById(int theId) {
        return magazineDAO.findById(theId).orElseThrow();
    }

    @Override
    public void save(Magazine theMagazine) {
        theMagazine.setId(0);
        magazineDAO.save(theMagazine);
    }

    @Override
    public Magazine findByName(String theName) {
        return magazineDAO.findByName(theName);
    }
}
