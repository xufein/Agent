package task_2_V2;

public interface Agent {
	
    public String showName();
    
    public int showId();

    public String showIP();

    public int showPort();
    
    public void setName(String name);
    
    public void setId(int id);

    public void setTask(String task);

    public void setStatus(Boolean status);

    public Boolean showStatus();

    public String showTask();
    
}
