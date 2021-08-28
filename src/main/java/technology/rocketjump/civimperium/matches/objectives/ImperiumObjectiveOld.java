package technology.rocketjump.civimperium.matches.objectives;

import static technology.rocketjump.civimperium.matches.objectives.ImperiumObjectiveOld.ObjectiveType.PUBLIC;
import static technology.rocketjump.civimperium.matches.objectives.ImperiumObjectiveOld.ObjectiveType.SECRET;

public enum ImperiumObjectiveOld {

	// Match 2 public objectives
	INTELLECTUAL("Intellectual ", 1, "Recruit 3 different types of Great Person", PUBLIC, true),
	WONDERWALL("Wonderwall", 1, "Have a city with ancient walls and a completed wonder", PUBLIC, true),
	PREPARED_FOR_WAR("Prepared for War", 1, "Have an improved source of horses and an improved source of iron within your borders", PUBLIC, true),

	FOUR_PILLARS("Four Pillars of Faith", 2, "Construct four temples", PUBLIC, true),
	EXPLORING("'Exploring'", 2, "Capture an opposing Civ's city with a Caravel", PUBLIC, true),
	LOVE_THE_OVERLORD("We Love the Overlord Day!", 2, "Have a city reach a population satisfaction level of Ecstatic (5+ excess amenities)", PUBLIC, true),

	BEAUTY_AND_BEAST("Beauty and the Beast", 3, "Own a completed district on a tile with Breathtaking Appeal adjacent to a completed district on a tile with Disgusting Appeal", PUBLIC, true),
	BULLY("Bully", 3, "Capture and control six City States (razed City States do not count)", PUBLIC, true),
	CURATOR("Curator ", 3, "Own three Great Works of Art and two Great Works of Music", PUBLIC, true),

	// later use public objectives
	GO_AWAY("Please Go Away", 1, "Achieve the Historic Moment 'Threatening Camp Destroyed'", PUBLIC, false),
	CAPITAL_PUNISHMENT("Capital Punishment", 2, "Take control of another Civ's capital city", PUBLIC, false),


	// Match 1 Public objectives
	IND_REVOLUTION("Industrial Revolution", 1, "Build and power a factory", PUBLIC, false),
	WARLORD("Warlord", 1, "Capture or raze a total of 5 enemy cities from opposing Civilizations. This total must be reached by capturing or razing cities from at least 2 different opponents.", PUBLIC, false),
	DIPLOMAT("Diplomat", 1, "Have Suzerainty over 3 different city states at the same time.", PUBLIC, false),
	PREACHER("Preacher", 1, "Control the Holy City of a religion enhanced with four beliefs.", PUBLIC, false),
	LITERATURE("Literature", 1, "Own 5 Great Works of Writing.", PUBLIC, false),
	THEOLOGIAN("Theologian", 2, "Have three Holy Cities follow your religion (including your own).", PUBLIC, false),
	ARCHITECT("Architect", 2, "Complete 5 Wonders, at least one of which must be from the Renaissance Era or later.", PUBLIC, false),
	POP_EXPLOSION("Population Explosion", 3, "Achieve the Historic Moment 'World's First Gigantic City' (25 population)", PUBLIC, false),

	// Secret objectives
	SCIENTIST("Scientist", 1, "Generate at least 150 Science Per Turn", SECRET, true),
	BISHOP("Bishop", 1, "Generate at least 150 Faith Per Turn", SECRET, true),
	ENLIGHTENED("Enlightened", 1, "Generate at least 150 Culture Per Turn", SECRET, true),
	ECONOMIST("Economist", 1, "Generate at least 100 Gold Per Turn", SECRET, true),
	COMMANDER("Commander", 1, "Have an army strength of 1,200", SECRET, true),
	TRAVEL_AGENT("Travel Agent", 1, "Generate at least 75 Tourism Per Turn", SECRET, true),
	FRIEND("Friend", 1, "Have an alliance with every other Civ in the game", SECRET, true),
	GRAND_DESIGNER("Grand Designer", 1, "Complete three Wonders from three different Eras", SECRET, true),
	COLONISER("Coloniser", 2, "Control three cities on a different continent(s) to your Capital each with a population of 5 or higher", SECRET, true),
	OPPRESSOR("Oppressor", 1, "Capture and control three City States (you may not raze them)", SECRET, true),
	METROPOLIS("Metropolis", 1, "Own three cities with population 15 or higher", SECRET, true),
	HEDONIST("Hedonist", 1, "Have access to at least one copy of six different luxury resources", SECRET, true),
	HELLO_DARKNESS("Hello Darkness My Old Friend", 1, "Earn two consecutive Dark Ages", SECRET, true),
	IMPRESSIVE_SETTLE("Impressive Settle", 1, "Build a district with a natural adjacency bonus of 5 or more (before modifiers from Government cards)", SECRET, true),
	MINISTER_SCIENCE("Minister for Science", 1, "Own a Campus with an adjacency bonus of 10 or more", SECRET, true),
	MINISTER_ARTS("Minister for Arts", 1, "Own a Theater Square with an adjacency bonus of 10 or more", SECRET, true),
	CHANCELLOR_EXCHEQUER("Chancellor of the Exchequer", 1, "Own a Commercial Hub with an adjacency bonus of 10 or more", SECRET, true),
	HARBOURMASTER("Harbourmaster", 1, "Own a Harbor with an adjacency bonus of 10 or more", SECRET, true),
	MINISTER_RELIG_AFFAIRS("Minister for Religious Affairs", 1, "Own a Holy Site with an adjacency bonus of 10 or more", SECRET, true),
	CITY_PLANNER("City Planner", 1, "Own seven different types of district in your Civilization", SECRET, true),
	HI_EVERYBODY("Hi Everybody!", 1, "Achieve the Historic Moment 'World's First to Meet All Civilizations'", SECRET, true),
	ITS_ROUND("Oh god it's ROUND", 1, "Achieve the Historic Moment 'World's First Circumnavigation'", SECRET, true),
	GIANT_SLAYER("Giant Slayer", 1, "Earn the Historic Moment 'Enemy Veteran Defeated'", SECRET, true),
	YOINK("Yoink!", 1, "Earn the Historic Moment 'Free City Joins'", SECRET, true),
	JUST_CANT_WAIT_TO_BE_DOGE("I Just Can't Wait To Be… Doge?", 1, "Earn the Historic Moment 'First Tier 2 Government in World'", SECRET, true),
	HERO("Hero", 1, "Achieve a Heroic Age", SECRET, true),
	NOBODY_EXPECTS("Nobody Expected That!", 1, "Achieve the Historic Moment 'World's First Inquisition'", SECRET, true),
	COLLECTORS_ITEMS("Collector's Items", 1, "Own two Relics", SECRET, true),

