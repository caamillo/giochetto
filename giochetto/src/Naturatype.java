import java.util.Map;
import java.util.HashMap;
public enum Naturatype {
    BLANK("blank"),
    OSCURITA("oscurità"),
    DEMONE("demone"),
    LUCE("luce"),
    UMANO("umano");
    public static Naturatype weaker(Naturatype natura){
        switch(natura){
            case OSCURITA:
                return LUCE;
            case DEMONE:
                return OSCURITA;
            case LUCE:
                return DEMONE;
            case UMANO:
                return DEMONE;
        }
        return BLANK;
    }
    public static Naturatype stronger(Naturatype natura){
        switch(natura){
            case DEMONE:
                return LUCE;
            case OSCURITA:
                return DEMONE;
            case LUCE:
                return OSCURITA;
            case UMANO:
                return LUCE;
        }
        return BLANK;
    }
    public static boolean isWeakerTo(Naturatype n1, Naturatype n2){
        if (weaker(n1) == n2) return true;
        return false;
    }
    public static boolean isStrongerTo(Naturatype n1, Naturatype n2){
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
