package technology.rocketjump.civblitz.model;

import org.stringtemplate.v4.ST;

public class SimpleFormatTest {


	public static void main(String... args) {

		String base = "Hello <name>";
		ST templated = new ST(base);
		templated.add("name", "Zsinj");
		System.out.println(templated.render());

	}
}
