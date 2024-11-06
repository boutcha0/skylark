package com.crudops.skylark.service;

import com.crudops.skylark.model.Infos;

import java.util.List;

public interface InfosService {

    public String createInfos(Infos infos) ;
    public String updateInfos(Infos infos) ;
    public String deleteInfos(String Id) ;
    public Infos getInfos(String Id) ;
    public List<Infos> getAllInfos() ;
}
