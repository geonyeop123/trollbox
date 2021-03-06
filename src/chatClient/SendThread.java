package chatClient;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SendThread extends Thread{
    private Socket clientSocket;
    private DataOutputStream dataOutputStream;
    private Scanner sc;

    @Override
    public void run() {
        String message;
        super.run();
        try{
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            sc = new Scanner(System.in);
            System.out.println("아이디를 입력해주세요.");
            String userID = sc.nextLine();
            makeUserID(userID); // 최초 접속 시 아이디 생성
            while(true){
                message = sc.nextLine();
                if(message.equals("/exit")){
                    break;
                }
                dataOutputStream.writeUTF(message);
            }

            sc.close(); // 접속 종료
            dataOutputStream.close();
            clientSocket.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void makeUserID(String userID){
        try {
            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream.writeUTF(userID);
            if (dataInputStream.readUTF().equals("0")) {
                    System.out.println("중복된 아이디입니다. 다시 아이디를 입력해주세요.");
                    String userID2 = sc.nextLine();
                    makeUserID(userID2);
            } else {
                MainClient.userID = userID; // 아이디 생성 완료
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
