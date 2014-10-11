package com.pourbaix.creature.editor.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pourbaix.creature.editor.domain.Creature;
import com.pourbaix.creature.editor.repository.CreatureRepository;

@Service
public class CreatureService {

	@Resource
	private CreatureRepository creatureRepository;

	public List<Creature> list() {
		return null;
	}

}
