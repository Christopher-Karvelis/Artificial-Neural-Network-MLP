package p3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import gui.ScatterPlotForP2;

public class P3 {
	ArrayList<float[]> trainingSet;
	ArrayList<float[]> means;
	ArrayList<ArrayList<float[]>> clusters;
	int numberOfClusters;
	float learningRate = 0.001f;
	
	public void initialize(){
		trainingSet = new ArrayList<float[]>(readFromFile("S2-Training-Set.txt")); 
		getUserInput();
		setRandomMeans();
	}
	
	public void executeModeOfLVQ(int mode) {
		if(mode == 1) {
			initialize();
			LVQ();
			printMeansResult(calculateClusteringError());
		}else if(mode == 2) {
			trainingSet = new ArrayList<float[]>(readFromFile("S2-Training-Set.txt")); 
			ArrayList<ArrayList<float[]>> bestClusters = new ArrayList<ArrayList<float[]>>();
			float minClusteringError = Float.MAX_VALUE;
			getUserInput();
			int i;
			for(i = 0; i < 5; i++) {
				System.out.println("----------------------------------------Execution No " + (i + 1) +"---------------------------------------");
				setRandomMeans();
				LVQ();
				if(calculateClusteringError() <= minClusteringError) {
					minClusteringError = calculateClusteringError();
					deepCopyArraylist(bestClusters, clusters);
				}
			}
			deepCopyArraylist(clusters, bestClusters);
			System.out.println("----------------------------------------------------------------------------------------------"
							 + "\nLowest Clustering Error = " + minClusteringError);
			printMeansResult(minClusteringError);
		}
	}
	
	public void LVQ() {
		int loopCounter = 0;
		resetClusters();
		ArrayList<float[]> previousMeans;
		do {
			resetClusters();
			loopCounter++;
			System.out.println("loop: " + loopCounter);
			previousMeans = new ArrayList<float[]>();
			int i;
			for(i = 0; i < means.size(); i++) {
				float coordinates[] = new float[2];
				coordinates[0] = means.get(i)[0];
				coordinates[1] = means.get(i)[1];
				previousMeans.add(coordinates);
			}
			Iterator<float[]> iterator = trainingSet.iterator();
			while (iterator.hasNext()) {
				float[] example = Arrays.copyOf(iterator.next(), 2);
				float minDistance = Float.MAX_VALUE;
				int minimumDistanceMean = -1;
				for(i = 0; i < numberOfClusters; i++) {
					float tempDistance = calculateEuclideanDistance(example, means.get(i));
					if(tempDistance <= minDistance) {
						minDistance = tempDistance;
						minimumDistanceMean = i;
					}
				}
				clusters.get(minimumDistanceMean).add(example);
				updateMeanOfWinner(minimumDistanceMean, example);
			}
			learningRate = learningRate * 0.95f;
			System.out.println("Learning rate =============== " + learningRate);
		}while(compareMeans(previousMeans));
		System.out.println("Clustering Error = " + calculateClusteringError());
	}
	
	private void deepCopyArraylist(ArrayList<ArrayList<float[]>> arrayList1, ArrayList<ArrayList<float[]>> arraylist2) {
		int i, j, k;
		for(i = 0; i < arraylist2.size(); i++) {
			ArrayList<float[]> cluster = new ArrayList<float[]>();
			arrayList1.add(cluster);	
			for(j = 0; j < arraylist2.get(i).size(); j++) {
				float coordinates[] = new float[arraylist2.get(i).get(j).length];
				for(k = 0; k < arraylist2.get(i).get(j).length; k++) {
					coordinates[k] =  arraylist2.get(i).get(j)[k];
				}
				arrayList1.get(i).add(coordinates);	
			}	
		}
	}
	
	private boolean compareMeans(ArrayList<float[]> previousMeans) {
		int i,j;
		boolean changed = false;
		for(i = 0; i < means.size(); i++) {
			for(j = 0; j< means.get(i).length; j++) {
				if(previousMeans.get(i)[j] != means.get(i)[j]) {
					changed = true;
				}
			}
		}
		return changed;
	}

