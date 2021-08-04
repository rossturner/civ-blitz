package technology.rocketjump.civimperium.infrastructurefix;

public class DlcMacedPersDep implements StaticModFile {
	@Override
	public String getFilename() {
		return "DLC_Maced_Pers.dep";
	}

	@Override
	public String getFileContent() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
				"<AssetObjects::GameDependencyData>\n" +
				"\t<ID>\n" +
				"\t\t<name text=\"DLC_Maced_Pers_Dep\"/>\n" +
				"\t\t<id text=\"7c15d87a-9b91-4344-b7ff-c605e6ac7fda\"/>\n" +
				"\t</ID>\n" +
				"\t<RequiredGameArtIDs>\n" +
				"\t\t<Element>\n" +
				"\t\t\t<name text=\"DLC4\"/>\n" +
				"\t\t\t<id text=\"d5642886-43d5-4ce9-9465-3a47b5c70096\"/>\n" +
				"\t\t</Element>\n" +
				"\t</RequiredGameArtIDs>\n" +
				"\t<SystemDependencies>\n" +
				"\t\t<Element>\n" +
				"\t\t\t<ConsumerName text=\"Landmarks\"/>\n" +
				"\t\t\t<ArtDefDependencyPaths>\n" +
				"\t\t\t\t<Element text=\"Landmarks_DLC_Maced_Pers.artdef\"/>\n" +
				"\t\t\t</ArtDefDependencyPaths>\n" +
				"\t\t</Element>\n" +
				"\t</SystemDependencies>\n" +
				"</AssetObjects::GameDependencyData>";
	}
}
