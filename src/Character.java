/**
 * Character class encapsulates game character data
 */
public class Character {

    // character attributes
    private String hairLength;
    private String glasses;
    private String facialHair;
    private String eyeColor;
    private String pimples;
    private String hat;
    private String hairColor;
    private String noseShape;
    private String faceShape;
    private String name;
    private boolean isGuessed;

    /**
     *  Creates a new character
     * @param name character name
     * @param hairLength hair length
     * @param glasses glasses color/ wearing glasses or not
     * @param facialHair facial hair color/ has facial hair or not
     * @param eyeColor eye color
     * @param pimples has pimples or not/ pimples colors
     * @param hat hat color/ wearing hat or not
     * @param hairColor hair color
     * @param noseShape nose shape
     * @param faceShape face shape
     */
    public Character(String name, String hairLength, String glasses, String facialHair, String eyeColor, String pimples, String hat, String hairColor, String noseShape, String faceShape) {
        this.hairLength = hairLength;
        this.glasses = glasses;
        this.facialHair = facialHair;
        this.eyeColor = eyeColor;
        this.pimples = pimples;
        this.hat = hat;
        this.hairColor = hairColor;
        this.noseShape = noseShape;
        this.faceShape = faceShape;
        this.name = name;
        this.isGuessed = false;
    }

    /**
     * Return an attributes value for the character
     * @param attributeName attribute name as in game config file
     * @return attribute value
     */
    public String getAttribute(String attributeName) {
        switch (attributeName) {
            case "hairLength":
                return hairLength;
            case "glasses":
                return glasses;
            case "facialHair":
                return facialHair;
            case "eyeColor":
                return eyeColor;
            case "pimples":
                return pimples;
            case "hat":
                return hat;
            case "hairColor":
                return hairColor;
            case "noseShape":
                return noseShape;
            case "faceShape":
                return faceShape;
            case "name":
                return name;
            default:
                return "Undefined attribute";
        }
    }

    /**
     * Player has been flipped or not (removed as a guessing option from the game -not guessable-)
     * @return true of the player is flipped
     */
    public boolean isFlipped() {
        return isGuessed;
    }

    /**
     * Set player as flipped (player is no longer guessable)
     */
    public void setFlipped() {
        isGuessed = true;
    }
}
