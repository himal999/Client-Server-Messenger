package sample;/*
author :Himal
version : 0.0.1
*/

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferWriter;

    public Server(ServerSocket serverSocket){
       try{ this.serverSocket = serverSocket;
        this.socket = serverSocket.accept();
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }catch (IOException e){
            System.out.println("Server Catching Error");
            e.printStackTrace();
           closeEverything(socket,bufferedReader,bufferWriter);
       }
    }

    public void sendMessageToClient(String messageToClient){
            try{
                bufferWriter.write(messageToClient);
                bufferWriter.newLine();
                bufferWriter.flush();
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("Error send message to Client");
                closeEverything(socket,bufferedReader,bufferWriter);
            }
    }

    public void receiveMessageFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                  try{ String messageFromClient = bufferedReader.readLine();
                   Controller.addLabel(messageFromClient,vBox);
                  }catch (IOException e){
                      e.printStackTrace();
                      System.out.println("Error Received Message");
                      closeEverything(socket,bufferedReader,bufferWriter);
                      break;
                  }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }

            if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
