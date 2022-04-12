public class Magia extends Carta{
    private int manacarta = -1;
    private int hpcarta = -1;
    private Ability ability = null;
    public Magia(Object toClone){
        super((((Carta)toClone)).getId(),((Carta)toClone).getNome(),((Carta)toClone).getTipo(),((Carta)toClone).getDesc());
        Magia carta = (Magia)toClone;
        if(!setMana(carta.getMana())) System.out.println("Errore mana");
        if(!setHP(carta.getHP())) System.out.println("Errore hp");
    }
    public Magia(int id,String nome,Cardtype tipo,String desc,int mana,int hp){
        super(id,nome,tipo,desc);
        if(!setMana(mana)) System.out.println("Errore mana");
        if(!setHP(hp)) System.out.println("Errore hp");
    }
    public int getMana(){
        return manacarta;
    }
    public int getHP(){
        return hpcarta;
    }
    public boolean setMana(int manacarta){
        if(manacarta>0 && this.manacarta != manacarta){
            this.manacarta = manacarta;
            return true;
        }
        return false;
    }
    public boolean setHP(int hpcarta){
        if(hpcarta>0 && this.hpcarta != hpcarta){
            this.hpcarta = hpcarta;
            return true;
        }
        return false;
    }
    public boolean setAbility(Ability ability){
        if(ability != null && this.ability != ability){
            this.ability = ability;
            return true;
        }
        return false;
    }
    public boolean isCarta(Object mostro){
        if(mostro instanceof Mostro)
            return super.isCarta(mostro);
        return false;
    }
    public String turnToJson(){
        StringBuffer sb = new StringBuffer(super.tunToJson());
        sb.deleteCharAt(sb.length() - 1);
        return sb + ",\"mana\":\"" + manacarta + "\",\"hp\":\"" + hpcarta + "\"}";
    }
    public String toString(){
        return super.toString() + ", " +
            "mana: " + manacarta + ", " +
            "hp: " + hpcarta;
    }
}
