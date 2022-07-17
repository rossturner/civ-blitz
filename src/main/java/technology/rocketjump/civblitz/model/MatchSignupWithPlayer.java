package technology.rocketjump.civblitz.model;

import technology.rocketjump.civblitz.codegen.tables.pojos.MatchSignup;
import technology.rocketjump.civblitz.codegen.tables.pojos.Player;

import java.util.List;
import java.util.Optional;

public class MatchSignupWithPlayer extends MatchSignup {

	private final Player player;
	private final List<Card> selectedCards;

	public MatchSignupWithPlayer(MatchSignup matchSignup, Player player, List<Card> selectedCards) {
		super(matchSignup);
		this.player = player;
		this.selectedCards = selectedCards;
	}

	public Player getPlayer() {
		return player;
	}

	public Optional<Card> getCard(CardCategory category) {
		return selectedCards.stream().filter(c -> category.equals(c.getCardCategory())).findAny();
	}

	public List<Card> getSelectedCards() {
		return selectedCards;
	}
}
