package com.crudops.skylark.controller;

import com.crudops.skylark.DTO.InfosDTO;
import com.crudops.skylark.response.ResponseHandler;
import com.crudops.skylark.service.InfosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/infos")
public class InfosRestController {

    private final InfosService infosService;

    public InfosRestController(InfosService infosService) {
        this.infosService = infosService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getInfos(@PathVariable("id") Long id) {
        InfosDTO infoDTO = infosService.getInfos(String.valueOf(id));
        return ResponseHandler.responseBuilder("Requested info provided", HttpStatus.OK, infoDTO);
    }

    @GetMapping
    public ResponseEntity<Object> getAllInfos() {
        List<InfosDTO> infosDTOs = infosService.getAllInfos();
        return ResponseHandler.responseBuilder("All infos retrieved successfully", HttpStatus.OK, infosDTOs);
    }

    @PostMapping
    public ResponseEntity<Object> createInfos(@RequestBody InfosDTO infosDTO) {
        InfosDTO createdInfo = infosService.createInfos(infosDTO);
        return ResponseHandler.responseBuilder("Informations created successfully", HttpStatus.CREATED, createdInfo);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateInfos(@PathVariable("id") Long id, @RequestBody InfosDTO infosDTO) {
        String message = infosService.updateInfos(infosDTO);
        return ResponseHandler.responseBuilder(message, HttpStatus.OK, infosDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteInfos(@PathVariable("id") String id) {
        infosService.deleteInfos(id);
        return ResponseHandler.responseBuilder("Informations deleted successfully", HttpStatus.OK, null);
    }
}
