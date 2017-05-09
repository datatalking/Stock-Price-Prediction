import numpy as np
import pandas as pd
from pandas import datetime
import math, time
import datetime as dt

def  fetch_data(name):
    # get the current system date : yy-mm-dd
    today = dt.date.today()
    #date_split = today.str.split('-').st
    #year
    year=  today.year
    #month of the year
    month=  today.month
    #day of the year
    day=  today.day - 1
    # url for yahoo finance csv data of the comany = name and end-date = today , start-date 01-01-2000
    url = 'http://chart.finance.yahoo.com/table.csv?s=%s&a=01&b=01&c=1990&d=%d&e=%d&f=%d&g=d&ignore=.csv' % (name,day,month,year)
    col_names = ['Date','Open','High','Low','Close','Volume','Adj Close']
    # reead data from the csv file and load it into the DataFrame
    df = pd.DataFrame(pd.read_csv(url, header=0, names=col_names))
    #split the date based on year month and day
    df.drop(['Volume','Adj Close','Date'], axis=1, inplace=True)
    return df
