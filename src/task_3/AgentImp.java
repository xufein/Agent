package task_3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgentImp implements Agent, Serializable {

	int id;
	String name;
	String homeIP;
	int homePort;
	String task = "";
	List<String> visited = new ArrayList<String>();

	public AgentImp(int id, String name, String homeIP, int homePort) {
		this.id = id;
		this.name = name;
		this.homeIP = homeIP;
		this.homePort = homePort;
	}

	@Override
	public int showId() {
		return id;
	}

	@Override
	public String showName() {
		return name;
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
	public List<String> showVisited() {
		return visited;
	}

	@Override
	public void setTask(String task) {
		this.task = task;
	}

	@Override
	public void setVisited(String visited) {
		this.visited.add(visited);
	}

}
