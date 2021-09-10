package technology.rocketjump.civblitz.modgenerator.sql;

import technology.rocketjump.civblitz.modgenerator.model.ModHeader;
import technology.rocketjump.civblitz.modgenerator.model.ModdedCivInfo;

import java.util.List;

public abstract class BlitzFileGenerator {

	public abstract String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo);

	public abstract String getFilename();

	public String getFileContents(ModHeader modHeader, List<ModdedCivInfo> civs) {
		StringBuilder builder = new StringBuilder();
		for (ModdedCivInfo civ : civs) {
			builder.append(getFileContents(modHeader, civ)).append("\n\n");
		}
		return builder.toString();
	}

}
