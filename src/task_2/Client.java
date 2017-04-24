package task_2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// receive
					ServerSocket listener = new ServerSocket(1234);
					Socket listenerSocket = listener.accept();
					ObjectInputStream receiveData = new ObjectInputStream(listenerSocket.getInputStream());
					Agent new_agent = (Agent) receiveData.readObject();
					System.out.println("New Id: " + new_agent.showId());
					System.out.println("New Name: " + new_agent.showName());
					System.out.println("Task: " + new_agent.showTask());
					if (new_agent.showTask().equals("HELLO WORLD")) {
						new_agent.setStatus(true);
						System.out.println("Mission Completed!");
					} else
						System.out.println("Mission Failed");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();

		Agent agent = new AgentImp(123, "Alice", "localhost", 1234); // set home IP and Port
		Socket clientSocket = new Socket("localhost", 4444); // connect to server
		// send
		ObjectOutputStream sendData = new ObjectOutputStream(clientSocket.getOutputStream());
		sendData.writeObject(agent);
		// clean
		sendData.close();
		clientSocket.close();
	}

}
