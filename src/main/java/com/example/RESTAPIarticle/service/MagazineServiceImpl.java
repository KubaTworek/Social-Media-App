package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.dao.MagazineDAO;
import com.example.RESTAPIarticle.entity.Magazine;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MagazineServiceImpl implements MagazineService{
    private final MagazineDAO magazineDAO;

    public MagazineServiceImpl(MagazineDAO magazineDAO) {
        this.magazineDAO = magazineDAO;
    }

    @Override
    public Magazine findById(int theId) {
        Optional<Magazine> result = magazineDAO.findById(theId);

        Magazine theMagazine = null;

        if (result.isPresent()) {
            theMagazine = result.get();
        }

        return theMagazine;
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
