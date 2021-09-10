package technology.rocketjump.civblitz.model;

public class PlayerDlcResponse {

	private final String dlcName;
	private final boolean enabled;

	public PlayerDlcResponse(String dlcName, boolean enabled) {
		this.dlcName = dlcName;
		this.enabled = enabled;
	}

	public String getDlcName() {
		return dlcName;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
