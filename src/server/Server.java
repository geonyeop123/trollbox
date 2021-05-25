package server;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public void socketSetting(){
        try{
            serverSocket = new ServerSocket(8080);
            System.out.println("서버소켓 연결");
            clientSocket = serverSocket.accept();
            System.out.println("클라이언트 연결");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void streamSetting(){
        try{
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        }catch(Exception e){}
    }
    public void receiveMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        String message = dataInputStream.readUTF();
                        System.out.println("상대방 : " + message);

                    }catch(EOFException e){
                        e.printStackTrace();
                        break;
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                allClose();
            }
        }).start();
    }
    public void sendMessage(){
        new Thread(new Runnable() {
            Scanner sc = new Scanner(System.in);
            @Override
            public void run() {
                while(true){
                    String message = sc.nextLine();
                    try{
                        System.out.println("나 :" + message);
                        dataOutputStream.writeUTF(message);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void allClose(){
        try{
            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
            serverSocket.close();
        }catch(Exception e){}
    }
    public Server(){
        socketSetting();
        streamSetting();
        sendMessage();
        receiveMessage();
    }
    public static void main(String[] args){
        new Server();
    }
}
