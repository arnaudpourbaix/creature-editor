package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Mod;

public interface ModRepository extends JpaRepository<Mod, Integer> {

	public Mod findByName(String name);

}
