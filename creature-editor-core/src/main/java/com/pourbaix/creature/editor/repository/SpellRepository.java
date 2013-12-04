package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Spell;

public interface SpellRepository extends JpaRepository<Spell, Integer> {

}
