# Artificial-Neural-Networks
This is a semester project for the subject "Articial Neural Networks", at the Department of Computer Science & Engineering, University of Ioannina.

## Purpose
The main task of this project was to create and train a multi-layer perceptron (MLP) using Gradient Descent and back-propagation from scratch. 
We also had to implement the K-MEANS and LVQ algorithms for clustering input data.

## Datasets
### S1
The first dataset used to train the MLP is located at Artificial-Neural-Networks/S1-Training-Set.txt

We have a problem of classifying three categories.
The dataset was created by randomly creating 6000 points ((x1, x2) in the plain) inside the square [-2, 2] x [-2, 2] (3000 for the training set and 3000 for the control set).
</br>
Preview of S1-Training-Set:
```
   -0.11006832, -1.5221529,   C1
    1.606715,   -1.9961953,   C1
   -0.6835481,   1.6608286,   C1
    1.0009558,   1.6722264,   C2
    1.4368737,  -1.5106521,   C3
    1.4114754,   0.64198136,  C2
   -1.86043,    -0.042966723, C1
   -1.3685632,   1.7563276,   C1
   -1.3290948,  -1.7714477,   C1
    1.5912795,   0.9927237,   C2
```

The points(x1, x2) are classified into three categories as follows:

1. category C2: if (x1 - 1)^2+ (x2 - 1)^2 <= 0.49 or  (x1 + 1)^2+ (x2 + 1)^2 <= 0.49,
2. category C3: if (x1 + 1)^2+ (x2 - 1)^2 <= 0.49 or  (x1 - 1)^2+ (x2 + 1)^2 <= 0.49,
5. category C1: in any other case.

Noise was added only to the training set as follows: for each point of the training set that belongs to category C2 or C3, with a probability of 0.1 change the category to C1.

### S2
The second dataset used as input for the K-MEANS and LVQ algorithms is located at Artificial-Neural-Networks/S2-Training-Set.txt
The dataset was created in the same way S1 did but without classifying the points.
</br>
Preview of S2-Training-Set:

       0.13456714, -0.077114135
      -0.21472594, -0.08527754
       0.15591341,  0.069631815
       0.014670819, 0.06411773
      -0.107621655, 0.22532195
       0.26412588, -0.116445765
      -0.22791314, -0.044834018
      -0.07124223, -0.28215548
       0.2712875,  -0.11488855
      -0.0253959,  -0.18017158
       0.20766318, -0.064438686

## MLP Algorithm (Define the architecture of MLP)
1. Created a multi-layer perceptron(MLP) with 2 hidden layers. The neurons of the first hidden layer have the **logistic(σ(u))** activation function, while the neurons of the second hidden layer have the **hyperbolic(tanh(u))** or **linear** activation function. Output neurons have the logistic activation function.
2. Implemented the gradient descent algorithm and updating the weights per batch of L points(mini-batches). The network supports online(L = 1) and batch(L = N) update of the weights. 
3. Load the training and the test set and start training.
5. At the end of each epoch calculate and print the value of the total square training error.

## How to use the MLP

User should define the following:

 - the number of input neurons: for the S1 dataset the number of input neurons should be equal to two, one for each coordinate x1 and x2. 
 - the number of categories: for the S1 dataset the number of categories is equal to three(C1, C2,C3), 
 - the number of neurons of the first hidden layer, 
 - the number of neurons of the second hidden layer and 
 - the type of activation function in the second hidden layer.

### Example 
      "C:\Program Files\Java\jdk-17.0.1\bin\java.exe" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.3.2\lib\idea_rt.jar=52938:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2021.3.2\bin" -Dfile.encoding=UTF-8 -classpath C:\Users\chris\Desktop\Artificial-Neural-Networks\bin;C:\Users\chris\Desktop\Artificial-Neural-Networks\JFreeChart\jcommon-1.0.8.jar;C:\Users\chris\Desktop\Artificial-Neural-Networks\JFreeChart\jfreechart-1.5.0.jar application

      -> Select one from the following options by typing :

      (1) For MLP program
      (2) For k-means program
      (3) For LVQ program
      (x) To exit

      -> 1

      -> Please select one from the following by typing:

      (1) For standard execution
      (2) For multiple execution training

      -> 1
      To define the architecture of the MLP use the command:

      -> "define -d -K -H1 -H2 -f"
          -d number of inputs
          -K number of categories
          -H1 number of neurons in first hidden level 
          -H2 number of neurons in the second hidden  level
          -f activation function for the second hidden level, type t(tahn) or l(linear)
      -> define -2 -3 -20 -15 -t

      ------------EPOCH 1------------
      Total Squared Error = 1013.0073

      ------------EPOCH 2------------
      Total Squared Error = 777.89185

      ------------EPOCH 3------------
      Total Squared Error = 760.41986
      
      ...
      
      ------------EPOCH 872------------
      Total Squared Error = 206.8943

      ------------EPOCH 873------------
      Total Squared Error = 206.79463

      Generalization Capability Of MLP = 96.73333%
      
![MLP_catergorization](https://user-images.githubusercontent.com/25777650/154112836-c9b1e05d-18e2-4e7e-bbed-74e6d1ccaf8e.png)
