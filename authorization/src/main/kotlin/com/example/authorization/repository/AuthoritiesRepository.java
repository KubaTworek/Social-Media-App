package com.example.authorization.repository;

import com.example.authorization.model.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface AuthoritiesRepository extends JpaRepository<Authorities, Integer> {
    Optional<Authorities> findAuthoritiesByAuthority(String authority);
}
