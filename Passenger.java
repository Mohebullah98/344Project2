class Passenger {//passenger class with seat,id and zone
  private int seat;
  private int zoneNumber;
  private int id;
  
  public Passenger(int i,int s,int z){
    id=i;
    seat=s;
    zoneNumber=z;
    
  }
  public void ticket(int s, int z){
    seat =s;
    zoneNumber =z;
  }
  public int getSeat(){
    return seat;
  }
  public int getZone(){
    return zoneNumber;
  }
  public int getID(){
    return id;
  }
 
  }
