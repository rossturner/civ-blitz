package technology.rocketjump.civimperium.modgenerator.model;

import java.util.UUID;

public class ModHeader {

	public final String modName;
	public final String modDescription;
	public final UUID id;

	private String startBiasCivType;

	public ModHeader(String modName, String modDescription, UUID id) {
		this.modName = modName;
		this.modDescription = modDescription;
		this.id = id;
	}

	public String getStartBiasCivType() {
		return startBiasCivType;
	}

	public void setStartBiasCivType(String startBiasCivType) {
		this.startBiasCivType = startBiasCivType;
	}
}
