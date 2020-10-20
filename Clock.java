class Clock implements Runnable{//clock thread
  private double time;

  public Clock(double t){
    time =t;
    new Thread(this).start();
  }
  public double getTime(){
    return time;
  }
  public void sleep(double _time){
    time =_time;
  }
  simulation s = new simulation();
  public void run(){
     try
           { 
          s.TimeForBoarding(); //notify when to board and exit
          s.TimeForExiting();
           } 
           catch(InterruptedException e) 
           { 
            e.printStackTrace(); 
           } 
      
  }
}