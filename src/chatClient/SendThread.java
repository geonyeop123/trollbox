package chatClient;

import chatServer.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SendThread extends Thread{
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Scanner sc;

    @Override
    public void run() {
        String message;
        super.run();
        try{
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            sc = new Scanner(System.in);

            makeUserID();
            while(true){
                message = sc.nextLine();
                if(message.equals("/exit")){
                    break;
                }
                dataOutputStream.writeUTF(message);
            }

            sc.close();
            dataOutputStream.close();
            clientSocket.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void makeUserID(){
        System.out.println("아이디를 입력해주세요.");
        String userID = sc.nextLine();
        try {
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream.writeUTF(userID);
            if (dataInputStream.readUTF().equals("0")) {
                while (true) {
                    System.out.println("중복된 아이디입니다. 다시 아이디를 입력해주세요.");
                    userID = sc.nextLine();
                    dataOutputStream.writeUTF(userID);
                    if (!dataInputStream.readUTF().equals("0")) {
                        MainClient.userID = userID;
                        break;
                    }
                }
            } else {
                MainClient.userID = userID;
                System.out.println("채팅에 입장하셨습니다. 건전한 채팅 부탁드립니다.");
                ReceiveThread receiveThread = new ReceiveThread();
                receiveThread.setClientSocket(clientSocket);
                receiveThread.start();
            }
        }catch(Exception e){}
    }
    public void setClientSocket(Socket socket){
        clientSocket = socket;
    }
}
