import java.net.ServerSocket;
import java.time.LocalTime;

public class Monitor extends Thread{
	
	private final static int V = 100;
	private final static int T1 = 10;
	private final static int T2 = 20;
	
	private ThreadPool threadPool;
	private JobQueue jobQueue;
	private boolean monitorIsBusy;
	private ServerSocket listener;
	
	public Monitor(ThreadPool tP, JobQueue jQ)
	{
		this.threadPool = tP;
		this.jobQueue = jQ;
		this.monitorIsBusy = true;
		this.listener = null;
	}
	
	@Override
	public void run()
	{
		threadPool.startPool();

		while (monitorIsBusy)
		{
			try
			{
				int jobQueueCheck = jobQueue.getSize();
				
				if (jobQueueCheck <= T1)
				{
					while (threadPool.maxCapacity() > 5)
					{
						threadPool.decreaseThreadsInPool();
						System.out.println("JobQueue size made ThreadPool size smaller on " + LocalTime.now() + " - Total ThreadPool size is: " + threadPool.numberThreadsRunning());
					}
				}
				
				else if (T1 < jobQueueCheck && jobQueueCheck <= T2)
				{
					while (threadPool.maxCapacity() < 10)
					{
						threadPool.increaseThreadsInPool();
						System.out.println("JobQueue size made ThreadPool size larger on " + LocalTime.now() + " - Total ThreadPool size is: " + threadPool.numberThreadsRunning());
					}
					while (threadPool.maxCapacity() > 10)
					{
						threadPool.decreaseThreadsInPool();
						System.out.println("JobQueue size made ThreadPool size smaller on " + LocalTime.now() + " - Total ThreadPool size is: " + threadPool.numberThreadsRunning());
					}
				}
				
				else if (T2 < jobQueueCheck)
				{
					while (threadPool.maxCapacity() < 20)
					{
						threadPool.increaseThreadsInPool();
						System.out.println("JobQueue size made ThreadPool size larger on " + LocalTime.now() + " - Total ThreadPool size is: " + threadPool.numberThreadsRunning());
					}
				}
				
				if (!this.jobQueue.isEmpty())
				{
					jobQueue.callNotifyAll();
				}
				
				Thread.sleep(V);
			}
			catch (InterruptedException e)
			{
				System.out.println(e.toString());
			}
		}
		
		threadPool.stopPool();
	}
	
	public void setMonitorIsBusy(boolean b)
	{
		this.monitorIsBusy = b;
	}
	
	public boolean isMonitorBusy()
	{
		return this.monitorIsBusy;
	}
	
	public JobQueue getJobQueue()
	{
		return jobQueue;
	}
	
	public ThreadPool getThreadPool()
	{
		return threadPool;
	}
	
	public void setListener(ServerSocket listener)
	{
		this.listener = listener;
	}
	
	public ServerSocket getListener()
	{
		return listener;
	}
}