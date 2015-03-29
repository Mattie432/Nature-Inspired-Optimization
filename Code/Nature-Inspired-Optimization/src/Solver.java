


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.jfree.ui.RefineryUtilities;
import org.moeaframework.Analyzer;
import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.RealVariable;
import org.yaml.snakeyaml.Yaml;


public class Solver {


	/**
	 * Main method which runs the problem using two algorithms and then plots
	 * both results on a line graph.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		System.out.println();
		System.out.println();
		System.out.println("---------- NSGAII ----------");
		float[][] t1 = RunTest("NSGAII");
		System.out.println();
		System.out.println();
		System.out.println("----------  GDE3  ----------"); 
		float[][] t2 = RunTest("GDE3"); 
		
		

		/**
		 * Plot the solutions
		 */
		plotParetoFront plotChart = new plotParetoFront("Pareto solutions", t1,t2);
		plotChart.pack();
		RefineryUtilities.centerFrameOnScreen(plotChart);
		plotChart.setVisible(true);

	}

	/**
	 * Runs the test with the algorithm as specified in the parameter.
	 * @param algorithmInUse : String - the algorithm name
	 * @return float[][] - array containing the two objective results for an run.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static float[][] RunTest(String algorithmInUse) throws IOException, FileNotFoundException {
		//Create a new analyser & include the hypervolume metric
		Analyzer analyzer = new Analyzer()
		.withProblem("PinnedPinnedSandwichBeamProblem")
		.includeHypervolume()
		.showIndividualValues()
		.showStatisticalSignificance();
 
		//Create an executor which uses the sandwichbeam problem
		Executor executor = new Executor()
		.withProblem("PinnedPinnedSandwichBeamProblem")
		.withMaxEvaluations(10000);
		
		//Perform 30 runs of the problem
		ArrayList<NondominatedPopulation> runSeeds = new ArrayList<NondominatedPopulation>();
		for(int i = 0; i < 30; i++) {
			NondominatedPopulation run = executor.withAlgorithm(algorithmInUse).run();
			System.out.println("Run:" + i + " Size:" + run.size());
			//Add the run results to the arraylist of runs
			runSeeds.add(run);
		}
		
		//Add all of the run results to the analyzer
		analyzer.addAll(algorithmInUse, runSeeds);

		//print the analysis (hypervolume values)
		analyzer.printAnalysis();
		
		//Extract the hypervolume values for each run
		ArrayList<Double> values = ExtractValuesFromAnalyzer(analyzer,algorithmInUse);
		
		//Find the best hypervolume value
		Double maxVal = values.get(0);
		int minIndex = 0;
		for(int i = 1; i<values.size(); i++){
			if(values.get(i) > maxVal){
				maxVal = values.get(i);
				minIndex = i;
			}
		}
		System.out.println("Best Hypervolume value : " + maxVal);
		
		//Select the best run based on the hypervolume values
		NondominatedPopulation result = runSeeds.get(minIndex);
				
		//display the results of the run
		int COUNT = result.size();
		float [][] data = new float[2][COUNT];
		int i=0;		
		System.out.format("d1          d2          d3          b           L           |    Objective1  Objective2%n");
		System.out.format("------------------------------------------------------------|--------------------------%n");
		for (Solution solution : result) {
			System.out.format("%.4f      %.4f      %.4f      %.4f      %.4f      |    %.4f      %.4f%n",
					((RealVariable)solution.getVariable(0)).getValue(),
					((RealVariable)solution.getVariable(1)).getValue(),
					((RealVariable)solution.getVariable(2)).getValue(),
					((RealVariable)solution.getVariable(3)).getValue(),
					((RealVariable)solution.getVariable(4)).getValue(),
					solution.getObjective(0),
					solution.getObjective(1)
					);
			data[0][i] = (float) solution.getObjective(0);
			data[1][i] = (float) solution.getObjective(1);
			i++;
		}
		
		return data;
	}

	/**
	 * Used to extract the results of the hypervolume metric from the output file of
	 * the analyzer (using yaml format).
	 * @param analyzer : Analyzer - the analyzer being used
	 * @param algorithm : String - the algorithm name
	 * @return ArrayList<Double> - the hypervolme values for each run
	 * @throws IOExceptio
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<Double> ExtractValuesFromAnalyzer(Analyzer analyzer, String algorithm)
			throws IOException, FileNotFoundException {
		
		//Save the results of the analyzer to a file
		File f = new File("test");
		analyzer.saveAnalysis(f);
		
		//open the file
		InputStream input = new FileInputStream(f);

		//parse the file
		Yaml yaml = new Yaml();
		LinkedHashMap<String, String> load = (LinkedHashMap<String, String>) yaml.load(input);
		Object alg = load.get(algorithm);
		Object hyp = ((LinkedHashMap<String, String>) alg).get("Hypervolume");
		Object val = ((LinkedHashMap<String, String>) hyp).get("Values");
		
		//extract the hypervolume values
		ArrayList<Double> values = (ArrayList<Double>) val;
		return values;
	}

}
