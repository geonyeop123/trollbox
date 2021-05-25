package chatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.net.Socket;

public class ClientManagerThread extends Thread{
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
        }catch(EOFException eof){
            String bye = userID + "님이 퇴장하셨습니다.";
            System.out.println(bye);
            sendMessage(bye);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            MainServer.outputList.remove(dataOutputStream);
            MainServer.userList.remove(userID);
            clientSocket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        for(int i = 0;i < MainServer.outputList.size();i++){
            try{
                MainServer.outputList.get(i).writeUTF(message);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void makeID(String message){
        if(MainServer.userList.contains(message)){
            try{
                dataOutputStream.writeUTF("0");
                    String reMessage = dataInputStream.readUTF();
                    makeID(reMessage);
            }catch(Exception e){}
        }else{
            userID = message;
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
