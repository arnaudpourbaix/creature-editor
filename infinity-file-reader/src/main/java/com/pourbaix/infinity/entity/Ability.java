package com.pourbaix.infinity.entity;

import java.util.ArrayList;
import java.util.List;

import com.pourbaix.infinity.resource.Effect;
import com.pourbaix.infinity.resource.StructEntry;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.ProRef;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.SectionCount;
import com.pourbaix.infinity.resource.datatype.Unknown;
import com.pourbaix.infinity.resource.datatype.UnsignDecNumber;
import com.pourbaix.infinity.resource.key.Keyfile;

public class Ability {
	private static final String s_abilityuse[] = { "", "", "Spell slots", "", "Innate slots" };
	protected static final String[] s_type = { "", "Melee", "Ranged", "Magical", "Launcher" };
	protected static final String[] s_targettype = { "", "Living actor", "Inventory", "Dead actor", "Any point within range", "Caster", "",
			"Caster (keep spell, no animation)" };
	protected static final String[] s_dmgtype = { "None", "Piercing", "Crushing", "Slashing", "Missile", "Fist", "Piercing or crushing",
			"Piercing or slashing", "Crushing or slashing", "Blunt missile" };
	protected static final String[] s_projectile = { "", "None", "Arrow", "Arrow exploding", "Arrow flaming", "Arrow heavy", "Arrow shocking", "Axe",
			"Axe exploding", "Axe flaming", "Axe heavy", "Axe shocking", "Bolt", "Bolt exploding", "Bolt flaming", "Bolt heavy", "Bolt shocking", "Bullet",
			"Bullet exploding", "Bullet flaming", "Bullet heavy", "Bullet shocking", "Burning hands", "Call lightning", "Chromatic orb", "Cone of cold",
			"Cone of fire", "Dagger", "Dagger exploding", "Dagger flaming", "Dagger heavy", "Dagger shocking", "Dart", "Dart exploding", "Dart flaming",
			"Dart heavy", "Dart shocking", "Magic missile", "Fireball", "Ice fragments", "Lightning bolt", "Skipping stone", "Sleep", "Skeleton animation",
			"Smoke ball", "Smoke large", "Smoke small", "Sparkle blue", "Sparkle gold", "Sparkle purple", "Sparkle ice", "Sparkle stone", "Sparkle black",
			"Sparkle chromatic", "Sparkle red", "Sparkle green", "Spear", "Spear exploding", "Spear flaming", "Spear heavy", "Spear shocking", "Star sprite",
			"Stoned", "Web travel", "Web ground", "Gaze", "Holy might", "Flame strike", "Magic missiles 1", "Magic missiles 2", "Magic missiles 3",
			"Magic missiles 4", "Magic missiles 5", "Magic missiles 6", "Magic missiles 7", "Magic missiles 8", "Magic missiles 9", "Magic missiles 10",
			"Magic missiles 11", "Invisible traveling", "Fire bolt", "Call lightning chain 1", "Call lightning chain 2", "Call lightning chain 3",
			"Call lightning chain 4", "Call lightning chain 5", "Call lightning chain 6", "Call lightning chain 7", "Call lightning chain 8",
			"Call lightning chain 9", "Call lightning chain 10", "Call lightning chain 11", "Fire storm", "Call lightning storm", "Instant area effect",
			"Cloud", "Skulltrap", "Color spray", "Ice storm", "Fire wall", "Glyph", "Grease", "Flame arrow green", "Flame arrow blue", "Fireball green",
			"Fireball blue", "Potion", "Potion exploding", "Acid blob", "Agannazar's scorcher", "Travel door", "Glow necromancy", "Glow alteration",
			"Glow enchantment", "Glow abjuration", "Glow illusion", "Glow conjure", "Glow invocation", "Glow divination", "Hit necromancy air",
			"Hit necromancy earth", "Hit necromancy water", "Hit alteration air", "Hit alteration earth", "Hit alteration water", "Hit enchantment air",
			"Hit enchantment earth", "Hit enchantment water", "Hit abjuration air", "Hit abjuration earth", "Hit abjuration water", "Hit illusion air",
			"Hit illusion earth", "Hit illusion water", "Hit conjure air", "Hit conjure earth", "Hit conjure water", "Hit invocation air",
			"Hit invocation earth", "Hit invocation water", "Hit divination air", "Hit divination earth", "Hit divination water", "Hit mushroom fire",
			"Hit mushroom grey", "Hit mushroom green", "Hit shaft fire", "Hit shaft light", "Hit swirl white", "Sparkle area blue", "Sparkle area gold",
			"Sparkle area purple", "Sparkle area ice", "Sparkle area stone", "Sparkle area black", "Sparkle area chromatic", "Sparkle area red",
			"Sparkle area green", "Instant area (party only)", "Instant area (not party)", "Sparkle area blue (party only)", "Sparkle area gold (party only)",
			"Sparkle area purple (party only)", "Sparkle area ice (party only)", "Sparkle area stone (party only)", "Sparkle area black (party only)",
			"Sparkle area chromatic (party only)", "Sparkle area red (party only)", "Sparkle area green (party only)", "Sparkle area blue (not party)",
			"Sparkle area gold (not party)", "Sparkle area purple (not party)", "Sparkle area ice (not party)", "Sparkle area stone (not party)",
			"Sparkle area black (not party)", "Sparkle area chromatic (not party)", "Sparkle area red (not party)", "Sparkle area green (not party)",
			"Sparkle area magenta (not party)", "Sparkle area orange (not party)", "Sparkle area magenta (party only)", "Sparkle area orange (party only)",
			"Sparkle area magenta", "Sparkle area orange", "Sparkle magenta", "Sparkle orange", "Non-sprite area", "Cloudkill", "Flame arrow ice", "Cow",
			"Hold", "Scorcher ice", "Acid blob mustard", "Acid blob grey", "Acid blob ochre", "Red holy might", "Hit necromancy area", "Hit alteration area",
			"Hit enchantment area", "Hit abjuration area", "Hit illusion area", "Hit conjure area", "Hit invocation area", "Hit divination area",
			"Fireball white", "Instant area effect small", "Lightning bolt ground", "Lightning no bounce", "Hit finger of death", "Beholder blast",
			"Hold necromancy", "Fireball ignore center", "Area ignore center", "Chain lightning", "Trap snare", "Small instant area",
			"Small area ignore center", "Glyph trap (not party)", "Small area (not party)", "Disintegrate", "Lightning bolt no bounce", "Spell attack",
			"Medium instant area", "Cloud (not party)", "Golem cloud", "Icewind glyph", "Fire seed", "Icewind insect", "Icewind chain insect", "Meteor swarm",
			"Chain insect", "Icewind glyph hit", "Insect hit", "Energy spear", "Fireball 3d", "Screaming skull", "Small comet", "Wilting",
			"Weird field (not party)", "Anti-fireball", "Skulltrap (not party)", "New lightning bolt (no bounce)", "Pillar of light", "Entangle area effect",
			"Delayed blast fireball", "Trap dart", "Red dragon breath", "Black dragon breath", "Silver dragon breath", "New color spray", "New cone of cold",
			"Holy blight", "Unholy blight", "Prismatic spray", "Instant area large (not party)", "Holy light pillar", "Red dragon hit", "Red dragon middle",
			"Fireball (just projectile)", "New hold necromancy", "Web (one person)", "Holy word (not party)", "Unholy word (not party)", "Power word, sleep",
			"MDK bullet", "Storm of vengeance", "Comet" };

