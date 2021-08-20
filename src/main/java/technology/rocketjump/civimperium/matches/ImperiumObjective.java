package technology.rocketjump.civimperium.matches;

import static technology.rocketjump.civimperium.matches.ImperiumObjective.ObjectiveType.PUBLIC;
import static technology.rocketjump.civimperium.matches.ImperiumObjective.ObjectiveType.SECRET;

public enum ImperiumObjective {

	// Public objectives
	IND_REVOLUTION("Industrial Revolution", 1, "Build and power a factory", PUBLIC),
	WARLORD("Warlord", 1, "Capture or raze a total of 5 enemy cities from opposing Civilizations. This total must be reached by capturing or razing cities from at least 2 different opponents.", PUBLIC),
	DIPLOMAT("Diplomat", 1, "Have Suzerainty over 3 different city states at the same time.", PUBLIC),
	PREACHER("Preacher", 1, "Control the Holy City of a religion enhanced with four beliefs.", PUBLIC),
	LITERATURE("Literature", 1, "Own 5 Great Works of Writing.", PUBLIC),
	THEOLOGIAN("Theologian", 2, "Have three Holy Cities follow your religion (including your own).", PUBLIC),
	ARCHITECT("Architect", 2, "Complete 5 Wonders, at least one of which must be from the Renaissance Era or later.", PUBLIC),
	POP_EXPLOSION("Population Explosion", 3, "Achieve the Historic Moment 'World's First Gigantic City' (25 population)", PUBLIC),

