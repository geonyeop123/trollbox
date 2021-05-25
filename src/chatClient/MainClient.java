package chatClient;

import java.net.Socket;

public class MainClient {
    public static String userID = null;

    public static void main(String[] args){
        try{
            Socket clientSocket = new Socket("127.0.0.1", 8090);
            System.out.println("클라이언트 소켓 연결");

            SendThread sendThread = new SendThread();
            sendThread.setClientSocket(clientSocket);
            sendThread.start();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
