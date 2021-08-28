package technology.rocketjump.civimperium.matches.objectives;

import technology.rocketjump.civimperium.codegen.tables.pojos.PublicObjective;

import java.util.List;

public class PublicObjectiveWithClaimants extends PublicObjective {

	private final List<String> claimedByPlayerIds;

	public PublicObjectiveWithClaimants(PublicObjective publicObjective, List<String> claimedByPlayerIds) {
		super(publicObjective);
		this.claimedByPlayerIds = claimedByPlayerIds;
	}

	public List<String> getClaimedByPlayerIds() {
		return claimedByPlayerIds;
	}

}
