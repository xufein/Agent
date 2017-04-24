package task_3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

public class Server {

	static String localIP = "192.168.1.66"; // set local IP
	static String group = "239.0.0.1";
	static int basePort = 1234;
	static int serverPort = 4444;
	static String localTask = "Windows "; // agent carry this message when arrive

	static Agent agent;
	static Vector<String> vector = new Vector<String>(); // save found server in vector
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		System.out.println("Server is running, type 'help' to get help.\n");
		cmd();
		listener();
		discover();
		agentArrive();
	}

	// broadcast to find server
	public static void discover() throws IOException {
		InetSocketAddress address = new InetSocketAddress(localIP, basePort);
		MulticastSocket discoverSocket = new MulticastSocket(address);
		byte[] discoverData = { 'R', 'E', 'Q', 'U', 'E', 'S', 'T' };
		DatagramPacket discoverPacket = new DatagramPacket(discoverData, discoverData.length,
				InetAddress.getByName("255.255.255.255"), basePort);
		discoverSocket.send(discoverPacket);
		discoverSocket.close();
	}

	// broadcast listener
	public static void listener() throws IOException {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						// receive
						MulticastSocket listenerSocket = new MulticastSocket(basePort);
						listenerSocket.joinGroup(InetAddress.getByName(group));
						byte[] listenerData = new byte[7];
						DatagramPacket listenerPacket = new DatagramPacket(listenerData, listenerData.length);
						listenerSocket.receive(listenerPacket);
						listenerSocket.close();
						// show packet source and save in vector
						String source = listenerPacket.getAddress().toString();
						String data = new String(listenerPacket.getData());
						System.out.println("Receive " + data + " from: " + source);
						if (data.equals("LEAVE  ")) {
							vector.remove(source);
							System.out.println(source + " leave group");
						}
						if (data.equals("REQUEST")) {
							if (vector.contains(source) != true)
								vector.addElement(source);
							// reply
							MulticastSocket replySocket = new MulticastSocket(serverPort);
							byte[] replyData = { 'R', 'E', 'P', 'L', 'Y', ' ', ' ' };
							DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length,
									listenerPacket.getAddress(), basePort);
							System.out.println("Send reply to " + listenerPacket.getAddress() + ":" + basePort + "\n");
							replySocket.send(replyPacket);
						}
						if (data.equals("REPLY  ")) {
							if (vector.contains(source) != true)
								vector.addElement(source);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	// leave group
	public static void leave() throws IOException {
		InetSocketAddress address = new InetSocketAddress(localIP, basePort);
		MulticastSocket discoverSocket = new MulticastSocket(address);
		byte[] discoverData = { 'L', 'E', 'A', 'V', 'E', ' ', ' ' };
		DatagramPacket discoverPacket = new DatagramPacket(discoverData, discoverData.length,
				InetAddress.getByName("255.255.255.255"), basePort);
		discoverSocket.send(discoverPacket);
		discoverSocket.close();
	}

	// server wait for agent arrive
	public static void agentArrive() throws IOException, ClassNotFoundException {
		ServerSocket serversocket = new ServerSocket(serverPort);
		while (true) {
			Socket agentSocket = serversocket.accept();
			ObjectInputStream inputStream = new ObjectInputStream(agentSocket.getInputStream());
			agent = (Agent) inputStream.readObject();
			System.out.print("Receive Agent Id: " + agent.showId() + ", Name: " + agent.showName());
			System.out.println(", Home: " + agent.showIP() + ":" + agent.showPort());
			// show visited and set new visited
			agent.setVisited(localIP + ":" + serverPort);
			System.out.println("Visited: " + agent.showVisited());
			// show task and set new task
			System.out.println("Task: " + agent.showTask());
			agent.setTask(agent.showTask() + localTask);
			System.out.println("New Task: " + agent.showTask() + "\n");
		}
	}

	// forward agent
	public static void agentForward(String server, String port) throws IOException {
		Socket clientSocket = new Socket(server, Integer.parseInt(port)); // destination
		ObjectOutputStream sendAgent = new ObjectOutputStream(clientSocket.getOutputStream());
		sendAgent.writeObject(agent);
		System.out.println("Forward agent to: " + server + ":" + Integer.parseInt(port));
	}

	public static void cmd() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					String command = scanner.nextLine();
					switch (command) {
					case "help":
						System.out.println("'discover' to boardcast.");
						System.out.println("'list' to show server.");
						System.out.println("'forward' to forward agent.");
						System.out.println("'quit' to leave group.");
						break;
					case "forward":
						System.out.println("Input 'destinationIP destinationPort'");
						String info = scanner.nextLine();
						String[] strArr = info.split(" ");
						try {
							agentForward(strArr[0], strArr[1]);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						break;
					case "list":
						System.out.println(vector);
						break;
					case "discover":
						try {
							discover();
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					case "quit":
						try {
							leave();
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.exit(0);
						break;
					}
				}
			}
		}).start();
	}

}
