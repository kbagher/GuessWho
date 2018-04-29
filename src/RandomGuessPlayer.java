import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Random guessing player.
 * This player is for task B.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player {

    private Character chosenCharacter;
    private HashMap<String, ArrayList<String>> attributes;
    private ArrayList<Character> characters;
    private ConfigurationLoader loader;

    /**
     * Loads the game configuration from gameFilename, and also store the chosen
     * person.
     *
     * @param gameFilename Filename of game configuration.
     * @param chosenName   Name of the chosen person for this player.
     *
     * @throws IOException If there are IO issues with loading of gameFilename.
     *                     Note you can handle IOException within the constructor and remove
     *                     the "throws IOException" method specification, but make sure your
     *                     implementation exits gracefully if an IOException is thrown.
     */
    public RandomGuessPlayer(String gameFilename, String chosenName) throws IOException {
        // load data from the given filename
        loader = new ConfigurationLoader(gameFilename);

        // list of all available characters in the loaded configuration file
        characters = loader.getCharacters();

        // list of all available attributes in the loaded configuration file
        attributes = loader.getAttributes();

        // user's chosen character
        chosenCharacter = getCharacterByName(chosenName);
    }

    public Guess guess() {
        ArrayList<Character> remainingCharacters = getRemainingCharacters();

        if (remainingCharacters.size() == 1) {
            return new Guess(Guess.GuessType.Person, "", remainingCharacters.get(0).getAttribute("name"));
        }

        while (true) {

            // random attribute
            String randomAttributeName = getRandomAttributeName();
            String randomAttributeValue = getRandomAttributeValue(randomAttributeName);

            // check for attribute guess redundancy
            if (isGuessRedundant(getRemainingCharacters(),randomAttributeName,randomAttributeValue))
                continue; // attribute guess is redundant

            // guess is not redundant
            return new Guess(Guess.GuessType.Attribute, randomAttributeName, randomAttributeValue);
        }
    }


    public boolean answer(Guess currGuess) {
        if (currGuess.getType() == Guess.GuessType.Attribute) { // Attribute guess
            if (currGuess.getAttribute().equals(currGuess.mAttribute)) {
                // attribute guess is correct
                return true;
            }
        } else { // Character guess
            if (chosenCharacter.getAttribute("name").equals(currGuess.getAttribute())) {
                // character guess is correct
                return true;
            }
        }
        return false; // guess is incorrect for either character ot attributes
    }


    public boolean receiveAnswer(Guess currGuess, boolean answer) {

        return true;
    } // end of receiveAnswer()


    private String getRandomAttributeValue(String attributeName){
        ArrayList<String> attributeValues = attributes.get(attributeName);

        Random random = new Random();
        int randomNumber = random.nextInt(attributeValues.size());

        return attributeValues.get(randomNumber);
    }


    private String getRandomAttributeName(){
        ArrayList<String> attributeKeys = new ArrayList<String>(attributes.keySet());

        Random random = new Random();
        int randomNumber = random.nextInt(attributeKeys.size());

        String randomAttributeKey = attributeKeys.get(randomNumber);

        return randomAttributeKey;
    }

    private boolean isGuessRedundant(ArrayList<Character> characters, String attributeName,String attributeValue){
        /*
        loop through all remaining characters to check for
        any redundant attribute guess.
        * */
        for (Character character: characters) {
            if (character.getAttribute(attributeName).equals(attributeValue))
                return true; // attribute guess is redundant
        }
        return false; // guess is not redundant
    }

    private int eliminateCharactersForAttribute() {
        int eliminateCounter = 0;


        return 1;
    }

    private ArrayList<Character> getRemainingCharacters() {
        ArrayList<Character> tmpCharacters = new ArrayList<>();
        for (Character ch : characters) {
            if (!ch.isGuessed())
                tmpCharacters.add(ch);
        }
        return tmpCharacters;
    }

    private Character getCharacterByName(String name) {
        for (Character ch : characters) {
            if (ch.getAttribute("name").equals(name)) {
                return ch;
            }
        }
        return null; // character not found
    }

} // end of class RandomGuessPlayer
