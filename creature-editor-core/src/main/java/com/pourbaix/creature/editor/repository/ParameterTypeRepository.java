package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.ParameterType;

public interface ParameterTypeRepository extends JpaRepository<ParameterType, String> {

}
