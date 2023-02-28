import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONArray;
public class Gioco {
    private Scanner kbds = new Scanner(System.in);
    private Scanner kbd = new Scanner(System.in);
    private Random rnd = new Random();
    private final int MAXPLAYERS = 2;
    private int NUMMAZZI = 2;
    private Player[] players;
    private Mazzo[] mazzi;
    private Ability[] abilities;
    private Terreno terreno = new Terreno();
    boolean idxPlayer;
    int idx;
    public Gioco(Player[] players) throws Exception{
        this.players = players;
        initMazzi();
        initAbilities();
        idx = 0;
        players[idx] = setMazzo(players[idx]);
        idx = 1;
        players[idx] = setMazzo(players[idx]);
        gameLoop();
    }
    public void gameLoop(){
        idxPlayer = firstPlayer();
        idx = idxPlayer ? 1 : 0;

        // Inizio Game
        int round = 0;
        boolean end = false;
        while(!end){
        	refresh();
            players[idx].increaseMana();
            players[idx].printStatus();
            players[idx].sendMsg("Mano di " + players[idx].getNickname() + ": [ " + players[idx].getMano().getNomiCarte() + " ]");
            boolean azionefinita = false;
            boolean cartaposizionata = false;
            while(!azionefinita){
                players[idx].sendMsg(players[idx].getNickname() + ", scegli cosa fare:");
                players[idx].sendMsg("1. Pesca una carta");
                players[idx].sendMsg("2. Piazza una carta sul terreno");
                players[idx].sendMsg("3. Attacca un mostro sul terreno o l'avversario");
                players[idx].sendMsg("4. Skip turno");
                String choice = players[idx].readMsg();
                int idxchoice;
                switch(choice){
                    case "1":
                        if(!(players[idx].getMazzo().isEmpty()) && !(players[idx].getMano().isFull())){
                            Object pickedcard = players[idx].pickCard();
                            if(((Carta)pickedcard).getTipo() == Cardtype.MOSTRO){
                                players[idx].sendMsg(((Mostro)pickedcard).toString());
                            }
                            azionefinita = true;
                        }else if(players[idx].getMazzo().isEmpty()){
                            players[idx].sendMsg("Mazzo Vuoto");
                        }else if(players[idx].getMano().isFull()){
                            players[idx].sendMsg("Mano piena");
                        }
                        break;
                    case "2":
                        if(!cartaposizionata){
                            Mazzo mano = players[idx].getMano();
                            idxchoice = selectCard(mano);
                            if(idxchoice >= 0){
                                if(mano.getCarta(idxchoice) instanceof Mostro){
                                    int manatoremove = ((Mostro)mano.getCarta(idxchoice)).getMana();
                                    if(players[idx].getMana() >= manatoremove){
                                        if(terreno.addCarta(idx,mano.pickCard(idxchoice))){
                                            players[idx].sendMsg("Carta aggiunta sul terreno");
                                            players[idx].sendMsg(terreno.turnPlayerTerrenoToMazzo(idx).getNomiCarte());
                                            players[idx].removeMana(manatoremove);
                                            refresh();
                                            cartaposizionata = true;
                                            break;
                                        }
                                    }else{
                                        players[idx].sendMsg("Non hai abbastanza mana");
                                    }
                                }else if(mano.getCarta(idxchoice) instanceof Magia){
                                    if(players[idx].getMana() >= ((Magia)mano.getCarta(idxchoice)).getMana()){
                                        if(terreno.addCarta(idx,mano.pickCard(idxchoice))){
                                            players[idx].sendMsg("Carta aggiunta sul terreno");
                                            players[idx].sendMsg(terreno.turnPlayerTerrenoToMazzo(idx).getNomiCarte());
                                            players[idx].removeMana(((Magia)mano.getCarta(idxchoice)).getMana());
                                            refresh();
                                            cartaposizionata = true;
                                            break;
                                        }
                                    }else{
                                        players[idx].sendMsg("Non hai abbastanza mana");
                                    }
                                }
                            }else{
                                players[idx].sendMsg("Non è stato possibile aggiungere la carta sul terreno");
                            }
                        }else{
                            players[idx].sendMsg("Non puoi posizionare un'altra carta");
                        }
                        break;
                    case "3":
                        int idxnemico = !idxPlayer ? 1 : 0;
                        players[idx].sendMsg("Con che carta vuoi attaccare?");
                        idxchoice = selectCard(terreno.turnPlayerTerrenoToMazzo(idx));
                        Mazzo mazzonemico = terreno.turnPlayerTerrenoToMazzo(idxnemico);
                        if(idxchoice >= 0 && mazzonemico.getTrueCardsId() != null && mazzonemico.getTrueCardsId().size() > 0){
                            players[idx].sendMsg("Terreno nemico:");
                            players[idx].sendMsg(mazzonemico.getNomiCarte());
                            players[idx].sendMsg("Quale carta vuoi attaccare?");
                            int idxchoice2 = selectCard(mazzonemico);
                            Object aggressoreObj = terreno.getCarteById(idx)[idxchoice];
                            Object vittimaObj = terreno.getCarteById(idxnemico)[idxchoice2];
                            if(attack(aggressoreObj, vittimaObj)){
                                if(vittimaObj instanceof Mostro){
                                    if(((Mostro)vittimaObj).getHP() <= 0){
                                        broadcastPlayers("il mostro " + ((Mostro)vittimaObj).getNome() + " è morto.");
                                        terreno.removeCarta(idxnemico, idxchoice2);
                                    }else{
                                        broadcastPlayers("Al mostro " + ((Mostro)vittimaObj).getNome() + " rimangono " + ((Mostro)vittimaObj).getHP() + " HP!");
                                    }
                                }else if(vittimaObj instanceof Magia){
                                    // Magia
                                }
                                azionefinita = true;
                            }
                        }else if(idxchoice >= 0 && round > 0){
                            if(attackPlayer(terreno.getCarteById(idx)[idxchoice], players[idxnemico])){
                                if(players[idxnemico].getHP() <= 0){
                                    broadcastPlayers("il player" + players[idxnemico].getNickname() + " è morto.");
                                    end = true;
                                }else{
                                    broadcastPlayers("Al player " + players[idxnemico].getNickname() + " rimangono " + players[idxnemico].getHP() + " HP!");
                                }
                                azionefinita = true;
                            }
                        }
                        break;
                    case "4":
                        azionefinita = true;
                        break;
                    /*
                    case "help":
                        players[idx].sendMsg("Di quale carta vuoi sapere la descrizione?");

                        Mazzo mazzoterreno = terreno.turnPlayerTerrenoToMazzo(idx);

                        players[idx].sendMsg("Terreno:");
                        players[idx].sendMsg(mazzoterreno.getNomiCarte());

                        players[idx].sendMsg("Mano:");
                        players[idx].sendMsg(players[idx].getMano().getNomiCarte());

                        players[idx].sendMsg("Mazzo:");
                        players[idx].sendMsg(players[idx].getMazzo().getNomiCarte());

                        choice = players[idx].readMsg();
                        break;
                    */
                }
                round++;
            }
            idxPlayer = !idxPlayer;
            idx = idxPlayer ? 1 : 0;
            if(players[0].getHP()<=0 || players[1].getHP()<=0){
                end = true;
            }
        }
        System.out.println("Fine della partita");
    }
    private void initMazzi() throws Exception{
        int count = 0, count2 = 0, count3 = 0;
        String jsonString = getJsonString("./giochetto/json/carte.json");
        JSONObject obj = new JSONObject(jsonString);
        JSONArray mazzicarte = obj.getJSONArray("data");
        NUMMAZZI = mazzicarte.length();
        mazzi = new Mazzo[NUMMAZZI];
        for(count = 0;count < NUMMAZZI;count++){
            String nomemazzo = mazzicarte.getJSONObject(count).getString("nomemazzo");
            System.out.println(nomemazzo);
            mazzi[count] = new Mazzo(5);
            mazzi[count].init(count, nomemazzo);
            JSONArray cartemazzo = mazzicarte.getJSONObject(count).getJSONArray("carte");
            for(count3 = 0;count3 < cartemazzo.length();count3++){
                if(cartemazzo.getJSONObject(count).getString("tipocarta").equalsIgnoreCase("mostro")){
                    mazzi[count].addCarta(new Mostro(
                        count3,
                        cartemazzo.getJSONObject(count3).getString("nomecarta"),
                        Cardtype.get(cartemazzo.getJSONObject(count3).getString("tipocarta")),
                        cartemazzo.getJSONObject(count3).getString("desccarta"),
                        cartemazzo.getJSONObject(count3).getInt("manacarta"),
                        cartemazzo.getJSONObject(count3).getInt("dmgcarta"),
                        cartemazzo.getJSONObject(count3).getInt("hpcarta"),
                        Naturatype.get(cartemazzo.getJSONObject(count3).getString("naturacarta"))
                    ));
                }
            }
            System.out.println(mazzi[count].toString());
            count2 += count3;
        }
        System.out.println("Inizializzati " + count + " " + ((count > 1) ? "mazzi" : "mazzo") + ", con un totale di " + count2 + " " + ((count2 > 1) ? "carte" : "carta"));
    }
    private void initAbilities() throws Exception{
        String jsonString = getJsonString("./giochetto/json/abilities.json");
        JSONObject obj = new JSONObject(jsonString);
        JSONArray abilities = obj.getJSONArray("data");
        this.abilities = new Ability[abilities.length()];
        for(int i=0;i<abilities.length();i++){
            this.abilities[i] = new Ability(
                i,
                abilities.getJSONObject(i).getString("name"),
                Abilitytype.get(abilities.getJSONObject(i).getString("type")),
                abilities.getJSONObject(i).getJSONObject("property")
            );
            System.out.println(this.abilities[i].toString());
        }
    }
    private Player setMazzo(Player p){
        boolean isnull = false;
        int idxmazzo;
        do{
            players[idx].sendMsg("Che mazzo vuoi usare? Inserire il numero");
            for(int i=0;i<NUMMAZZI;i++){
                if(mazzi[i] != null)
                    players[idx].sendMsg((i+1) + ". " + mazzi[i].getNomeMazzo());
            }
            idxmazzo = Integer.parseInt(players[idx].readMsg());
            if(idxmazzo > NUMMAZZI || idxmazzo < 1)
                isnull = true;
            else
                isnull = mazzi[idxmazzo-1] == null;
        }while(isnull);
        p.setMazzo(mazzi[idxmazzo - 1]);
        mazzi[idxmazzo-1] = null;
        return p;
    }

