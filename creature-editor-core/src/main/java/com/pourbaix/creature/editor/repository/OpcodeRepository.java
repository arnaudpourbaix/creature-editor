package com.pourbaix.creature.editor.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.pourbaix.creature.editor.domain.Opcode;

public interface OpcodeRepository extends JpaRepository<Opcode, Integer> {

	@Query("select o from Opcode o left join fetch o.parameters params join fetch params.parameter p left join fetch p.values where o.id = :id")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public Opcode findOpcodeById(@Param("id") int id);

}
