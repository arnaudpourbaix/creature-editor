package com.pourbaix.creature.editor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pourbaix.creature.editor.domain.Spell;

public interface SpellRepository extends JpaRepository<Spell, Integer> {

	@Query("SELECT s FROM Spell s JOIN FETCH s.mod")
	public List<Spell> findAllFetchMod();

}
