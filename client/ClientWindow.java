import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.json.JSONObject;
import org.json.JSONArray;

public class ClientWindow extends JFrame{
	
	private JSONObject player;
	private JSONObject enemy;
	private JSONObject terreno;
	
	private JButton attackBtn, placeBtn, pickBtn;
	private JPanel playerPanel;
	private JPanel enemyPanel;
	
	public ClientWindow(JSONObject p1Obj,JSONObject p2Obj,JSONObject terrenoObj){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				setPlayer(p1Obj);
				setEnemy(p2Obj);
				setTerreno(terrenoObj);
			    JFrame frame = new JFrame();
			    frame.setTitle("Client Giochino");
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.add(new UserPanel(),BorderLayout.PAGE_END);
			    frame.add(new GamePanel(),BorderLayout.CENTER);
			    frame.setResizable(false);
			    frame.setSize(1280,720);
			    frame.setLocationRelativeTo(null);
			    frame.setVisible(true);
			}
			
		});
	}
	
	public JSONObject getPlayer() {
		return player;
	}
	
	public void setPlayer(JSONObject player) {
		this.player = player;
	}
	
	public JSONObject getEnemy() {
		return enemy;
	}
	
	public void setEnemy(JSONObject enemy) {
		this.enemy = enemy;
	}
	
	public JSONObject getTerreno() {
		return terreno;
	}
	
	public void setTerreno(JSONObject terreno) {
		this.terreno = terreno;
	}
	
	public class GamePanel extends JPanel{
		public GamePanel() {
			playerPanel = new PlayerPanel(player);
			enemyPanel = new PlayerPanel(enemy);
			setBackground(Color.gray);
			setLayout(new BorderLayout());
			add(enemyPanel,BorderLayout.PAGE_START);
			add(playerPanel,BorderLayout.PAGE_END);
		}
	}
	
	public class PlayerPanel extends JPanel{
		
		JSONObject player;
		
		public PlayerPanel(JSONObject player) {
			this.player = player;
			setBackground(Color.white);
			setPreferredSize(new Dimension(200,280));
			setLayout(new FlowLayout(FlowLayout.CENTER,100,25));
			addCards();
		}
		public void addCards() {
			String word = player.getInt("id") == 0 ? "first" : "second";
			for(int i=0;i<5;i++) {
				try{
					add(new Card(terreno.getJSONArray(word).getJSONObject(i)),BorderLayout.CENTER);
				}catch(Exception e){
					add(new Card(null),BorderLayout.CENTER);
				}
			}
		}
		public void refresh() {
			removeAll();
			addCards();
			revalidate();
			repaint();
			System.out.println("refreshed");
		}
	}
	
	public class UserPanel extends JPanel{
		public UserPanel() {
			JButton[] actionbtns = {
					new ActionBtn("Pick"),
					new ActionBtn("Attack"),
					new ActionBtn("Place")
			};
            setPreferredSize(new Dimension(200,80));
            setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
            add(Box.createVerticalGlue());
            add(Box.createHorizontalGlue());
            for(JButton actionbtn : actionbtns) {
            	add(actionbtn);
            	add(Box.createRigidArea(new Dimension(100, 0)));
            }add(Box.createHorizontalGlue());
            add(Box.createVerticalGlue());
		}
	}
	
	public class ActionBtn extends JButton implements ActionListener{
		private String action;
		public ActionBtn(String action) {
			addActionListener(this);
			setEnabled(false);
			this.action = action;
			setText(this.action);
			Dimension actiondimension = new Dimension(150,50);
			setMaximumSize(actiondimension);
			setMinimumSize(actiondimension);
			setPreferredSize(actiondimension);
			setBackground(Color.LIGHT_GRAY);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println(this.action);
		}
	}
	
	public void refresh(JSONObject p1Obj,JSONObject p2Obj,JSONObject terrenoObj) {
		if(playerPanel != null && playerPanel instanceof PlayerPanel) {
			setPlayer(p1Obj);
			setEnemy(p2Obj);
			setTerreno(terrenoObj);
			((PlayerPanel)playerPanel).refresh();
			((PlayerPanel)enemyPanel).refresh();
		}
	}
}