	I_LOVE_LEARNING("I Love Learning", 2, "Be the first player to research 30 technologies", SECRET, true),
	NOW_IM_A_BELIEVER("Now I'm a Believer", 2, "Be the first player to fully enhance their religion with four beliefs", SECRET, true),
	PRETTY_AS_A_PICTURE("Pretty as a Picture", 2, "Be the first player to earn a Great Artist", SECRET, true),
	GRAND_MARSHAL("Grand Marshal", 2, "Own a fully completed Encampment district (barracks/stable, armory and military academy)", SECRET, true),
	BATH_OF_ZEUS("By the Bath of Zeus!", 2, "Construct the Great Bath and Statue of Zeus wonders", SECRET, true),
	THE_COLOSSAL_OBJECTIVE("The Colossal Objective", 2, "Construct the Colosseum and the Colossus wonders", SECRET, true),
	A_WASTE_OF_TIME("A Waste of Time", 2, "Construct the Jebel Barkal and Kotoku-in wonders", SECRET, true),
	PETRAFIED("Petra-fied", 2, "Be the first to construct any two of the following wonders: Chichen Itza, Etemenanki, Huey Teocalli, Mausoleum at Halicarnassus, Petra, St.Basil's Cathedral", SECRET, true),
	TRADER("Trader", 2, "Achieve the Historic Moment 'First Trading Post in All Civilizations'", SECRET, true),
	CALL_AN_AMBULANCE("Call an ambulance! But not for me.", 2, "Achieve the Historic Moment 'Emergency Successfully Defended'", SECRET, true),
	TRADE_HUB("Trade Hub", 2, "Own two cities with Commercial Hubs built adjacent to Harbors", SECRET, true),
	BIG_SPENDER("Big Spender", 2, "Achieve the Historic Moment 'Great Person Lured by Gold'", SECRET, true),
	GOOD_FAITH_DEPOSIT("Good Faith Deposit", 2, "Achieve the Historic Moment 'Great Person Lured by Faith'", SECRET, true),

	A_PALACE_IN_THE_SUN("A Palace in the Sun", 3, "Construct the Potala Palace and Hermitage wonders", SECRET, true),
	WASTE_OF_TIME_MODERN("A Waste of Time: Modern Edition", 3, "Construct the Panama Canal and Golden Gate Bridge wonders", SECRET, true),
	TMNT("Teenage Mutant Ninja Turtles", 3, "Recruit two of the following Great People: Leonardo da Vinci, Michelangelo, Donatello", SECRET, true),
	FLY_LIKE_AN_EAGLE("Fly Like an Eagle", 3, "Achieve the Historic Moment 'World's First Flight'", SECRET, true),
	TOURIST_TRAP("Tourist Trap", 3, "Culturally dominate two opposing Civs", SECRET, true),
	ART_ATTACK("Art Attack", 3, "Capture an enemy city containing a Great Work of Art", SECRET, true),
	CANT_DEFEAT_METAL("You Can't Defeat The Metal", 3, "Achieve the Historic Moment 'First Rock Band Concert in World'", SECRET, true),

	// Deactivated secret objectives
	INTELLECTUAL_SECRET("Intellectual", 1, "Earn three different types of Great People", SECRET, false),
	POLITICIAN("Politician", 1, "Score 8 Diplomatic Victory Points", SECRET, false),
	GOLDFINGERS("Goldfingers", 1, "Earn two consecutive Golden Ages", SECRET, false),
	UNDER_THE_SEA("Under the Sea", 2, "Achieve the Historic Moment 'World's First Shipwreck Excavated'", SECRET, false),
	DIDNT_EVEN_FAKE_IT("I Didn't Even Fake It", 3, "Achieve the Historic Moment 'World's First Landing on the Moon'", SECRET, false),
	;

	public final String objectiveName;
	public final int numStars;
	public final String description;
	public final ObjectiveType objectiveType;
	public final boolean active;

	public enum ObjectiveType {
		PUBLIC, SECRET
	}

	ImperiumObjectiveOld(String objectiveName, int numStars, String description, ObjectiveType objectiveType, boolean active) {
		this.objectiveName = objectiveName;
		this.numStars = numStars;
		this.description = description;
		this.objectiveType = objectiveType;
		this.active = active;
	}
}
