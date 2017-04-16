package task_2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		Agent agent = new AgentImp(123, "Alice");
		Socket clientSocket = new Socket("localhost", 4444);
		// send
		ObjectOutputStream sendData = new ObjectOutputStream(clientSocket.getOutputStream());
		sendData.writeObject(agent);
		// receive
		ObjectInputStream receiveData = new ObjectInputStream(clientSocket.getInputStream());
		Agent new_student = (Agent) receiveData.readObject();
		System.out.println("New Id: " + new_student.showId());
		System.out.println("New Name: " + new_student.showName());
		// clean
		receiveData.close();
		sendData.close();
		clientSocket.close();
	}

}
