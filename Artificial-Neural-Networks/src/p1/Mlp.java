package p1;

public class Mlp {
	private Neuron[] inputLayer;
	private Neuron[] hiddenLayer1;
	private Neuron[] hiddenLayer2;
	private Neuron[] outputLayer;
	private int numberOfLayers;
	private int numberOfCategories;
	private int correctGuesses;
	private int totalGuesses;
	private float totalSquaredError;
	public static int INPUTLAYER = 0;
	public static int HIDDENLAYER1 = 1;
	public static int HIDDENLAYER2 = 2;
	public static int OUTPUTLAYER = 3;
	public static float LEARNING_RATE = 0.001f;

		
	public Mlp(int dimensions, int categories, int hiddenLayer1Size, int hiddenLayer2Size, String activationFunction) {
		numberOfLayers = 4;
		numberOfCategories = categories;
		totalSquaredError = 0;
		totalGuesses = 0;
		correctGuesses = 0;
		inputLayer = new Neuron[dimensions];
		for(int i = 0; i < dimensions; i++) {
			inputLayer[i] = new Neuron("none");
		}
		outputLayer = new Neuron[categories];
		for(int i = 0; i < categories; i++) {
			outputLayer[i] = new Neuron("logistic");
			outputLayer[i].setRandomInputWeights(hiddenLayer2Size + 1);
		}
		hiddenLayer1 = new Neuron[hiddenLayer1Size];
		for(int i = 0; i < hiddenLayer1Size; i++) {
			hiddenLayer1[i] = new Neuron("logistic");
			hiddenLayer1[i].setRandomInputWeights(dimensions + 1);
		}
		hiddenLayer2 = new Neuron[hiddenLayer2Size];
		for(int i = 0; i < hiddenLayer2Size; i++) {
			hiddenLayer2[i] = new Neuron(activationFunction);
			hiddenLayer2[i].setRandomInputWeights(hiddenLayer1Size + 1);
		}
	}
	
	public float calculateSumOfInput(Neuron[] previousLayer, Neuron currentNeuron) {
		float sum = 0;
		int i;
		for(i = 0; i < previousLayer.length; i++) {
			sum += previousLayer[i].getOutput() * currentNeuron.getInputWeights()[i];
		}
		sum +=  currentNeuron.getInputWeights()[i];
		return sum;
	}
	
	public float[] forwardPass(float[] x) {
		//System.out.println("Forward Passing");
		for(int i = 0; i < numberOfLayers; i++) {
			if(i == INPUTLAYER) {
				for(int j = 0; j < inputLayer.length; j++) {
					inputLayer[j].setOutput(x[j]);
				}
			}else if(i == HIDDENLAYER1) {
				for(int j = 0; j < hiddenLayer1.length; j++) {
					float sum = calculateSumOfInput(inputLayer, hiddenLayer1[j]);
					float y = hiddenLayer1[j].activateFunction(sum);
					hiddenLayer1[j].setOutput(y);
				}
			}else if(i == HIDDENLAYER2) {
				for(int j = 0; j < hiddenLayer2.length; j++) {
					float sum = calculateSumOfInput(hiddenLayer1, hiddenLayer2[j]);
					float y = hiddenLayer2[j].activateFunction(sum);
					hiddenLayer2[j].setOutput(y);
				}
			}else if(i == OUTPUTLAYER) {
				for(int j = 0; j < outputLayer.length; j++) {
					float sum = calculateSumOfInput(hiddenLayer2, outputLayer[j]);
					float y = outputLayer[j].activateFunction(sum);
					outputLayer[j].setOutput(y);
				}
			}
		}
		float[] y = new float[numberOfCategories];
		for(int i = 0; i < numberOfCategories; i++) {
			y[i] = outputLayer[i].getOutput();
		}
		return y;
	}
	
