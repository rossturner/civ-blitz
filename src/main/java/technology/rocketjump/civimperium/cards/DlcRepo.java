package technology.rocketjump.civimperium.cards;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;
import technology.rocketjump.civimperium.codegen.tables.pojos.PlayerDlcSetting;

import java.util.List;

import static technology.rocketjump.civimperium.codegen.tables.PlayerDlcSetting.PLAYER_DLC_SETTING;

@Component
public class DlcRepo {

	private final DSLContext create;

	@Autowired
	public DlcRepo(DSLContext create) {
		this.create = create;
	}

	public List<PlayerDlcSetting> getAllForPlayer(Player player) {
		return create.selectFrom(PLAYER_DLC_SETTING)
				.where(PLAYER_DLC_SETTING.PLAYER_ID.eq(player.getPlayerId()))
				.fetchInto(PlayerDlcSetting.class);
	}

	public void addSetting(Player player, String selectedDlcName) {
		PlayerDlcSetting setting = new PlayerDlcSetting();
		setting.setPlayerId(player.getPlayerId());
		setting.setDlcName(selectedDlcName);
		create.newRecord(PLAYER_DLC_SETTING, setting).store();
	}
}
