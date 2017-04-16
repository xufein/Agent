package task_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class DiscoveryServer extends Thread {

	public static String group = "239.0.0.1";
	public static int basePort = 1234;
	public static int serverPort = 4444; // change this while multiple server
	public static MulticastSocket receiveSocket;
	public static MulticastSocket replySocket;

	public DiscoveryServer(InetAddress mcastAddr, int basePort, int serverPort) throws IOException {
		receiveSocket = new MulticastSocket(basePort);
		replySocket = new MulticastSocket(serverPort);
		receiveSocket.joinGroup(mcastAddr);
		this.start();
	}

	public void run() {
		try {
			// receive
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			receiveSocket.receive(receivePacket);
			InetAddress clientIP = receivePacket.getAddress(); // client IP
			System.out.println("Received data from: " + clientIP + ":" + receivePacket.getPort() + " with length: "
					+ receivePacket.getLength());
			System.out.write(receivePacket.getData(), 0, receivePacket.getLength());
			System.out.println();
			receiveSocket.close();
			// reply
			byte[] replyData = new byte[1024];
			String data = "Reply from server";
			replyData = data.getBytes();
			DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length, clientIP, basePort);
			replySocket.send(replyPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			new DiscoveryServer(InetAddress.getByName(group), basePort, serverPort);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
