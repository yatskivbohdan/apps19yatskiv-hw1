package ua.edu.ucu.tempseries;

import java.util.InputMismatchException;

public class TemperatureSeriesAnalysis {

    private static final int DEFAULT_SIZE = 30;
    private static final double MIN_TEMPERATURE = -273.0;
    private double[] series;
    private int capacity;
    private int size;



    public TemperatureSeriesAnalysis() {
        this.series = new double[DEFAULT_SIZE];
        this.capacity = DEFAULT_SIZE;
        this.size = 0;
    }

    public TemperatureSeriesAnalysis(double[] temperatureSeries) {
        for (double temperature:temperatureSeries) {
            if (temperature < MIN_TEMPERATURE) {
                throw new InputMismatchException("There is a temperature "
                        + "lower than the minimum possible");
            }
        }
        this.capacity = temperatureSeries.length;
        this.size = temperatureSeries.length;
        this.series = new double[this.capacity];
        System.arraycopy(temperatureSeries, 0, this.series, 0, this.size);
    }

    public void checkLength() {
        if (this.size == 0) {
            throw new IllegalArgumentException("The series is empty");
        }
    }

    public double average() {
        checkLength();
        double sum = 0.0;
        for (double temperature:this.series) {
            sum += temperature;
        }
        return sum/this.series.length;
    }

    public double deviation() {
        checkLength();
        double devSum = 0.0;
        double avg = this.average();
        for (double temperature:this.series) {
            devSum += (temperature-avg)*(temperature-avg);
        }
        return Math.sqrt(devSum / this.size);
    }

    public double min() {
        checkLength();
        double min = this.series[0];
        for (double temperature:this.series) {
            if (temperature < min) {
                min = temperature;
            }
        }
        return min;
    }

    public double max() {
        checkLength();
        double max = this.series[0];
        for (double temperature:this.series) {
            if (temperature > max) {
                max = temperature;
            }
        }
        return max;
    }

    public double findTempClosestToZero() {
        return this.findTempClosestToValue(0.0);

    }

    public double findTempClosestToValue(double tempValue) {
        checkLength();
        double closestDist = Math.abs(this.series[0]-tempValue);
        double closestEl = this.series[0];
        for (double temperature:this.series) {
            if (Math.abs(temperature-tempValue) < closestDist) {
                closestEl = temperature;
                closestDist = Math.abs(temperature-tempValue);
            }
            else if (Math.abs(Math.abs(temperature-tempValue)-closestDist)
                    < Double.MIN_VALUE && temperature > 0.0) {
                closestEl = temperature;

            }
        }
        return closestEl;
    }

    public double[] findTempsLessThen(double tempValue) {
        checkLength();
        int arrSize = 0;
        for (double temperature:this.series) {
            if (temperature < tempValue) {
                arrSize++;
            }
        }
        double[] newArr = new double[arrSize];
        int i = 0;
        for (double temperature:this.series) {
            if (temperature < tempValue) {
                newArr[i] = temperature;
                i++;
            }
        }
        return newArr;
    }

    public double[] findTempsGreaterThen(double tempValue) {
        checkLength();
        int arrSize = 0;
        for (double temperature:this.series) {
            if (temperature > tempValue) {
                arrSize++;
            }
        }
        double[] newArr = new double[arrSize];
        int i = 0;
        for (double temperature:this.series) {
            if (temperature > tempValue) {
                newArr[i] = temperature;
                i++;
            }
        }
        return newArr;
    }


    public TempSummaryStatistics summaryStatistics() {
        checkLength();
        double avg = this.average();
        double dev = this.deviation();
        double min = this.min();
        double max = this.max();
        return new TempSummaryStatistics(avg, dev, min, max);
    }

    public int addTemps(double... temps) {

        while (this.capacity < this.size + temps.length) {
            this.capacity *= 2;
            if (this.capacity == 0) {
                this.capacity = 1;
            }

        }
        double[] newSeries = new double[this.capacity];
        if (this.size > 0) {
            System.arraycopy(this.series, 0, newSeries, 0, this.size);
        }
        this.series = newSeries;

        int i = 0;
        for (int j = this.size; j < this.size+temps.length; j++) {
            this.series[j] = temps[i];
            i++;
        }
        this.size += temps.length;
        return this.size;
    }
}
