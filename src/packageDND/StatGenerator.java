package packageDND;

import java.math.*;
import java.util.* ;

public class StatGenerator {
	
	private ArrayList<ArrayList<BigInteger>> Return = new ArrayList<ArrayList<BigInteger>>() ;
	
	public StatGenerator( int size , int layers , int threads ) {
		Return = GenerateStatThreads( size , layers , threads) ;
	}
	
	public StatGenerator( int size , int layers ) {
		Return = GenerateStatsLayered( size , layers ) ;
	}
	
	public StatGenerator( int size ) {
		Return = GenerateStat( size ) ;
	}
	
	public StatGenerator() {
		Return = null ;
	}
	
	public ArrayList<ArrayList<BigInteger>> getStats() {
		return Return ;
	}
	
	private ArrayList<ArrayList<BigInteger>> EmptyStats(){
		ArrayList<ArrayList<BigInteger>> EmptyStats = new ArrayList<ArrayList<BigInteger>>() ;
		for( int in = 0 ; in < 3 ; in++ ) {
			ArrayList<BigInteger> Temp = new ArrayList<BigInteger>() ;
			for( int i = 0 ; i < 16 ; i++ ) {
				Temp.add( BigInteger.ZERO ) ;
			}
			EmptyStats.add(Temp) ;
		}
		return EmptyStats ;
	}
	
	
	private ArrayList<ArrayList<BigInteger>> GenerateStatThreads( int size , int layers , int threads ) {
		
		ArrayList<StatThread> Threads = new ArrayList<StatThread>() ;		
		for( int i = 0; i < threads; i++ ) {
			Threads.add( new StatThread( size , layers ) ) ;
		}
		
		boolean ThreadsRunning ;
		
		 do {	
			ThreadsRunning = false ;
			for( int i = 0; i < Threads.size(); i++ ) {
				if( Threads.get(i).isRunning() ) {
					ThreadsRunning = true ;
				}
			}
			try { Thread.sleep(1000) ; } catch ( InterruptedException e ) { e.printStackTrace() ; }
		} while(ThreadsRunning) ;
		

		ArrayList<ArrayList<BigInteger>> TempStats = new ArrayList<ArrayList<BigInteger>>() ;
		ArrayList<ArrayList<BigInteger>> Stats = EmptyStats() ;

		for( int in = 0 ; in < Threads.size() ; in++ ) {
			TempStats = Threads.get(in).getStats() ;
			for( int i = 0 ; i < TempStats.get(0).size() ; i++ ) {
				Stats.get(0).set( i , Stats.get(0).get(i).add( TempStats.get(0).get(i) ) ) ;
				Stats.get(1).set( i , Stats.get(1).get(i).add( TempStats.get(1).get(i) ) ) ;
				Stats.get(2).set( i , Stats.get(2).get(i).add( TempStats.get(2).get(i) ) ) ;
			}
		}	
		for( int i = 0; i < Threads.size(); i++ ) {
			Threads.get(i).delete() ;
		}
		
		return Stats ;
	}
	
	public ArrayList<ArrayList<BigInteger>> GenerateStatsLayered( int size , int layers ) {


		ArrayList<ArrayList<BigInteger>> TempStats = new ArrayList<ArrayList<BigInteger>>() ;
		ArrayList<ArrayList<BigInteger>> Stats = EmptyStats() ;
		
		if( layers > 1 ) {
			for( int ind = 0 ; ind < size ; ind++ ) {
				TempStats = this.GenerateStatsLayered( size , layers-1 ) ;
				for( int i = 0 ; i < TempStats.get(0).size() ; i++ ) {
					Stats.get(0).set( i , Stats.get(0).get(i).add( TempStats.get(0).get(i) ) ) ;
					Stats.get(1).set( i , Stats.get(1).get(i).add( TempStats.get(1).get(i) ) ) ;
					Stats.get(2).set( i , Stats.get(2).get(i).add( TempStats.get(2).get(i) ) ) ;
				}
			}
			return Stats ;
		}else{
			TempStats = this.GenerateStat( size ) ;
			for( int i = 0 ; i < TempStats.get(0).size() ; i++ ) {
				Stats.get(0).set( i , Stats.get(0).get(i).add( TempStats.get(0).get(i) ) ) ;
				Stats.get(1).set( i , Stats.get(1).get(i).add( TempStats.get(1).get(i) ) ) ;
				Stats.get(2).set( i , Stats.get(2).get(i).add( TempStats.get(2).get(i) ) ) ;
			}
			return Stats ;
		}
	}
	
	private ArrayList<ArrayList<BigInteger>> GenerateStat( int size ) {
		

		ArrayList<Integer> TempIntStats = new ArrayList<Integer>() ;
		ArrayList<ArrayList<BigInteger>> Stats = EmptyStats() ;
		
		
		int Value = 0 ;
		for( int ind = 0 ; ind < size ; ind++ ) {
			TempIntStats = RollStats() ;
			int min = 20 ;
			int max = -1 ;
			for( int i = 0 ; i < TempIntStats.size(); i++ ) {
				Value = TempIntStats.get(i) - 3 ;
				Stats.get(0).set( Value , Stats.get(0).get(Value).add(BigInteger.ONE) ) ;
				if( Value < min ) {
					min = Value ;
				}
				if( Value > max ) {
					max = Value ;
				}
			}
			Stats.get(1).set( min , Stats.get(1).get(min).add(BigInteger.ONE) ) ;
			Stats.get(2).set( max , Stats.get(2).get(max).add(BigInteger.ONE) ) ;
			
		}
		
		return Stats;
	}
	
	private ArrayList<Integer> RollStats(){
		
		ArrayList<Integer> IntStats = new ArrayList<Integer>() ;
		
		for( int i = 0; i < 6; i++ ) {
			int total = 0 ;
			int min = RollDice() ;
			int temp = 0 ;
			for( int ind = 0 ; ind < 3 ; ind++ ) {
				temp = RollDice() ;
				if ( temp < min ) {
					total += min ;
					min = temp ;
				}else {
					total += temp ;
				}
			}
			IntStats.add(total) ;
		}
		
		return IntStats ;
	}
	
	private int RollDice() {
		return (int)(Math.random()*6)+1 ;
	}
}
