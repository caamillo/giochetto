import java.util.concurrent.TimeUnit;

import java.io.IOException;

import java.util.ArrayList;

public class WaitingRoom implements Runnable{
    private final int MAXPLAYERS = 2;
    private ArrayList<Player> players = new ArrayList<Player>();
    public ArrayList<Player> getPlayers(){
        return players;
    }
    @Override
    public void run(){
        while(true){
            try{
                while(players.size() < MAXPLAYERS){
                    broadcastMessage("Attendere che l'altro giocatore si unisca...");
                    TimeUnit.SECONDS.sleep(1);
                }
                Player[] players2 = new Player[MAXPLAYERS];
                for(int i=0;i<MAXPLAYERS;i++){
                    Player player2 = new Player(players.get(i).getId(),players.get(i).getNickname());
                    player2.setClientHandler(players.get(i).getClientHandler());
                    players2[i] = player2;
                }
                new Gioco(players2);
                cleanRoom();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void join(Player player) throws IOException,InterruptedException,Exception{
        while(!addPlayer(player)){
            player.sendMsg("waiting for join");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println("Sei entrato nella stanza");
    }
    public boolean addPlayer(Player player){
        if (players.size() >= MAXPLAYERS) return false;
        players.add(player);
        return true;
    }
    public boolean removePlayer(int idPlayer){
        for(Player player : players){
            if(player.getId() == idPlayer){
                players.remove(player);
                return true;
            }
        }
        return false;
    }

    public void broadcastMessage(String text){
        for(Player p : players){
            p.sendMsg(text);
        }
    }

    public void cleanRoom(){
        players.clear();
    }
}