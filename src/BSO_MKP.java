import java.util.ArrayList;
import java.util.List;
import java.util.Random;



// class Bee {
//     List<Integer> solution;
//     double fitness;
//     int changed;

//     public Bee(List<Integer> solution, double fitness) {
//         this.solution = solution;
//         this.fitness = fitness;
//         this.changed=0;
//     }
// }

public class BSO_MKP {
    static Random random = new Random();


    public static void main(String[] args) {
        List<Integer> knapsackCapacityList = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        knapsackCapacityList = Datameta.getSacs(9, "sacs.csv");
        items = Datameta.getItems(9, "items1.csv" );

        // initializeItems(knapsackCapacityList,items);
        int numItems = 9;
        int numKnapsacks = 9;
        double RHO = 0.5;
        int MAX_ITERATIONS = 25;
        int NUM_BEES = 50;

        
        Bee bestBee = bsoalgoexec( numItems, numKnapsacks,MAX_ITERATIONS,NUM_BEES,knapsackCapacityList,items,RHO);

        
        System.out.println("Best solution: " + bestBee.solution);
        System.out.println("Best fitness: " + bestBee.fitness);
        System.out.println("level: " + bestBee.changed);
    }

   static Bee bsoalgoexec( int numItems, int numKnapsacks,int MAX_ITERATIONS,int NUM_BEES,List<Integer> knapsackCapacityList,List<Item> items,double RHO){
    List<Bee> bees = initializeBees(knapsackCapacityList, items,numKnapsacks, numItems,NUM_BEES);

        for (int iter = 0; iter < MAX_ITERATIONS; iter++) {
            for (Bee bee : bees) {
                exploreNeighbor(bee,knapsackCapacityList,items,numKnapsacks, numItems);
            }

            updateOnlookerBees(bees,knapsackCapacityList,items,numKnapsacks, numItems);

            // // Scout bee phase
            for (Bee bee : bees) {
                if (random.nextDouble() < RHO) { // Randomly abandon poor solutions
                    scout(bee,knapsackCapacityList,items,numKnapsacks, numItems);
                }
            }

            // Update global best solution (optional)
        }

        // Print the best solution found (optional)
        Bee bestBee = getBestBee(bees);

        return bestBee;

    }







    static List<Bee> initializeBees(List<Integer> knapsackCapacityList,List<Item> items,int numKnapsacks,int numItems,int NUM_BEES) {
        List<Bee> bees = new ArrayList<>();
        for (int i = 0; i < NUM_BEES; i++) {
            List<Integer> solution = generateRandomSolution(numKnapsacks, numItems);
            Bee bee = new Bee(solution, 0);
            evaluateSolution(bee,knapsackCapacityList,items, numKnapsacks, numItems);
            bees.add(bee);
        }
        return bees;
    }

