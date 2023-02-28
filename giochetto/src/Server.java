import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket serverSocket;
    private static final int PORT = 9000;
	private WaitingRoom wroom = new WaitingRoom();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
		Thread threadRoom = new Thread(wroom);
		threadRoom.start();
    }

    public void startServer() throws InterruptedException,Exception{
        try {
            System.out.println("Server startato");
            int count = 0;
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                System.out.println("Un client si è unito");
				String nickname = bufferedReader.readLine();
				Player player = new Player(count,nickname);
                ClientHandler clientHandler = new ClientHandler(socket);
				player.setClientHandler(clientHandler);
				clientHandler.setPlayer(player);
                Thread thread = new Thread(clientHandler);
                thread.start();
                count++;
				wroom.join(player);
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if(serverSocket != null)
                serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException,InterruptedException,Exception{
        ServerSocket serverSocket = new ServerSocket(PORT);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}