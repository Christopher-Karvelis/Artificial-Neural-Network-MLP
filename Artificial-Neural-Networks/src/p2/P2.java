package p2;

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

public class P2 {
	private ArrayList<float[]> trainingSet;
	private ArrayList<float[]> means;
	private ArrayList<ArrayList<float[]>> clusters;
	private int numberOfClusters;
	
	public void initialize(){
		trainingSet = new ArrayList<float[]>(readFromFile("S2-Training-Set.txt")); 
		getUserInput();
		setRandomMeans();
	}
	
	public void executeModeOfKmeans(int mode) {
		if(mode == 1) {
			initialize();
			Kmeans();
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
				Kmeans();
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

	public void Kmeans() {
		int loopCounter = 0;
		do {
			resetClusters();
			loopCounter++;
			System.out.println("loop: " + loopCounter);
			Iterator<float[]> iterator = trainingSet.iterator();
			while (iterator.hasNext()) {
				int i;
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
			}
		}while(updateMeans());
		System.out.println("Clustering Error = " + calculateClusteringError());
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
		//printExampleSet(exampleSet)
		return exampleSet;
	}
	
	private float calculateEuclideanDistance(float[] vector1, float[] vector2) {
		int i;
		float sum = 0;
		for(i = 0; i < vector1.length; i++) {
			sum += Math.pow((vector1[i] - vector2[i]), 2);
		}
		return sum;
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
	
	public boolean updateMeans() {
		int i;
		boolean changed = false;
		//System.out.println("=================OLD MEANS=================");
		//printMeans();
		for(i = 0; i < means.size(); i++) {
			Iterator<float[]> iterator = clusters.get(i).iterator();
			float sumX = 0;
			float sumY = 0;
			int count = 0;
			while (iterator.hasNext()) {
				float[] example = Arrays.copyOf(iterator.next(), 2);
				sumX += example[0];
				sumY += example[1];
				count++;
			}
			if(count !=0) {
				sumX = sumX / count;
				sumY = sumY / count;
				if(means.get(i)[0] != sumX || means.get(i)[1] != sumY) {
					changed = true;
				}
				means.get(i)[0] = sumX;
				means.get(i)[1] = sumY;	
			}
			
		}
		//System.out.println("=================NEW MEANS=================");
		//printMeans();
		return changed;
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
		//pintClusters(numberOfClusters, means)
	}
	
	public void resetClusters() {
		int i;
		clusters = new ArrayList<ArrayList<float[]>>();
		for(i = 0; i < numberOfClusters; i++) {
			ArrayList<float[]> cluster = new ArrayList<float[]>();
			clusters.add(cluster);			
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
		      ScatterPlotForP2 example = new ScatterPlotForP2("K-means Clustering for M = " + numberOfClusters
		    		  										+ "\n clustering error = " + minClusteringError, means, clusters);
		      example.setSize(800, 400);
		      example.setLocationRelativeTo(null);
		      example.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		      example.setVisible(true);
		    });
	}
	
	public void printMeans() {
		int i;
		for(i = 0; i < means.size(); i++) {
			System.out.println(means.get(i)[0] + " " + means.get(i)[1]);
		}
	}

	private  void printExampleSet(ArrayList<float[]> exampleSet){
		for(int i = 0; i < exampleSet.size(); i++) {
			for(int j = 0; j < 2; j++) {
				System.out.print(exampleSet.get(i)[j] + " ");
			}
			System.out.println("");
		}
	}

	private  void pintClusters(int numberOfClusters){
		for(int i = 0; i < numberOfClusters; i++) {
			for(int j = 0; j < 2; j++) {
				System.out.println(means.get(i)[j]);
			}
		}
	}

}
