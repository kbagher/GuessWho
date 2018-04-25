import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ConfigurationLoader {

    HashMap<String, ArrayList<String>> attributes;
    ArrayList<Character> characters;
    String fileName;

    public ConfigurationLoader(String fileName) {
        this.fileName = fileName;
        attributes = new HashMap<String, ArrayList<String>>();
        characters = new ArrayList<>();
        loadFileContents();
    }

    private void loadFileContents() {
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            loadAttributes(br);
            loadCharacters(br);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAttributes(BufferedReader br) throws IOException {
        for (int line = 0; line < 9; line++) {
            String[] attribute = br.readLine().split(" ");
            ArrayList<String> attributesList = new ArrayList<>();

            for (int i = 1; i < attribute.length - 1; i++) {
                attributesList.add(attribute[i]);
            }
            attributes.put(attribute[0], attributesList);
        }
    }

    private void loadCharacters(BufferedReader br) throws IOException {

        // skip first empty line
        String line = br.readLine();
        String hairLength = null, glasses = null, facialHair = null, eyeColor = null, pimples = null, hat = null, hairColor = null, noseShape = null, faceShape = null, name = null;

        while (line != null) {
            name = br.readLine();
            for (int x = 0; x < 9; x++) {
                String[] attribute = br.readLine().split(" ");
                switch (attribute[0]) {
                    case "hairLength":
                        hairLength = attribute[1];
                    case "glasses":
                        glasses = attribute[1];
                    case "facialHair":
                        facialHair = attribute[1];
                    case "eyeColor":
                        eyeColor = attribute[1];
                    case "pimples":
                        pimples = attribute[1];
                    case "hat":
                        hat = attribute[1];
                    case "hairColor":
                        hairColor = attribute[1];
                    case "noseShape":
                        noseShape = attribute[1];
                    case "faceShape":
                        faceShape = attribute[1];
                }
            }
            characters.add(new Character(name, hairLength, glasses, facialHair, eyeColor, pimples, hat, hairColor, noseShape, faceShape));
            // skip empty line between characters
            line = br.readLine();
        }
    }

    public HashMap<String, ArrayList<String>> getAttributes() {
        return attributes;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }
}
