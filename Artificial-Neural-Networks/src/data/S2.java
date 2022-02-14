package data;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

public class S2 {
	private float real_min;
	private float real_max;
	private float random_min;
	private float random_max;
	private Random random;

	public S2() {
		random = new Random();
	}

	void setRandomLimits(float min, float max) {
		random_min = min;
		random_max = max;
	}

	void setRealLimits(float min, float max) {
		real_min = min;
		real_max = max;
	}
	
	public void generateExampleSetS2() throws FileNotFoundException, UnsupportedEncodingException{
		float X1, X2;
		PrintWriter trainingSetWriter = new PrintWriter("S2-Training-Set.txt", "UTF-8");
		//examples for circle (0,0) r = 0.3
		for(int i = 0; i < 100; i++) {
			setRealLimits(-0.3f, 0.3f);
			setRandomLimits(-0.301f, 0.301f);
			do{
				X1 = generateRandomFloat();
				X2 = generateRandomFloat();
			}while(Math.pow(X1, 2) + Math.pow(X2, 2) > Math.pow(0.3, 2));
			trainingSetWriter.println(X1 + ", " + X2);
		}
		//examples in [-1.1,-0.5] x [0.5,1.1]
		for(int i = 0; i < 100; i++) {
			setRealLimits(-1.1f, -0.5f);
			setRandomLimits(-1.101f, -0.501f);
			X1 = generateRandomFloat();
			setRealLimits(0.5f, 1.1f);
			setRandomLimits(0.501f, 1.101f);
			X2 = generateRandomFloat();
			trainingSetWriter.println(X1 + ", " + X2);
		}
		//examples in  [-1.1,-0.5] x [-1.1,-0.5]
		for(int i = 0; i < 100; i++) {
			setRealLimits(-1.1f, -0.5f);
			setRandomLimits(-1.101f, -0.501f);
			X1 = generateRandomFloat();
			X2 = generateRandomFloat();
			trainingSetWriter.println(X1 + ", " + X2);
		}
		//examples in  [0.5,1.1] x [-1.1,-0.5]
		for(int i = 0; i < 100; i++) {
			setRealLimits(0.5f, 1.1f);
			setRandomLimits(0.501f, 1.101f);
			X1 = generateRandomFloat();
			setRealLimits(-1.1f, -0.5f);
			setRandomLimits(-1.101f, -0.501f);
			X2 = generateRandomFloat();
			trainingSetWriter.println(X1 + ", " + X2);
		}
		// examples in [0.5,1.1] x [0.5,1.1]
		for(int i = 0; i < 100; i++) {
			setRealLimits(0.5f, 1.1f);
			setRandomLimits(0.501f, 1.101f);
			X1 = generateRandomFloat();
			X2 = generateRandomFloat();
			trainingSetWriter.println(X1 + ", " + X2);
		}
		// examples in [-1,1]x[-1,1]
		for(int i = 0; i < 100; i++) {
			setRealLimits(-1.1f, 1.1f);
			setRandomLimits(-1.101f, 1.101f);
			X1 = generateRandomFloat();
			X2 = generateRandomFloat();
			if(i == 99) {
				trainingSetWriter.print(X1 + ", " + X2);
			}else {
				trainingSetWriter.println(X1 + ", " + X2);
			}			
		}
		trainingSetWriter.close();
	}
	
	public float generateRandomFloat(){
		float x = random_min + (random_max - random_min) * random.nextFloat();
		if(x <= real_min) {
			return real_min;
		}else if(x >= real_max) {
			return real_max;
		}
		return x;
	}
}
