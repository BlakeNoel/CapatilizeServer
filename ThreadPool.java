
import java.util.*;

public class ThreadPool {

	private int maxCapacity;
	private int actualNumberThreads;
	private ArrayList<WorkerThread> holders;
	private Monitor myMonitor;
	
	public ThreadPool()
	{
		this.maxCapacity = 5;
		this.actualNumberThreads = 0;
		this.holders = new ArrayList<WorkerThread>();
	}
	
	public synchronized void startPool()
	{
		for (int i = 0; i < maxCapacity; i++)
		{
			holders.add(new WorkerThread(myMonitor));
			holders.get(i).start();
			actualNumberThreads++;
		}
	}
	
	public synchronized void increaseThreadsInPool()
	{
		if(maxCapacity < 40)
		{
			maxCapacity = maxCapacity*2;
			
			while(holders.size() < maxCapacity)
			{
				WorkerThread newWorker = new WorkerThread(myMonitor);
				holders.add(newWorker);
				newWorker.start();
				actualNumberThreads++;
			}
		}
	}
	
	public synchronized void decreaseThreadsInPool()
	{
		if(maxCapacity > 5)
		{
			maxCapacity = maxCapacity/2;
			
			while(holders.size() > maxCapacity)
			{
				holders.get(actualNumberThreads - 1).gracefulDeath();
				
				//might need more code here for waiting for thread to die gracefully.
				
				holders.remove(actualNumberThreads - 1);
				actualNumberThreads--;
			}
		}
		
	}
	
	public synchronized void stopPool()
	{
		for (int i = 0; i < maxCapacity; i++)
		{
			holders.get(i).gracefulDeath();
			actualNumberThreads--;
		}
		
		myMonitor.getJobQueue().callNotifyAll();
		
		for (int i = 0; i < maxCapacity; i++)
		{
			try
			{
				holders.get(i).join();
			}
			catch (InterruptedException e)
			{
				System.out.println("Couldn't join worker threads during exit.");
			}
		}
	}
	
	public synchronized int numberThreadsRunning()
	{
		return this.actualNumberThreads;
	}
	
	public synchronized int maxCapacity()
	{
		return this.maxCapacity;
	}
	
	public void setMonitor(Monitor monitor)
	{
		this.myMonitor = monitor;
	}
	
	public synchronized void callWait()
	{
		try
		{
			this.wait();
		}
		catch (InterruptedException e)
		{
			System.out.println(e.toString());
		}
	}
}
