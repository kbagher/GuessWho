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

    /**
     * chosen character object
     */
    private Character chosenCharacter;
    /**
     * All available attributes loaded from the game configuration
     */
    private HashMap<String, ArrayList<String>> attributes;
    /**
     * All available characters loaded from the game configuration
     */
    private ArrayList<Character> characters;
    /**
     * Game configuration loader
     */
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

        /*
        Check if there is only one character left to guess.
        If yes, return the last character as a guess.
         */
        if (remainingCharacters.size() == 1) {
            return new Guess(Guess.GuessType.Person, "", remainingCharacters.get(0).getAttribute("name"));
        }

        /*
         Random attribute guess.
         This will also check and prevent for any guess redundancy,
         making sure that each guess is unique.
         */
        while (true) {
            // random attribute
            String randomAttributeName = getRandomAttributeName();
            String randomAttributeValue = getRandomAttributeValue(randomAttributeName);
            // check for attribute guess redundancy
            if (isGuessRedundant(getRemainingCharacters(), randomAttributeName, randomAttributeValue))
                continue; // redundant attribute guess

            // guess is not redundant
            return new Guess(Guess.GuessType.Attribute, randomAttributeName, randomAttributeValue);
        }
    }

    public boolean answer(Guess currGuess) {
        if (currGuess.getType() == Guess.GuessType.Attribute) { // Attribute guess
            String guessAttribute = currGuess.getAttribute();
            String guessValue = currGuess.getValue();

            boolean matchingAttributeAndValue = chosenCharacter.getAttribute(guessAttribute).equals(guessValue);
            if (matchingAttributeAndValue) {
                // attribute guess is correct
                return true;
            }
        } else { // Character guess
//            System.out.println("Guessing Character");
            boolean characterGuess = chosenCharacter.getAttribute("name").equals(currGuess.getValue());
            if (characterGuess) {
                // character guess is correct
                return true;
            }
        }
        return false; // guess is incorrect for either character ot attributes
    }


    public boolean receiveAnswer(Guess currGuess, boolean answer) {
        if (currGuess.getType() == Guess.GuessType.Attribute) { // attribute guess
            if (answer) {
                for (Character character : getRemainingCharacters()) {
                    boolean attributeMatch = character.getAttribute(currGuess.getAttribute()).equals(currGuess.getValue());
                    if (!attributeMatch)
                        character.setFlipped(); //eliminate all non matching attribute
                }
            } else {
                for (Character character : getRemainingCharacters()) {
                    boolean attributeMatch = character.getAttribute(currGuess.getAttribute()).equals(currGuess.getValue());
                    // avoid eliminating the chosen character
                    if (attributeMatch  && character != chosenCharacter)
                        character.setFlipped(); // eliminate all matching attribute
                }
            }
        } else { // character guess
            if (answer)
                return true; // opponent's answer is correct

            /*
             Opponent's answer is incorrect.
             Thus, change status of guessed character to "guessed"
            */
            for (Character character : getRemainingCharacters()) {
                boolean characterMatch = character.getAttribute("name").equals(currGuess.getValue());
                if (characterMatch)
                    character.setFlipped(); // incorrectly guessed character
            }
        }
        return false;
    }


    /**
     * Get random attribute value for a given attribute name
     *
     * @param attributeName the attribute name
     *
     * @return random attribute value for the give attribute name
     */
    private String getRandomAttributeValue(String attributeName) {

        // list of all available attribute values for the given attribute name
        ArrayList<String> attributeValues = attributes.get(attributeName);

        // random number ranged between 0 and the number of available attribute's values
        Random random = new Random();
        int randomNumber = random.nextInt(attributeValues.size());

        // random attribute value
        return attributeValues.get(randomNumber);
    }

    /**
     * Get  a random attribute name from the list
     * of all available attributes.
     *
     * @return random attribute name
     */
    private String getRandomAttributeName() {

        // list of all available attribute names
        ArrayList<String> attributeKeys = new ArrayList<String>(attributes.keySet());

        // random number ranged between 0 and the number of available attribute names
        Random random = new Random();
        int randomNumber = random.nextInt(attributeKeys.size());

        // random attribute name
        return attributeKeys.get(randomNumber);
    }

    private boolean isGuessRedundant(ArrayList<Character> characters, String attributeName, String attributeValue) {
        /*
        loop through all remaining characters to check for
        any redundant attribute guess that has been maade before.
        </br>
        Unique guess (question) happens if there at least one match between the given (attribute, value) pair and the remaining
        characters. if no match found, it will be considered a redundant guessing (question)
        * */
        for (Character character : characters) {
            if (character.getAttribute(attributeName).equals(attributeValue))
                return false; // attribute guess is not redundant
        }
        return true; // guess attribute is redundant
    }

    /**
     * Return all remaining characters which have not yet been guessed nor matched
     * any of the (attribute,valu) pair asked in the the questions
     *
     * @return remaining not-guessed characters
     */
    private ArrayList<Character> getRemainingCharacters() {
        /**
         * check all characters weather they have been marked as guessed or eliminated
         * and return them as a list
         */
        ArrayList<Character> tmpCharacters = new ArrayList<>();
        for (Character ch : characters) {
            if (!ch.isFlipped())
                tmpCharacters.add(ch); // add the character to the not-guessed characters
        }
        return tmpCharacters; // list of remaining not-guessed characters
    }

    /**
     * Get the character object for the specified name
     *
     * @param name character name (ID)
     *
     * @return character object for the given name
     */
    private Character getCharacterByName(String name) {
        for (Character ch : characters) {
            if (ch.getAttribute("name").equals(name)) {
                return ch;
            }
        }
        return null; // character not found
    }
}