import java.util.*;
class Obj1 {
    Sac sac;
    Item item;
    int g;
    int h;
    int profondeur;
    List<Obj1> chemain;

    public Obj1(Sac sac, Item item, int g, int profondeur, int h) {

        this.sac = sac;
        this.item = item;
        this.g = g;
        this.h = h;
        this.profondeur = profondeur;
        this.chemain = new ArrayList<>();
    }

    public void addNodeToChemain(Obj1 node) {
        chemain.add(node);
    }

    public List<Obj1> getChemain() {
        return chemain;
    }

    public void updateChemain(List<Obj1> newPath) {
        chemain = new ArrayList<>(newPath);
    }
}