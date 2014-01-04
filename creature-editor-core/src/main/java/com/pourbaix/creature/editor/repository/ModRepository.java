package com.pourbaix.creature.editor.repository;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.pourbaix.creature.editor.domain.Mod;

public interface ModRepository extends JpaRepository<Mod, Integer> {

	@Query("select m from Mod m where upper(m.name) = upper(:name)")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
	public Mod findByName(@Param("name") String name);

}
