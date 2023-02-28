import java.util.Random;
import java.util.ArrayList;
public class Mazzo{
    private int idmazzo = -1;
    private String nomemazzo = "Blank Mazzo";
    private int MAXCARTE;
    private Object[] carte;
    private Random rnd = new Random();
    public Mazzo(int MAXCARTE){
        this.MAXCARTE = MAXCARTE;
        carte = new Object[MAXCARTE];
    }
    public void init(int id,String nome){
        this.idmazzo = id;
        this.nomemazzo = nome;
    }
    public void setCarte(Object[] carte){
        MAXCARTE = carte.length;
        this.carte = carte;
    }
    public int getId(){
        return idmazzo;
    }
    public String getNomeMazzo(){
        return nomemazzo;
    }
    public Object[] getCarte(){
        return carte;
    }
    public Object getCarta(int id){
        return carte[id];
    }
    public boolean addCarta(Object carta){
        int idxcarta = getEmptyPos();
        if(idxcarta >= 0){
            carte[idxcarta] = carta;
            return true;
        }
        return false;
    }
    public boolean removeCarta(int indcarta){
        if(indcarta < MAXCARTE && indcarta >= 0){
            carte[indcarta] = null;
            return true;
        }
        return false;
    }
    public ArrayList<Integer> getTrueCardsId(){
        ArrayList<Integer> actuallycardsid = new ArrayList<Integer>();
        boolean added = false;
        for(int i = 0;i < MAXCARTE;i++){
            if(carte[i] != null){
                actuallycardsid.add(i);
                added = true;
            }
        }
        if(added)
            return actuallycardsid;
        return null;
    }
    public Object pickCard(int idx){
        Object picked = null;
        if(carte[idx] instanceof Mostro){
            picked = new Mostro(carte[idx]);
        }else if(carte[idx] instanceof Magia){
            picked = new Magia(carte[idx]);
        }
        if (picked != null) removeCarta(idx);
        return picked;
    }
    private int getEmptyPos(){
        for(int i=0;i<MAXCARTE;i++){
            if(carte[i] == null)
                return i;
        }
        return -1;
    }
    public String getNomiCarte(){
        String nomecarte = "";
        boolean empty = true;
        for(int i=0;i<MAXCARTE;i++){
            boolean added = false;
            if(carte[i] != null && carte[i] instanceof Carta){
                nomecarte += ((Carta)carte[i]).getNome();
                added = true;
                empty = false;
            }
            if(added)
                nomecarte += ", ";
        }
        if(!empty)
            return nomecarte.substring(0,nomecarte.length()-2);
        return "Vuoto";
    }
    public boolean isFull(){
        return getEmptyPos() < 0;
    }
    public boolean isEmpty(){
        return getTrueCardsId() == null;
    }
    public String turnToJson(){
        String json = "[";
        int count = 0;
        for(Object carta : carte){
            if(carta instanceof Carta){
                if (((Carta)carta).getTipo() == Cardtype.MOSTRO) json += ((Mostro)carta).turnToJson();
            }else{
                json += "null";
            }
            if(count < carte.length - 1){
                json += ",";
            }
            count++;
        }
        if (count < MAXCARTE && count > 0) json += ",";
        while(count < MAXCARTE){
            json += -1;
            if(count < MAXCARTE - 1){
                json += ",";
            }
            count++;
        }
        json += "]";
        return json;
    }
    public String toString(){
        return "Mazzo " +
                    "id: " + idmazzo + ", " +
                    "nome: " + nomemazzo + ", " +
                    "carte: { " + getNomiCarte() + " }";                         
    }
}