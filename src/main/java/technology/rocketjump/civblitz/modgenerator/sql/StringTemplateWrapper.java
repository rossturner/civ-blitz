package technology.rocketjump.civblitz.modgenerator.sql;

import org.stringtemplate.v4.ST;

import java.util.Map;

public class StringTemplateWrapper {


	public static String ST(String baseString, Map<String, String> replacements) {
		ST templated = new ST(baseString);
		for (Map.Entry<String, String> entry : replacements.entrySet()) {
			templated.add(entry.getKey(), entry.getValue());
		}
		return templated.render();
	}
}
