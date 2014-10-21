package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.domain.Game;
import com.pourbaix.creature.editor.domain.Mod;

public interface CreatureRepository extends JpaRepository<Creature, Integer> {
	
	
	@Query("DELETE FROM Creature c WHERE c.resource = :resource AND c.game = :game AND c.mod = :mod")
	@Modifying
	@Transactional
	public void deleteByResourceAndGameAndMod(@Param("resource") String resource, @Param("game") Game game, @Param("mod") Mod mod);

}
