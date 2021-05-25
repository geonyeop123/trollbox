package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket clientSocket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    public boolean isThread = true;

    public void socketSetting(){
        try{
            clientSocket = new Socket("127.0.0.1",8080);
            System.out.println("클라이언트 연결");
        }catch(Exception e){}
    }

    public void streamSetting(){
        try{
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        }catch(Exception e){
        }
    }
    public void receiveMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isThread){
                    try{
                        String message = dataInputStream.readUTF();
                        System.out.println("상대방 : " + message);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void sendMessage(){
        new Thread(new Runnable() {
            Scanner sc = new Scanner(System.in);
            @Override
            public void run() {
                while(isThread){
                    String message = sc.nextLine();
                    if(message.equals("/exit")){
                        isThread = false;
                        break;
                    }else{
                        try{
                            System.out.println("나 :" + message);
                            dataOutputStream.writeUTF(message);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                allClose();
            }
        }).start();

    }
    public void allClose(){
        try{
            dataInputStream.close();
            dataOutputStream.close();
            clientSocket.close();
        }catch(Exception e){
        }
    }
    public Client(){
        socketSetting();
        streamSetting();
        receiveMessage();
        sendMessage();
    }
    public static void main(String[] args){
        new Client();
    }
}
