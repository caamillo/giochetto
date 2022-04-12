public abstract class Carta{
    public int idcarta = -1;
    public String nomecarta = "Blank Card";
    public Cardtype tipocarta = Cardtype.BLANK;
    public String desccarta = "Blank Description";
    public Carta(int id,String nome,Cardtype tipo,String desc){
        if (!setId(id)) System.out.println("Errore id");
        if (!setNome(nome)) System.out.println("Errore nome");
        if (!setTipo(tipo)) System.out.println("Errore tipo");
        if (!setDesc(desc)) System.out.println("Errore desc");
    }
    public boolean setId(int idcarta){
        if(idcarta>=0 && this.idcarta != idcarta){
            this.idcarta = idcarta;
            return true;
        }
        return false;
    }
    public boolean setNome(String nomecarta){
        if(nomecarta.length()>0 && !(this.nomecarta.equalsIgnoreCase(nomecarta))){
            this.nomecarta = nomecarta;
            return true;
        }
        return false;
    }
    public boolean setTipo(Cardtype tipocarta){
        if(tipocarta != Cardtype.BLANK && tipocarta != this.tipocarta){
            if(tipocarta == Cardtype.MOSTRO){
                this.tipocarta = tipocarta;
                return true;
            }
        }
        return false;
    }
    public boolean setDesc(String desccarta){
        if(desccarta.length()>0 && !(this.desccarta.equalsIgnoreCase(desccarta))){
            this.desccarta = desccarta;
            return true;
        }
        return false;
    }
    public int getId(){
        return idcarta;
    }
    public String getNome(){
        return nomecarta;
    }
    public Cardtype getTipo(){
        return tipocarta;
    }
    public String getDesc(){
        return desccarta;
    }
    public boolean isCarta(Object carta){
        if(carta instanceof Carta)
            return ((Carta)carta).getId() == idcarta;
        return false;
    }
    public String tunToJson(){
        return "{\"id\":" + idcarta + ",\"nome\":\"" + nomecarta + "\",\"tipo\":\"" + tipocarta.getAbbreviation() + "\",\"desc\":\"" + desccarta + "\"}";
    }
    public String toString(){
        return "carta " +
                "id: " + idcarta + ", " +
                "nome: " + nomecarta + ", " +
                "tipo: " + tipocarta.getAbbreviation() + ", " +
                "desc: " + desccarta;
    }
}