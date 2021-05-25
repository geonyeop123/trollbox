package chatClient;

import java.io.DataInputStream;
import java.net.Socket;

public class ReceiveThread extends Thread{
    private Socket clientSocket;
    @Override
    public void run() {
        super.run();
        String message;
        try{
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            while(true){
                message = dataInputStream.readUTF();
                System.out.println(message);
            }
        }catch(Exception e){
            System.out.println("종료되었습니다.");
        }
    }

    public void setClientSocket(Socket socket){
        clientSocket = socket;
    }
}
