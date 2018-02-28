
import java.net.ServerSocket;
import java.net.SocketException;

public class CapitalizeServer {

    public static void main(String[] args) throws Exception {

    	System.out.println("The server is waking up.");

    	ThreadPool threadPool = new ThreadPool();
    	JobQueue jobQueue = new JobQueue();
    	Monitor monitor = new Monitor(threadPool, jobQueue);
    	threadPool.setMonitor(monitor);
    	jobQueue.setMonitor(monitor);
    	
    	int clientNumber = 0;
    	ServerSocket listener = new ServerSocket(9898);
    	
    	monitor.setListener(listener);
    	monitor.start();
    	
    	System.out.println("The server is running.");
    	
    	try 
    	{
            while (monitor.isMonitorBusy()) 
            {
                jobQueue.addToQ(new Job(listener.accept(), clientNumber++));
            }
        }
    	catch (SocketException e) 
    	{
    		monitor.join();
            System.out.println("The server has died gracefully.");
        }
    }
}
