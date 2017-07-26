package structure;

import model.Restaurant;
import structures.BoundedQueue;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by REA on 7/23/2017.
 * There are a few things that seem to make up the core of the ant colony optimization (ACO) algorithm.
 * 1. An ant colony that produces "ants" to traverse a graph.
 * 2. The ants transmit information by leaving a trail of pheromones along the path they walk.
 * 3. The pheromone evaporates over time.  Basically, we need some sort of limitation on the pheromone left behind.
 * 4. The ants follow stronger pheromone trails.
 */
public class AntColony{


    private static final int ANT_LIFE_SPAN = 20;
    private int antDeaths;
    private int antFoundShorterPath;
    private int antTookLongerPath;
    private int antTookShorterPath;
    private ArrayList<Restaurant> vertices;
    private ArrayList<Integer> pheromoneLevels;
    private ArrayList<Restaurant> bestSolution;
    private int numberOfVertices;
    private double shortestDistance;
    private int numberOfSolutionsSubmitted;

    private ArrayList<Restaurant> path;
    private double pathDistance;
    private Restaurant startVertex;
    private Restaurant endVertex;

    Graph<Restaurant> graph;

    public AntColony(Graph<Restaurant> graph, Restaurant startVertex, Restaurant endVertex){
        this.graph = graph;
        numberOfVertices = graph.getNumberOfVertices();
        this.startVertex = startVertex;
        this.endVertex = endVertex;

        vertices = new ArrayList<>(numberOfVertices);
        pheromoneLevels = new ArrayList<>(numberOfVertices);
        path = new ArrayList<>(numberOfVertices);

        // Break our direct path
        graph.addEdge(startVertex, endVertex, -1);
    }

    /**Sends out the number of ants you pass as an integer in an attempt to find the shortest path to the end vertex.
     *
     * @param ants
     */
    public void sendOutTheAnts(int ants){
        int i = 0;
        while (i < ants){
            sendOutAnt();
            i++;
        }
    }

    /** Returns an ordered queue with the best ant solution found.
     *  Returns null when no solution is found.
     *
     * @return ordered queue of best ant solution
     */
    public BoundedQueue<Restaurant> getBestAntSolution(){
        BoundedQueue<Restaurant> queue = null;

        if (numberOfSolutionsSubmitted == 0){
            System.out.println("No solution found.  Silly ants.");
        }
        else{
            queue = new BoundedQueue<>(bestSolution.size());
            int i;
            for (i = 0; i < bestSolution.size() - 1; i++){

                bestSolution.get(i + 1).setDistance(graph.weightIs(bestSolution.get(i), bestSolution.get(i + 1)));
                queue.enqueue(bestSolution.get(i));
            }
            queue.enqueue(bestSolution.get(i));
        }

        printSummary();

        return queue;
    }

    private void sendOutAnt(){

        BoundedQueue<Restaurant> adjacentVertices;
        Restaurant currentVertex = startVertex;
        int verticesVisited = 0;
        boolean found = false;
        path.clear();

        graph.clearMarks();
        addVertex(currentVertex);

        while (verticesVisited < ANT_LIFE_SPAN) {
            path.add(currentVertex);
            graph.markVertex(currentVertex);

            if (currentVertex.compareTo(endVertex) == 0){
                found = true;
                break;
            }

            adjacentVertices = getUnmarkedAdjVertices(currentVertex);
            Restaurant mostPheromone = null;
            Restaurant container = null;
            Restaurant randomVertex = null;
            boolean pheromoneFound = false;
            Random randomNumber = new Random();

            // Should give me a random number between 0 and adjacentVertices.size() - 1
            // For 1 to 50: rand.nextInt((max-min) + 1) + min);
            // The bound goes from min(inclusive) to max(exclusive)
            // https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
            int randomInt = randomNumber.nextInt(adjacentVertices.size());
            int iteration = 0;

            while (!adjacentVertices.isEmpty()) {

                container = adjacentVertices.dequeue();
                // Takes care of our first iteration
                if (mostPheromone == null) {
                    mostPheromone = container;
                }

                addVertex(container);

                // If our new vertex has a higher pheromone level than our current one.
                if (getPheromoneLevels(mostPheromone) < getPheromoneLevels(container)) {
                    mostPheromone = container;
                    pheromoneFound = true;
                }

                if (iteration == randomInt){
                    randomVertex = container;
                }
                iteration++;
            }

            if (pheromoneFound) {
                int pheromoneLevel = getPheromoneLevels(mostPheromone);
                if (pheromoneLevel >= 7){
                    // 7 out of 10 possible values will force the ant to go to the vertex with the most pheromone
                    if (randomNumber.nextInt(10) < 7){
                        currentVertex = mostPheromone;
                    }
                    else{
                        currentVertex = randomVertex;
                    }

                }
                else{
                    // We increase the likelihood of the ant going to our high pheromone vertex
                    // with every level of pheromone
                    if (randomNumber.nextInt(9 - pheromoneLevel) == 0){
                        currentVertex = mostPheromone;
                    }
                    else{
                        currentVertex = randomVertex;
                    }
                }
            }
            else{
                // Just in case we somehow managed to not get a randomvertex.  Highly unlikely to be an issue
                if (randomVertex != null){
                    currentVertex = randomVertex;
                }
                else{
                    currentVertex = container;
                }

            }

            verticesVisited++;
        }

        if (!found){
            antDeaths++;
            /*
            if (antDeaths == 1){
                System.out.println(antDeaths + " death");
            }
            else{
                System.out.println(antDeaths + " deaths");
            }
            System.out.println("Died at " + currentVertex);
            */
        }
        else {
            // Compare our solution to the best current solution.
            //System.out.println("End vertex found by ant.");
            submitSolution();
        }
    }