    static List<Integer> generateRandomSolution(int numKnapsacks,int numItems) {
        List<Integer> solution = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            solution.add(random.nextInt(numKnapsacks)); // Assign each Item to a random knapsack
        }
        return solution;
    }



    static void evaluateSolution(Bee bee,List<Integer> knapsackCapacityList,List<Item> items,int numKnapsacks,int numItems) {
        int[] knapsackWeights = new int[numKnapsacks];
        int[] knapsackValues = new int[numKnapsacks];


        for (int i = 0; i < numKnapsacks; i++) {
            knapsackWeights[i] = 0;
            knapsackValues[i] = 0;
        }

        for (int i = 0; i < numItems; i++) {
            int knapsackIndex = bee.solution.get(i);
            if (knapsackIndex >= numKnapsacks) {
                // System.out.println("Invalid knapsack index" + knapsackIndex);
                continue;
            }
            knapsackWeights[knapsackIndex] += items.get(i).poids;
            knapsackValues[knapsackIndex] += items.get(i).valeur;
        }

        bee.fitness = 0;
        for (int i = 0; i < numKnapsacks; i++) {
            while (knapsackWeights[i]>knapsackCapacityList.get(i)) {
                mutatealter(bee,i,numKnapsacks,numItems);

                for (int ii = 0; ii < numKnapsacks; ii++) {
                    knapsackWeights[ii] = 0;
                    knapsackValues[ii] = 0;
                }
        
                for (int ii = 0; ii < numItems; ii++) {
                    int knapsackIndex = bee.solution.get(ii);
                    if (knapsackIndex >= numKnapsacks) {
                        // System.out.println("Invalid knapsack index" + knapsackIndex);
                        continue;
                    }
                    knapsackWeights[knapsackIndex] += items.get(i).poids;
                    knapsackValues[knapsackIndex] += items.get(i).valeur;
                }
            }
        }


        for (int i = 0; i < numKnapsacks; i++) {
            if (knapsackWeights[i] <= knapsackCapacityList.get(i) ) {
                bee.fitness += knapsackValues[i];
            }
        }


    }









    static void exploreNeighbor(Bee bee,List<Integer> knapsackCapacityList,List<Item> items,int numKnapsacks,int numItems) {
        int randomItem = random.nextInt(numItems);
        int randomKnapsack = random.nextInt(numKnapsacks);
        List<Integer> neighborSolution = new ArrayList<>(bee.solution);
        double fitness1 = bee.fitness;
        // neighborSolution.set(randomItem, randomKnapsack); //mutation
        bee.solution.set(randomItem, randomKnapsack);

        evaluateSolution(bee, knapsackCapacityList,items, numKnapsacks,numItems);
        if (bee.fitness < fitness1) {
            bee.solution = neighborSolution;
            bee.fitness = fitness1;
        }else{
            bee.changed+=1;
        }
    }

    static void updateOnlookerBees(List<Bee> bees,List<Integer> knapsackCapacityList,List<Item> items,int numKnapsacks,int numItems) {
        // Calculate probabilities for each bee to become an onlooker
        double[] probabilities = calculateProbabilities(bees);
    
        // Select onlooker bees based on probabilities
        for (int i = 0; i < bees.size(); i++) {
            if (random.nextDouble() < probabilities[i]) {
                Bee onlooker = bees.get(i);
                exploreNeighbor(onlooker,knapsackCapacityList,items,numKnapsacks,numItems); // Onlooker explores a neighbor solution
            }
        }
    }
    
    static double[] calculateProbabilities(List<Bee> bees) {
        // Calculate probabilities based on the fitness of employed bees
        double totalFitness = 0;
        for (Bee bee : bees) {
            totalFitness += bee.fitness;
        }
    
        double[] probabilities = new double[bees.size()];
        for (int i = 0; i < bees.size(); i++) {
            probabilities[i] = bees.get(i).fitness / totalFitness;
        }
        return probabilities;
    }
    
    static void scout(Bee bee,List<Integer> knapsackCapacityList,List<Item> items,int numKnapsacks,int numItems) {
        // If the bee has not improved for a certain number of iterations, scout for a new solution
        if (bee.fitness <= 0) {
            bee.solution = generateRandomSolution(numKnapsacks,numItems); // Generate a new random solution
            evaluateSolution(bee,knapsackCapacityList, items,numKnapsacks,numItems); // Evaluate the new solution
        }
    }
    

    static Bee getBestBee(List<Bee> bees) {
        Bee bestBee = bees.get(0);
        for (int i = 1; i < bees.size(); i++) {
            if (bees.get(i).fitness > bestBee.fitness) {
                bestBee = bees.get(i);
            }
        }
        return bestBee;
    }
    // static void initializeItems(List<Integer> knapsackCapacityList,List<Item> items) {
    // items.add(new Item(10, 20));
    // items.add(new Item(11, 21));
    // items.add(new Item(13, 24));
    // items.add(new Item(12, 23));
    // items.add(new Item(14, 26));
    // items.add(new Item(15, 28));
    // items.add(new Item(16, 30));
    // items.add(new Item(17, 32));
    // items.add(new Item(19, 36));
    // items.add(new Item(18, 34));
    // knapsackCapacityList.add(21);
    // knapsackCapacityList.add(16);
    // knapsackCapacityList.add(11);
    // knapsackCapacityList.add(14);
    // knapsackCapacityList.add(19);
    // knapsackCapacityList.add(23);
    // knapsackCapacityList.add(27);
    // knapsackCapacityList.add(29);
    // knapsackCapacityList.add(13);


    // }
    static void mutatealter(Bee bee,int index,int numKnapsacks,int numItems) {
        int patata = random.nextInt(numItems);

        while (bee.solution.get(patata)!=index) {
            patata = random.nextInt(numItems);
            
        }
        bee.solution.set(patata, numKnapsacks);


    }
}



