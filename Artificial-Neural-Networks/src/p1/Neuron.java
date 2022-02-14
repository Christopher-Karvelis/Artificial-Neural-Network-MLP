package p1;

import java.util.Random;

public class Neuron {
	private float[] inputWeights;
	private float[] semiDerivativesOfInputWeights;
	private float output;
	private String activationFunction;
	private float deltaError;
	
	
	public Neuron(String activationFunction) {
		this.activationFunction = activationFunction;
	}
	
	public void setRandomInputWeights(int numberOfWeights) {
		inputWeights = new float[numberOfWeights];
		for(int i = 0; i < numberOfWeights; i++) {
			Random random = new Random();
			float random_min = -1.01f;
			float random_max = 1.01f;
			float w = random_min + (random_max - random_min) * random.nextFloat();
			float real_min = -1.0f;
			float real_max = 1.0f;
			if(w <= real_min) {
				w = real_min;
			}else if(w >= real_max) {
				w = real_max;
			}
			inputWeights[i] = w ;
		}
		semiDerivativesOfInputWeights = new float[numberOfWeights];
	}
	
	public void setOutput(float output){
		this.output = output;
	}
	
	public void resetSemiDerivativesOfInputWeights() {
		semiDerivativesOfInputWeights = new float[inputWeights.length];
	}
	
	public float activateFunction(float x) {
		if(activationFunction.equals("t")) {
			return (float) Math.tanh(x);
		}else if(activationFunction.equals("l")){
			return x;
		}else{
			return (float) (((float)1)/( 1 + Math.pow(Math.E,(-1*x))));
		}
	}
	
	public float activateFunctionDerivative(float x) {
		if(activationFunction.equals("t")) {
			return (float) (((float) 1)/Math.pow(Math.cosh(x), 2));
		}else if(activationFunction.equals("l")){
			return 1;
		}else{
			return x*(1-x);
		}
	}
	
	public void setDeltaError(float deltaError) {
		this.deltaError = deltaError;
	}
	
	public float getOutput() {
		return output;
	}

	public float[] getInputWeights() {
		return inputWeights;
	}

	public float getDeltaError() {
		return deltaError;
	}

	public void setSemiDerivativesOfInputWeights(float[] semiDerivativesOfInputWeights) {
		for(int i = 0; i < semiDerivativesOfInputWeights.length; i++) {
			this.semiDerivativesOfInputWeights[i] += semiDerivativesOfInputWeights[i];
		}
	}

	public void updateInputWeights(float LEARNING_RATE) {
		for(int i = 0; i < inputWeights.length; i++) {
			//System.out.println("weight = " + inputWeights[i]);
			inputWeights[i] = inputWeights[i] - (LEARNING_RATE*semiDerivativesOfInputWeights[i]);
		}
	}
}