    // We only adjust our solution if its total distance is less than our shortestDistance
    // Pheromone will only be added for new/shorter solutions
    // Pheromone will be reduced for the longer path when a shorter path is found.
    private void submitSolution(){
        pathDistance = 0;

        for (int i = 0; i < path.size() - 1; i++){
            pathDistance += graph.weightIs(path.get(i), path.get(i + 1));
        }

        if (numberOfSolutionsSubmitted == 0){
            bestSolution = new ArrayList<>(path);
            shortestDistance = pathDistance;

            for (int i = 0; i < path.size(); i++){
                // Increase our pheromone level for every vertex in the submitted solution
                setPheromoneLevels(path.get(i), getPheromoneLevels(path.get(i)) + 1);
            }
            antFoundShorterPath++;
        }
        else if (pathDistance < shortestDistance){
            // Reduce pheromone levels for the longer path
            for (int i = 0; i < bestSolution.size(); i++){
                // Decrease our pheromone level for every vertex in the submitted solution
                setPheromoneLevels(bestSolution.get(i), getPheromoneLevels(bestSolution.get(i)) - 1);
            }
            // Update our best solution
            bestSolution = new ArrayList<>(path);
            shortestDistance = pathDistance;
            //System.out.println("Shorter route found.");
            //System.out.println("New shorter distance: " + shortestDistance + "\n");
            int pheromoneBonus = 2;


            for (int i = 0; i < path.size(); i++){
                // Increase our pheromone level for every vertex in the shorter solution
                setPheromoneLevels(path.get(i), getPheromoneLevels(path.get(i)) + 2);
            }
            antFoundShorterPath++;
        }
        else if (pathDistance == shortestDistance){
            //System.out.println("Same distance route found.\n");
            antTookShorterPath++;
        }
        else{
            //System.out.println("Longer route found.\n");
            antTookLongerPath++;
        }
        numberOfSolutionsSubmitted++;
    }

    private void printSummary(){
        System.out.println("Ant deaths: " + antDeaths);
        System.out.println("Shorter paths found: " + antFoundShorterPath);
        System.out.println("Longer paths used: " + antTookLongerPath);
        System.out.println("Shorter path used: " + antTookShorterPath);
        //System.out.println("Shortest distance route found: " + shortestDistance);
        System.out.println();
    }

    private void addVertex(Restaurant vertex){
        // If our arraylist does not contain the vertex
        if (!vertices.contains(vertex)){
            vertices.add(vertex);
            pheromoneLevels.add(0);
        }
    }
    // Vertex should already be on our list
    private void setPheromoneLevels(Restaurant vertex, int pheromoneLevel){
        int index = vertices.indexOf(vertex);
        pheromoneLevels.set(index, pheromoneLevel);
    }

    // Vertex should already be on our list
    private int getPheromoneLevels(Restaurant vertex){
        int index = vertices.indexOf(vertex);
        return pheromoneLevels.get(index);
    }

    private BoundedQueue<Restaurant> getUnmarkedAdjVertices(Restaurant startVertex){
        BoundedQueue<Restaurant> queue = new BoundedQueue<>(graph.getNumberOfVertices());
        BoundedQueue<Restaurant> temp = graph.getToVertices(startVertex);
        Restaurant container;

        while (!temp.isEmpty()){
            container = temp.dequeue();

            if (!graph.isMarked(container)){
                queue.enqueue(container);
            }
        }
        return queue;
    }
}
