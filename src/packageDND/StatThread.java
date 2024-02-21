package packageDND;

import java.math.BigInteger;
import java.util.ArrayList;

public class StatThread implements Runnable {

	private Thread thread ;
	private volatile boolean running = false ;
	
	private int Size ;
	private int Layers ;
	
	private volatile ArrayList<ArrayList<BigInteger>> Stats = new ArrayList<ArrayList<BigInteger>>() ; 
	
	public StatThread( int size , int layers ) {
		Size = size ;
		Layers = layers ;
		this.start() ;
	}

	public void run() {
		Stats = new StatGenerator( Size , Layers ).getStats() ;
		running = false ;
	}
	
	public ArrayList<ArrayList<BigInteger>> getStats(){
		return Stats ;
	}
	public boolean isRunning(){
		return running ;
	}
	
	public void delete() {
		this.stop() ;
	}
	
	private synchronized void start(){
		if(running) { return; }
		running = true ;
		thread = new Thread(this) ;
		thread.start() ;
	}
	
	private synchronized void stop(){
		running = false ;
		try{ thread.join() ; } catch(InterruptedException e){ e.printStackTrace() ; }
	}

}
