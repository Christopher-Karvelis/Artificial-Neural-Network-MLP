package gui;
import java.awt.Color;
import java.awt.Shape;
import java.io.Serial;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

public class ScatterPlotForP2 extends JFrame {
	@Serial
	private static final long serialVersionUID = 6294689542092367723L;
	
	public ScatterPlotForP2(String title, ArrayList<float[]> means, ArrayList<ArrayList<float[]>> clusters) {
		super("Scatterplot");
		// Create dataset
		XYDataset dataset = createDataset(means, clusters);
		// Create chart
		JFreeChart chart = ChartFactory.createScatterPlot(title, "X-Axis", "Y-Axis", dataset);
		//Changes background color
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(220,220,220));
		Shape cross = ShapeUtilities.createRegularCross(4, 0.01f);
		plot.getRenderer().setSeriesShape(0, cross);
		plot.getRenderer().setSeriesPaint(1, Color.green);
		plot.getRenderer().setSeriesPaint(0, Color.black);
		// Create Panel
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

	private XYDataset createDataset(ArrayList<float[]> means, ArrayList<ArrayList<float[]>> clusters) {
		int i,j;
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries meansSeries = new XYSeries("Means");
		for(i = 0; i < means.size(); i++) {
			meansSeries.add(means.get(i)[0], means.get(i)[1]);
		}
	    dataset.addSeries(meansSeries);
	    XYSeries examplesSeries = new XYSeries("Examples");
		for(i = 0; i < clusters.size(); i++) {
			for(j = 0; j < clusters.get(i).size(); j++)
				examplesSeries.add(clusters.get(i).get(j)[0], clusters.get(i).get(j)[1]);
		}
	    dataset.addSeries(examplesSeries);
		return dataset;
	}
}