/*name:Yiwen le
school email: yle3@u.rochester.edu
date of submisson: 3/7/2023
Assignment Number: 4
 */
import java.util.Scanner;

public class Airplane {
    private static class Air {
        private String name;
        private float towMin;
        private float towMax;
        private float range;
        private float speed;
        private float hourlyCost;
        private float fuelRate;

        public Air(String name, float towMin, float towMax, float range, float speed, float hourlyCost, float fuelRate) {
            this.name = name;
            this.towMin = towMin;
            this.towMax = towMax;
            this.range = range;
            this.speed = speed;
            this.hourlyCost = hourlyCost;
            this.fuelRate = fuelRate;
        }

        public float getHourlyCost(float flightTime) {
            return this.hourlyCost * flightTime;
        }

        public float getFuelCost(float takeoffWeight, float distance) {
            float flightTime = distance / this.speed;
            float totalWeight = takeoffWeight + (distance * this.fuelRate);
            return this.fuelRate * totalWeight * flightTime;
        }

        public boolean canCarry(float cargoMass) {
            return (cargoMass + this.towMin) <= this.towMax;
        }

        public float getMaxDistance() {
            return this.range;
        }

        public float getProfit(float cargoMass, float distance, float payment) {
            if (!canCarry(cargoMass)) {
                return -1.0f;
            }
            float flightTime = distance / this.speed;
            float cost = getHourlyCost(flightTime) + getFuelCost(this.towMin + cargoMass, distance);
            float revenue = payment;
            float profit = revenue - cost;
            return profit;
        }

        public String getName() {
            return this.name;
        }
    }

    public static void main(String[] args) {
        Scanner b = new Scanner(System.in);

        int numAirplanes = b.nextInt();
        Air[] fleet = new Air[numAirplanes];
        for (int i = 0; i < numAirplanes; i++) {
            String name = b.next();
            float towMin = b.nextFloat();
            float towMax = b.nextFloat();
            float range = b.nextFloat();
            float speed = b.nextFloat();
            float hourlyCost = b.nextFloat();
            float fuelRate = b.nextFloat();
            fleet[i] = new Air(name, towMin, towMax, range, speed, hourlyCost, fuelRate);
        }

        float totalProfit = 0.0f;
        b.nextLine();
        String line = b.nextLine();
        while (!line.trim().equals("quit")) {
            Scanner lineSc = new Scanner(line);
            float cargoMass = lineSc.nextFloat();
            float distance = lineSc.nextFloat();
            float payment = lineSc.nextFloat();

            double maxProfit = -1.0;
            Air bestOption = null;
            for (Air airplane : fleet) {
                float profit = airplane.getProfit(cargoMass, distance, payment);
                if (profit > maxProfit) {
                    maxProfit = profit;
                    bestOption = airplane;
                }
            }

            if (bestOption == null) {
                System.out.println("Decline");
            } else{
                System.out.printf("%s %.2f\n", bestOption.getName(), maxProfit);
                totalProfit += maxProfit;
            }
            line = b.nextLine();
        }

        System.out.printf("profit: %.2f\n", totalProfit);
    }
}
