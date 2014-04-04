package com.pourbaix.creature.editor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pourbaix.creature.editor.domain.Parameter;
import com.pourbaix.creature.editor.domain.ParameterEnum;

public interface ParameterRepository extends JpaRepository<Parameter, ParameterEnum> {

	@Query("SELECT DISTINCT p FROM Parameter p JOIN FETCH p.parameterType LEFT JOIN FETCH p.parameterValues WHERE p.parameterType.id = :typeId ORDER BY p.name")
	public List<Parameter> findByTypeId(@Param("typeId") String typeId);

	@Query("SELECT DISTINCT p FROM Parameter p JOIN FETCH p.parameterType LEFT JOIN FETCH p.parameterValues WHERE p.name = :name")
	public Parameter findByName(@Param("name") ParameterEnum name);

}
