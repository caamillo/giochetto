public class CartaTerreno{
    private Object carta;
    private Player owner;
    private boolean isturned = false;
    public CartaTerreno(Carta carta,Player owner){
        this.carta = carta;
        this.owner = owner;
    }
    public Object getCarta(){
        return carta;
    }
    public Player getOwner(){
        return owner;
    }
    public boolean isTurned(){
        return isturned;
    }
    public void turnIt(){
        if(!isturned)
            isturned = true;
    }
    public boolean isCarta(Object carta){
        if(carta instanceof CartaTerreno)
            return ((Carta)this.carta).isCarta(((CartaTerreno)carta).getCarta());
        return false;
    }
}