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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InfosServiceImpl implements InfosService {

    private final InfosRepository infosRepository;
    private final InfosMapper infosMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public InfosDTO createInfos(InfosDTO infosDTO) {
        if (infosRepository.findByEmail(infosDTO.getEmail()).isPresent()) {
            throw new InfosValidationException("An entry with this email already exists.");
        }

        Info info = infosMapper.toEntity(infosDTO);
        info.setPassword(passwordEncoder.encode(infosDTO.getPassword()));

        Info savedInfo = infosRepository.save(info);
        InfosDTO savedInfoDto = infosMapper.toDto(savedInfo);
        savedInfoDto.setPassword(null);
        return savedInfoDto;
    }

    @Transactional
    @Override
    public String updateInfos(InfosDTO infosDTO) {
        Info existingInfo = infosRepository.findById(String.valueOf(infosDTO.getId()))
                .orElseThrow(() -> new InfosNotFoundException("Info with ID " + infosDTO.getId() + " not found."));

        infosRepository.findByEmail(infosDTO.getEmail()).ifPresent(info -> {
            if (!info.getId().equals(existingInfo.getId())) {
                throw new InfosValidationException("An entry with this email already exists.");
            }
        });

        existingInfo.setEmail(infosDTO.getEmail());
        existingInfo.setName(infosDTO.getName());
        existingInfo.setAdresse(infosDTO.getAdresse());

        if (infosDTO.getPassword() != null && !infosDTO.getPassword().isEmpty()) {
            existingInfo.setPassword(passwordEncoder.encode(infosDTO.getPassword()));
        }

        infosRepository.save(existingInfo);
        return "Informations are updated successfully";
    }

    @Override
    public InfosDTO getInfosById(Long id) {
        Info info = infosRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new InfosNotFoundException("Info with ID " + id + " not found."));

        InfosDTO infosDTO = infosMapper.toDto(info);
        infosDTO.setPassword(null); // Do not return password in DTO
        return infosDTO;
    }

    @Override
    public List<InfosDTO> getAllInfos() {
        List<Info> infos = infosRepository.findAll();
        return infos.stream()
                .map(info -> {
                    InfosDTO infosDTO = infosMapper.toDto(info);
                    infosDTO.setPassword(null); // Do not return password in DTO
                    return infosDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public String deleteInfosById(Long id) {
        Info existingInfo = infosRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new InfosNotFoundException("Info with ID " + id + " not found."));

        infosRepository.delete(existingInfo);
        return "Info with ID " + id + " has been deleted successfully";
    }

    @Override
    public InfosDTO getInfos(String id) {
        Info info = infosRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Info with ID " + id + " not found."));

        InfosDTO infosDTO = infosMapper.toDto(info);
        infosDTO.setPassword(null); // Do not return password in DTO
        return infosDTO;
    }

    @Transactional
    @Override
    public void deleteInfos(String id) {
        Info existingInfo = infosRepository.findById(id)
                .orElseThrow(() -> new InfosNotFoundException("Info with ID " + id + " not found."));

        infosRepository.delete(existingInfo);
    }
}
