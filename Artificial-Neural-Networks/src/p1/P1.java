package p1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import gui.ScatterPlotForP1;

import java.util.Scanner;

public class P1 {
	private HashMap<float[], float[]> trainingSet;
	private HashMap<float[], float[]> testSet;
	private ArrayList<float[]> correctGuesses;
	private ArrayList<float[]> wrongGuesses;
	private Mlp mlp;


	public P1() {
		
	}
	
	public void executeModeOfMLP(int mode) {
		if(mode == 1) {
			loadExampleSets();
			createMLP();
			trainMLPWithGradientDecent();
			printTestResult(-1, -1, -1, "");
		}else if(mode == 2) {
			loadExampleSets();
			ArrayList<float[]> bestCorrectGuesses = new ArrayList<float[]>();
			ArrayList<float[]> bestWrongGuesses = new ArrayList<float[]>();
			int[] miniBachesSizes = {1, trainingSet.size()/10, trainingSet.size()/100, trainingSet.size()};
			int[][] hiddenLayerSizes = {
				      {5 ,3}, 
				      {7, 4}, 
				      {8, 5},
			};
			String[] activationFunctions = {"t", "l"};
			float maxGeneralizationCapability = -1f;
			int minLayerSizes = -1;
			int minMiniBachesSize = -1;
			int minActivationFunction = -1;
			int i,j, k;
			for(i = 0; i < hiddenLayerSizes.length; i++) {
				for(j = 0; j < miniBachesSizes.length; j++) {
					for(k = 0; k < activationFunctions.length; k++) {
						mlp = new Mlp(2, 3, hiddenLayerSizes[i][0], hiddenLayerSizes[i][0], activationFunctions[k]);
						trainMLPWithGradientDecent();
						float currentGeneralizationCapability = mlp.getGeneralizationCapability();
						if(currentGeneralizationCapability >= maxGeneralizationCapability) {
							maxGeneralizationCapability = currentGeneralizationCapability;
							deepCopyArraylist(bestCorrectGuesses, correctGuesses);
							deepCopyArraylist(bestWrongGuesses, wrongGuesses);
							minLayerSizes = i;
							minMiniBachesSize = j;
							minActivationFunction = k;
						}
					}
				}
			}
			deepCopyArraylist(correctGuesses, bestCorrectGuesses);
			deepCopyArraylist(wrongGuesses, bestWrongGuesses);
			printTestResult(hiddenLayerSizes[minLayerSizes][0], hiddenLayerSizes[minLayerSizes][1], miniBachesSizes[minMiniBachesSize], activationFunctions[minActivationFunction]);
			System.out.println("Best generaliazation capability = " + 100*maxGeneralizationCapability + "%");
		}
	}
	
	private void deepCopyArraylist(ArrayList<float[]> arrayList1, ArrayList<float[]> arraylist2) {
		int i,j;
		for(i = 0; i < arraylist2.size(); i++) {
			float coordinates[] = new float[arraylist2.get(i).length];
			for(j = 0; j < arraylist2.get(i).length; j++) {
				coordinates[j] = arraylist2.get(i)[j];
			}
			arrayList1.add(coordinates);
		}
	}

	public void calculateOutputsWithForwardPass() {
		int count = 0;
		Iterator<Entry<float[], float[]>> iterator = trainingSet.entrySet().iterator();
		while (iterator.hasNext()) {
	        Map.Entry<float[], float[]> pair = (Map.Entry <float[], float[]>)iterator.next();
	        float[] x = new float[pair.getKey().length];
	        float[] t = Arrays.copyOf(pair.getValue(), pair.getValue().length);
	        for(int i = 0; i < pair.getKey().length; i++) {
	        	x[i] = pair.getKey()[i];
	        }
	        mlp.forwardPass(x);
	        mlp.backprop(x, t);
	        count ++;
			int miniBachesSize = 50;
			if(count == miniBachesSize) {
	        	count = 0;
	        	mlp.updateWeights();
	        }
	   }
	}
	
	public void testGeneralizationCapability() {
		correctGuesses = new ArrayList<float[]>();
		wrongGuesses = new ArrayList<float[]>();
		Iterator<Entry<float[], float[]>> iterator = testSet.entrySet().iterator();
		while (iterator.hasNext()) {
	        Map.Entry<float[], float[]> pair = (Map.Entry <float[], float[]>)iterator.next();
	        float[] x = new float[pair.getKey().length];
	        float[] t = Arrays.copyOf(pair.getValue(), pair.getValue().length);
	        for(int i = 0; i < pair.getKey().length; i++) {
	        	x[i] = pair.getKey()[i];
	        }
	        mlp.forwardPass(x);
			int CORRECT_GUESS = 1;
			if(mlp.clculateGeneralizationForExample(t) == CORRECT_GUESS) {
	        	correctGuesses.add(x);
	        }else {
	        	wrongGuesses.add(x);
	        }
		}
	    System.out.println("\nGeneralization Capability Of MLP = " + mlp.getGeneralizationCapability()*100 + "%");
	}
	
