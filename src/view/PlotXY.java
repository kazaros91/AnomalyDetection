package view;

import java.io.IOException;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;


public class PlotXY {

//	private List<XYSeries> collection;
	private XYChart chart;
	private String plotName;
	private String outputDirectory;
	
	public PlotXY(String plotName, String xName, String Yname) {
		// call another constructor with the default outputDirectory = System.getProperty("user.dir");
		this( plotName, xName, Yname, System.getProperty("user.dir") + "/" );
	}
	
	public PlotXY(String plotName, String xName, String yName, String outputDirectory) {
		this.plotName = plotName;
		this.outputDirectory = outputDirectory;
		
		// Create Chart
	    chart = new XYChart(800, 400);
	    chart.setTitle(plotName);
	    chart.setXAxisTitle(xName);
	    chart.setYAxisTitle(yName);
	}
	
	public void add(List<Double> yData, String seriesName) {
		 this.add(null, yData, seriesName);
	}
	
	public void add(List<Double> xData, List<Double> yData, String seriesName) {
		 chart.addSeries(seriesName, xData, yData);
	}
	
	public void draw() {
	    // Show it
//	    new SwingWrapper<XYChart>(chart).displayChart();
	 
		// write the drawing into the file
	    try {
	    		VectorGraphicsEncoder.saveVectorGraphic(chart, outputDirectory + plotName, VectorGraphicsFormat.EPS);
//	    	    BitmapEncoder.saveBitmap(chart, outputDirectory + plotName, BitmapFormat.JPG);
	    }
	    catch (IOException e) {
	    		e.printStackTrace();
	    }
    }
	
	// uncomment for testing PlotXY
//	public static void main(String [] args) {
//		XYChart chart = new XYChart(500, 400);
//	    chart.setTitle("chartName");
//	    chart.setXAxisTitle("X");
//	    chart.setYAxisTitle("Y");
//	    
//	    int [] y1 =  {1, 2, 3, 4, 1};
//	    XYSeries series1 = chart.addSeries("series1", y1);
//	    
//	    int [] y2 =  {0, 3, 2, 5, 5};
//	    XYSeries series2 = chart.addSeries("series2", y2);
//	    
//	    String outputDirectory = System.getProperty("user.dir") + "/";
//	    try {
//	    		VectorGraphicsEncoder.saveVectorGraphic(chart, outputDirectory + "2", VectorGraphicsFormat.EPS);
//	    }
//	    catch (IOException e) {
//    		e.printStackTrace();
//	    }
//	}
	
	
}
