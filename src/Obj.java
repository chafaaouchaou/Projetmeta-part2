import java.util.*;
public class Obj {
    Sac sac;
    Item item;
    int weighttillnow;
    int profondeur;
    List<Obj> chemain;

    public Obj(Sac sac, Item item, int weighttillnow, int profondeur) {

        this.sac = sac;
        this.item = item;
        this.weighttillnow = weighttillnow;
        this.profondeur = profondeur;
        this.chemain = new ArrayList<>();
    }

    public void addNodeToChemain(Obj node) {
        chemain.add(node);
    }

    public List<Obj> getChemain() {
        return chemain;
    }

    public void updateChemain(List<Obj> newPath) {
        chemain = new ArrayList<>(newPath);
    }

    
}


