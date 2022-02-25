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


import time
import warnings
import numpy as np
from keras.layers.core import Dense, Activation, Dropout
from keras.layers.recurrent import LSTM
from keras.models import Sequential
import matplotlib.pyplot as plt


def load_data(data_sheet, seq_len):
    # number of features = no of columns of the .csv file
    number_of_features = len(data_sheet.columns)
    # get the data in form of matrix from the .csv file
    data = data_sheet.as_matrix()
    # print("*****DATA******")
    # print(data)
    sequence_length = seq_len + 1
    result = []
    for index in range(len(data) - sequence_length):
        result.append(data[index: index + sequence_length])

    result = np.array(result)
    row = round(0.9 * result.shape[0])
    train = result[:int(row), :]
    x_train = train[:, :-1]
    y_train = train[:, -1][:,-1]
    x_test = result[int(row):, :-1]
    y_test = result[int(row):, -1][:,-1]

    x_train = np.reshape(x_train, (x_train.shape[0], x_train.shape[1], number_of_features ))
    x_test = np.reshape(x_test, (x_test.shape[0], x_test.shape[1], number_of_features ))
    return [x_train, y_train, x_test, y_test]


def build_model(batch_size, output_dim):
        # as the first layer in a Sequential model
        model = Sequential()

        # 160 ==>  model.output_shape(batch_size, units)
        # to stack recurrent layers, you must use return_sequences=True
        # on any recurrent layer that feeds into another recurrent layer.
        # note that you only need to specify the input size on the first layer.
        model.add(LSTM(160, input_shape=(batch_size, output_dim), return_sequences=True))
        model.add(Dropout(0.2))
        model.add(LSTM(80, input_shape=(batch_size, output_dim), return_sequences=False))
        model.add(Dropout(0.2))
        # adding linear dense layer to aggregate the date from predcittion
        model.add(Dense(1,init='uniform',activation='linear'))
        # compile our model using loss function = mean sqaured error and adam optimizer
        model.compile(loss='mse',optimizer='adam',metrics=['accuracy'])
        return model


def plot_model(predict_x, y_test):
    plt.plot(predict_x,color='red', label='Prediction')
    plt.plot(y_test,color='green', label='True data')
    plt.legend()
    plt.show()
