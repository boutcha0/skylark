package com.crudops.skylark.service;

import com.crudops.skylark.DTO.InfosDTO;
import com.crudops.skylark.model.Info;
import jakarta.transaction.Transactional;

import java.util.List;

public interface InfosService {



    InfosDTO createInfos(InfosDTO infosDTO);

    String updateInfos(InfosDTO infosDTO);

    public void deleteInfos(String Id) ;
    public InfosDTO getInfos(String Id) ;
    public List<InfosDTO> getAllInfos() ;
}
