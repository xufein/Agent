package task_3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;

public class Client {

	static String serverIP = "192.168.1.66";
	static int serverPort = 4444;
	static String homeIP = "192.168.1.66"; // set local IP
	static int homePort = 1234;

	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws UnknownHostException, IOException {
		cmd();
		agentHome();
		// agentSend();
		agentSend("7", "Courier", "192.168.1.66", "4444"); // test

	}

	// public static void agentSend() throws UnknownHostException, IOException {
	// Agent agent = new AgentImp(007, "Courier", homeIP, homePort); // home
	// Socket clientSocket = new Socket(serverIP, serverPort); // server
	// ObjectOutputStream sendAgent = new
	// ObjectOutputStream(clientSocket.getOutputStream());
	// sendAgent.writeObject(agent);
	// sendAgent.close();
	// clientSocket.close();
	// }

	public static void agentSend(String id, String name, String serverIP, String serverPort)
			throws UnknownHostException, IOException {
		Agent agent = new AgentImp(Integer.parseInt(id), name, homeIP, homePort); // home
		Socket clientSocket = new Socket(serverIP, Integer.parseInt(serverPort)); // server
		ObjectOutputStream sendAgent = new ObjectOutputStream(clientSocket.getOutputStream());
		sendAgent.writeObject(agent);
		sendAgent.close();
		clientSocket.close();
	}

	public static void agentHome() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket homeSocket = new ServerSocket(homePort);
					while (true) {
						Socket agentSocket = homeSocket.accept();
						ObjectInputStream inputStream = new ObjectInputStream(agentSocket.getInputStream());
						Agent new_agent = (Agent) inputStream.readObject();
						System.out.println("Agent back to home");
						System.out.print("Id: " + new_agent.showId() + ", Name: " + new_agent.showName());
						System.out.println(", From: " + new_agent.showIP() + ":" + new_agent.showPort());
						System.out.println("Task: " + new_agent.showTask());
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	// TODO: add function
	public static void cmd() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					String command = scanner.nextLine();
					switch (command) {
					case "list":
						System.out.println("'new' to send new agent.");
						System.out.println("'discover' to boardcast.");
						break;
					case "new":
						System.out.println("Input 'id name serverIP serverPort'");
						String info = scanner.nextLine();
						String[] strArr = info.split(" ");
						try {
							agentSend(strArr[0], strArr[1], strArr[2], strArr[3]);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						break;
					case "vector":
						System.out.println(Server.vector);
						break;
					case "discover":
						try {
							Server.discover();
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}).start();
	}

}
