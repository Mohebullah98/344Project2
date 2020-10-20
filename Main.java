import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.*;
import java.util.*;
class Main {
  
  public static void main(String[] args) throws Exception {
    simulation s = new simulation();
   // System.out.println("Please Enter The Amount of Passengers:");
   // Scanner Scan = new Scanner(System.in);
   // s.numPassengers = Scan.nextInt();//number passengers enter via command line
   InetAddress ip = InetAddress.getByName("localhost");
   Socket so = new Socket(ip,5000);
   DataInputStream dis = new DataInputStream(so.getInputStream()); 
   DataOutputStream dos = new DataOutputStream(so.getOutputStream());
   dos.writeUTF("start");
   
    s.setSeats();
    s.shuffleArray(s.availableSeats);//randomize seats
    
    
    for(int i=0;i<s.numPassengers;i++){
      if(s.planefilled) break;//once plane is booked, no more passengers allowed
      new Passengers(i);//passenger threads
      Thread.sleep((int)s.randomdoublebetween(150.0,200.0));
    }
    
    new checkingCounter(0);//counter thread
    new checkingCounter(1);//counter thread
    new FlightAttendant(1);//FlightAttendant
    new Clock(0.0);//clock thread
    dos.close();
    dis.close();
  }
 
}