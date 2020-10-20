import java.util.*;
import java.text.DecimalFormat;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
class simulation{
public static int counterNum=4;
public static double currentTime;

public static int seatCount=0;
 
public static DecimalFormat df = new DecimalFormat("0.00");

public static boolean planefilled = false;
public static int pass=0;
public static int seat =0;
public static int zone=1;
public static int numPassengers =30;
public static int totalSeats=30;
public static int count =0;
public static Clock cl = new Clock(0.0);
public static int waitingPassenger =0;
public static int seatedPassengers =0;
public static Object zone1 = new Object();
public static Object zone2 = new Object();
public static Object zone3 = new Object();
public static Object Seating = new Object();
public static Object Time = new Object();
public static Vector waitingp = new Vector(totalSeats);
public static Vector boardingp = new Vector(totalSeats);
public static Vector waitingForBoard = new Vector(totalSeats);
public static Vector exitingPassengers = new Vector(totalSeats);
public static Vector zone1p = new Vector(totalSeats);
public static Vector zone2p = new Vector(totalSeats);
public static Vector zone3p = new Vector(totalSeats); 
public static int availableSeats[] = new int[30];



public void setSeats(){ //initialize array for seats
  for(int i=0;i<availableSeats.length;i++){
    availableSeats[i]=i+1;
  }
}
    public static void shuffleArray(int[] a) { //randomize seats
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }  
    }

    public static void swap(int[] a, int i, int swapped) {
        int temp = a[i];
        a[i] = a[swapped];
        a[swapped] = temp;
    }




public static double randomdoublebetween(double from, double to){
   double d = (Math.random()*((to-from)+1))+from;
   return d;//calculate random number to simulate time passed
    }

public void arrive()throws InterruptedException{//passenger service method
  Object convey= new Object();
  synchronized(convey){
    if(cannotcheck(convey)){//passengers will wait if counter is full
      while(true){
        try{convey.wait();//wait for notify, not interrupt
        break;
        }
        catch(InterruptedException e){continue;}//race ignored
      }
    }
  
  counterNum--;//if counter not full passengers will recieve their seats
  if(pass+1==30) seat=0;//there are only 30 seats
  seat=availableSeats[pass];//select seat from randomized array
  pass+=1;
  zone=1;
  if(seat>10 && seat<=20) zone=2;
  if(seat>20&&seat<=30) zone=3;
  currentTime = cl.getTime();
  Passenger p = new Passenger(pass,seat,zone);
  cl.sleep(currentTime+randomdoublebetween(0.1, 2.5));//simulate time that each passenger takes to arrive
  currentTime=cl.getTime();
  
  System.out.println("Passenger "+ p.getID() +" has arrived at  " +df.format(currentTime)+" Minutes"+ " with seat:" +seat+" and zone:"+zone  );
  counterNum++;
  waitingForBoard.addElement(p);//passengers added to wait boarding vector
  if(pass==totalSeats){//if there are enough passengers to fill the plane we move on
    planefilled=true;
    System.out.println("Plane capacity reached");
  }
  if(numPassengers<totalSeats && pass==numPassengers){//if there are less passengers than seats, plane is booked when all passengers recieve a seat
    planefilled=true;
    System.out.println("All passengers have boarded");
  }
  }
}
public synchronized boolean cannotcheck(Object convey){
  boolean status;
  if(counterNum==0){//if counter is full passengers will wait
    waitingp.addElement(convey);
    status =true;
  }
  else{
    status=false;
  }
  return status;
}
public void assign()throws InterruptedException{
  synchronized(this){
    if(!waitingp.isEmpty()&&counterNum>0){//will notify passengers when space available on line
    waitingp.elementAt(0).notify();
    waitingp.removeElementAt(0);;
    }
    
  }
  
}
public synchronized void waitForboarding()throws InterruptedException{
  //passenger service method
  Passenger p;
  
  p = (Passenger) waitingForBoard.get(0);
  waitingForBoard.removeElementAt(0);
  boardingp.addElement(p);
  //Passengers will block based on their zone
  synchronized(zone1){ 
    if(p.getZone()==1){
    zone1p.addElement(p);//zone vectors keep track of passengers waiting in each zone
    while(true){
        try{zone1.wait();
        break;
        }
        catch(InterruptedException e){continue;}
      }
  }
  }
 
  synchronized(zone2){ 
    if(p.getZone()==2){
    zone2p.addElement(p);
    while(true){
        try{zone2.wait();
        break;
        }
        catch(InterruptedException e){continue;}
      }
  }
  }
  synchronized(zone3){ 
    if(p.getZone()==3){
    zone3p.addElement(p);
    while(true){
        try{zone3.wait();
        break;
        }
        catch(InterruptedException e){continue;}
      }
  }
  }
  
  }

public synchronized void notifyBoarding()throws InterruptedException{
  //Flight attendant service method
    synchronized(Time){
      Time.wait();
    }
     cl.sleep(currentTime+60.00);//simulate time till plane arrival
     currentTime=cl.getTime();
     System.out.println("The Plane is ready for boarding at : "+df.format(currentTime)+" Minutes");
     synchronized(zone1){//notify all passengers from each zone
       zone1.notifyAll();
     }
     synchronized(zone2){
       zone2.notifyAll();
     }
     synchronized(zone3){
       zone3.notifyAll();
  
     }
     
  synchronized(Seating){
    Seating.wait();
    System.out.println("The Plane door has closed and is ready for Takeoff");
  }
  
  }

