package technology.rocketjump.civblitz.modgenerator.model;

import technology.rocketjump.civblitz.modgenerator.sql.actsofgod.ActOfGod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModHeader {

	public final String modName;
	public final String modDescription;
	public final UUID id;
	public final List<ActOfGod> actsOfGod = new ArrayList<>();

	public ModHeader(String modName, String modDescription, UUID id) {
		this.modName = modName;
		this.modDescription = modDescription;
		this.id = id;

//		actsOfGod.add(new Cowvalry());
//		actsOfGod.add(new HypatiasBlessing());
	}

}
