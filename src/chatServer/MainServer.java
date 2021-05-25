package chatServer;


import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainServer {
    public static ArrayList<DataOutputStream> outputList;
    public static ArrayList<String> userList;
    public static void main(String[] args){
        outputList = new ArrayList<DataOutputStream>();
        userList = new ArrayList<String>();
        try{
            ServerSocket serverSocket = new ServerSocket(8090);
            System.out.println("서버 소켓 연결");
            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("클라이언트 소켓 연결");
                System.out.println(userList);
                ClientManagerThread clientManagerThread = new ClientManagerThread();
                clientManagerThread.setClientSocket(clientSocket);
                clientManagerThread.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
