import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ComputerSyncServer {
    public static void main(String args[]) throws Exception{
        final int PORT = 12582;
        ServerSocket welcomeSocket = null;

        try{
            welcomeSocket = new ServerSocket(PORT);
            System.out.println("server starting...");
        }
        catch(IOException e){
            e.printStackTrace();
        }

        while(true){
            Socket connectionSocket = welcomeSocket.accept();
            System.out.println("connection accepted!");

            //create a thread with the connection socket to handle the request of the client
            RequestProcessor processor = new RequestProcessor(connectionSocket);
            Thread thread = new Thread(processor);
            thread.start();
        }
    }
}

final class RequestProcessor implements Runnable{
    private Socket requestSocket;

    RequestProcessor(Socket s){
        requestSocket = s;
    }
    public void run(){
    	ArrayList<String> broadcastFileNames = readConfig();
    	ArrayList<File> broadcastFiles = createBroadcastFiles(broadcastFileNames);
    }

    private ArrayList<String> readConfig(){
        //create array list of file names/directories read in from config file
        ArrayList<String> broadcastList = new ArrayList<String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader("config.txt"));

            String configLine = reader.readLine();
            while(configLine != null){
                broadcastList.add(configLine);
                configLine = reader.readLine();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return broadcastList;
    }
    
    private ArrayList<File> createBroadcastFiles(ArrayList<String> fNames){
    	ArrayList<File> broadcastFiles = new ArrayList<File>();

    	for(int i = 0; i < fNames.size(); i++){
    		File curFile = new File(fNames.get(i));
    		broadcastFiles.add(curFile);
    	}

    	return broadcastFiles;
    }
}