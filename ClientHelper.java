import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class ClientHelper implements Runnable{
  
  private Socket sock;
  final DataOutputStream dos;
  final DataInputStream dis;
  private ServerSocket serversock;
  
  public ClientHelper(Socket s, DataInputStream di, DataOutputStream doos){
    sock =s;
    dos = doos;
    dis =di;
   new Thread(this).start();
  }
  simulation s = new simulation();
  public void communication()throws Exception{
    while(true){//listens for messages from client and for each particular method, it has a response.
    String input = dis.readUTF();
    if (input.equals("start")){
    System.out.println("Just connected to "+sock.getRemoteSocketAddress());
    }
    if (input.equals("arrive")){
      int id = dis.readInt();
    System.out.println("Passenger "+(id+1)+" connected to server for arrival ");
    }
    if (input.equals("assign")){
       System.out.println("Checking counter has connected to server for assignment ");
    }
    if (input.equals("notifyboard")){
       System.out.println("Flight Attendant connected to server for notify boarding");
    }
    if (input.equals("exiting")){
       System.out.println("Flight Attendant contacted server for exiting");
    }
    if(input.equals("Boarding")){
      int num= dis.readInt();
      System.out.println("Passenger "+(num)+ " contacted server for boarding");
    }
    if(input.equals("Plane landed")){
      System.out.println("----Flight Attendant alerted Server that Plane has Landed---");
    }
    if(input.equals("Leaving")){
      int num= dis.readInt();
      System.out.println("Passenger "+(num)+ " contacted server for Exiting");
    }
    if(input.equals("Finished")){
      sock.close();
      break;
    }
    
    }
    
  }
  public void run(){
    try{
      communication();
    }
    catch(Exception e){

    }
  }
}