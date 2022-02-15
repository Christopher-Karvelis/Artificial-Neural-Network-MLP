# Artificial-Neural-Networks
This is a semester project for the subject "Articial Neural Networks", at the Department of Computer Science & Engineering, University of Ioannina.

## Purpose
The main task of this project was to create and train a multi-layer perceptron (MLP) using Gradient Descent and back-propagation from scratch. 
We also had to implement the K-MEANS and LVQ algorithms for clustering input data.

## Datasets
### S1
The first dataset used to train the MLP is located at Artificial-Neural-Networks/S1-Training-Set.txt

We have a problem of classifying three categories.
The dataset was created by randomly creating 6000 points ((x1, x2) in the plain) inside the square [-2, 2] x [-2, 2] (3000 for the training set and 3000 for the control set). Then the points(x1, x2) are classified into three categories as follows:

1. category C2: if (x1 - 1)^2+ (x2 - 1)^2 <= 0.49 or  (x1 + 1)^2+ (x2 + 1)^2 <= 0.49,
2. category C3: if (x1 + 1)^2+ (x2 - 1)^2 <= 0.49 or  (x1 - 1)^2+ (x2 + 1)^2 <= 0.49,
5. category C1: in any other case.

Noise was added only to the training set as follows: for each point of the training set that belongs to category C2 or C3, with a probability of 0.1 change the category to C1.

### S2
The second dataset used as input for the K-MEANS and LVQ algorithms is located at Artificial-Neural-Networks/S2-Training-Set.txt
The dataset was created in the same way S1 did but without classifying the points.

## MLP Algorithm (Define the architecture of MLP)
1. Created a multi-layer perceptron(MLP) with 2 hidden layers. The neurons of the first hidden layer have the **logistic(σ(u))** activation function, while the neurons of the second hidden layer have the **hyperbolic(tanh(u))** or **linear** activation function. Output neurons have the logistic activation function.
2. Implemented the gradient descent algorithm and updating the weights per batch of L examples(mini-batches). The network supports (L = 1) online and (l = N) batch update of the weights. 
3. Load the training and the test set and start training.
5. At the end of each epoch calculate and print the value of the total square training error.

## How to use the MLP
User should define the following: 
    - the number of input neurons: for the S1 dataset the number of input neurons should be equal to two, one for each coordinate x1 and x2. 
    - the number of categories: for the S1 dataset the number of categories is equal to three(C1, C2,C3), 
    - the number of neurons of the first hidden layer, 
    - the number of neurons of the second hidden layer and 
    - the type of activation function in the second hidden layer.

## How to Use the K-means and LVQ Algorithms
