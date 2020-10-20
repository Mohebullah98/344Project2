import java.io.*;
import java.net.*;
import java.util.*;
class FlightAttendant implements Runnable{//Flight attendant thread
  private String name;
  private int id;

  public FlightAttendant(int i){
    id=i;
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
   public void run() {
        try{
          InetAddress ip = InetAddress.getByName("localhost");
          Socket so1 = new Socket(ip,5000);//each thread establishes connection with server
          DataInputStream dis1 = new DataInputStream(so1.getInputStream()); 
          DataOutputStream dos1 = new DataOutputStream(so1.getOutputStream());
          dos1.writeUTF("notifyboard");
          s.notifyBoarding();
          s.exiting();
          dos1.writeUTF("exiting");
          dos1.writeUTF("Finished");
          so1.close();
        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }
}