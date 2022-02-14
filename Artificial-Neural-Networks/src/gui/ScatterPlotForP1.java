package gui;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ScatterPlotForP1 extends JFrame {
	private static final long serialVersionUID = 6294689542092367723L;
	
	public ScatterPlotForP1(String title, ArrayList<float[]> correctGuesses, ArrayList<float[]> wrongGuesses) {
		super("Scatterplot");
		// Create dataset
		XYDataset dataset = createDataset(correctGuesses, wrongGuesses);
		// Create chart
		JFreeChart chart = ChartFactory.createScatterPlot(
				title, 
		  "X-Axis", "Y-Axis", dataset);
		//Changes background color
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(220,220,220));
		plot.getRenderer().setSeriesPaint(1, Color.red);
		plot.getRenderer().setSeriesPaint(0, Color.green);
		// Create Panel
		ChartPanel panel = new ChartPanel(chart);
		setContentPane(panel);
	}

	private XYDataset createDataset(ArrayList<float[]> correctGuesses, ArrayList<float[]> wrongGuesses) {
		int i;
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries correctSeries = new XYSeries("Correct");
		for(i = 0; i < correctGuesses.size(); i++) {
			correctSeries.add(correctGuesses.get(i)[0], correctGuesses.get(i)[1]);
		}
	    dataset.addSeries(correctSeries);
	    XYSeries wrongSeries = new XYSeries("Wrong");
		for(i = 0; i < wrongGuesses.size(); i++) {
			wrongSeries.add(wrongGuesses.get(i)[0], wrongGuesses.get(i)[1]);
		}
	    dataset.addSeries(wrongSeries);
		return dataset;
	}
}