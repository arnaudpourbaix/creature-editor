package com.pourbaix.infinity.factory;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pourbaix.infinity.datatype.Flag;
import com.pourbaix.infinity.datatype.IdsEnum;
import com.pourbaix.infinity.domain.Creature;
import com.pourbaix.infinity.resource.StringResource;
import com.pourbaix.infinity.resource.StringResourceException;
import com.pourbaix.infinity.resource.key.Keyfile;
import com.pourbaix.infinity.resource.key.ResourceEntry;
import com.pourbaix.infinity.util.DynamicArray;

@Service
public class CreatureFactory {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CreatureFactory.class);

	@Autowired
	private IdentifierFactory identifierFactory;

	private static final String[] FLAGS = { "No flags set", "Identified", "No corpse", "Permanent corpse", "Original class: Fighter", "Original class: Mage",
			"Original class: Cleric", "Original class: Thief", "Original class: Druid", "Original class: Ranger", "Fallen paladin", "Fallen ranger",
			"Export allowed", "Hide status", "Large creature", "Moving between areas", "Been in party", "Holding item", "Clear all flags", "", "", "", "", "",
			"", "Allegiance tracking", "General tracking", "Race tracking", "Class tracking", "Specifics tracking", "Gender tracking", "Alignment tracking",
			"Uninterruptible" };

	private static final String s_attacks[] = { "0", "1", "2", "3", "4", "5", "1/2", "3/2", "5/2", "7/2", "9/2" };

	public Creature getCreature(String entryName) throws FactoryException {
		ResourceEntry entry = Keyfile.getInstance().getResourceEntry(entryName);
		if (entry == null) {
			throw new FactoryException("Entry doesn't exist: " + entryName);
		}
		return getCreature(entry);
	}

	public Creature getCreature(ResourceEntry entry) throws FactoryException {
		try {
			Creature creature = new Creature();
			creature.setResource(entry.getResourceName());
			byte buffer[] = entry.getResourceData();
			creature.setName(StringResource.getInstance().getStringRef(buffer, 0x08));
			creature.setTooltip(StringResource.getInstance().getStringRef(buffer, 0x0c));
			creature.setFlags(new Flag((long) DynamicArray.getInt(buffer, 0x10), 4, FLAGS));
			creature.setExperienceValue(DynamicArray.getInt(buffer, 0x14));
			creature.setPowerLevelOrExperience(DynamicArray.getInt(buffer, 0x18));
			creature.setGold(DynamicArray.getInt(buffer, 0x1c));
			creature.setStatus(identifierFactory.getFirstValueByKey(IdsEnum.State.getResource(), (long) DynamicArray.getInt(buffer, 0x20)));
			creature.setCurrentHitPoint(DynamicArray.getShort(buffer, 0x24));
			creature.setMaximumHitPoint(DynamicArray.getShort(buffer, 0x26));
			creature.setAnimationId(DynamicArray.getInt(buffer, 0x28));
			creature.setAnimationLabel(identifierFactory.getFirstValueByKey(IdsEnum.Animation.getResource(), (long) creature.getAnimationId()));
			creature.setEffectFlag(DynamicArray.getByte(buffer, 0x33));
			creature.setReputation(DynamicArray.getUnsignedByte(buffer, 0x44));
			creature.setHideInShadows(DynamicArray.getUnsignedByte(buffer, 0x45));
			creature.setNaturalAC(DynamicArray.getShort(buffer, 0x46));
			creature.setEffectiveAC(DynamicArray.getShort(buffer, 0x48));
			creature.setCrushingAC(DynamicArray.getShort(buffer, 0x4a));
			creature.setMissileAC(DynamicArray.getShort(buffer, 0x4c));
			creature.setPiercingAC(DynamicArray.getShort(buffer, 0x4e));
			creature.setSlashingAC(DynamicArray.getShort(buffer, 0x50));
			creature.setThac0(DynamicArray.getByte(buffer, 0x52));
			creature.setReputation((byte) DynamicArray.getUnsignedByte(buffer, 0x44));

			// list.add(new Bitmap(buffer, offset + 75, 1, "# attacks", s_attacks));
			// list.add(new DecNumber(buffer, offset + 76, 1, "Save vs. death"));
			// list.add(new DecNumber(buffer, offset + 77, 1, "Save vs. wand"));
			// list.add(new DecNumber(buffer, offset + 78, 1, "Save vs. polymorph"));
			// list.add(new DecNumber(buffer, offset + 79, 1, "Save vs. breath"));
			// list.add(new DecNumber(buffer, offset + 80, 1, "Save vs. spell"));
			// list.add(new DecNumber(buffer, offset + 81, 1, "Resist fire"));
			// list.add(new DecNumber(buffer, offset + 82, 1, "Resist cold"));
			// list.add(new DecNumber(buffer, offset + 83, 1, "Resist electricity"));
			// list.add(new DecNumber(buffer, offset + 84, 1, "Resist acid"));
			// list.add(new DecNumber(buffer, offset + 85, 1, "Resist magic"));
			// list.add(new DecNumber(buffer, offset + 86, 1, "Resist magic fire"));
			// list.add(new DecNumber(buffer, offset + 87, 1, "Resist magic cold"));
			// list.add(new DecNumber(buffer, offset + 88, 1, "Resist slashing"));
			// list.add(new DecNumber(buffer, offset + 89, 1, "Resist crushing"));
			// list.add(new DecNumber(buffer, offset + 90, 1, "Resist piercing"));
			// list.add(new DecNumber(buffer, offset + 91, 1, "Resist missile"));

			logger.debug(creature.toString());
			return creature;
		} catch (IOException | StringResourceException e) {
			throw new FactoryException(entry.getResourceName() + ": " + e.getMessage());
		}
	}
}