    private int selectCard(Mazzo mazzo){
        ArrayList<Integer> carte = mazzo.getTrueCardsId();
        String choice;
        if(carte != null && carte.size() > 0 && !(terreno.turnPlayerTerrenoToMazzo(idx).isFull())){
            int idxchoice;
            if(carte.size() > 1){
                players[idx].sendMsg("Che carta vuoi selezionare? Indicare il numero 1 - " + carte.size());
                for(int i=0;i<carte.size();i++){
                    players[idx].sendMsg((i+1) + ". " + ((Carta)(mazzo.getCarta(carte.get(i)))).getNome() + ",");
                }

                choice = players[idx].readMsg();
                idxchoice = Integer.parseInt(choice) - 1;

                while(idxchoice > carte.size() || idxchoice < 0){
                    players[idx].sendMsg("ERRORE: Inserire il numero della carta da selezionare");
                }

            }else{
                idxchoice = 0;
            }
            players[idx].sendMsg(mazzo.toString());
            players[idx].sendMsg("Vuoi selezionare " + ((Carta)(mazzo.getCarta(carte.get(idxchoice)))).getNome() + "?");
            choice = players[idx].readMsg();
            if(choice.equalsIgnoreCase("si")){
                return carte.get(idxchoice);
            }
        }else{
            players[idx].sendMsg("Non hai nessuna carta da selezionare");
        }
        return -1;
    }

