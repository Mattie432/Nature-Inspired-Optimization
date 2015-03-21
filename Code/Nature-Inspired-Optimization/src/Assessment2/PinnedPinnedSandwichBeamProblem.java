package Assessment2;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;


public class PinnedPinnedSandwichBeamProblem extends AbstractProblem {

	private double [][] pEc = new double[3][3]; //density p, young constant E, cost c for each material

	public PinnedPinnedSandwichBeamProblem() {
		super(5,2,0);
		
		//material 1
		pEc[0][0] = 100.0;
		pEc[0][1] = 1.60E+09;
		pEc[0][2] = 500.0;
		//material 2
		pEc[1][0] = 2770.0;
		pEc[1][1] = 7.00E+10;
		pEc[1][2] = 1500.0;
		//material 2
		pEc[1][0] = 7780.0;
		pEc[1][1] =  2.00E+11;
		pEc[1][2] = 800.0;
	}
	
	
	@Override
	public void evaluate(Solution solution) {
		double[] x = EncodingUtils.getReal(solution);
		double f1 = 0.0; // == v (frequency)
		double f2 = 0.0; // == cost
		
		double b = x[3];
		double L = x[4];
		
		double EI = 2*b / 3 * (pEc[0][1]*Math.pow(x[0],3) + pEc[1][1]*(Math.pow(x[1],3)-Math.pow(x[0],3)) + pEc[2][1]*( Math.pow(x[2],3) - Math.pow(x[1],3) ) );
		double u = 2*b * ( (pEc[0][0] * x[0]) + (pEc[1][0] * ( x[1] - x[0] )) + (pEc[2][0] * (x[2] - x[1])) );
		
		f1 = Math.PI / 2*Math.pow(L,2) * (Math.pow(EI / u,0.5));
		f2 = 2*b*L * ((pEc[0][2] * x[0]) + (pEc[1][2] * (x[1] - x[0])) + (pEc[2][2] * (x[2] - x[1])) );
		
		solution.setObjective(0, f1);
		solution.setObjective(1, f2);

	}

	@Override
	public Solution newSolution() {
		Solution solution = new Solution(numberOfVariables, numberOfObjectives);
				
		solution.setVariable(0,  new RealVariable(0.3, 0.6));       // d_1
		solution.setVariable(1,  new RealVariable(0.01, 0.58));     // d_2
		solution.setVariable(2,  new RealVariable(0.01, 0.58));     // d_3
		
		solution.setVariable(3,  new RealVariable(0.3, 0.55));      // b (beam width)
		solution.setVariable(4,  new RealVariable(3, 6));           // L (beam length)
		
		
		
		return solution;
	}
	
	
	
}
