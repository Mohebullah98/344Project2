import java.io.*;
import java.net.*;
import java.util.*;
class Passengers implements Runnable{//passenger thread
  private int id;
  private String name;
public Passengers(int id){
  this.id=id;
  new Thread(this).start();
}
 public void setName(String n){
    name=n;
  }
  public String getName(){
    return name;
  }
  public static long time = System.currentTimeMillis();
 public void msg(String m) {
 System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
 }
simulation s = new simulation();
  public void run(){
    //passengers keep coming until plane is booked
     try
           { //each passenger establish connection with server
          InetAddress ip = InetAddress.getByName("localhost");
          Socket so1 = new Socket(ip,5000);
          DataInputStream dis1 = new DataInputStream(so1.getInputStream()); 
          DataOutputStream dos1 = new DataOutputStream(so1.getOutputStream());
           dos1.writeUTF("arrive");
           dos1.writeInt(id);
           s.arrive(); 
           s.waitForboarding();
           s.Boarding();
           s.exit();
           dos1.writeUTF("Finished");
           so1.close();
           
           } 
           catch(Exception e) 
           { 
            e.printStackTrace(); 
           } 
    }
}