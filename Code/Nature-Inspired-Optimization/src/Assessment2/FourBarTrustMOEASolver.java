package Assessment2;
import java.io.IOException;

import org.jfree.ui.RefineryUtilities;
import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;


public class FourBarTrustMOEASolver {



	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//configure and run this experiment
		NondominatedPopulation result = new Executor()
		.withProblem("FourBarTrussPlanProblem")
		.withAlgorithm("NSGAII")
		.withMaxEvaluations(10000)
		.distributeOnAllCores()
		.run();

		//display the results
		int COUNT = result.size();
		float [][] data = new float[2][COUNT];
		int i=0;		
		System.out.format("Objective1  Objective2%n");
		for (Solution solution : result) {
			System.out.format("%.4f      %.4f%n",
					solution.getObjective(0),
					solution.getObjective(1));
			data[0][i] = (float) solution.getObjective(0);
			data[1][i] = (float) solution.getObjective(1);
			i++;
		}

		plotParetoFront plotChart = new plotParetoFront("Pareto solutions", data);
		plotChart.pack();
		RefineryUtilities.centerFrameOnScreen(plotChart);
		plotChart.setVisible(true);

		Analyzer analyzer = new Analyzer()
		.withProblem("FourBarTrussPlanProblem")
		.includeAllMetrics()
		.showStatisticalSignificance();

		Executor executor = new Executor()
		.withProblem("FourBarTrussPlanProblem")
		.withMaxEvaluations(10000);		
		analyzer.addAll("NSGAII", executor.withAlgorithm("NSGAII").runSeeds(50));

		analyzer.printAnalysis();

		
	}

}