	// Secret objectives
	SCIENTIST("Scientist", 1, "Generate at least 150 Science Per Turn", SECRET),
	BISHOP("Bishop", 1, "Generate at least 150 Faith Per Turn", SECRET),
	ENLIGHTENED("Enlightened", 1, "Generate at least 150 Culture Per Turn", SECRET),
	ECONOMIST("Economist", 1, "Generate at least 200 Gold Per Turn", SECRET),
	COMMANDER("Commander", 1, "Have an army strength of 1,200", SECRET),
	TRAVEL_AGENT("Travel Agent", 1, "Generate at least 75 Tourism Per Turn", SECRET),
	POLITICIAN("Politician", 1, "Score 8 Diplomatic Victory Points", SECRET),
	FRIEND("Friend", 1, "Have an alliance with every other Civ in the game", SECRET),
	INTELLECTUAL("Intellectual", 1, "Earn three different types of Great People", SECRET),
	GRAND_DESIGNER("Grand Designer", 1, "Complete three Wonders from three different Eras", SECRET),
	COLONISER("Coloniser", 2, "Control three cities on a different continent(s) to your Capital each with a population of 5 or higher", SECRET),
	OPPRESSOR("Oppressor", 1, "Capture and control three City States (you may not raze them)", SECRET),
	METROPOLIS("Metropolis", 1, "Own three cities with population 15 or higher", SECRET),
	HEDONIST("Hedonist", 1, "Have access to at least one copy of six different luxury resources", SECRET),
	GOLDFINGERS("Goldfingers", 1, "Earn two consecutive Golden Ages", SECRET),
	HELLO_DARKNESS("Hello Darkness My Old Friend", 1, "Earn two consecutive Dark Ages", SECRET),
	IMPRESSIVE_SETTLE("Impressive Settle", 1, "Build a district with a natural adjacency bonus of 5 or more (before modifiers from Government cards)", SECRET),
	MINISTER_SCIENCE("Minister for Science", 1, "Own a Campus with an adjacency bonus of 10 or more", SECRET),
	MINISTER_ARTS("Minister for Arts", 1, "Own a Theater Square with an adjacency bonus of 10 or more", SECRET),
	CHANCELLOR_EXCHEQUER("Chancellor of the Exchequer", 1, "Own a Commercial Hub with an adjacency bonus of 10 or more", SECRET),
	HARBOURMASTER("Harbourmaster", 1, "Own a Harbor with an adjacency bonus of 10 or more", SECRET),
	MINISTER_RELIG_AFFAIRS("Minister for Religious Affairs", 1, "Own a Holy Site with an adjacency bonus of 10 or more", SECRET),
	CITY_PLANNER("City Planner", 1, "Own seven different types of district in your Civilization", SECRET),
	HI_EVERYBODY("Hi Everybody!", 1, "Achieve the Historic Moment 'World's First to Meet All Civilizations'", SECRET),
	ITS_ROUND("Oh god it's ROUND", 1, "Achieve the Historic Moment 'World's First Circumnavigation'", SECRET),
	GIANT_SLAYER("Giant Slayer", 1, "Earn the Historic Moment 'Enemy Veteran Defeated'", SECRET),
	YOINK("Yoink!", 1, "Earn the Historic Moment 'Free City Joins'", SECRET),
	JUST_CANT_WAIT_TO_BE_DOGE("I Just Can't Wait To Be… Doge?", 1, "Earn the Historic Moment 'First Tier 2 Government in World'", SECRET),
	I_LOVE_LEARNING("I Love Learning", 2, "Be the first player to research 30 technologies", SECRET),
	NOW_IM_A_BELIEVER("Now I'm a Believer", 2, "Be the first player to fully enhance their religion with four beliefs", SECRET),
	PRETTY_AS_A_PICTURE("Pretty as a Picture", 2, "Be the first player to earn a Great Artist", SECRET),
	GRAND_MARSHAL("Grand Marshal", 2, "Own a fully completed Encampment district (barracks/stable, armory and military academy)", SECRET),
	BATH_OF_ZEUS("By the Bath of Zeus!", 2, "Construct the Great Bath and Statue of Zeus wonders", SECRET),
	THE_COLOSSAL_OBJECTIVE("The Colossal Objective", 2, "Construct the Colosseum and the Colossus wonders", SECRET),
	A_WASTE_OF_TIME("A Waste of Time", 2, "Construct the Jebel Barkal and Kotoku-in wonders", SECRET),
	PETRAFIED("Petra-fied", 2, "Be the first to construct any two of the following wonders: Chichen Itza, Etemenanki, Huey Teocalli, Mausoleum at Halicarnassus, Petra, St.Basil's Cathedral", SECRET),
	TRADER("Trader", 2, "Achieve the Historic Moment 'First Trading Post in All Civilizations'", SECRET),
	UNDER_THE_SEA("Under the Sea", 2, "Achieve the Historic Moment 'World's First Shipwreck Excavated'", SECRET),
	CALL_AN_AMBULANCE("Call an ambulance! But not for me.", 2, "Achieve the Historic Moment 'Emergency Successfully Defended'", SECRET),
	A_PALACE_IN_THE_SUN("A Palace in the Sun", 3, "Construct the Potala Palace and Hermitage wonders", SECRET),
	WASTE_OF_TIME_MODERN("A Waste of Time: Modern Edition", 3, "Construct the Panama Canal and Golden Gate Bridge wonders", SECRET),
	TMNT("Teenage Mutant Ninja Turtles", 3, "Recruit two of the following Great People: Leonardo da Vinci, Michelangelo, Donatello", SECRET),
	FLY_LIKE_AN_EAGLE("Fly Like an Eagle", 3, "Achieve the Historic Moment 'World's First Flight'", SECRET),
	DIDNT_EVEN_FAKE_IT("I Didn't Even Fake It", 3, "Achieve the Historic Moment 'World's First Landing on the Moon'", SECRET),
	TOURIST_TRAP("Tourist Trap", 3, "Culturally dominate two opposing Civs", SECRET);

	public final String objectiveName;
	public final int numStars;
	public final String description;
	public final ObjectiveType objectiveType;

	public enum ObjectiveType {
		PUBLIC, SECRET
	}

	ImperiumObjective(String objectiveName, int numStars, String description, ObjectiveType objectiveType) {
		this.objectiveName = objectiveName;
		this.numStars = numStars;
		this.description = description;
		this.objectiveType = objectiveType;
	}
}
