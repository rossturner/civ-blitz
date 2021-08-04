package technology.rocketjump.civimperium.infrastructurefix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InfrastructureFixFileProvider {

	private List<StaticModFile> allFiles;

	@Autowired
	public InfrastructureFixFileProvider() {
		allFiles = List.of(
				new DlcIndonesKhmerDep(),
				new DlcMacedPersDep(),
				new DlcNubiaAmanitDep(),
				new Exp1Dep(),
				new LandmarksIndones(),
				new LandmarksMacedPers(),
				new LandmarksNubia(),
				new LandmarksExp1()
		);
	}

	public List<StaticModFile> getAll() {
		return allFiles;
	}

}
