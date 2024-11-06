package com.crudops.skylark.controller;

import com.crudops.skylark.model.Infos;
import com.crudops.skylark.response.ResponseHandler;
import com.crudops.skylark.service.InfosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/infos")
public class InfosRestController {


    InfosService infosService;

    public InfosRestController(InfosService infosService) {
        this.infosService = infosService;
    }

//List user infos from DB
@GetMapping("{Id}")
    public ResponseEntity<Object> getinfos(@PathVariable("Id") String Id){

    return ResponseHandler.responseBuilder("Requested infos provided", HttpStatus.OK, infosService.getInfos(Id));

    }
//List All users infos from DB
@GetMapping()
    public List<Infos> getAllInfos(){

        return  infosService.getAllInfos();

    }

@PostMapping
    public String createInfos(@RequestBody Infos infos){

        infosService.createInfos(infos);
        return "Informations are created successfully";
}

@PutMapping
public String updateInfos(@RequestBody Infos infos){

        infosService.updateInfos(infos);
        return "Informations are updated successfully";
    }


@DeleteMapping("{Id}")
public String deleteInfos(@PathVariable("Id") String Id){

        infosService.deleteInfos(Id);
        return "Informations are deleted successfully";
    }


}
