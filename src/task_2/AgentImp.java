package task_2;

import java.io.Serializable;
import java.net.InetAddress;

public class AgentImp implements Agent, Serializable {

	int id;
	String name;
	
	public AgentImp(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String showName() {
		return name;
	}

	@Override
	public int showId() {
		return id;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

}
