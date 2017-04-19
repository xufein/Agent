package task_3;

import java.io.IOException;
import java.io.ObjectInputStream;
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

	public static Vector<String> vector = new Vector();
	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// cmd();
		listener();
		discover();
		agentArrive();
	}

	public static void discover() throws IOException {
		InetSocketAddress address = new InetSocketAddress(localIP, basePort);
		MulticastSocket discoverSocket = new MulticastSocket(address);
		byte[] discoverData = { 'R', 'E', 'Q', 'U', 'E', 'S', 'T' };
		DatagramPacket discoverPacket = new DatagramPacket(discoverData, discoverData.length,
				InetAddress.getByName("255.255.255.255"), basePort);
		discoverSocket.send(discoverPacket);
	}

	public static void listener() throws IOException {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						// receive
						MulticastSocket listenerSocket = new MulticastSocket(basePort);
						listenerSocket.joinGroup(InetAddress.getByName(group));
						byte[] listenerData = new byte[1024];
						DatagramPacket listenerPacket = new DatagramPacket(listenerData, listenerData.length);
						listenerSocket.receive(listenerPacket);
						System.out.println(
								"Receive request from: " + listenerPacket.getAddress() + ":" + listenerPacket.getPort());
						// TODO: filter duplicated address
						vector.addElement(new String(listenerPacket.getAddress() + ":" + listenerPacket.getPort()));
						// reply
						MulticastSocket replySocket = new MulticastSocket(serverPort);
						byte[] replyData = { 'R', 'E', 'P', 'L', 'Y' };
						DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length,
								listenerPacket.getAddress(), basePort);
						replySocket.send(replyPacket);
						//
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static void agentArrive() throws IOException, ClassNotFoundException {
		ServerSocket serversocket = new ServerSocket(serverPort);
		Socket agentSocket = serversocket.accept();
		ObjectInputStream inputStream = new ObjectInputStream(agentSocket.getInputStream());
		Agent agent = (Agent) inputStream.readObject();
		System.out.print("Receive Agent ");
		System.out.print("Id: " + agent.showId() + ", Name: " + agent.showName());
		System.out.println(", From: " + agent.showIP() + ":" + agent.showPort());
		System.out.println("Task: " + agent.showTask());
	}

	public static void cmd() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					String command = scanner.nextLine();
					switch (command) {
					case "list":
						System.out.println("'discover' to boardcast.");
						System.out.println("'vector' to show server.");
						break;
					case "vector":
						System.out.println(vector);
						break;
					case "discover":
						try {
							discover();
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
