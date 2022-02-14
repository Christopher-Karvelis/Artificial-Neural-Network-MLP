package data;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class S1{
	private int numOfExamples;
	private String category;
	private Random random;

	public S1(int numOfExamples) {
		this.numOfExamples = numOfExamples;
		random = new Random();
	}
	
	public float generateRandomFloat(){
		float random_max = 2.01f;
		float random_min = -2.01f;
		float x = random_min + (random_max - random_min) * random.nextFloat();
		float real_min = -2.0f;
		float real_max = 2.0f;
		if(x <= real_min) {
			return real_min;
		}else if(x >= real_max) {
			return real_max;
		}
		return x;
	}
	void generateExampleSetS1(PrintWriter writer, Boolean noise) {
	 	float X1;
		float X2;
		for(int i = 0; i < numOfExamples; i++) {	
			X1 = generateRandomFloat();
			X2 = generateRandomFloat();
			configureCategory(X1, X2);
			//Add noise only for training set
			if(noise) {
				addNoise();
			}
			//get rid of newline character at the end of file
			if(i == 2999) {
				writer.print(X1 + ", " + X2 + ", " + category);
			}else {
				writer.println(X1 + ", " + X2 + ", " + category);
			}
		}
		writer.close();
	}
				
	private void addNoise() {
		int randomInteger = new Random().nextInt(9 + 1) + 1;
		if((category.equals("C2") || category.equals("C3")) && randomInteger == 1) {
			category = "C1";
		}
	}

	private void configureCategory(float X1, float X2) {
		if((Math.pow((X1 - 1), 2) + Math.pow((X2 - 1), 2)) <= 0.49){
			category = "C2";
		}else if((Math.pow((X1 + 1), 2) + Math.pow((X2 + 1), 2)) <= 0.49) {
			category = "C2";
		}else if((Math.pow((X1 + 1), 2) + Math.pow((X2 - 1), 2)) <= 0.49){
			category = "C3";
		}else if((Math.pow((X1 - 1), 2) + Math.pow((X2 + 1), 2)) <= 0.49) {
			category = "C3";
		}else {
			category = "C1";
		}
	}

	public void generateExampleSets() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter trainingSetWriter = new PrintWriter("S1-Training-Set.txt", "UTF-8");
		PrintWriter testSetWriter = new PrintWriter("S1-Test-Set.txt", "UTF-8");
		generateExampleSetS1(trainingSetWriter, true);
		generateExampleSetS1(testSetWriter, false);
	}
}