	protected List<StructEntry> list;

	public Ability(byte buffer[], int offset, int number) {
		list = new ArrayList<>();
		list.add(new Bitmap(buffer, offset, 2, "Type", s_type));
		list.add(new Bitmap(buffer, offset + 2, 2, "Ability location", s_abilityuse));
		list.add(new ResourceRef(buffer, offset + 4, "Icon", "BAM"));
		list.add(new Bitmap(buffer, offset + 12, 1, "Target", s_targettype));
		list.add(new UnsignDecNumber(buffer, offset + 13, 1, "# targets"));
		list.add(new DecNumber(buffer, offset + 14, 2, "Range (feet)"));
		list.add(new DecNumber(buffer, offset + 16, 2, "Minimum level"));
		list.add(new DecNumber(buffer, offset + 18, 2, "Casting speed"));
		list.add(new DecNumber(buffer, offset + 20, 2, "Bonus to hit"));
		list.add(new DecNumber(buffer, offset + 22, 2, "Dice size"));
		list.add(new DecNumber(buffer, offset + 24, 2, "# dice thrown"));
		list.add(new DecNumber(buffer, offset + 26, 2, "Damage bonus"));
		list.add(new Bitmap(buffer, offset + 28, 2, "Damage type", s_dmgtype));
		list.add(new SectionCount(buffer, offset + 30, 2, "# effects", Effect.class));
		list.add(new DecNumber(buffer, offset + 32, 2, "First effect index"));
		list.add(new DecNumber(buffer, offset + 34, 2, "# charges"));
		list.add(new Unknown(buffer, offset + 36, 2));
		if (Keyfile.getInstance().resourceExists("PROJECTL.IDS"))
			list.add(new ProRef(buffer, offset + 38, "Projectile"));
		else
			list.add(new Bitmap(buffer, offset + 38, 2, "Projectile", s_projectile));
		// super(superStruct, "Spell ability " + number, buffer, offset);
	}

}
