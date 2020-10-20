import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Server{
  private int id;
  
  public Server(int i){
    id =i;

  }
  
  public static void main (String []args)throws Exception{
    ServerSocket ss =new ServerSocket(5000);
    while(true){//server will wait for requests indefinitely
    Socket s = null;
    try{
      s = ss.accept();
      DataInputStream dis = new DataInputStream(s.getInputStream()); 
      DataOutputStream dos = new DataOutputStream(s.getOutputStream());//maintain communication with client thread
      new ClientHelper(s,dis,dos);//new client helper thread created for each connection with server
    }
    catch(Exception e){
      s.close();
    }
    }
  }
 
}