import java.util.Map;
import java.util.HashMap;
public enum Cardtype{
    BLANK("blank"),
    MOSTRO("mostro"),
    MAGIA("magia"),
    TRAPPOLA("trappola");

    private final String abbreviation;

    private static final Map<String, Cardtype> lookup = new HashMap<String, Cardtype>();

    static {
        for (Cardtype c : Cardtype.values()) {
            lookup.put(c.getAbbreviation(), c);
        }
    }

    private Cardtype(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Cardtype get(String abbreviation) {
        return lookup.get(abbreviation);
    }
}
