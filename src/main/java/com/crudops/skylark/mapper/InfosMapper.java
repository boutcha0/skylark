package com.crudops.skylark.mapper;

import com.crudops.skylark.DTO.InfosDTO;
import com.crudops.skylark.model.Info;
import org.springframework.stereotype.Component;

@Component
public class InfosMapper {

    public Info toEntity(InfosDTO dto) {
        Info info = new Info();
        info.setId(dto.getId());
        info.setEmail(dto.getEmail());
        info.setAdresse(dto.getAdresse());
        info.setName(dto.getName());
        return info;
    }

    public InfosDTO toDto(Info info) {
        InfosDTO dto = new InfosDTO();
        dto.setId(info.getId());
        dto.setEmail(info.getEmail());
        dto.setAdresse(info.getAdresse());
        dto.setName(info.getName());
        return dto;
    }
}
