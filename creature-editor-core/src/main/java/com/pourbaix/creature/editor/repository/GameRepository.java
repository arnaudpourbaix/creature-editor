package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.Game;
import com.pourbaix.infinity.datatype.GameVersionEnum;

public interface GameRepository extends JpaRepository<Game, GameVersionEnum> {

}