    private boolean attack(Object obj1, Object obj2){
        if(obj1 instanceof Carta && obj2 instanceof Carta){
            Carta card1 = (Carta)obj1;
            Carta card2 = (Carta)obj2;
            if(card1.getTipo() == Cardtype.MOSTRO){
                Mostro mostro1 = (Mostro)card1;
                if(card2.getTipo() == Cardtype.MOSTRO){
                    Mostro mostro2 = (Mostro)card2;
                    if(Naturatype.isStrongerTo(mostro1.getNatura(),mostro2.getNatura())) ((Mostro)card2).damage(((Mostro)card1).getDmg() * 2);
                    else if(Naturatype.isWeakerTo(mostro1.getNatura(),mostro2.getNatura())) ((Mostro)card2).damage(((Mostro)card1).getDmg() / 2);
                    else ((Mostro)card2).damage(((Mostro)card1).getDmg());
                }else if(card2.getTipo() == Cardtype.MAGIA){
                    // Magia
                }
                refresh();
                return true;
            }
        }
        return false;
    }

    private boolean attackPlayer(Object obj1, Player p){
        if(obj1 instanceof Carta){
            Carta card1 = (Carta)obj1;
            if(card1.getTipo() == Cardtype.MOSTRO){
                p.damage(((Mostro)card1).getDmg());
            }else if(card1.getTipo() == Cardtype.MAGIA){
                // Magia
            }
            refresh();
            return true;
        }
        return false;
    }

    private void refresh(){
        for(Player p : players){
            p.sendMsg("/refresh");
            if(p.getId() != 1){
                p.sendMsg(players[0].turnToJson());
                p.sendMsg(players[1].turnToJson());
            }else{
                p.sendMsg(players[1].turnToJson());
                p.sendMsg(players[0].turnToJson());
            }
            p.sendMsg(terreno.turnToJson());
        }
    }

    private void broadcastPlayers(String text){
        for(Player p : players){
            p.sendMsg("GAME: " + text);
        }
    }

    private boolean firstPlayer(){
        boolean idxPlayer = rnd.nextBoolean();
        players[idx].sendMsg("Inizia il " + (idxPlayer == false ? "primo" : "secondo") + " giocatore");

        if(idxPlayer){
            players[1].increaseMana();
        }else{
            players[0].increaseMana();
        }

        return idxPlayer;
    }
    private String getJsonString(String path)throws Exception{
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}