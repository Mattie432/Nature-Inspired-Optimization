package Assessment1;

import java.util.Random;

import org.apache.commons.math3.distribution.NormalDistribution;

public class Mutations {

	/**
	 * Implementation of the Uniform Mutation strategy. Uniform mutation
	 * replaces a random component xi in the search point by a new random value.
	 * 
	 * @param x
	 *            : double[] - the original X array
	 * @return double[] - the mutated array.
	 */
	public static double[] UniformMutation(double[] x) {
		double[] xNew = x.clone();

		// pick a random index to mutate
		int max = x.length - 1;
		int min = 0;
		Random rand = new Random();
		int index = rand.nextInt((max - min) + 1) + min;

		// Generate a new random number for that index
		xNew[index] = -10 + Math.random() * (10 - -10);

		return xNew;
	}

	/**
	 * In non-uniform mutation a random component xi is modified by adding or
	 * subtracting a random value. The mutation strength, i. e., the size of
	 * this change, decreases over time.
	 * 
	 * @param x
	 *            : double[] - the original X array
	 * @param currentIterationNumber
	 *            : double - the current iteration
	 * @param numberOfIterations
	 *            : double - the number of total iterations
	 * @param b
	 *            : double - user defined parameter
	 * @return double[] - the mutated array.
	 */
	public static double[] NonUniformMutation(double[] x,
			int currentIterationNumber, int numberOfIterations, double b) {
		double[] xNew = x.clone();

		// pick a random index to mutate
		int max = x.length - 1;
		int min = 0;
		Random rand = new Random();
		int index = rand.nextInt((max - min) + 1) + min;
		double newValue;

		// randomly select a mutation
		if (rand.nextDouble() >= 0.5) {
			double nonUniformMutationDelta = NonUniformMutationDelta(
					currentIterationNumber, (100 - xNew[index]), b,
					numberOfIterations);
			newValue = xNew[index] + nonUniformMutationDelta;
		} else {
			double nonUniformMutationDelta = NonUniformMutationDelta(
					currentIterationNumber, (xNew[index] + 100), b,
					numberOfIterations);
			newValue = xNew[index] - nonUniformMutationDelta;
		}
		xNew[index] = newValue;

		return xNew;
	}

	/**
	 * This is the Delta(t, y) function for the second mutation algorithm
	 * 
	 * @param t
	 *            : double - current iteration number
	 * @param y
	 *            : double - y value
	 * @param b
	 *            : double - the users parameter
	 * @param T
	 *            : double - Total number of iterations
	 * @return double - The mutated exponent
	 */
	@SuppressWarnings("unused")
	private static double NonUniformMutationDelta(double t, double y, double b,
			double T) {
		Random rand = new Random();
		// Unsure if R should be either '0 or 1' or a double between those
		// ranges. r ∈ [0, 1] so shouldnt r be either '0 or 1' but this brakes
		// the algorithm so I implemented the double version.
		double r = rand.nextDouble();
		double result = (y * 
				Math.pow(1.0 - 
		                Math.pow(r,
		                         (1.0 - t / T)
		                         )
		                ,b)
	                );
		double result2 = (y * (1.0 - 
                Math.pow(r,
                        Math.pow((1.0 - t /T),b)
                        )));
		return result;
	}

	/**
	 * Gaussian mutation adds a normally-distributed random value to each
	 * component of the search point. The ‘size’ of this value is controlled by
	 * a parameter σ, which is usually called step size.
	 * 
	 * @param x
	 *            : double[] - the original X array
	 * @param stepSize
	 *            : double - the step size
	 * @return double[] - the mutated X array
	 */
	public static double[] GaussianMutation(double[] x, double stepSize) {
		double[] xNew = x.clone();

		NormalDistribution dist = new NormalDistribution(0, 1);

		for (int i = 0; i < xNew.length; i++) {
			double t = dist.sample();
			double newElement = xNew[i] + (stepSize * t);
			if(newElement < -100.0){
				newElement = -100.0;
			}else if(newElement > 100.0){
				newElement = 100.0;
			}
			xNew[i] = newElement;
		}

		return xNew;
	}
}