public void Boarding()throws Exception{
    //passenger service method
  InetAddress ip = InetAddress.getByName("localhost");
  Socket so1 = new Socket(ip,5000);//passengers communicate with server before boarding
  DataInputStream dis1 = new DataInputStream(so1.getInputStream()); 
  DataOutputStream dos1 = new DataOutputStream(so1.getOutputStream());
  synchronized(zone1){//passengers boarding from each zone
    if(!zone1p.isEmpty()){
    Passenger p =(Passenger)zone1p.get(0);
    zone1p.removeElementAt(0);
    seatedPassengers+=1;
    System.out.println("Passenger "+p.getID()+" is boarding...");
    dos1.writeUTF("Boarding");
    dos1.writeInt(p.getID());
    Thread.sleep((int)randomdoublebetween(100.0,250.0));//simulate time to board for each passenger
    while(true){
        try{zone1.wait();//passengers will wait during flight
        break;
        }
        catch(InterruptedException e){continue;}
      }
    
  }
  }
    synchronized(zone2){
      if(!zone2p.isEmpty()){
    Passenger p =(Passenger)zone2p.get(0);
    zone2p.removeElementAt(0);
    seatedPassengers+=1;
    System.out.println("Passenger "+p.getID()+" is boarding...");
    dos1.writeUTF("Boarding");//write to server
    dos1.writeInt(p.getID());
    Thread.sleep((int)randomdoublebetween(100.0,250.0));
    while(true){
        try{zone2.wait();
        break;
        }
        catch(InterruptedException e){continue;}
      }
  }
  }
    synchronized(zone3){
      if(!zone3p.isEmpty()){
    Passenger p =(Passenger)zone3p.get(0);
    zone3p.removeElementAt(0);
    seatedPassengers+=1;
    System.out.println("Passenger "+p.getID()+" is boarding...");
    dos1.writeUTF("Boarding");
    dos1.writeInt(p.getID());
    Thread.sleep((int)randomdoublebetween(100.0,250.0));
    synchronized(Seating){
      if(seatedPassengers==30) Seating.notify();
      if(numPassengers<totalSeats && seatedPassengers ==numPassengers) Seating.notify();
    }
    while(true){
        try{zone3.wait();
        break;
        }
        catch(InterruptedException e){continue;}
      }
     
  }
}
}

public void exiting() throws Exception{
  //Flight attendant service method
    synchronized(Time){
      Time.wait();//wait to be notified by clock when to leave
    }
    InetAddress ip = InetAddress.getByName("localhost");
    Socket so1 = new Socket(ip,5000);
    DataInputStream dis1 = new DataInputStream(so1.getInputStream()); 
    DataOutputStream dos1 = new DataOutputStream(so1.getOutputStream());//contact server when exiting
    cl.sleep(currentTime+120.0); //advance time to simulate flight
    currentTime = cl.getTime();
    dos1.writeUTF("Plane landed");
    System.out.println("Plane has reached destination at "+ df.format(currentTime)+ " Minutes");
  while(boardingp.size()>0){ //passengers keep being notified while there are still passengers on board
  Passenger p =(Passenger) departuree(boardingp);
  //ensure passengers exit in ascending seat order.
    
    synchronized(zone1){
      if(p.getZone()==1){
      zone1.notify();//passengers notified from their zone
      dos1.writeUTF("Leaving");//communicate with server
      dos1.writeInt(p.getID());
    }
  }
    synchronized(zone2){
      if(p.getZone()==2){
      zone2.notify();
      dos1.writeUTF("Leaving");
      dos1.writeInt(p.getID());

    }
  }
  
    synchronized(zone3){
      if(p.getZone()==3){
      zone3.notify();
      dos1.writeUTF("Leaving");
      dos1.writeInt(p.getID());

    }
  }
  System.out.println("Passenger "+p.getID()+" from seat "+p.getSeat()+ " has exited.");
  Thread.sleep((int)randomdoublebetween(100.0,250.0));//simulate time it takes for passengers to leave
  }
  synchronized(Seating){//flight attendant cleans and waits to be notified by last passenger
    Seating.notify();
    Seating.wait();
   System.out.println("Flight attendant has exited.");
  }

}
public void exit()throws InterruptedException{//passenger service method
  synchronized(Seating){
  if(boardingp.isEmpty()){//last passenger notifies flight attendant
    Seating.wait();
    Seating.notify(); 
  }
  }
}
public void TimeForBoarding()throws InterruptedException{
  Thread.sleep(500);
  synchronized(Time){
  Time.notify();
  }//for clock to notify Flight attendant for boarding
}
public void TimeForExiting()throws InterruptedException{
  Thread.sleep(11000);
  synchronized(Time){
   Time.notify();
  }//for clock to notify Flight attendant for exiting
}

public Object departuree(Vector A){ //find passenger with first seat so they can exit in ascending order
  seatCount++; //next smallest
  for(int i=0;i<A.size();i++){
    Passenger a = (Passenger) A.get(i);
    if(a.getSeat()==seatCount) {//looking for smallest seat number
      Object x = A.get(i);
      A.removeElementAt(i);
      return x;
  }
  //there may be less passengers than seats so take that into account
  if(i==A.size()-1&& a.getSeat()!=seatCount){
    i=-1;//restart loop and check if next minimum seat is taken
    seatCount++;
  }
  
}
return A.get(0);
}

}