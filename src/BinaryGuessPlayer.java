import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Binary-search based guessing player.
 * This player is for task C.
 * <p>
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class BinaryGuessPlayer implements Player {

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
    public BinaryGuessPlayer(String gameFilename, String chosenName) throws IOException {
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

        AttributeValuePair[] attributesCount = countAndSortAttributes(remainingCharacters);

//        for (int x = 0; x < attributesCount.length; x++) {
//            System.out.print(attributesCount[x].getCounter() + " ");
//        }
//        System.out.println();

        int attributeIndex = binarySearch(remainingCharacters.size(), attributesCount);

//        System.out.println("Middle: " + attributeIndex);

        if (attributeIndex == -1) {
            System.out.println("OOPPS");
        }

        return new Guess(Guess.GuessType.Attribute, attributesCount[attributeIndex].getAttribute(), attributesCount[attributeIndex].getValue());
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
                        character.setFlipped(); // eliminate all non matching attribute
                }
            } else {
                for (Character character : getRemainingCharacters()) {
                    boolean attributeMatch = character.getAttribute(currGuess.getAttribute()).equals(currGuess.getValue());
                    // avoid eliminating the chosen character
                    if (attributeMatch && character != chosenCharacter)
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
     * Binary search for attribute which removes half characters
     *
     * @param remainingCharacters number of remaining characters
     * @param attributesCount     sorted counter of attributes
     *
     * @return attribute index to be used in the next guess or -1 if not value found
     */
    private int binarySearch(int remainingCharacters, AttributeValuePair[] attributesCount) {
        int targetValue = remainingCharacters / 2;
        System.out.println("search for " + targetValue);
        int lowerValue = 0;
        int higherValue = attributesCount.length - 1;

        while (higherValue >= lowerValue) {
            int middle = (lowerValue + higherValue) / 2;

            if (attributesCount[middle].getCounter() < targetValue) {
                lowerValue = middle + 1;
            } else if (attributesCount[middle].getCounter() > targetValue) {
                higherValue = middle - 1;
            } else {
                return middle;
            }
        }
        return -1; // no value found
    }

    /**
     * Return all remaining characters which have not yet been guessed nor matched
     * any of the (attribute,value) pair asked in the the questions
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
     * Count attribute's occurrence of the giving characters and sort in an ascendant way
     * @param characters characters to count attribute's occurrence for
     * @return AttributesValue pairs with occurrence in the giving characters list
     */
    private AttributeValuePair[] countAndSortAttributes(ArrayList<Character> characters) {
        HashMap<String, Integer> attributeMapCount = countAttributeOccurrence(characters);
        return sortAttributeMap(attributeMapCount);
    }


    /**
     * Sort attributes by their occurrence
     * @param attributesCounter attributes value occurrence
     * @return ascendant sorted attributes pair
     */
    private AttributeValuePair[] sortAttributeMap(HashMap<String, Integer> attributesCounter) {

        AttributeValuePair[] pair = new AttributeValuePair[attributesCounter.size()];

        // convert hash into ArrayList
        int x = 0;
        for (String key : attributesCounter.keySet()) {
            String attribute = key.split(" ")[0];
            String value = key.split(" ")[1];
            pair[x] = new AttributeValuePair(attribute, value, attributesCounter.get(key));
            x++;
        }
        Arrays.sort(pair);
        return pair;
    }


    /**
     * Count attributes occurrence for the giving characters
     * @param characters the characters list to count the attribute's occurence for
     * @return attributes and value pair with the total occurence in the giving characters list
     */
    private HashMap<String, Integer> countAttributeOccurrence(ArrayList<Character> characters) {

        HashMap<String, Integer> counter = new HashMap<>();

        for (String attribute : attributes.keySet()) {
            for (Character character : characters) {
                String attributeValue = character.getAttribute(attribute);
                Integer attributeCount = counter.get(attribute + " " + attributeValue);
                attributeCount = attributeCount == null ? 1 : attributeCount + 1;
                counter.put(attribute + " " + attributeValue, attributeCount);
            }
        }

        return counter;
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
