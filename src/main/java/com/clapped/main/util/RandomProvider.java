package com.clapped.main.util;

public interface RandomProvider {
    boolean getChanceOutcome(int successChance);
    int getRandInt(int bound);
    int getRandInt(int lower, int upper);
    double getRandDouble(double lower, double upper);
}
