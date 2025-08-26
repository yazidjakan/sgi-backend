package com.jakan.auth_service.transformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractTransformer<E,D> {
    public abstract E toEntity(D dto);
    public abstract D toDto(E entity);
    public List<E> toEntity(List<D> dtos){
        if(dtos == null || dtos.isEmpty()){
            return null;
        }else{
            List<E> entities=new ArrayList<>();
            for(D dto: dtos){
                entities.add(toEntity(dto));
            }
            return entities;
        }
    }
    public List<D> toDto(List<E> entities){
        if(entities == null || entities.isEmpty()){
            return null;
        }else{
            List<D> dtos=new ArrayList<>();
            for(E entity:entities){
                dtos.add(toDto(entity));
            }
            return dtos;
        }
    }
    public Set<E> toEntitySet(Set<D> dtos){
        if(dtos == null || dtos.isEmpty()){
            return null;
        }else{
            Set<E> entities=new HashSet<>();
            for(D dto : dtos){
                entities.add(toEntity(dto));
            }
            return entities;
        }
    }
    public Set<D> toDtoSet(Set<E> entities){
        if(entities == null || entities.isEmpty()){
            return null;
        }else{
            Set<D> dtos=new HashSet<>();
            for(E entity: entities){
                dtos.add(toDto(entity));
            }
            return dtos;
        }
    }
}
