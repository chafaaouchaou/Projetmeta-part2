import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MultipleKnapsackGeneticAlgorithm {


   


    static Random random = new Random();

    





















    public static void main(String[] args) {
        List<Solution> mergedSolutions = new ArrayList<>();
        List<Integer> knapsackCapacity = new ArrayList<>();
        knapsackCapacity=Datameta.getSacs(2, "sacs.csv");
        List<Item> items = new ArrayList<>();
        items = Datameta.getItems(9,"items1.csv");
        int populationSize = 50;
        double mutationRate = 0.1;
        int generations = 30;
        int numberOfItems = items.size();
        int numberOfKnapsacks = knapsackCapacity.size();

        Population population = geneticalgoexec(mergedSolutions,knapsackCapacity,items,populationSize,mutationRate,generations,numberOfItems,numberOfKnapsacks);
        
        System.out.println("Best solution found: " + population.solutions.get(0).fitness);
        for ( Integer val: population.solutions.get(0).chromosome) {
            System.out.println(val);
            
        }
    }


    static Population geneticalgoexec( List<Solution> mergedSolutions,List<Integer> knapsackCapacity,List<Item> items,  int populationSize, double mutationRate, int generations,int numberOfItems, int numberOfKnapsacks)
    {
        Population population = initializePopulation(items,populationSize,numberOfItems,numberOfKnapsacks,knapsackCapacity);
        mergedSolutions.addAll(population.solutions);
        for (int i = 0; i < generations; i++) {
            population = evolvePopulation(population,items,populationSize, numberOfItems,numberOfKnapsacks,mutationRate,knapsackCapacity);
            mergedSolutions.addAll(population.solutions);
            Collections.sort(mergedSolutions, (s1, s2) -> Integer.compare(s2.fitness, s1.fitness));
            population.solutions.clear();
            for (int j = 0; j < populationSize; j++) {
                population.solutions.add(mergedSolutions.get(j));
            }
            mergedSolutions.clear();
            mergedSolutions.addAll(population.solutions);



        }
        evaluateSolution(population.solutions.get(0),items,numberOfItems,numberOfKnapsacks,knapsackCapacity);
        return population;

    }











    static Population initializePopulation(List<Item> items,int populationSize,int numberOfItems,int numberOfKnapsacks,List<Integer> knapsackCapacity) 
    {
        Population population = new Population();
        for (int i = 0; i < populationSize; i++) {
            Solution solution = new Solution(numberOfItems);
            for (int j = 0; j < numberOfItems; j++) {
                solution.chromosome.set(j, random.nextInt(numberOfKnapsacks+1));
            }
            evaluateSolution(solution,items,numberOfItems,numberOfKnapsacks,knapsackCapacity);
            population.solutions.add(solution);
        }
        Collections.sort(population.solutions, (s1, s2) -> Integer.compare(s2.fitness, s1.fitness));
        return population;
    }

    static void evaluateSolution(Solution solution,List<Item> items,int numberOfItems,int numberOfKnapsacks,List<Integer> knapsackCapacity) {
        int[] knapsackWeights = new int[numberOfKnapsacks];
        int[] knapsackValues = new int[numberOfKnapsacks];


        for (int i = 0; i < numberOfKnapsacks; i++) {
            knapsackWeights[i] = 0;
            knapsackValues[i] = 0;
        }

        for (int i = 0; i < numberOfItems; i++) {
            int knapsackIndex = solution.chromosome.get(i);
            if (knapsackIndex >= numberOfKnapsacks) {
                // System.out.println("Invalid knapsack index" + knapsackIndex);
                continue;
            }
            knapsackWeights[knapsackIndex] += items.get(i).poids;
            knapsackValues[knapsackIndex] += items.get(i).valeur;
        }

        solution.fitness = 0;
        for (int i = 0; i < numberOfKnapsacks; i++) {
            while (knapsackWeights[i]>knapsackCapacity.get(i)) {
                mutatealter(solution,i,numberOfItems,numberOfKnapsacks);

                for (int ii = 0; ii < numberOfKnapsacks; ii++) {
                    knapsackWeights[ii] = 0;
                    knapsackValues[ii] = 0;
                }
        
                for (int ii = 0; ii < numberOfItems; ii++) {
                    int knapsackIndex = solution.chromosome.get(ii);
                    if (knapsackIndex >= numberOfKnapsacks) {
                        // System.out.println("Invalid knapsack index" + knapsackIndex);
                        continue;
                    }
                    knapsackWeights[knapsackIndex] += items.get(i).poids;
                    knapsackValues[knapsackIndex] += items.get(i).valeur;
                }
            }
        }


        for (int i = 0; i < numberOfKnapsacks; i++) {
            if (knapsackWeights[i] <= knapsackCapacity.get(i) ) {
                solution.fitness += knapsackValues[i];
            }
        }


    }

    static Population evolvePopulation(Population population,List<Item> items,int populationSize,int numberOfItems,int numberOfKnapsacks, double mutationRate,List<Integer> knapsackCapacity ) {
        Population newPopulation = new Population();

        for (int i = 0; i < populationSize; i++) {
            Solution parent1 = selectParent(population,populationSize);
            Solution parent2 = selectParent(population,populationSize);
            Solution child = crossover(parent1, parent2,numberOfItems);
            Solution child2 = new Solution(numberOfItems); 
            for (int j = 0; j < numberOfItems; j++) {
                child2.chromosome.set(j,child.chromosome.get(j));
            }


            mutate(child2,numberOfItems,numberOfKnapsacks,mutationRate);
            evaluateSolution(child,items,numberOfItems,numberOfKnapsacks,knapsackCapacity);
            evaluateSolution(child2,items,numberOfItems,numberOfKnapsacks,knapsackCapacity);
            newPopulation.solutions.add(child);
            newPopulation.solutions.add(child2);
        }

        // System.out.println("patata is patata " +newPopulation.solutions.size() );
        Collections.sort(newPopulation.solutions, (s1, s2) -> Integer.compare(s2.fitness, s1.fitness));
        return newPopulation;
    }

    static Solution selectParent(Population population,int populationSize) {
        int index = random.nextInt(populationSize);
        return population.solutions.get(index);
    }

    static Solution crossover(Solution parent1, Solution parent2,int numberOfItems) {
        Solution child = new Solution(numberOfItems);
        int crossoverPoint = random.nextInt(numberOfItems);
        for (int i = 0; i < numberOfItems; i++) {
            if (i < crossoverPoint) {
                child.chromosome.set(i, parent1.chromosome.get(i));
            } else {
                child.chromosome.set(i, parent2.chromosome.get(i));
            }
        }
        return child;
    }

    static void mutate(Solution solution,int numberOfItems,int numberOfKnapsacks,double mutationRate) {
        for (int i = 0; i < numberOfItems; i++) {
            if (Math.random() < mutationRate) {
                solution.chromosome.set(i, random.nextInt(numberOfKnapsacks));
            }
        }
    }
    static void mutatealter(Solution solution,int index,int numberOfItems,int numberOfKnapsacks) {
        int patata = random.nextInt(numberOfItems);

        while (solution.chromosome.get(patata)!=index) {
            patata = random.nextInt(numberOfItems);
            
        }
        solution.chromosome.set(patata, numberOfKnapsacks);


    }
}
