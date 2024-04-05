import java.util.List;
public class Bee {
    List<Integer> solution;
    double fitness;
    int changed;

    public Bee(List<Integer> solution, double fitness) {
        this.solution = solution;
        this.fitness = fitness;
        this.changed=0;
    }
}

