public class Mostro extends Carta{
    private int manacarta = -1;
    private int dmgcarta = -1;
    private int hpcarta = -1;
    private Naturatype naturacarta = Naturatype.BLANK;
    public Mostro(Object toClone){
        super((((Carta)toClone)).getId(),((Carta)toClone).getNome(),((Carta)toClone).getTipo(),((Carta)toClone).getDesc());
        Mostro carta = (Mostro)toClone;
        if(!setMana(carta.getMana())) System.out.println("Errore mana");
        if(!setDmg(carta.getDmg())) System.out.println("Errore dmg");
        if(!setHP(carta.getHP())) System.out.println("Errore hp");
        if(!setNatura(carta.getNatura())) System.out.println("Errore natura");
    }
    public Mostro(int id,String nome,Cardtype tipo,String desc,int mana,int dmg,int hp,Naturatype natura){
        super(id,nome,tipo,desc);
        if(!setMana(mana)) System.out.println("Errore mana");
        if(!setDmg(dmg)) System.out.println("Errore dmg");
        if(!setHP(hp)) System.out.println("Errore hp");
        if(!setNatura(natura)) System.out.println("Errore natura");
    }
    public int getMana(){
        return manacarta;
    }
    public int getDmg(){
        return dmgcarta;
    }
    public int getHP(){
        return hpcarta;
    }
    public Naturatype getNatura(){
        return naturacarta;
    }
    public boolean setMana(int manacarta){
        if(manacarta>0 && this.manacarta != manacarta){
            this.manacarta = manacarta;
            return true;
        }
        return false;
    }
    public boolean setDmg(int dmgcarta){
        if(dmgcarta>0 && this.dmgcarta != dmgcarta){
            this.dmgcarta = dmgcarta;
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
    public boolean setNatura(Naturatype naturacarta){
        if(naturacarta != Naturatype.BLANK && this.naturacarta != naturacarta){
            this.naturacarta = naturacarta;
            return true;
        }
        return false;
    }
    public void damage(int dmg){
        hpcarta -= dmg;
    }
    public boolean isCarta(Object mostro){
        if(mostro instanceof Mostro)
            return super.isCarta(mostro);
        return false;
    }
    public String turnToJson(){
        StringBuffer sb = new StringBuffer(super.tunToJson());
        sb.deleteCharAt(sb.length() - 1);
        return sb + ",\"mana\":\"" + manacarta + "\",\"dmg\":\"" + dmgcarta + "\",\"hp\":\"" + hpcarta + "\",\"natura\":\"" + naturacarta.getAbbreviation() + "\"}";
    }
    public String toString(){
        return super.toString() + ", " +
            "mana: " + manacarta + ", " +
            "dmg: " + dmgcarta + ", " +
            "hp: " + hpcarta + ", " +
            "natura: " + naturacarta.getAbbreviation();
    }
}
