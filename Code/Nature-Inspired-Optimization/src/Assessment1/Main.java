package Assessment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Random;

public class Main {
	// Number of iterations to run
	public static int T = 5000;
	// set the n-dimensional SPHERE
	public static int n = 10;
	// Array for elements of X
	public static double[] X = new double[n];
	// Array for the final fitness value of each iteration
	public static double[] iterations = new double[T];
	// Min & max values for the search space
	private int searchSpaceMin = -100;
	private int searchSpaceMax = 100;
	// Which mutation algorithm to use
	private int mutationAlgorithm = 0;
	//How many runs of the mutation to do?
	private static int numRuns = 30;

	/**
	 * Main method to call the functions which make up the (1+1) evolution
	 * strategy algorithm.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Assessment1.Main me = new Assessment1.Main();
		me.getMutationAlgorithm();
		
		try{
			File p = new File("Iterations.txt");
			p.delete();
		}catch(Exception e){
			System.out.println(e);
		}
		
		while(numRuns > 0){
			n = 10;
			iterations = new double[T];
			X = new double[n];
			
			me.createInitialSearchPool();
			me.iterate();
			me.printResults();
			me.printToFile();
			System.out.println();
			numRuns--;
		}
	}

	/**
	 * Ask the user which algorithm they want to run
	 */
	private void getMutationAlgorithm() {
		// Get the user to enter the number of iterations
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.println(" [1] : Uniform Mutation");
			System.out.println(" [2] : Non-Uniform Mutation");
			System.out.println(" [3] : Gaussian Mutation");
			System.out.println(" [4] : (1+1) evolution strategy with 1/5-rule");

			System.out.print("Which algorithm to use? : ");
			mutationAlgorithm = Integer.parseInt(br.readLine());
			System.out.println();

		} catch (NumberFormatException nfe) {
			System.err.println("Invalid Format!");
			getNumberOfIterations();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Prints the array of X to the console
	 */
	private void printResults() {
		System.out.print("                    Value X=[ ");
		for (int i = 0; i < n; i++) {
			System.out.print(X[i] + " ");
		}
		System.out.println("]");
		System.out.println("Value f(x)=" + calculateFitness(X));
	}

	/**
	 * Prints the array of the iterations to a file in the root dir.
	 * @throws IOException 
	 */
	private void printToFile() throws IOException {
			String[] linesIn = new String[T];
		try {
			FileReader fr = new FileReader("Iterations.txt");
			BufferedReader br = new BufferedReader(fr);
			int count = 0;
			String line = br.readLine();
			while(line != null){
				linesIn[count] = line;
				line = br.readLine();
				count ++;
			}
			br.close();
			fr.close();
		}

		catch (IOException e) {
			System.out.println(e);
		}
		
		
			FileWriter fw = new FileWriter("Iterations.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			for (int i = 0; i < iterations.length; i++) {
				if(linesIn[i] == null){
					linesIn[i] = "";
				}
				out.write(linesIn[i] + " " + iterations[i] + "");
				out.println();
			}
			out.close();
	}

	/**
	 * Request the user to enter a number of iterations to run the algorithm
	 * for.
	 */
	private void getNumberOfIterations() {
		// Get the user to enter the number of iterations
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.print("Enter the number of iterations: ");
		try {
			T = Integer.parseInt(br.readLine());
			System.out.println("Number of iterations set to " + T);
			iterations = new double[T];

		} catch (NumberFormatException nfe) {
			System.err.println("Invalid Format!");
			getNumberOfIterations();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Crates the initial search pool by generating uniform random number for
	 * each element in the X array.
	 */
	private void createInitialSearchPool() {

		// Populate the X array with random numbers
		for (int i = 0; i < n; i++) {
			double rand = searchSpaceMin + Math.random() * (searchSpaceMax);
			X[i] = rand;
		}
		System.out.print("Initial value of 'X' set to [ ");
		for (int i = 0; i < n; i++) {
			System.out.print(X[i] + " ");
		}
		System.out.println("]");
	}

	/**
	 * When called this function loops for the number of iterations defined in
	 * the variable T. This calls the mutate method for each iteration.
	 * 
	 * @throws Exception
	 */
	private void iterate() throws Exception {
		
		int count = 0;
		
		//These variables are used for the 1/5 evolution
		Random rand = new Random();
		int OneFiveRuleStepSize = rand.nextInt(100);
		int nIterations = 100;
		int g = 0;
		int b = 0;

		// Loop for the number of iterations.
		while (count < T) {
			// save the fitness value for the iteration
			iterations[count] = calculateFitness(X);

			// Create the offspring via mutation
			double[] offspring;

			if (mutationAlgorithm == 1) {
				offspring = Mutations.UniformMutation(X);
			} else if (mutationAlgorithm == 2) {
				// B is a parameter set by the user?
				double bParam = 60.0;
				offspring = Mutations.NonUniformMutation(X, count + 1, T,
						bParam);
			} else if (mutationAlgorithm == 3) {
				double stepSize = 10.0;
				offspring = Mutations.GaussianMutation(X, stepSize);
			} else if (mutationAlgorithm == 4) {
				offspring = Mutations.GaussianMutation(X, OneFiveRuleStepSize);
			} else {
				throw new Exception("Invalid algorithm number "
						+ mutationAlgorithm);
			}

			if (mutationAlgorithm == 4) {

				if (fitnessCheck(offspring)) {
					X = offspring;
					g = g + 1;
				} else {
					b = b + 1;
				}

				if (b + g == nIterations) {
					double gOVERn = (double) g / (double) nIterations;
					double oneOVERfive = (double) 1 / (double) 5;
					if (gOVERn > oneOVERfive) {
						OneFiveRuleStepSize = OneFiveRuleStepSize * 2;
					} else if (gOVERn < oneOVERfive) {
						OneFiveRuleStepSize = OneFiveRuleStepSize / 2;
					}
					g = 0;
					b = 0;
				}
				
			// Check the fitness, if better replace X with offspring
			}else if (fitnessCheck(offspring)) {
					X = offspring;
				//If using the 1/5 rule then do this instead
				} else 
			count++;
		}
	}

	/**
	 * Check the fitness of an offspring is better than the current X
	 * 
	 * @param offspring
	 *            : double[] - the offspring
	 * @return boolean - Is better
	 */
	private boolean fitnessCheck(double[] offspring) {
		// Calculate the fitness of both offspring and X
		double offspringFitness = calculateFitness(offspring);
		double parentFitness = calculateFitness(X);

		// Check which is the better fitness
		if (offspringFitness < parentFitness) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Calculate the fitness of an array using the SPHERE function.
	 * 
	 * @param x
	 *            : double[] - the array to check
	 * @return int - the fitness value
	 */
	private double calculateFitness(double[] x) {
		double sum = 0;
		int count = n - 1;
		while (count >= 0) {
			sum = sum + (x[count] * x[count]);
			count--;
		}
		return sum;
	}

}