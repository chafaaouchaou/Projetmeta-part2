import java.util.*;
class Solution {
    List<Integer> chromosome = new ArrayList<>();
    int fitness = 0;

    public Solution(int size) {
        for (int i = 0; i < size; i++) {
            chromosome.add(0);
        }
    }
}