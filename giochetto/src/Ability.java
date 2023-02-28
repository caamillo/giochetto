import org.json.JSONObject;

public class Ability {
    private int id = -1;
    private String abilityname = "Blank Ability";
    private Abilitytype abilitytype = Abilitytype.BLANK;
    private JSONObject abilityjson = null;
    public Ability(int id,String name,Abilitytype type,JSONObject json){
        if(!setId(id)) System.out.println("Errore Id");
        if (!setName(name)) System.out.println("Errore nome");
        if (!setType(type)) System.out.println("Errore tipo");
        if (!setJson(json)) System.out.println("Errore json");
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return abilityname;
    }
    public Abilitytype getType(){
        return abilitytype;
    }
    public JSONObject getJson(){
        return abilityjson;
    }
    public boolean setId(int id){
        if(id >= 0 && this.id != id){
            this.id = id;
            return true;
        }
        return false;
    }
    public boolean setName(String name){
        if(name.length() > 0 && !(name.equalsIgnoreCase(abilityname))){
            abilityname = name;
            return true;
        }
        return false;
    }
    public boolean setType(Abilitytype type){
        if(type != Abilitytype.BLANK && type != abilitytype){
            abilitytype = type;
            return true;
        }
        return false;
    }
    public boolean setJson(JSONObject json){
        if(json != null && abilityjson != json){
            this.abilityjson = json;
            return true;
        }
        return false;
    }
    public String toString(){
        return "abilità " +
                "id: " + id + ", " +
                "nome: " + abilityname + ", " +
                "tipo: " + abilitytype + ", " +
                "json: " + abilityjson.toString();
    }
}