package com.pourbaix.creature.editor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pourbaix.creature.editor.domain.Parameter;

public interface ParameterRepository extends JpaRepository<Parameter, String> {

	@Query("SELECT p FROM Parameter p WHERE p.parameterType.id = :typeId ORDER BY p.name")
	public List<Parameter> findByTypeId(@Param("typeId") String typeId);

}
