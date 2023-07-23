/* Ruiyang Peng
rpeng3@u.rochester.edu
3/2/2023
HW4 Airplane
 */

import java.util.Scanner;
//create plane class
public class Airplane {
    private final String name;
    private final double towMin;
    private final double towMax;
    private final double range;
    private final double speed;
    private final double hourlyCost;
    private final double fuelRate;

//Constructor
    public Airplane(String name,double towMin,double towMax,double range,double speed, double hourlyCost,double fuelRate){
        this.name= name;
        this.towMin= towMin;
        this.towMax= towMax;
        this.range= range;
        this.speed= speed;
        this.hourlyCost= hourlyCost;
        this.fuelRate= fuelRate;
    }
//Getters
    public String getName(){
        return name;
    }

    public double getTowMin(){
        return towMin;
    }

    public double getTowMax(){
        return towMax;
    }

    public double getRange(){
        return range;
    }

    public double getSpeed(){
        return speed;
    }

    public double getFuelRate() {
        return fuelRate;
    }

    public double getHourlyCost() {
        return hourlyCost;
    }
    //cost of each contract
    public double getCost(double mass, double distance){
        double fuelCost = getFuelRate()*(distance/getSpeed())*(mass+getTowMin());
        double flightCost = getHourlyCost()*(distance/getSpeed());
        double totalCost = flightCost+fuelCost;
        return totalCost;
    }
    //profit of each flight
    public double getProfit(double payment,double mass,double distance){
        double profit= payment-getCost(mass,distance);
        return profit;
    }
    //check if the cargo too heavy or out of maximum range of plane
    public boolean contractViable(double mass,double distance){
        double totalWeight = mass+getTowMin();
        if(totalWeight<=getTowMax()&&distance<=getRange()){
            return true;
        }else{
            return false;
        }
    }

        public static void main(String[] args) {
        //Store all the plane information
        Scanner scanner = new Scanner(System.in);
        int numPlanes = scanner.nextInt();
        Airplane[] fleet = new Airplane[numPlanes]; //array to store information of each plane

        for(int i = 0; i<numPlanes; i++){
            String name= scanner.next();
            double towMin = scanner.nextDouble();
            double towMax = scanner.nextDouble();
            double range = scanner.nextDouble();
            double speed = scanner.nextDouble();
            double hourlyCost = scanner.nextDouble();
            double fuelRate = scanner.nextDouble();
            fleet[i]= new Airplane(name, towMin, towMax,range, speed,hourlyCost,fuelRate);
        }

        scanner.nextLine();

        double totalProfit=0.0;
        while(true){
            String inputContract = scanner.nextLine();

            if(inputContract.equals("quit")){
                break;

            }else { // loop through each contract and evaluate them for each plane
                String[] contractParts = inputContract.split(" ");  //Storing the mass, distance and payment of each contract
                double mass = Double.parseDouble(contractParts[0]);
                double distance = Double.parseDouble(contractParts[1]);
                double payment = Double.parseDouble(contractParts[2]);

                Airplane bestPlane=null;
                double bestProfit=0.0;

                for(Airplane airplane:fleet) {          //calculate profit for each airplane for this contract
                    if (airplane.contractViable(mass,distance) == true){
                        double profit=airplane.getProfit(payment,mass,distance);
                        if(profit>bestProfit&&profit>0){        //finding the best plane and corresponding profit for this contract
                            bestProfit=profit;
                            bestPlane=airplane;
                        }
                    }

                }
                if(bestPlane==null){
                    System.out.println("decline");
                }else{
                    System.out.println(bestPlane.getName()+" "+String.format("%.2f",bestProfit));
                }
                totalProfit=totalProfit+bestProfit;
            }


        }
        System.out.println("Profit: "+String.format("%.2f",totalProfit));
    }

}
