
import java.net.Socket;

public class Job {

	private Socket socket;
    private int clientNumber;

    public Job(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }
    
    public Socket getSocket()
    {
    	return this.socket;
    }
    
    public int getClientNumber()
    {
    	return this.clientNumber;
    }
}