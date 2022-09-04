package com.example.RESTAPIarticle.dao;

import com.example.RESTAPIarticle.entity.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MagazineDAO extends JpaRepository<Magazine, Integer> {
    Magazine findByName(String name);
}
