package technology.rocketjump.civimperium.infrastructurefix;

public class Exp1Dep implements StaticModFile {
	@Override
	public String getFilename() {
		return "Exp1.dep";
	}

	@Override
	public String getFileContent() {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
				"<AssetObjects::GameDependencyData>\n" +
				"\t<ID>\n" +
				"\t\t<name text=\"Exp1_Dep\"/>\n" +
				"\t\t<id text=\"7c15d87a-9b91-4344-b7ff-c605e6ac7fda\"/>\n" +
				"\t</ID>\n" +
				"\t<RequiredGameArtIDs>\n" +
				"\t\t<Element>\n" +
				"\t\t\t<name text=\"Expansion1\"/>\n" +
				"\t\t\t<id text=\"7446c8fe-29eb-44f8-801f-098f681cc5c5\"/>\n" +
				"\t\t</Element>\n" +
				"\t</RequiredGameArtIDs>\n" +
				"\t<SystemDependencies>\n" +
				"\t\t<Element>\n" +
				"\t\t\t<ConsumerName text=\"Landmarks\"/>\n" +
				"\t\t\t<ArtDefDependencyPaths>\n" +
				"\t\t\t\t<Element text=\"Landmarks_Exp1.artdef\"/>\n" +
				"\t\t\t</ArtDefDependencyPaths>\n" +
				"\t\t</Element>\n" +
				"\t</SystemDependencies>\n" +
				"</AssetObjects::GameDependencyData>";
	}
}
