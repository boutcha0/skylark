package com.crudops.skylark.repository;

import com.crudops.skylark.model.Info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfosRepository extends JpaRepository <Info, String> {
    Optional<Info> findByEmail(String email);
}
