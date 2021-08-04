package technology.rocketjump.civimperium.infrastructurefix;

public class DlcIndonesKhmerDep implements StaticModFile {
	@Override
	public String getFilename() {
		return "DLC_Indones_Khmer.dep";
	}

	@Override
	public String getFileContent() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
				"<AssetObjects::GameDependencyData>\n" +
				"\t<ID>\n" +
				"\t\t<name text=\"DLC_Indones_Khmer_Dep\"/>\n" +
				"\t\t<id text=\"7c15d87a-9b91-4344-b7ff-c605e6ac7fda\"/>\n" +
				"\t</ID>\n" +
				"\t<RequiredGameArtIDs>\n" +
				"\t\t<Element>\n" +
				"\t\t\t<name text=\"DLC6\"/>\n" +
				"\t\t\t<id text=\"4fd22d21-5ec7-4ce9-aca6-8d8b623e5a82\"/>\n" +
				"\t\t</Element>\n" +
				"\t</RequiredGameArtIDs>\n" +
				"\t<SystemDependencies>\n" +
				"\t\t<Element>\n" +
				"\t\t\t<ConsumerName text=\"Landmarks\"/>\n" +
				"\t\t\t<ArtDefDependencyPaths>\n" +
				"\t\t\t\t<Element text=\"Landmarks_DLC_Indones_Khmer.artdef\"/>\n" +
				"\t\t\t</ArtDefDependencyPaths>\n" +
				"\t\t</Element>\n" +
				"\t</SystemDependencies>\n" +
				"</AssetObjects::GameDependencyData>";
	}
}
