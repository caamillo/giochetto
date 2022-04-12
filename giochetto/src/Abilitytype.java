import java.util.Map;
import java.util.HashMap;

public enum Abilitytype {
    BLANK("blank"),
    BLOCK("block"),
    SKIP("skip"),
    DAMAGEUP("damageup"),
    DAMAGEDOWN("damagedown"),
    HEALTHUP("healthup"),
    HEALTHDOWN("healthdown");

    private final String abbreviation;

    private static final Map<String, Abilitytype> lookup = new HashMap<String, Abilitytype>();

    static {
        for (Abilitytype c : Abilitytype.values()) {
            lookup.put(c.getAbbreviation(), c);
        }
    }

    private Abilitytype(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Abilitytype get(String abbreviation) {
        return lookup.get(abbreviation);
    }
}