	public void trainMLPWithGradientDecent() {
		int epoch = 0;
		float previousTotalSquaredError = 0;
		float currentTotalSquaredError = 0;
		do{
			previousTotalSquaredError = currentTotalSquaredError;
			epoch++;
			System.out.println("\n------------EPOCH " + epoch + "------------");
			mlp.setTotalSquaredError(0);
			calculateOutputsWithForwardPass();
			currentTotalSquaredError = mlp.getTotalSquaredError();
			System.out.println("Total Squared Error = " + mlp.getTotalSquaredError());
			
		}while(epoch < 500 || (Math.abs(previousTotalSquaredError - currentTotalSquaredError) > 0.1f));
		testGeneralizationCapability();
	}

	public void loadExampleSets() {
		trainingSet = new HashMap<float[], float[]>(readFromFile("S1-Training-Set.txt")); 
		testSet = new HashMap<float[], float[]>(readFromFile("S1-Test-Set.txt")); 
	}
	
	public void createMLP() {
		@SuppressWarnings("resource")
		Scanner inputScanner = new Scanner(System.in);
		String[] arguments;
		do {
			System.out.println("To define the architecture of the MLP use the command:\n\n"
					+ "-> \"define -d -K -H1 -H2 -f\""
					+ "\n    -d number of inputs"
					+ "\n    -K number of categories"
					+ "\n    -H1 number of neurons in first hidden level "
					+ "\n    -H2 number of neurons in the second hidden  level"
					+ "\n    -f activation function for the second hidden level, type t(tahn) or l(linear)");
			System.out.print("-> ");
			String userInput = inputScanner.nextLine();
			arguments = userInput.split("-");
			for(int i = 0; i < arguments.length; i++) {
				arguments[i] = arguments[i].trim();
			}
		}while(checkInput(arguments) == 0);
		mlp = new Mlp(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]), Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]), arguments[5]);
	}
	
	private int checkInput(String[] arguments) {
		if(!(arguments[0].equals("define"))) {
			return 0;
		}else if(arguments.length != 6) {
			return 0;
		}else if(!(arguments[5].equals("t")) && !(arguments[5].equals("l"))) {
			return 0;
		}else {
			for(int i = 1; i < 4; i++) {
				if(!(arguments[i].matches("[0-9]+"))){
					return 0;
				}
			}
		}
		return 1;
	}
	
	public void printTestResult(int H1, int H2, int minMiniBachesSize, String activationFunction) {
		SwingUtilities.invokeLater(() -> {
			  ScatterPlotForP1 example;
			  if(H1 != -1 && minMiniBachesSize != -1 && H2 != -1) {
				  example = new ScatterPlotForP1("MLP with H1 = " + H1 + ", H2 = " + H2 + ", L = " + minMiniBachesSize + " and activation function of H2 = " + activationFunction, correctGuesses, wrongGuesses);
			  }else {
				  example = new ScatterPlotForP1("MLP categorization for training set", correctGuesses, wrongGuesses);
			  }
			  example.setSize(800, 400);
			  example.setLocationRelativeTo(null);
			  example.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			  example.setVisible(true);
		});
	}
	
	public HashMap<float[], float[]> readFromFile(String filename) {
		HashMap<float[], float[]> exampleSetMap = new HashMap<float[], float[]>();
		try {
	         // open input stream test.txt for reading purpose.
			 File fileName = new File(filename);
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
	         String thisLine;
	         while ((thisLine = bufferedReader.readLine()) != null) {
	        	String[] xn = thisLine.split(",");
	        	float[] target = {0, 0, 0};
	        	float[] example = new float[2];
	        	for(int i = 0; i < xn.length; i++) {
	        		xn[i] = xn[i].trim();
	        		if((i == 2) && xn[i].equals("C1")) {
	        			target[0] = 1;
	        		}else if((i == 2) && xn[i].equals("C2")) {
	        			target[1] = 1;
	        		}else if((i == 2) && xn[i].equals("C3")) {
	        			target[2] = 1;
	        		}else if(i != 2) {
	        			example[i] = Float.parseFloat(xn[i].trim());
	        		}
	        	}
	        	exampleSetMap.put(example, target);
	         }
	         bufferedReader.close();
	    } catch(Exception e) {
	         e.printStackTrace();
	    }
		return exampleSetMap;
	}
}
