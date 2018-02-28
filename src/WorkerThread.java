import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class WorkerThread extends Thread{
	
	private Job currentJob;
	private Monitor myMonitor;
	private boolean workerThreadIsBusy;

	public WorkerThread(Monitor myMonitor)
	{
		this.currentJob = null;
		this.myMonitor = myMonitor;
		this.workerThreadIsBusy = true;
	}
	
	@Override
	public void run()
	{
			while (workerThreadIsBusy)
			{
				while (currentJob == null && workerThreadIsBusy)
				{
					myMonitor.getJobQueue().callWait();
					currentJob = myMonitor.getJobQueue().removeFromQ();
				}
				if (!workerThreadIsBusy)
				{
					break;
				}
				try
				{
		            BufferedReader in = new BufferedReader(new InputStreamReader(currentJob.getSocket().getInputStream()));
		            PrintWriter out = new PrintWriter(currentJob.getSocket().getOutputStream(), true);
		
		            out.println("Enter a valid command such as: ADD,x,y SUB,x,y MUL,x,y DIV,x,y or KILL");
		            out.println("Note: x and y are doubles.\n");
		
		            boolean alive = true;
		            while (alive) {
		                String input = in.readLine();
		                String result = "";
		                
		                if (input == null) {
		                    break;
		                }
		                
		                String temp[] = input.split(",");
		                
		                if (temp[0].equals("KILL"))
		                {
		                	myMonitor.setMonitorIsBusy(false);
		                	this.workerThreadIsBusy = false;
		                	alive = false;
		                	myMonitor.getListener().close();
		                }
		                else if (temp[0].equals("ADD"))
		                {
		                	try
		                	{
		                		result = String.valueOf((Double.parseDouble(temp[1]) + Double.parseDouble(temp[2])));
		                	}
		                	catch (Exception e)
		                	{
		                		result = "Please enter a valid command.";
		                	}
		                }
		                else if (temp[0].equals("SUB"))
		                {
		                	try
		                	{
		                		result = String.valueOf((Double.parseDouble(temp[1]) - Double.parseDouble(temp[2])));
		                	}
		                	catch (Exception e)
		                	{
		                		result = "Please enter a valid command.";
		                	}
		                }
		                else if (temp[0].equals("MUL"))
		                {
		                	try
		                	{
		                		result = String.valueOf((Double.parseDouble(temp[1]) * Double.parseDouble(temp[2])));
		                	}
		                	catch (Exception e)
		                	{
		                		result = "Please enter a valid command.";
		                	}
		                }
		                else if (temp[0].equals("DIV"))
		                {
		                	try
		                	{
		                		result = String.valueOf((Double.parseDouble(temp[1]) / Double.parseDouble(temp[2])));
		                	}
		                	catch (Exception e)
		                	{
		                		result = "Please enter a valid command.";
		                	}
		                }
		                else
		                {
		                	result = "Please enter a valid command.";
		                }
		                out.println(result);
		                System.out.println("Client #" + currentJob.getClientNumber() + " has been serviced.");
		            }
				}
				catch (IOException e) 
				{
		            log("Thread available for new job due to error handling client# " + currentJob.getClientNumber() + ": " + e);
				}
	            
	    		currentJob = null;
			}
    }
	
	public void setJob(Job job)
	{
		this.currentJob = job;
	}
	
	public synchronized void gracefulDeath()
	{
		this.workerThreadIsBusy = false;
	}
	
	private void log(String message) 
	{
        System.out.println(message);
    }
}
