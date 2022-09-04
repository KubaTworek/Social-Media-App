package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.entity.Magazine;
public interface MagazineService {
    Magazine findById(int theId);
    void save(Magazine theMagazine);
    Magazine findByName(String theName);
}
