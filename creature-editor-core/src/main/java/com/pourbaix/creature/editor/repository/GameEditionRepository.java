package com.pourbaix.creature.editor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pourbaix.creature.editor.domain.GameEdition;
import com.pourbaix.creature.editor.domain.GameEditionEnum;

public interface GameEditionRepository extends JpaRepository<GameEdition, GameEditionEnum> {

}
