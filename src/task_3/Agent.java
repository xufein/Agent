package task_3;

import java.util.List;

public interface Agent {

	public int showId();

	public String showName();

	public String showIP();

	public int showPort();

	public String showTask();

	public List showVisited();

	public void setTask(String task);

	public void setVisited(String visited);

}
