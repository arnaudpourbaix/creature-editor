package com.pourbaix.infinity.resource.spl;

import com.pourbaix.infinity.resource.AbstractAbility;
import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.Effect;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.ProRef;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.SectionCount;
import com.pourbaix.infinity.resource.datatype.Unknown;
import com.pourbaix.infinity.resource.datatype.UnsignDecNumber;
import com.pourbaix.infinity.resource.key.Keyfile;

final class Ability extends AbstractAbility {
	private static final String s_abilityuse[] = { "", "", "Spell slots", "", "Innate slots" };

	Ability() throws Exception {
		super(null, "Spell ability", new byte[40], 0);
	}

	Ability(AbstractStruct superStruct, byte buffer[], int offset, int number) throws Exception {
		super(superStruct, "Spell ability " + number, buffer, offset);
	}

	@Override
	protected int read(byte buffer[], int offset) throws Exception {
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
		return offset + 40;
	}
}
