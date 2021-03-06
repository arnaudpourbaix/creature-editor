package com.pourbaix.infinity.resource.are;

import com.pourbaix.infinity.resource.AbstractStruct;
import com.pourbaix.infinity.resource.cre.CreResource;
import com.pourbaix.infinity.resource.datatype.Bitmap;
import com.pourbaix.infinity.resource.datatype.DecNumber;
import com.pourbaix.infinity.resource.datatype.Flag;
import com.pourbaix.infinity.resource.datatype.HexNumber;
import com.pourbaix.infinity.resource.datatype.IdsBitmap;
import com.pourbaix.infinity.resource.datatype.ResourceRef;
import com.pourbaix.infinity.resource.datatype.TextString;
import com.pourbaix.infinity.resource.datatype.Unknown;

public final class Actor extends AbstractStruct {
	public static final String[] s_orientation = { "South", "SSW", "SW", "WSW", "West", "WNW", "NW", "NNW", "North", "NNE", "NE", "ENE", "East", "ESE", "SE",
			"SSE" };
	private static final String[] s_noyes = { "No", "Yes" };
	private static final String[] s_yesno = { "CRE attached", "CRE not attached", "Has seen party", "Toggle invulnerability", "Override script name" };
	static final String[] s_schedule = { "Not active", "00:30-01:29", "01:30-02:29", "02:30-03:29", "03:30-04:29", "04:30-05:29", "05:30-06:29", "06:30-07:29",
			"07:30-08:29", "08:30-09:29", "09:30-10:29", "10:30-11:29", "11:30-12:29", "12:30-13:29", "13:30-14:29", "14:30-15:29", "15:30-16:29",
			"16:30-17:29", "17:30-18:29", "18:30-19:29", "19:30-20:29", "20:30-21:29", "21:30-22:29", "22:30-23:29", "23:30-00:29" };

	public Actor() throws Exception {
		super(null, "Actor", new byte[272], 0);
	}

	public Actor(AbstractStruct superStruct, byte buffer[], int offset, int nr) throws Exception {
		super(superStruct, "Actor " + nr, buffer, offset);
	}

	@Override
	protected int read(byte buffer[], int offset) throws Exception {
		list.add(new TextString(buffer, offset, 32, "Name"));
		list.add(new DecNumber(buffer, offset + 32, 2, "Position: X"));
		list.add(new DecNumber(buffer, offset + 34, 2, "Position: Y"));
		list.add(new DecNumber(buffer, offset + 36, 2, "Destination: X"));
		list.add(new DecNumber(buffer, offset + 38, 2, "Destination: Y"));
		// if (ResourceFactory.getGameID() == ResourceFactory.ID_BG2 ||
		// ResourceFactory.getGameID() == ResourceFactory.ID_BG2TOB)
		list.add(new Flag(buffer, offset + 40, 4, "Loading", s_yesno));
		// else
		// list.add(new Bitmap(buffer, offset + 40, 4, "Is visible?", s_noyes));
		list.add(new Bitmap(buffer, offset + 44, 2, "Is spawned?", s_noyes));
		list.add(new Unknown(buffer, offset + 46, 2));
		list.add(new IdsBitmap(buffer, offset + 48, 4, "Animation", "ANIMATE.IDS"));
		list.add(new Bitmap(buffer, offset + 52, 2, "Orientation", s_orientation));
		list.add(new Unknown(buffer, offset + 54, 2));
		list.add(new DecNumber(buffer, offset + 56, 4, "Expiry time"));
		list.add(new DecNumber(buffer, offset + 60, 2, "Wander distance"));
		list.add(new DecNumber(buffer, offset + 62, 2, "Follow distance"));
		list.add(new Flag(buffer, offset + 64, 4, "Present at", s_schedule));
		list.add(new DecNumber(buffer, offset + 68, 4, "# times talked to"));
		list.add(new ResourceRef(buffer, offset + 72, "Dialogue", "DLG"));
		list.add(new ResourceRef(buffer, offset + 80, "Override script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 88, "General script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 96, "Class script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 104, "Race script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 112, "Default script", "BCS"));
		list.add(new ResourceRef(buffer, offset + 120, "Specifics script", "BCS"));
		if (buffer[offset + 128] == 0x2a) // *
			list.add(new TextString(buffer, offset + 128, 8, "Character"));
		else
			list.add(new ResourceRef(buffer, offset + 128, "Character", "CRE"));
		HexNumber creOffset = new HexNumber(buffer, offset + 136, 4, "CRE structure offset");
		list.add(creOffset);
		list.add(new DecNumber(buffer, offset + 140, 4, "CRE structure size"));
		list.add(new Unknown(buffer, offset + 144, 128));

		if (creOffset.getValue() != 0)
			list.add(new CreResource(this, "CRE file", buffer, creOffset.getValue()));

		return offset + 272;
	}
}
