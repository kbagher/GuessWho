
public class Character {
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
    }

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
}
