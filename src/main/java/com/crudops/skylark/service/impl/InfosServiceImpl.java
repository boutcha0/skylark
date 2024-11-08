package com.crudops.skylark.service.impl;

import com.crudops.skylark.DTO.InfosDTO;
import com.crudops.skylark.exception.InfosNotFoundException;
import com.crudops.skylark.exception.InfosValidationException;
import com.crudops.skylark.mapper.InfosMapper;
import com.crudops.skylark.model.Info;
import com.crudops.skylark.repository.InfosRepository;
import com.crudops.skylark.service.InfosService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfosServiceImpl implements InfosService {

    private static final Logger logger = LoggerFactory.getLogger(InfosServiceImpl.class);
    private final InfosRepository infosRepository;
    private final InfosMapper infosMapper;


    @Transactional
    @Override
    public InfosDTO createInfos(InfosDTO infosDTO) {
        Info info = infosMapper.toEntity(infosDTO);

        // Validate email uniqueness only
        if (infosRepository.findByEmail(info.getEmail()).isPresent()) {
            throw new InfosValidationException("An entry with this email already exists.");
        }

        // Save the entity; the ID will be auto-generated
        Info savedInfo = infosRepository.save(info);
        logger.info("HTTP Status: {}, Message: Info created successfully with ID: {}", HttpStatus.CREATED, savedInfo.getId());

        // Convert saved entity back to DTO and return
        return infosMapper.toDto(savedInfo);
    }


    @Transactional
    @Override
    public String updateInfos(InfosDTO infosDTO) {
        Info info = infosMapper.toEntity(infosDTO);

        if (!infosRepository.existsById(String.valueOf(info.getId()))) {
            throw new InfosNotFoundException("Info with ID " + info.getId() + " not found.");
        }

        infosRepository.findByEmail(info.getEmail()).ifPresent(existingInfo -> {
            if (!existingInfo.getId().equals(info.getId())) {
                throw new InfosValidationException("An entry with this email already exists.");
            }
        });

        infosRepository.save(info);
        logger.info("HTTP Status: {}, Message: Informations updated successfully for ID: {}", HttpStatus.OK, info.getId());

        return "Informations are updated successfully";
    }

    @Transactional
    @Override
    public void deleteInfos(String id) {
        if (!infosRepository.existsById(id)) {
            throw new InfosNotFoundException("Info with ID " + id + " not found.");
        }

        infosRepository.deleteById(id);
        logger.info("HTTP Status: {}, Message: Info deleted successfully with ID: {}", HttpStatus.OK, id);
    }

    @Transactional
    @Override
    public InfosDTO getInfos(String id) {
        Info info = infosRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Requested information does not exist."));
        logger.info("HTTP Status: {}, Message: Info retrieved successfully for ID: {}", HttpStatus.OK, id);

        return infosMapper.toDto(info);
    }

    @Transactional
    @Override
    public List<InfosDTO> getAllInfos() {
        List<Info> allInfos = infosRepository.findAll();
        logger.info("HTTP Status: {}, Message: All infos retrieved successfully", HttpStatus.OK);

        return allInfos.stream().map(infosMapper::toDto).collect(Collectors.toList());
    }
}
