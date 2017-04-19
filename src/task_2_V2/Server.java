package task_2_V2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Socket serversocket = null;
		ObjectInputStream inputStream = null;
		ObjectOutputStream outputSteam = null;
		ServerSocket server = new ServerSocket(4444); // server port

		while (true) {
			// receive
			serversocket = server.accept();
			inputStream = new ObjectInputStream(serversocket.getInputStream());
			Agent agent = (Agent) inputStream.readObject();
			System.out.print("Id: " + agent.showId() + ", Name: " + agent.showName());
			System.out.println(", From: " + agent.showIP() + ":" + agent.showPort());
			// clean
			inputStream.close();
			serversocket.close();

			// update and send back
			agent.setId(456);
			agent.setName("Bob");
			agent.setTask("HELLO WORLD");
			Socket clientSocket = new Socket(agent.showIP(), agent.showPort());
			outputSteam = new ObjectOutputStream(clientSocket.getOutputStream());
			outputSteam.writeObject(agent);
			// clean
			outputSteam.close();
			clientSocket.close();
		}
	}

}
