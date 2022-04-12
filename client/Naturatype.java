import java.util.Map;
import java.util.HashMap;
public enum Naturatype {
    BLANK("blank"),
    FUOCO("fuoco"),
    ACQUA("acqua"),
    ERBA("erba");
    public Naturatype weaker(Naturatype natura){
        switch(natura){
            case FUOCO:
                return ACQUA;
            case ACQUA:
                return ERBA;
            case ERBA:
                return FUOCO;
        }
        return BLANK;
    }
    public Naturatype stronger(Naturatype natura){
        switch(natura){
            case FUOCO:
                return ERBA;
            case ACQUA:
                return FUOCO;
            case ERBA:
                return ACQUA;
        }
        return BLANK;
    }
    public boolean isWeakerTo(Naturatype n1, Naturatype n2){
        if (weaker(n1) == n2) return true;
        return false;
    }
    public boolean isStrongerTo(Naturatype n1, Naturatype n2){
        if (stronger(n1) == n2) return true;
        return false;
    }

    private final String abbreviation;

    private static final Map<String, Naturatype> lookup = new HashMap<String, Naturatype>();

    static {
        for (Naturatype c : Naturatype.values()) {
            lookup.put(c.getAbbreviation(), c);
        }
    }

    private Naturatype(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Naturatype get(String abbreviation) {
        return lookup.get(abbreviation);
    }
}
