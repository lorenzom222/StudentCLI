/*
Tianyou Tu
ttu6@u.edu.rochester
23/3/2
 */

import java.util.Scanner;

public class Airplane {
    private String name;
    private double TOW_min;
    private double TOW_max;
    private double range;
    private double speed;
    private double hourly_cost;
    private double fuel_rate;

    public Airplane(String name, double TOW_min, double TOW_max, double range, double speed, double hourly_cost, double fuel_rate) {
        this.name = name;
        this.TOW_min = TOW_min;
        this.TOW_max = TOW_max;
        this.range = range;
        this.speed = speed;
        this.hourly_cost = hourly_cost;
        this.fuel_rate = fuel_rate;
    }

    public String getName() {
        return name;
    }

    public double getTOW_min() {
        return TOW_min;
    }

    public double getTOW_max() {
        return TOW_max;
    }

    public double getRange() {
        return range;
    }

    public double getSpeed() {
        return speed;
    }

    public double getHourly_cost() {
        return hourly_cost;
    }

    public double getFuel_rate() {
        return fuel_rate;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int numAirplanes = scanner.nextInt();
        Airplane[] airplanes = new Airplane[numAirplanes];

        for (int i = 0; i < numAirplanes; i++) {
            String name = scanner.next();
            double TOW_min = scanner.nextDouble();
            double TOW_max = scanner.nextDouble();
            double range = scanner.nextDouble();
            double speed = scanner.nextDouble();
            double hourly_cost = scanner.nextDouble();
            double fuel_rate = scanner.nextDouble();
            airplanes[i] = new Airplane(name, TOW_min, TOW_max, range, speed, hourly_cost, fuel_rate);
        }

        String cargoMass = scanner.next();
        double totalProfit = 0;
        while (!cargoMass.equals("quit")) {
            double mass = Double.parseDouble(cargoMass);
            double distance = scanner.nextDouble();
            double payment = scanner.nextDouble();
            double maxProfit = 0;
            String maxProfitName = "";
            boolean isProfitable = false;
            for (Airplane airplane : airplanes) {
                double fuel_rate = airplane.getFuel_rate();
                double totalMass = airplane.getTOW_min() + mass;
                double hourlyCost = airplane.getHourly_cost();
                double time = distance / airplane.speed;
                double profit = 0;
                if (totalMass <= airplane.getTOW_max()) {
                    profit = payment - fuel_rate * totalMass * time - hourlyCost * time;
                }
                if (maxProfit < profit) {
                    maxProfit = profit;
                    maxProfitName = airplane.getName();
                }
                if (maxProfit > 0) {
                    isProfitable = true;
                }
            }
            if (isProfitable) {
                totalProfit += maxProfit;
                System.out.printf("%s %.2f%n", maxProfitName, maxProfit);
            } else {
                System.out.println("Decline");
            }
            cargoMass = scanner.next();
        }
        System.out.printf("%s %.2f%n", "Profit:" , totalProfit);
        }
    }
