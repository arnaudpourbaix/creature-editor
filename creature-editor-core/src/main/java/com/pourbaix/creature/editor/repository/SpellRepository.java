package com.pourbaix.creature.editor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pourbaix.creature.editor.domain.Spell;

public interface SpellRepository extends JpaRepository<Spell, Integer> {

	@Query("SELECT s FROM Spell s JOIN FETCH s.mod WHERE s.id = :id")
	public Spell findById(@Param("id") Integer id);

	@Query("SELECT s FROM Spell s WHERE s.mod.id = :modId ORDER BY s.resource")
	public List<Spell> findByModId(@Param("modId") Integer modId);

	@Query("DELETE FROM Spell s WHERE s.mod.id = :modId")
	@Modifying
	@Transactional
	public void deleteByModId(@Param("modId") Integer modId);

}
