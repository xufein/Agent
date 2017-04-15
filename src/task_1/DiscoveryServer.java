package task_1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DiscoveryServer extends Thread {

	public static String group = "239.0.0.1";
	public static int basePort = 1234;
	public static int serverPort = 4444;
	public static MulticastSocket receiveSocket;
	// public static MulticastSocket replySocket;

	public DiscoveryServer(InetAddress mcastAddr, int basePort, int serverPort) throws IOException {
		receiveSocket = new MulticastSocket(basePort);
		// replySocket = new MulticastSocket(serverPort);
		receiveSocket.joinGroup(mcastAddr);
		// replySocket.joinGroup(mcastAddr);
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

			// reply
			// byte[] replyData = new byte[1024];
			// String data = "Reply from server";
			// replyData = data.getBytes();
			// DatagramPacket replyPacket = new DatagramPacket(replyData,
			// replyData.length, InetAddress.getByName("192.168.56.1"), 1234);
			//// while(true)
			// replySocket.send(replyPacket);

			 DatagramSocket serverSocket = new DatagramSocket(serverPort);
			 byte[] sendData = new byte[1024];
			 String data = "Reply from server";
			 sendData = data.getBytes();
			 DatagramPacket sendPacket = new DatagramPacket(sendData,
			 sendData.length,
			 InetAddress.getByName("192.168.56.1"), basePort);
			 // while(true)
			 serverSocket.send(sendPacket);

//			reply();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void reply() throws IOException {
		DatagramSocket serverSocket = new DatagramSocket(serverPort);
		byte[] sendData = new byte[1024];
		String data = "Hello";
		sendData = data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("192.168.56.1"),
				basePort);
		// while(true)
		serverSocket.send(sendPacket);
	}

	public static void main(String[] args) {
		try {
			new DiscoveryServer(InetAddress.getByName(group), basePort, serverPort);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
