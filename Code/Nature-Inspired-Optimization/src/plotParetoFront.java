import java.awt.RenderingHints;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


public class plotParetoFront extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor, plots the two objective functions of a single run as
	 * an XY scatter diagram
	 * @param chartTitle : String - title 
	 * @param data : float[][] - the values of objective1&2 for each run
	 */
	public plotParetoFront(String chartTitle, float[][] data) {
		super(chartTitle);

        final NumberAxis domainAxis = new NumberAxis("Objective 1");
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis("Objective 2");
        rangeAxis.setAutoRangeIncludesZero(false);
		//final FastScatterPlot plot = new FastScatterPlot(data, domainAxis, rangeAxis);
        

        XYDataset dataset = new XYSeriesCollection();
        ((XYSeriesCollection) dataset).addSeries(createDataset(data, "NSGAII"));

        final XYItemRenderer renderer = new StandardXYItemRenderer();
		final XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        
        final JFreeChart chart = new JFreeChart("Fast Scatter Plot", plot);
//        chart.setLegend(null);

        // force aliasing of the rendered content..
        chart.getRenderingHints().put
            (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final ChartPanel panel = new ChartPanel(chart, true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
  //      panel.setHorizontalZoom(true);
    //    panel.setVerticalZoom(true);
        panel.setMinimumDrawHeight(10);
        panel.setMaximumDrawHeight(2000);
        panel.setMinimumDrawWidth(20);
        panel.setMaximumDrawWidth(2000);
        
        setContentPane(panel);
	}

	/**
	 * 
	 * Constructor, plots the two objective functions of a two runs using a
	 * line chart.
	 * @param chartTitle : String - title 
	 * @param data : float[][] - the values of objective1&2 for each run
	 * @param data2 : float[][] - the values of objective1&2 for each run
	 */
	public plotParetoFront(String chartTitle, float[][] data, float[][] data2) {
		super(chartTitle);

        final NumberAxis domainAxis = new NumberAxis("Objective 1");
        domainAxis.setAutoRangeIncludesZero(false);
        final NumberAxis rangeAxis = new NumberAxis("Objective 2");
        rangeAxis.setAutoRangeIncludesZero(false);
        
        XYDataset dataset = new XYSeriesCollection();
        ((XYSeriesCollection) dataset).addSeries(createDataset(data, "NSGAII"));
        ((XYSeriesCollection) dataset).addSeries(createDataset(data2, "GDE3"));
        

		//final FastScatterPlot plot = new FastScatterPlot(data, domainAxis, rangeAxis);
        final XYItemRenderer renderer = new StandardXYItemRenderer();
		final XYPlot plot = new XYPlot(dataset, domainAxis, rangeAxis, renderer);
        
        final JFreeChart chart = new JFreeChart("Fast Scatter Plot", plot);
//        chart.setLegend(null);

        // force aliasing of the rendered content..
        chart.getRenderingHints().put
            (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final ChartPanel panel = new ChartPanel(chart, true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
  //      panel.setHorizontalZoom(true);
    //    panel.setVerticalZoom(true);
        panel.setMinimumDrawHeight(10);
        panel.setMaximumDrawHeight(2000);
        panel.setMinimumDrawWidth(20);
        panel.setMaximumDrawWidth(2000);
        
        setContentPane(panel);
	}

	
	/**
	 * Creates a dataset based off the data given
	 * @param data : float[][] - the values of objective1&2 for each run
	 * @param seriesname : String - the name of the series
	 * @return XYSeries - the created data set
	 */
	private XYSeries createDataset(float[][] data, String seriesname) {

        final XYSeries series1 = new XYSeries(seriesname);

        for(int i = 0; i < data[0].length; i++){
        	series1.add(data[0][i], data[1][i]);
        }
        
        return series1;

    }

	
}
