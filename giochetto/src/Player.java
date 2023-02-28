import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
public class  Player {
    private int id;
    private String nickname;
    private int hp = 8000;
    private int mana = 0;
    private final int MAXMANA = 10;
    private final int MAXMANO = 8;
    private Mazzo mazzo;
    private Mazzo mano = new Mazzo(MAXMANO);
    private Random rnd = new Random();
    private ClientHandler clienthandler;
    public Player(int id,String nickname){
        if(!setId(id)) System.out.println("Errore id");
        if(!setNickname(nickname)) System.out.println("Errore nickname");
    }
    private boolean setId(int id){
        if(id>=0){
            this.id = id;
            return true;
        }
        return false;
    }
    private boolean setNickname(String nickname){
        if(nickname.length()>0){
            this.nickname = nickname;
            return true;
        }
        return false;
    }
    public boolean setClientHandler(ClientHandler clienthandler){
        if(clienthandler != null){
            this.clienthandler = clienthandler;
            return true;
        }
        return false;
    }
    public void setMazzo(Mazzo mazzo){
        this.mazzo = mazzo;
    }
    public void setMano(Mazzo mano){
        this.mano = mano;
    }
    public int getId(){
        return id;
    }
    public String getNickname(){
        return nickname;
    }
    public int getHP(){
        return hp;
    }
    public int getMana(){
        return mana;
    }
    public Mazzo getMazzo(){
        return mazzo;
    }
    public Mazzo getMano(){
        return mano;
    }
    public ClientHandler getClientHandler(){
        return clienthandler;
    }
    public boolean addCardToMazzo(Object card){
        return mazzo.addCarta(card);
    }
    public boolean removeCardToMazzo(int card){
        return mazzo.removeCarta(card);
    }
    public boolean addCardToHand(Object card){
        return mano.addCarta(card);
    }
    public boolean addCardToHand(int card){
        return mano.removeCarta(card);
    }
    public Object pickCard(){
        if(!mazzo.isEmpty() && !mano.isFull()){
            ArrayList<Integer> truecards = mazzo.getTrueCardsId();
            Object pickedcard = mazzo.pickCard(truecards.get(rnd.nextInt(truecards.size())));
            mano.addCarta(pickedcard);
            return pickedcard;
        }
        return null;
    }
    public boolean increaseMana(){
        if(mana < MAXMANA){
            mana++;
            return true;
        }
        return false;
    }
    public void removeMana(int manatoremove){
        if(mana - manatoremove >= 0){
            mana -= manatoremove;
        }else if(mana > 0){
            mana = 0;
        }
    }
    public void damage(int dmg){
        hp -= dmg;
    }
    public boolean isPlayer(Player p){
        if(p.getId() == id){
            return true;
        }
        return false;
    }

    public void sendMsg(String text){
        try{
            clienthandler.getBufferedWriter().write(text);
            clienthandler.getBufferedWriter().newLine();
            clienthandler.getBufferedWriter().flush();
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public String readMsg(){
        try {
            return clienthandler.getBufferedReader().readLine();
        } catch (IOException e) {
            return "ERRORE";
        }
    }

    public String turnToJson(){
        return "{\"id\":" + id +",\"nickname\":\"" + nickname + "\"}";
    }

    public void printStatus(){
        System.out.println(
            nickname + " status: [ " +
                "HP: " + hp + ", " +
                "MANA: " + mana + ", " +
                "CARTE NEL MAZZO: " +  (mazzo.isEmpty() ? "Nessuna" : mazzo.getTrueCardsId().size()) + ", " +
                "CARTE NELLA MANO: " + (mano.isEmpty() ? "Nessuna" : mano.getTrueCardsId().size()) +
            " ]"
        );
    }
}
