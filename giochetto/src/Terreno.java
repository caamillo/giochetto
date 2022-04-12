public class Terreno {
    private final int MAXCARTE = 5;
    private final int MAXPLAYERS = 2;
    private Object[][] carte = new Object[MAXPLAYERS][MAXCARTE];
    public Object[][] getCarte(){
        return carte;
    }
    public Object[] getCarteById(int id){
        return carte[id];
    }
    public void setCarte(int id,Object[] carte){
        this.carte[id] = carte;
    }
    public boolean addCarta(int id,Object carta){
        int idxcarta = getEmptyPos(id);
        if(idxcarta >= 0 && carta != null){
            carte[id][idxcarta] = carta;
            return true;
        }
        return false;
    }
    public boolean removeCarta(int id,int indcarta){
        if(indcarta < carte.length){
            carte[id][indcarta] = null;
            return true;
        }
        return false;
    }
    private int getEmptyPos(int id){
        for(int i=0;i<MAXCARTE;i++){
            if(carte[id][i] == null)
                return i;
        }
        return -1;
    }
    public Mazzo turnPlayerTerrenoToMazzo(int idx){
        Object[] carteterreno = getCarteById(idx);
        Mazzo mazzoterreno = new Mazzo(MAXCARTE);
        mazzoterreno.setCarte(carteterreno);
        return mazzoterreno;
    }
    public String turnToJson(){
        return "{\"first\":" + turnPlayerTerrenoToMazzo(0).turnToJson() + ",\"second\":" + turnPlayerTerrenoToMazzo(1).turnToJson() +"}";
    }
}
