
public class AttributeValuePair implements Comparable<AttributeValuePair> {
    private String attribute;
    private String value;
    private int count;

    public AttributeValuePair(String attribute, String value,int counter) {
        this.attribute = attribute;
        this.value = value;
        this.count = counter;
    }

    public void increaseCounter() {
        this.count++;
    }

    public int getCounter() {
        return this.count;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(AttributeValuePair o) {
        int counter = ((AttributeValuePair) o).getCounter();
        return this.count - counter;
    }
}
