package packageDND;

import java.math.* ;
import java.util.*;

public class DNDMain {

	  //Total Sample Size = Threads * ( Size ^ Layers )
	private final static int Size   = 100 ;
	private final static int Layers = 2 ; // Minimum 2
	private final static int Threads = 10 ;
	
	public static void main(String[] args) {
		
		ArrayList<ArrayList<BigInteger>> Stats = new ArrayList<ArrayList<BigInteger>>() ;
		
		BigInteger SampleSize = BigInteger.valueOf(Threads).multiply(   BigInteger.valueOf(Size).pow(Layers)   ) ;

		System.out.println( "\tTesting " + Size + " Size with " + Layers + " Layers across " + Threads + " Threads.");
		System.out.println( "\tSample Size : Threads * ( Size ^ Layers ) = " + SampleSize ) ;

		double TimeStart = System.currentTimeMillis() ;
		
		Stats = new StatGenerator( Size , Layers , Threads ).getStats() ;
		
		double ElapsedTime = System.currentTimeMillis() - TimeStart ;
		
		System.out.print( "\tElapsed Time: " + (ElapsedTime/1000) + " Seconds\n\n" ) ;
		
		System.out.print( "\tValues  : " + Stats.get(0) + "\n" );
		System.out.print( "\tMinimums: " + Stats.get(1) + "\n" );
		System.out.print( "\tMaximums: " + Stats.get(2) + "\n\n" );

		BigDecimal Total = new BigDecimal(0) ;

		BigDecimal MinAvg = new BigDecimal( 0 ) ;
		BigDecimal MaxAvg = new BigDecimal( 0 ) ;
		BigDecimal Avg = new BigDecimal( 0 ) ;
		
		for( int i = 0; i < Stats.get(1).size() ; i++ ) {
			Total = Total.add( new BigDecimal( Stats.get(1).get(i) ) ) ;
		}
		System.out.print( "\tTotal: " + Total + "\n") ;

		for( int i = 0; i < Stats.get(1).size() ; i++ ) {
			MinAvg = MinAvg.add( new BigDecimal(Stats.get(1).get(i)).multiply( new BigDecimal(i + 3).divide( Total , MathContext.DECIMAL128 ) ) ) ;
			MaxAvg = MaxAvg.add( new BigDecimal(Stats.get(2).get(i)).multiply( new BigDecimal(i + 3).divide( Total , MathContext.DECIMAL128 ) ) ) ;
		}
		
		Total = Total.multiply( BigDecimal.valueOf( 6 ) ) ;

		for( int i = 0; i < Stats.get(0).size() ; i++ ) {
			Avg = Avg.add( new BigDecimal(Stats.get(0).get(i)).multiply( new BigDecimal(i + 3).divide( Total , MathContext.DECIMAL128 ) ) ) ;
		}

		System.out.print( "\n\tMinAvg: " + MinAvg ) ;
		System.out.print( "\n\tAvg:    " + Avg ) ;
		System.out.print( "\n\tMaxAvg: " + MaxAvg ) ;
		
	}
}
