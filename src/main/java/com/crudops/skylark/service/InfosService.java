package com.crudops.skylark.service;

import com.crudops.skylark.DTO.InfosDTO;
import com.crudops.skylark.model.Info;
import jakarta.transaction.Transactional;

import java.util.List;

public interface InfosService {



    InfosDTO createInfos(InfosDTO infosDTO);

    String updateInfos(InfosDTO infosDTO);


    InfosDTO getInfosById(Long id);

    List<InfosDTO> getAllInfos();


    @Transactional
    String deleteInfosById(Long id);

    InfosDTO getInfos(String id);

     void deleteInfos(String id);

    @Transactional
    Long getIdByEmail(String email);
}
