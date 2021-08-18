package technology.rocketjump.civimperium.modgenerator.sql;

import technology.rocketjump.civimperium.modgenerator.model.ModHeader;
import technology.rocketjump.civimperium.modgenerator.model.ModdedCivInfo;

public interface ImperiumFileGenerator {

	String getFileContents(ModHeader modHeader, ModdedCivInfo civInfo);

	String getFilename();

}