	public void backprop(float[] x, float[] t) {
		//System.out.println("Backpropagation");
		for(int i = numberOfLayers-1; i > 0; i--) {
			if(i == OUTPUTLAYER) {					
				for(int j = 0; j < outputLayer.length; j++) {
					float output = outputLayer[j].getOutput();
					float deltaError = outputLayer[j].activateFunctionDerivative(output) * (output - t[j]);
					float[] semiDerivativesOfInputWeights = new float[outputLayer[j].getInputWeights().length];
					int w;
					for(w = 0; w < hiddenLayer2.length; w++) {
						semiDerivativesOfInputWeights[w] = hiddenLayer2[w].getOutput() * deltaError;
					}
					semiDerivativesOfInputWeights[w] = deltaError;
					outputLayer[j].setDeltaError(deltaError);
					outputLayer[j].setSemiDerivativesOfInputWeights(semiDerivativesOfInputWeights);					
				}
				updateTotalSquaredError(t);
			}else if(i == HIDDENLAYER2) {
				for(int j = 0; j < hiddenLayer2.length; j++) {
					float output = hiddenLayer2[j].getOutput();
					float sum = 0;
					float deltaError = hiddenLayer2[j].activateFunctionDerivative(output);
					for(int k = 0; k < outputLayer.length; k++) {
						sum += (outputLayer[k].getInputWeights()[j])*outputLayer[k].getDeltaError();
					}
					deltaError = deltaError * sum;
					float[] semiDerivativesOfInputWeights = new float[hiddenLayer2[j].getInputWeights().length];
					int w;
					for(w = 0; w < hiddenLayer1.length; w++) {
						semiDerivativesOfInputWeights[w] = hiddenLayer1[w].getOutput() * deltaError;
					}
					semiDerivativesOfInputWeights[w] = deltaError;
					hiddenLayer2[j].setDeltaError(deltaError); 
					hiddenLayer2[j].setSemiDerivativesOfInputWeights(semiDerivativesOfInputWeights);
				}
			}else if(i == HIDDENLAYER1) {
				for(int j = 0; j < hiddenLayer1.length; j++) {
					float output = hiddenLayer1[j].getOutput();
					float sum = 0;
					float deltaError;
					deltaError = hiddenLayer1[j].activateFunctionDerivative(output);
					for(int k = 0; k < hiddenLayer2.length; k++) {
						sum += hiddenLayer2[k].getInputWeights()[j]*hiddenLayer2[k].getDeltaError();
					}
					deltaError = deltaError * sum;
					float[] semiDerivativesOfInputWeights = new float[hiddenLayer1[j].getInputWeights().length];
					int w;
					for(w = 0; w < inputLayer.length; w++) {
						semiDerivativesOfInputWeights[w] = inputLayer[w].getOutput() * deltaError;
					}
					semiDerivativesOfInputWeights[w] = deltaError;
					hiddenLayer1[j].setDeltaError(deltaError);
					hiddenLayer1[j].setSemiDerivativesOfInputWeights(semiDerivativesOfInputWeights);
				}
			}
		}
	}
	
	public void setTotalSquaredError(float totalSquaredError) {
		this.totalSquaredError = totalSquaredError;
	}
	
	public void updateTotalSquaredError(float[] t) {
		float sum = 0;
		for(int i = 0; i < outputLayer.length; i++) {
			 float output = outputLayer[i].getOutput();
			 sum +=  Math.pow((output - t[i]), 2);
		}
		sum = sum/(float)2;
		totalSquaredError += sum;
	}

	public int clculateGeneralizationForExample(float[] t) {
		int i;
		int success;
		float out[] = new float[outputLayer.length];
		for(i = 0; i < outputLayer.length; i++) {
			out[i] = outputLayer[i].getOutput();
		}
		float max = -1;
		int pointerOfMax = 0;
		for(i = 0; i < t.length; i++) {
			if(out[i] > max) {
				max = out[i];
				pointerOfMax = i;
			}
		}
		if(t[pointerOfMax] == 1) {
			correctGuesses++;
			success = 1;
		}else {
			success = 0;
		}
		totalGuesses++;
		return success;
	}
	
	public void updateWeights() {
		for(int i = 1; i< numberOfLayers; i++) {
			if(i == HIDDENLAYER1) {
				for(int j = 0; j < hiddenLayer1.length; j++) {
					hiddenLayer1[j].updateInputWeights(LEARNING_RATE);
					hiddenLayer1[j].resetSemiDerivativesOfInputWeights();
				}
			}else if(i == HIDDENLAYER2) {
				for(int j = 0; j < hiddenLayer2.length; j++) {
					hiddenLayer2[j].updateInputWeights(LEARNING_RATE);
					hiddenLayer2[j].resetSemiDerivativesOfInputWeights();
				}
			}else if(i == OUTPUTLAYER) {
				for(int j = 0; j < outputLayer.length; j++) {
					outputLayer[j].updateInputWeights(LEARNING_RATE);
					outputLayer[j].resetSemiDerivativesOfInputWeights();
				}
			}
		}
	}

	public int getNumberOfCategories() {
		return numberOfCategories;
	}
	
	public float getTotalSquaredError() {
		return totalSquaredError;
	}

	public float getGeneralizationCapability() {
		return correctGuesses / (float)totalGuesses;
	}
}
