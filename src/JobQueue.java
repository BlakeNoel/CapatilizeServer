
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.io.IOException;


public class JobQueue{
	private Monitor monitor;
	private int minSize, maxSize, total;
	private Queue<Job> Q;
	
	public JobQueue()
	{
		minSize = 0;
		maxSize = 50;
		Q = new LinkedList<>();
	}
	
	public synchronized void addToQ(Job j)
	{
		if (!monitor.isMonitorBusy())
		{
			return;
		}
		
		try
		{
			PrintWriter out = new PrintWriter(j.getSocket().getOutputStream(), true);

			if (getSize() >= maxSize)
			{
				out.println("The server is too busy right now, please try later!");
				return;
			}
			else
			{
				out.println("Hello, you are client #" + j.getClientNumber() + ". The server will help you shortly...");
			}
		}
		catch (IOException e)
		{
			System.out.println(e.toString());
		}
        
		while (this.getSize() >= maxSize)
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
		Q.add(j);
		callNotifyAll();
	}
	
	public synchronized Job removeFromQ()
	{
		if (!monitor.isMonitorBusy())
		{
			return null;
		}
		while (this.getSize() <= minSize)
		{
			try
			{
				this.wait();
			}
			catch (InterruptedException e)
			{
				System.out.println(e.toString());
			}
			if (this.isEmpty()/* && !this.readerBusy*/)
			{
				return null;
			}
		}
		Job j = Q.remove();
		callNotifyAll();
		return j;
	}
	
	public synchronized int getSize()
	{
		return this.Q.size();
	}
	
	public synchronized boolean isEmpty()
	{
		if (this.getSize() > 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
		
	public synchronized void setMonitor(Monitor monitor)
	{
		this.monitor = monitor;
	}
	
	public synchronized void setTotal(int i)
	{
		this.total = i;
	}
	
	public synchronized int getTotal()
	{
		return this.total;
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
	
	public synchronized void callNotifyAll()
	{
		this.notifyAll();
	}
}
