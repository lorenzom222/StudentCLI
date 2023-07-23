import java.util.Scanner;

public class Airplane {
	
	private String name;
	private float TOW_min, TOW_max, range, speed, hourly_cost, fuel_rate ;
	private float mass, distance, payments;
	
	public Airplane(String name, float Tow_min, float TOW_max, float range, float speed, float hourly_cost, float fuel_rate) {
		this.name = name;
		this.TOW_min = Tow_min;
		this.TOW_max = TOW_max;
		this.range = range;
		this.speed = speed;
		this.hourly_cost = hourly_cost;
		this.fuel_rate = fuel_rate;
	}
	
//	public float getTOW_min() {
//        return TOW_min;
//    }
//	
	public String getName() {
        return name;
    }
//	
//	public float getTOW_max() {
//        return TOW_max;
//    }
//	public float getRange() {
//        return range;
//    }
//	
//	public float gethourly_cost() {
//        return hourly_cost;
//    }
//	
//	public float getfuel_rate() {
//        return fuel_rate;
//    }
	

	
	public static void main(String[] args) {
		
		System.out.println("Enter the number of planes");
		Scanner s = new Scanner(System.in);
		int numAirplanes = s.nextInt();
		
		Airplane[] airplanes = new Airplane[numAirplanes];
		
		//Plane Attributes
		for(int i=0; i<numAirplanes; i++) {
			System.out.println("Enter the Airplane number" +(i+1)+"'s "+ "attributes: <name> <TOW_min> <TOW_max> <range> <speed> <hourly_cost> <fuel\\_rate>.");
			
			String source = s.next();
			float TOW_min = s.nextFloat();
			float TOW_max = s.nextFloat();
			float range = s.nextFloat();
			float speed = s.nextFloat();
			float hourly_cost = s.nextFloat();
			float fuel_rate = s.nextFloat();
			
			airplanes[i] = new Airplane(source, TOW_min, TOW_max, range, speed, hourly_cost, fuel_rate);
		}
		
		
		//new variables
		float totalProfit = 0;
		float profits = 0;
		
		//Three tokens
		
		System.out.println("Enter: <mass> <distance> <payment>.");
		
		while (true){
			
			
			String bestPlane = null;
			float bestProfit = Float.NEGATIVE_INFINITY;
			
			String input=s.next();
			
			if(input.equals("quit")) {
				break;
			}
			
			float mass = Float.parseFloat(input);
			float distance = s.nextFloat();
			float payments = s.nextFloat();
			boolean foundplane = false;
			
			
			//Airplane[] value = new Airplane[3];
			
		
//			for(int i=0; i<numAirplanes;i++) {	
			for(Airplane item: airplanes) {	
				
				//if(airplanes[i].TOW_min <= mass && mass <= airplanes[i].TOW_max && distance <= airplanes[i].range) {
					float flightTime = distance/item.speed;
					float toFuelCost = item.fuel_rate;
					float toCost = item.hourly_cost;
					profits = payments - toCost;
					
					if(profits > 0 && profits>bestProfit) {
						bestProfit = profits;
						bestPlane = item.name;	
						foundplane = true;
						}				
				//TA's formula	
				//profits = payments-(airplanes[i].hourly_cost * (distance/airplanes[i].speed)*mass*(airplanes[i].fuel_rate));
				//}
			}
			
			if(foundplane) {
				bestProfit = Math.round(bestProfit*100.0f)/100.0f;
				System.out.println(bestPlane +" "+bestProfit);
				totalProfit += bestProfit;
			}else {
				System.out.println("Decline");
			}
			
//			if (bestPlane == null) {
//	            System.out.println("Decline");
//	            break;
//	        } else {
//	            System.out.println(bestPlane +" "+bestProfit);
//	            totalProfit += bestProfit;
//	        }
					
		}
		
		System.out.printf("Total profit: $%.2f\n", totalProfit);
        s.close();
        
	}//main method
}


