package technology.rocketjump.civblitz.infrastructurefix;

public class DlcNubiaAmanitDep implements StaticModFile {
	@Override
	public String getFilename() {
		return "DLC_Nubia_Amanit.dep";
	}

	@Override
	public String getFileContent() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
				"<AssetObjects::GameDependencyData>\n" +
				"\t<ID>\n" +
				"\t\t<name text=\"DLC_Nubia_Amanit_Dep\"/>\n" +
				"\t\t<id text=\"7c15d87a-9b91-4344-b7ff-c605e6ac7fda\"/>\n" +
				"\t</ID>\n" +
				"\t<RequiredGameArtIDs>\n" +
				"\t\t<Element>\n" +
				"\t\t\t<name text=\"DLC5\"/>\n" +
				"\t\t\t<id text=\"754001af-1b3f-43a5-8138-8fb13e2bf173\"/>\n" +
				"\t\t</Element>\n" +
				"\t</RequiredGameArtIDs>\n" +
				"\t<SystemDependencies>\n" +
				"\t\t<Element>\n" +
				"\t\t\t<ConsumerName text=\"Landmarks\"/>\n" +
				"\t\t\t<ArtDefDependencyPaths>\n" +
				"\t\t\t\t<Element text=\"Landmarks_DLC_Nubia_Amanit.artdef\"/>\n" +
				"\t\t\t</ArtDefDependencyPaths>\n" +
				"\t\t</Element>\n" +
				"\t</SystemDependencies>\n" +
				"</AssetObjects::GameDependencyData>";
	}
}
