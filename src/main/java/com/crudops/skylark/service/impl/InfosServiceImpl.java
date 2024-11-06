package com.crudops.skylark.service.impl;

import com.crudops.skylark.exception.InfosNotFoundException;
import com.crudops.skylark.model.Infos;
import com.crudops.skylark.repository.InfosRepository;
import com.crudops.skylark.service.InfosService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfosServiceImpl implements InfosService {

    InfosRepository infosRepository ;

    public InfosServiceImpl(InfosRepository infosRepository) {
        this.infosRepository = infosRepository;
    }


    @Override
    public String createInfos(Infos infos) {
        infosRepository.save(infos);
        return "Success";
    }

    @Override
    public String updateInfos(Infos infos) {
        infosRepository.save(infos);
        return "updated";
    }

    @Override
    public String deleteInfos(String Id) {
        infosRepository.deleteById(Id);
        return "Deleted";
    }

    @Override
    public Infos getInfos(String Id) {

        if(infosRepository.findById(Id).isEmpty())

            throw new InfosNotFoundException("Requested Infromations does not exist");
            return infosRepository.findById(Id).get();
    }

    @Override
    public List<Infos> getAllInfos() {
        return infosRepository.findAll();
    }
}
