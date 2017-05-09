# Author:   Divya Agarwal
# Date:     16th Dec 2017
# Title:    Stock Price Value Prediction from Yahoo Historical using Machine Learning
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
# Modules required:
# pandas:
# pip install pandas
# tensorlflow
# keras :
# pip install keras
#


import load_data as ld
import model_configure as mc
import pandas as pd
import math
import matplotlib.pyplot as plt
from keras.models import Sequential
from keras.layers.recurrent import LSTM
from keras.layers.core import Dense, Activation, Dropout
import time

# name of the comapny who's stocks you want to predict
name = 'AAPL'
df = ld.fetch_data(name)

file_name = name+'_2017_data.csv'
# covert the data frame to csv format
df.to_csv(file_name)

#normalize
df['Low'] = df['Low'] / 100
df['High'] = df['High'] / 100
df['Open'] = df['Open'] / 100
df['Close'] = df['Close'] / 100

# set the length of sequaence to 20
batch_size = 20
x_train, y_train, x_test, y_test = mc.load_data(df[::-1],  batch_size)
print("X_train", x_train.shape)
print("y_train", y_train.shape)
print("x_test", x_test.shape)
print("y_test", y_test.shape)

# call the custom build function to build our model
# pass batch_size and output_dim as parameters
output_dim = 4
model = mc.build_model(batch_size, output_dim)

# Train the model
model.fit( x_train, y_train, batch_size=450, nb_epoch=7, validation_split=0.15, shuffle=True, verbose=1)

# Evaluate your model
train_score = model.evaluate(x_train, y_train, verbose=0)
print('Training Score: %f ' % train_score[0])

test_score = model.evaluate(x_test, y_test, verbose=0)
print('Testing Score: %f ' % test_score[0])

# predicted values of x,y
predict_x = model.predict(x_test)
for i in range(len(y_test)):
    predict_y = predict_x[i][0]

#plotting the graph for predictions vs actual
mc.plot_model(predict_x, y_test)
