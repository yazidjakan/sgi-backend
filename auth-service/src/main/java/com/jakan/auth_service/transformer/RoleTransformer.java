package com.jakan.auth_service.transformer;

import com.jakan.auth_service.dto.RoleDto;
import com.jakan.auth_service.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleTransformer extends AbstractTransformer<Role, RoleDto> {
    @Override
    public Role toEntity(RoleDto dto) {
        if(dto == null) {
            return null;
        }else{
            Role entity=new Role();
            entity.setId(dto.id());
            entity.setNom(dto.nom());
            return entity;
        }
    }

    @Override
    public RoleDto toDto(Role entity) {
        if(entity == null) {
            return null;
        }else{
            RoleDto dto=new RoleDto(
                    entity.getId(),
                    entity.getNom()
            );
            return dto;
        }
    }
}