	public  float calculateClusteringError() {
		int i, j;
		float clusteringError = 0;
		for(i = 0; i < clusters.size(); i++) {
			for(j = 0; j < clusters.get(i).size(); j++) {
				clusteringError += calculateEuclideanDistance(clusters.get(i).get(j), means.get(i));
			}
		}
		return clusteringError;
	}
	
	private boolean updateMeanOfWinner(int minimumDistanceMean, float[] example) {
		int i;
		boolean changed = false;
		float[] weights = Arrays.copyOf(means.get(minimumDistanceMean), means.get(minimumDistanceMean).length);
		for(i = 0; i < weights.length; i++) {
			 means.get(minimumDistanceMean)[i] += learningRate*(example[i] - weights[i]);
			 if(weights[i] != means.get(minimumDistanceMean)[i]) {
					changed = true;
			 }
		}
		return changed;
	}
	
	private float calculateEuclideanDistance(float[] vector1, float[] vector2) {
		int i;
		float sum = 0;
		for(i = 0; i < vector1.length; i++) {
			sum += Math.pow((vector1[i] - vector2[i]), 2);
		}
		return sum;
	}
	
	public ArrayList<float[]> readFromFile(String filename) {
		ArrayList<float[]> exampleSet = new ArrayList<float[]>();
		try {
	         // open input stream test.txt for reading purpose.
			 File fileName = new File(filename);
			 BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
	         String thisLine;
	         while ((thisLine = bufferedReader.readLine()) != null) {
	        	String[] xn = thisLine.split(",");
	        	float[] example = new float[2];
	        
	        	for(int i = 0; i < xn.length; i++) {
	        		example[i] = Float.parseFloat(xn[i].trim());
	        	}
	        	exampleSet.add(example);
	         }
	         bufferedReader.close();
	    } catch(Exception e) {
	         e.printStackTrace();
	    }
		return exampleSet;
	}
	
	public void resetClusters() {
		int i;
		clusters = new ArrayList<ArrayList<float[]>>();
		for(i = 0; i < numberOfClusters; i++) {
			ArrayList<float[]> cluster = new ArrayList<float[]>();
			clusters.add(cluster);			
		}
	}
	
	public void setRandomMeans() {
		means = new ArrayList<float[]>();
		Random random = new Random();
		//System.out.println(trainingSet.size());
		int i;
		for(i = 0; i < numberOfClusters; i++) {
			int m;
			do {
				m = random.nextInt(trainingSet.size());
				//System.out.println(m);
				//System.out.println(trainingSet.get(m)[0] + " " + trainingSet.get(m)[1]);
			}while(means.contains(trainingSet.get(m)));
			means.add(trainingSet.get(m));
		}
	}
	
	private int checkInput(String[] arguments) {
		if(!(arguments[0].equals("define"))) {
			return 0;
		}else if(arguments.length != 2) {
			return 0;
		}else if(!(arguments[1].matches("[0-9]+"))){
			return 0;	
		}
		return 1;
	}
	
	public void getUserInput() {
		@SuppressWarnings("resource")
		Scanner inputScanner = new Scanner(System.in);
		String[] arguments;
		do {
			System.out.println("To define the number of clusters use the command:\n\n"
					+ "-> \"define -M\""
					+ "\n    -M number of clusters");
			System.out.print("-> ");
			String userInput = inputScanner.nextLine();
			arguments = userInput.split("-");
			for(int i = 0; i < arguments.length; i++) {
				arguments[i] = arguments[i].trim();
			}
		}while(checkInput(arguments) == 0);
		//inputScanner.close();
		numberOfClusters = Integer.parseInt(arguments[1]);
	}
	
	public void printMeansResult(float minClusteringError) {
		SwingUtilities.invokeLater(() -> {
		      ScatterPlotForP2 example = new ScatterPlotForP2("LVQ Clustering for M = " + numberOfClusters
		    		  										+ "\n clustering error = " + minClusteringError, means, clusters);
		      example.setSize(800, 400);
		      example.setLocationRelativeTo(null);
		      example.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		      example.setVisible(true);
		});
	}
}
