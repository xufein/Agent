package task_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Vector;

public class DiscoveryClient extends Thread {

	public static String group = "255.255.255.255";
	public static int basePort = 1234;
	public static MulticastSocket clientSocket;
	public static Vector vector = new Vector();
	public static InetSocketAddress address = new InetSocketAddress("192.168.1.86", 1234);

	public DiscoveryClient(InetAddress mcastAddr, int basePort) throws IOException {
//		clientSocket = new MulticastSocket(basePort);
		clientSocket = new MulticastSocket(address);
		discover(clientSocket, basePort);
		this.start(); // start listener
	}

	public void discover(MulticastSocket mcastSocket, int basePort) throws IOException {
		// send request
		byte[] sendData = new byte[1024];
		String data = "Request from client";
		sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(group),
				basePort);
		clientSocket.send(sendPacket);
	}

	public void run() {
		while (true) {
			try {
				byte[] receiveData = new byte[1024];
				DatagramPacket responsePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(responsePacket);
				String serverReply = new String(responsePacket.getData());
				System.out.println("Server response: " + serverReply);
				System.out.println(responsePacket.getAddress() + ":" + responsePacket.getPort());
				// add to Vector
				String serverAddress = responsePacket.getAddress().toString();
				String serverIP = Integer.toString(responsePacket.getPort());
				vector.addElement(new String(serverAddress + ":" + serverIP));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Vector getDiscoveryResult() {
		return vector;
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		new DiscoveryClient(InetAddress.getByName(group), basePort);
	}

}