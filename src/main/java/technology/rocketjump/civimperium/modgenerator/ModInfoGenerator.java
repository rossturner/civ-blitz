package technology.rocketjump.civimperium.modgenerator;

import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;
import technology.rocketjump.civimperium.modgenerator.sql.ImperiumFileGenerator;

import java.time.Instant;
import java.util.Map;

@Component
public class ModInfoGenerator implements ImperiumFileGenerator {

	private String modName = "UNSET";

	@Override
	public String getFileContents(ModHeader modHeader, Map<CardCategory, Card> selectedCards) {
		modName = modHeader.modName;
		return getModInfoContent(modHeader);
	}

	public String getModInfoContent(ModHeader modHeader) {
		StringBuilder contentBuilder = new StringBuilder();

		contentBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		contentBuilder.append("<Mod id=\"").append(modHeader.id).append("\" version=\"1\">\n" +
				"  <Properties>\n" +
				"    <Name>Imperium ").append(modHeader.modName).append("</Name>\n" +
				"    <Description>").append(modHeader.modDescription).append("</Description>\n" +
				"    <Created>").append(Instant.now().getEpochSecond()).append("</Created>\n" +
				"    <Teaser>").append(modHeader.modDescription).append("</Teaser>\n" +
				"    <Authors>Zsinj</Authors>\n");

		contentBuilder.append("    <CompatibleVersions>2.0</CompatibleVersions>\n" +
				"  </Properties>\n" +
				"  <Dependencies>\n" +
				"    <Mod id=\"4873eb62-8ccc-4574-b784-dda455e74e68\" title=\"Expansion: Gathering Storm\" />\n" +
				"  </Dependencies>\n" +
				"  <References>\n" +
				"    <Mod id=\"E3F53C61-371C-440B-96CE-077D318B36C0\" title=\"DLC: Australia Civilization Pack\" />\n" +
				"    <Mod id=\"02A8BDDE-67EA-4D38-9540-26E685E3156E\" title=\"DLC: Aztec Civilization Pack\" />\n" +
				"    <Mod id=\"1F367231-A040-4793-BDBB-088816853683\" title=\"DLC: Khmer and Indonesia Civilization Pack\" />\n" +
				"    <Mod id=\"643EA320-8E1A-4CF1-A01C-00D88DDD131A\" title=\"DLC: Nubia Civilization Pack\" />\n" +
				"    <Mod id=\"E2749E9A-8056-45CD-901B-C368C8E83DEB\" title=\"DLC: Persia and Macedon Civilization Pack\" />\n" +
				"    <Mod id=\"3809975F-263F-40A2-A747-8BFB171D821A\" title=\"DLC: Poland Civilization Pack\" />\n" +
				"    <Mod id=\"2F6E858A-28EF-46B3-BEAC-B985E52E9BC1\" title=\"DLC: Vikings Content\" />\n" +
				"  </References>\n" +
				"  <FrontEndActions>\n" +
				"    <UpdateDatabase id=\"Update_DB_Frontend\">\n" +
				"      <File>Configuration.sql</File>\n" +
				"    </UpdateDatabase>\n" +
				"    <UpdateColors id=\"Colors\">\n" +
				"      <File>Colors.sql</File>\n" +
				"    </UpdateColors>\n" +
				"    <UpdateText id=\"Text\">\n" +
				"      <File>LeaderText.sql</File>\n" +
				"    </UpdateText>\n" +
				"    <UpdateIcons id=\"Icons\">\n" +
				"      <File>Icons.sql</File>\n" +
				"    </UpdateIcons>\n" +
				"  </FrontEndActions>\n" +
				"  <InGameActions>\n" +
				"    <UpdateDatabase id=\"Update_InGame_DB\">\n" +
				"      <File>Civilization.sql</File>\n" +
				"      <File>CivTraits.sql</File>\n" +
				"      <File>Geography.sql</File>\n" +
				"      <File>Leader.sql</File>\n" +
				"    </UpdateDatabase>\n" +
				"    <UpdateColors id=\"Colors\">\n" +
				"      <File>Colors.sql</File>\n" +
				"    </UpdateColors>\n" +
				"    <UpdateText id=\"Text\">\n" +
				"      <File>LeaderText.sql</File>\n" +
				"    </UpdateText>\n" +
				"    <UpdateIcons id=\"Icons\">\n" +
				"      <File>Icons.sql</File>\n" +
				"    </UpdateIcons>\n" +
				"  </InGameActions>\n" +
				"  <Files>\n" +
				"    <File>Civilization.sql</File>\n" +
				"    <File>CivTraits.sql</File>\n" +
				"    <File>Colors.sql</File>\n" +
				"    <File>Configuration.sql</File>\n" +
				"    <File>Geography.sql</File>\n" +
				"    <File>Icons.sql</File>\n" +
				"    <File>Leader.sql</File>\n" +
				"    <File>LeaderText.sql</File>\n" +
				"  </Files>\n" +
				"</Mod>");

		return contentBuilder.toString();
	}

	@Override
	public String getFilename() {
		return "Imperium"+modName+".modinfo";
	}
}
