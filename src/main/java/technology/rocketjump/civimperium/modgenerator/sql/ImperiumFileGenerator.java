package technology.rocketjump.civimperium.modgenerator.sql;

import technology.rocketjump.civimperium.model.Card;
import technology.rocketjump.civimperium.model.CardCategory;
import technology.rocketjump.civimperium.modgenerator.model.ModHeader;

import java.util.Map;

public interface ImperiumFileGenerator {

	String getFileContents(ModHeader modHeader, Map<CardCategory, Card> selectedCards);

	String getFilename();

}
