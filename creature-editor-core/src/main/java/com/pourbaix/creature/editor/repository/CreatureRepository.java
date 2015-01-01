package com.pourbaix.creature.editor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.domain.Game;
import com.pourbaix.creature.editor.domain.Mod;

public interface CreatureRepository extends JpaRepository<Creature, Integer> {
	
	public List<Creature> findByResourceAndGameAndMod(String resource, Game game, Mod mod);
	
	@Query("select distinct c from Creature c order by resource asc")
	@EntityGraph(value = "Creature.lists", type = EntityGraphType.LOAD)
	public List<Creature> findAll();
	
	@EntityGraph(value = "Creature.edit", type = EntityGraphType.LOAD)
	@Query("select distinct c from Creature c where c.id = :id")
	public Creature getCreatureById(@Param("id") Integer id);


}
