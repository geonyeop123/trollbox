package chatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;

public class ClientManagerThread extends Thread{ // 다중 클라이언트를 관리하는 클래스
    private Socket clientSocket;
    private String userID;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    @Override
    public void run() {
        super.run();
        try{
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            String message;
            while(true){
                message = dataInputStream.readUTF();
                if(userID == null){
                    makeID(message);
                }else{
                    sendMessage(userID + " : " + message);
                }
            }
        }catch(EOFException eof){ // 유저 퇴장 시 처리
            String bye = userID + "님이 퇴장하셨습니다.";
            System.out.println(bye);
            sendMessage(bye);

            MainServer.outputList.remove(dataOutputStream);
            MainServer.userList.remove(userID);
            try{
                clientSocket.close();
            }catch(Exception e){}
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){ // 클라이언트에게 유저의 메시지 전송
        for(int i = 0;i < MainServer.outputList.size();i++){
            try{
                MainServer.outputList.get(i).writeUTF(message);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void makeID(String message){ // 아이디 생성
        if(MainServer.userList.contains(message)){
            try{
                dataOutputStream.writeUTF("0");
                    String reMessage = dataInputStream.readUTF();
                    makeID(reMessage);
            }catch(Exception e){}
        }else{
            userID = message; // 아이디 생성 완료
            String welcome = userID + "님이 접속하셨습니다.";
            MainServer.outputList.add(dataOutputStream);
            MainServer.userList.add(userID);
            System.out.println(welcome);
            sendMessage(welcome);
        }
    }
    public void setClientSocket(Socket socket){
        clientSocket = socket;
    }

}
