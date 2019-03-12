# AnomalyDetection
1. Add .jar files to the Build Path from “dependencies” folder.
2. Run src/controller/Experiment.java class file. 
3. In the folder Experiments you will get the curves of Decision Values and ROC. 

Note that Experiment.java produces the curves with default hyper parameters for 
N = 2, 3, 4, 5 and store the results in Experiments folder’s TEST_N=2, TEST_N=3, TEST_N=4, TEST_N=5 folders accordingly. 

To change the default hyper parameters you can find them in the src/controller/Experiment.java file:
a. W = 5 (line 33)
b. lengths (lines 34-36).
c. e1 = weights (lines 38-40).
d. e2 = weights2 (lines. 41-43)
e. windowSize (line 46)
f. eta (line 49)
g. N = 2, 3, 4, 5 (line 72)
The hyper parameters will be stored in “Experiments/hyperparameters.txt” file.

To modify the choice of the training or detection data:
trainingId = a (line 77)
detectionId  = id2 (line 122) 

Good luck!
