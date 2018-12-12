# AnomalyDetection
1. Add .jar files to the Build Path from “dependencies” folder.
2. Run src/controller/Experiment.java class file. 
3. In the folder Experiments you will get the curves of Decision Values and ROC. 

Note that Experiment.java produces the curves by default for 
N = 2, 3, 4, 5 and store the results in Experiments folder’s TEST_N=2, TEST_N=3, TEST_N=4, TEST_N=5 subfolders accordingly. 

To change the default hyper parameters you can find them in the src/controller/Experiment.java file:
a. lengths (lines 30-32).
b. e1 (lines 34-36).
c. e2 = weights2 (lines. 37-39)
d. windowSize (line 42)
e. eta (line 46)
f. N = 2, 3, 4, 5 (line 59)
In addition, you can write the hyper parameters to “Experiments/hyperparameters.rtf” file.

To modify the choice of the training or detection data:
a = trainingId (line 96)
True positive data index = id2 (line 135) 

Good luck!
