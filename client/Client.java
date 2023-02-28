import java.io.*;

import java.util.Scanner;

import java.net.Socket;

import org.json.JSONObject;
import org.json.JSONArray;

public class Client {
	private static final String HOST = "localhost";
	private static final int PORT = 9000;
	private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private static ClientWindow clientwindow;
	public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.username = username;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
	public static void main(String[] args)  throws IOException{
        Scanner kbd = new Scanner(System.in);
        System.out.print("Inserire username: ");
        String username = kbd.nextLine();

        Socket socket = new Socket(HOST, PORT);

        Client client = new Client(socket, username);
		
        client.listenForMessage();
        client.sendMessage();
	}

	public void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    // Manda username 
                    bufferedWriter.write(username);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
        
                    Scanner kbd = new Scanner(System.in);
        
                    while (socket.isConnected()) {
                        String messageToSend = kbd.nextLine();
                        bufferedWriter.write(messageToSend);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

	public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String text;
                while (socket.isConnected()) {
                    try {
                        text = bufferedReader.readLine();
                        if(text.charAt(0)!='/') System.out.println(text);
                        else{
                            handleCommand(text);
                        }
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void handleCommand(String command){
        command = command.substring(1);
        try{
            if(command.equalsIgnoreCase("refresh")){
                String p1Json = bufferedReader.readLine();
                String p2Json = bufferedReader.readLine();
                String terrenoJson = bufferedReader.readLine();
                try{
                    refresh(
                        new JSONObject(p1Json),
                        new JSONObject(p2Json),
                        new JSONObject(terrenoJson)
                    );
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void refresh(JSONObject p1Obj,JSONObject p2Obj,JSONObject terrenoObj){
        if(clientwindow == null) clientwindow = new ClientWindow(p1Obj,p2Obj,terrenoObj);
        else{
            clientwindow.refresh(p1Obj,p2Obj,terrenoObj);
        }
    }

	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}