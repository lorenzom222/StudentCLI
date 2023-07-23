import java.util.Scanner;

/**
 * Airplane Delivery Service
 *
 * Author: [Robert Zhan]
 * School Email: [bzhan@u.rochester.edu]
 * Last Modified: [March 2, 2023]
 *
 * This program reads in a fleet description and contract listing from standard input, and outputs
 * the best option for each contract as well as the total profit accumulated over all contracts.
 *
 * Technical comments have been included where appropriate to explain complex expressions and high-level
 * control flow.
 */

public class Airplane {
    // Instance variables
    private String name;
    private double towMin;
    private double towMax;
    private double range;
    private double speed;
    private double hourlyCost;
    private double fuelRate;

    // Constructors
    public Airplane(String name, double towMin, double towMax, double range, double speed, double hourlyCost, double fuelRate) {
        this.name = name;
        this.towMin = towMin;
        this.towMax = towMax;
        this.range = range;
        this.speed = speed;
        this.hourlyCost = hourlyCost;
        this.fuelRate = fuelRate;
    }

    // Getters
    public String getName() {
        return name;
    }

    public double getTowMin() {
        return towMin;
    }

    public double getTowMax() {
        return towMax;
    }

    public double getRange() {
        return range;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHourlyCost() {return hourlyCost;}

    public double getFuelRate() {
        return fuelRate;
    }

    // Methods
    public double calculateCost(double cargoMass, double distance) {
        // Calculate flight time in hours
        double flightTime = distance / getSpeed();

        // Calculate fuel cost
        double fuelCost = getFuelRate() * (getTowMin() + cargoMass) * flightTime;

        // Calculate flight time cost
        double flightTimeCost = flightTime * getHourlyCost();

        // Calculate total delivery cost
        return fuelCost + flightTimeCost;
    }

    public static String findBestOption(double contractMass, double contractDistance, double contractPayment, Airplane[] fleet) {
        String bestOption = null;
        double bestProfit = 0.0;

        // loop through each airplane in the fleet
        for (Airplane plane : fleet) {
            //System.out.println(plane.getName()+"range is"+plane.getRange()+"contract distance is"+contractDistance);
            if (plane.getRange() <= contractDistance||(plane.getTowMax()-plane.getTowMin())<contractMass) {
                continue;
            }
            // calculate the delivery cost for this airplane and contract
            double deliveryCost = plane.calculateCost(contractMass, contractDistance);

            // calculate the revenue for this contract
            double revenue = contractPayment - deliveryCost;

            // check if this airplane is the most profitable so far
            if (revenue > 0 && revenue > bestProfit) {
                bestOption = plane.getName();
                bestProfit = revenue;
            }
        }

        // if no profitable option exists, return "decline"
        if (bestOption==null) {
            return "decline";
        } else {
            // otherwise, return the best option and profit, formatted to two decimal places
            return bestOption + " " + bestProfit;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // read in the fleet description
        int numAirplanes = scanner.nextInt();
        Airplane[] fleet = new Airplane[numAirplanes];
        for (int i = 0; i < numAirplanes; i++) {
            String name = scanner.next();
            double towMin = scanner.nextDouble();
            double towMax = scanner.nextDouble();
            double range = scanner.nextDouble();
            double speed = scanner.nextDouble();
            double hourlyCost = scanner.nextDouble();
            double fuelRate = scanner.nextDouble();
            fleet[i] = new Airplane(name, towMin, towMax, range, speed, hourlyCost, fuelRate);
        }

        double totalProfit = 0.0;
        String[] results = new String[1000];
        int index=0;
        // read in the contract listing and find the best option for each contract
        while (scanner.hasNext() && !scanner.hasNext("quit")) {
            double contractMass = scanner.nextDouble();
            double contractDistance = scanner.nextDouble();
            double contractPayment = scanner.nextDouble();

            String bestOption = findBestOption(contractMass, contractDistance, contractPayment, fleet);

            if (!bestOption.equals("decline")) {
                //accurate to two decimal places
                results[index++] = bestOption.split(" ")[0] + " " + String.format("%.2f",Double.parseDouble(bestOption.split(" ")[1]));
            }else{
                results[index++] = bestOption;
            }

            if (!bestOption.equals("decline")) {
                //The sum should be calculated using data that has not been rounded to two decimal places.
                double profit = Double.parseDouble(bestOption.split(" ")[1]);
                totalProfit += profit;
            }
        }

        // print the results
        for (int i = 0; i < index; i++) {
            System.out.println(results[i]);
        }

        // print the total profit accumulated over all contracts
        System.out.println("Profit: " + String.format("%.2f", totalProfit));
    }


}