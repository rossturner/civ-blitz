package technology.rocketjump.civimperium.auth;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import technology.rocketjump.civimperium.codegen.tables.pojos.AuditLog;
import technology.rocketjump.civimperium.codegen.tables.pojos.Match;
import technology.rocketjump.civimperium.codegen.tables.pojos.Player;

import java.time.LocalDateTime;
import java.util.List;

import static technology.rocketjump.civimperium.codegen.tables.AuditLog.AUDIT_LOG;

@Component
public class AuditLogger {

	private final DSLContext create;

	@Autowired
	public AuditLogger(DSLContext create) {
		this.create = create;
	}

	public void record(Player admin, String action, Match match) {
		AuditLog newLog = new AuditLog();
		newLog.setPlayerId(admin.getPlayerId());
		newLog.setDiscordUsername(admin.getDiscordUsername());
		newLog.setDatetime(LocalDateTime.now());
		newLog.setAction(action);
		newLog.setMatchId(match.getMatchId());
		create.newRecord(AUDIT_LOG, newLog).store();
	}

	public List<AuditLog> getRecentLogs() {
		return create.selectFrom(AUDIT_LOG)
				.orderBy(AUDIT_LOG.DATETIME.desc())
				.limit(50)
				.fetchInto(AuditLog.class);
	}

}
