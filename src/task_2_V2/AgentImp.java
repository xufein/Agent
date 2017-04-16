package task_2_V2;

import java.io.Serializable;
import java.net.InetAddress;

public class AgentImp implements Agent, Serializable {

	int id;
	String name;
	String homeIP;
	int homePort;
	String task = "";
	Boolean status = false;
	
	public AgentImp(int id, String name, String homeIP, int homePort) {
		this.id = id;
		this.name = name;
		this.homeIP = homeIP;
		this.homePort = homePort;
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
	public String showIP() {
		return homeIP;
	}

	@Override
	public int showPort() {
		return homePort;
	}
	
	@Override
	public String showTask() {
		return task;
	}

	@Override
	public Boolean showStatus() {
		return status;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public void setTask(String task) {
		this.task = task;
	}
	
	@Override
	public void setStatus(Boolean status) {
		this.status = status;
	}

}
