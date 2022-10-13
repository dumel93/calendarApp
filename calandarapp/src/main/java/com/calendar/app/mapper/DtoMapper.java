package com.calendar.app.mapper;

import java.util.Set;
import java.util.stream.Collectors;

public interface DtoMapper<E, D> {

    D toDTO(E entity);

    default Set<D> toDTO(Set<E> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }
}
