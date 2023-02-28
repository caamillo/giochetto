import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.json.JSONArray;

import java.lang.Math;

public class Card extends JPanel{
	private JSONObject card = null;
	
	private int idcarta = -1;
	private String nomecarta = null;
	private Cardtype tipocarta = Cardtype.BLANK;
	private String desccarta = null;
	private int hpcarta = -1;
	private int dmgcarta = -1;
	private Naturatype naturacarta = null;
	
	public Card(JSONObject cardObj) {
		setCard(card);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(150,230));
		setLayout(new BorderLayout());
		//this.idcarta = idcard;
		this.card = cardObj;
		if(card == null  || Cardtype.get(card.getString("tipo")) == Cardtype.BLANK)
			add(new propertyPanel(),BorderLayout.CENTER);
		else {
			nomecarta = card.getString("nome");
			tipocarta = Cardtype.get(card.getString("tipo"));
			desccarta = card.getString("desc");
			if(tipocarta == Cardtype.MOSTRO) {
				hpcarta = card.getInt("hp");
				dmgcarta = card.getInt("dmg");
				naturacarta = Naturatype.get(card.getString("natura"));
			}
			add(
				new propertyPanel(
					nomecarta,
					tipocarta,
					desccarta,
					String.valueOf(hpcarta),
					String.valueOf(dmgcarta),
					naturacarta
				),
				BorderLayout.CENTER
			);
		}
	}
	public Object getCard() {
		return card;
	}
	public void setCard(JSONObject card) {
		this.card = card;
	}
	/*
	private JSONObject getCartaByIdx(int idx){
		if(idx < 0) return null;
		try{
			String jsonString = getJsonString();
			JSONObject obj = new JSONObject(jsonString);
			JSONArray mazzicarte = obj.getJSONArray("data");
			JSONArray mazzo = mazzicarte.getJSONObject(Math.round(idx/5)).getJSONArray("carte");
			return mazzo.getJSONObject(idx - (Math.round(idx/5)*5));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	private String getJsonString()throws Exception{
        return new String(Files.readAllBytes(Paths.get("./json/carte.json")));
    }
	*/
	private class propertyPanel extends JPanel{
		propertyPanel(){
			setBackground(Color.BLACK);
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			add(Box.createVerticalGlue());
			add(new propertyLabel("VUOTO"));
			add(Box.createVerticalGlue());
		}
		propertyPanel(String nomecarta,Cardtype tipocarta,String desccarta,String hpcarta,String dmgcarta,Naturatype naturacarta){
			setBackground(Color.BLACK);
			setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			add(Box.createVerticalGlue());
			add(new propertyLabel("nome",nomecarta));
			add(new propertyLabel("tipo",tipocarta.getAbbreviation()));
			add(new propertyLabel("desccarta",desccarta));
			if(tipocarta == Cardtype.MOSTRO) {
				add(new propertyLabel("hpcarta",hpcarta));
				add(new propertyLabel("dmgcarta",dmgcarta));
				add(new propertyLabel("naturacarta",naturacarta.getAbbreviation()));
			}
			add(Box.createVerticalGlue());
		}
	}
	private class propertyLabel extends JLabel{
		propertyLabel(String text) {
			setText(text);
			setForeground(Color.white);
			setAlignmentX(JLabel.CENTER_ALIGNMENT);
			setSize(getPreferredSize());
		}
		propertyLabel(String type, String text) {
			setText(type + ": " + text);
			setForeground(Color.white);
			setAlignmentX(JLabel.CENTER_ALIGNMENT);
			setSize(getPreferredSize());
		}
	}
	private class ClickListener extends MouseAdapter{
		
	}
}