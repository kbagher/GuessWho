
/**
 * Encapsulates a character attribute and its associated value
 */
public class AttributeValuePair implements Comparable<AttributeValuePair> {
    /**
     * attribute name
     */
    private String attribute;
    /**
     * attribute value
     */
    private String value;
    /**
     * attribute's number of occurrence in the game
     */
    private int count;

    /**
     * creates a new attribute and value pair
     * @param attribute attribute name
     * @param value attribute value
     * @param counter number of occurrence
     */
    public AttributeValuePair(String attribute, String value,int counter) {
        this.attribute = attribute;
        this.value = value;
        this.count = counter;
    }

    /**
     * Get attribute's number of occurrence
     * @return
     */
    public int getCounter() {
        return this.count;
    }

    /**
     * get attribute's name
     * @return
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Get attribute's value
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     *
     * @param o attribute and value pair object
     * @return smallest attribute count
     */
    @Override
    public int compareTo(AttributeValuePair o) {
        int counter = ((AttributeValuePair) o).getCounter();
        return this.count - counter;
    }
}
