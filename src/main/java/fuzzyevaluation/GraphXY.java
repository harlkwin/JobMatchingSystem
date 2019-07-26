package fuzzyevaluation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class GraphXY  extends ApplicationFrame{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GraphXY( String applicationTitle, String chartTitle ,FuzzyEvaluation fe ) {
	      super(applicationTitle);
	      JFreeChart xylineChart = ChartFactory.createXYLineChart(
	         chartTitle ,
	         "Category" ,
	         "Score" ,
	         createDataset(fe) ,
	         PlotOrientation.VERTICAL ,
	         true , true , false);
	        
	      ChartPanel chartPanel = new ChartPanel( xylineChart );
	      chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
	      final XYPlot plot = xylineChart.getXYPlot( );
	      
	      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
	      renderer.setSeriesPaint( 0 , Color.RED );
	      renderer.setSeriesPaint( 1 , Color.GREEN );
	      renderer.setSeriesPaint( 2 , Color.YELLOW );
	      renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
	      renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
	      renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
	      plot.setRenderer( renderer ); 
	      setContentPane( chartPanel ); 
	   }
	   
	   private XYDataset createDataset(FuzzyEvaluation fe ) {
		   ArrayList<Candidat> candidats=fe.getCandidats();
		   ArrayList<Term> lt=fe.getLinguistiquesystem();
		      final XYSeriesCollection dataset = new XYSeriesCollection( );     
	     for(Candidat ca:candidats) {
	    	  XYSeries f = new XYSeries( "candidat "+ca.getId() );
	    	  f.add(ca.getMoyArithmetiqueFloueByExpert().getA(), 0);
	    	  f.add(ca.getMoyArithmetiqueFloueByExpert().getB(), 1)
	    	  ;
	    	  f.add(ca.getMoyArithmetiqueFloueByExpert().getC(), 0);
	    	  dataset.addSeries( f);  
	     }
	     
	     for(Term term:lt) {
	    	 XYSeries f = new XYSeries( "Term "+term.getTerm() );
	    	  f.add(term.getAppartenance().getA(), 0);
	    	  f.add(term.getAppartenance().getB(), 1);
	    	  f.add(term.getAppartenance().getC(), 0);
	    	  dataset.addSeries( f);
	     }
	             
	      
	      return dataset;
	   }
	   
	   
	   
	   public static void main( String[ ] args ) {
		   FuzzyEvaluation fe=new FuzzyEvaluation(3,3,0.2,0.2,0.6);
		   fe.addAllEvaluateur();
			fe.addAllCandidat(3);
			fe.addAllLinguicTerm(3);
			fe.addAllCompetences();
			fe.affectEvaluationToCandidat();
			//fe.printEvaluateurs();
			fe.affectDistanceAndMAFE();
	      GraphXY chart = new GraphXY("candidats",
	         "graphe du fonction d'appartenance",fe);
	      chart.pack( );          
	      RefineryUtilities.centerFrameOnScreen( chart );          
	      chart.setVisible( true ); 
	   }
}