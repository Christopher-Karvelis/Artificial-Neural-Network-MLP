# Artificial-Neural-Network
This is a semester project for the subject "Articial Neural Networks", at the Department of Computer Science & Engineering, University of Ioannina.

## Purpose
The main task of this project is to create and train a multi-layer perceptron (MLP) using Gradient Descent and back-propagation from scratch. 

## Dataset
We have a problem of classifying three categories. I randomly create 6000 examples ( 6000 points (x1, x2) in the level) inside the square [-2, 2] x [-2, 2] (3000 for the training set and 3000 for the control set). Then each example (x1, x2) (out of 6000 examples) is classified into a category of three categories as follows:

1. if (x1 - 1)^2+ (x2 - 1)^2 <= 0.49, then (x1, x2) is classified in category C2,
2. if (x1 + 1)^2+ (x2 + 1)^2 <= 0.49, then (x1, x2) is classified in category C2,
3. if (x1 + 1)^2+ (x2 - 1)^2 <= 0.49, then (x1, x2) is classified in category C3,
4. if (x1 - 1)^2+ (x2 + 1)^2 <= 0.49, then (x1, x2) is classified in category C3,
5. otherwise classified in category C1.

Noise was added only to the training set as follows: for each example of the training set that belongs to category C2 or C3, with a probability of 0.1 we change the category and assign it to category C1.

## Algorithm
1. Create a multi-layer perceptron (MLP) with 2 hidden layers. The neurons of the first hidden layer have the logistic activation function (σ (u)), while the neurons of the second hidden layer have the hyperbolic (tanh (u)) or linear activation function. Output neurons have the logistic activation function.
2. Define the number of input neurons, the number of categories, the number of neurons of the first hidden layer, the number of neurons of the second hidden layer and the type of activation function in the second hidden layer.
3. Load the training and the test set.
4. I define the architecture of MLP.
5. Implement of the gradient descent algorithm and updating the weights per batch of L examples (mini-batches). If L = 1 we have online update, while if L = N we have batch update. At the end of each epoch calculate the value of the total square training error.
