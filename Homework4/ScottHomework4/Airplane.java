/*Name: Scott Sun
 * Email: ssun27@u.rochester.edu
 * Name of the assignment: Airplane
 * Last modified: FEB 27 2023
 */
import java.util.Scanner;
public class Airplane {
    private String name;
    private float TOW_min;
    private float TOW_max;
    private float range;
    private float speed;
    private float hourly_cost;
    private float fuel_rate;

    public Airplane(String name, float TOW_min, float TOW_max, float range, float speed, float hourly_cost, float fuel_rate) {
        this.name = name;
        this.TOW_min = TOW_min;
        this.TOW_max = TOW_max;
        this.range = range;
        this.speed = speed;
        this.hourly_cost = hourly_cost;
        this.fuel_rate = fuel_rate;
    }

    public String getname() {
        return name;
    }

    public float getMIN() {
        return TOW_min;
    }

    public float getMAX() {
        return TOW_max;
    }

    public float getRange() {
        return range;
    }

    public float getspeed() {
        return speed;
    }

    public float getHourly_cost() {
        return hourly_cost;
    }

    public float getFuel_rate() {
        return fuel_rate;
    }

    public static void main(String[] args) {
        Scanner S = new Scanner(System.in);
        int numberofplant = S.nextInt();
        Airplane Fleet[] = new Airplane[numberofplant];
        for (int i = 0; i <= numberofplant - 1; i++) {
            Fleet[i] = new Airplane(S.next(), S.nextFloat(), S.nextFloat(), S.nextFloat(), S.nextFloat(), S.nextFloat(), S.nextFloat());
        }
        float totalpro = 0;
        while (S.hasNextLine()) {
            if (S.hasNextFloat()) {
                float mass = S.nextFloat();
                float distance = S.nextFloat();
                float payment = S.nextFloat();
                float profitmax = 0;
                String namemax = "";
                for (int k = 0; k <= numberofplant - 1; k++) {
                    float totalmass = Fleet[k].getMIN() + mass;
                    float time = distance /Fleet[k].getspeed();
                    float totalhourcost = time * Fleet[k].getHourly_cost();
                    if ((totalmass <= Fleet[k].getMAX()) && (Fleet[k].getRange() >= distance)) {
                        float profit = payment - (totalmass * time * Fleet[k].getFuel_rate() + totalhourcost);
                        if (profit > profitmax && profit > 0) {
                            profitmax = profit;
                            namemax = Fleet[k].getname();
                        }
                    }
                }
                if (profitmax > 0) {
                    System.out.printf("%s %.2f", namemax, profitmax);
                    totalpro += profitmax;
                } else {
                    System.out.println("decline");
                }
            } else {
                String temp = S.next();
                if (temp.equals("quit")) {
                    System.out.printf("Profit: %.2f", totalpro);
                    break;
                }
            }
        }
    }
}





































