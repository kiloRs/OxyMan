package com.thebetterchoice.oxyman;

import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomizedGroup<E> {
    private final NavigableMap<Double, E> map;
    private final Random random;
    private double total;

    public RandomizedGroup() {
        this(new Random());
    }

    public RandomizedGroup(Random random) {
        this.map = new TreeMap<>();
        this.total = 0.0D;
        this.random = random;
    }

    public TreeMap<Double,E> get(){
        return (TreeMap<Double, E>) map;
    }
    public void add(double weight, E result) {
        if (weight > 0.0D) {
            this.total += weight;
            this.map.put(this.total, result);
        }

    }
    public boolean canLaunch(int maxWeight, double weight, E res){
        if (maxWeight == -1){
            return true;
        }
        if (this.total + weight> maxWeight){
            return false;
        }
        return !map.isEmpty();
    }

    public E next() {
        double value = this.random.nextDouble() * this.total;
        Map.Entry<Double, E> temp = this.map.ceilingEntry(value);
        return temp != null ? temp.getValue() : null;
    }
}