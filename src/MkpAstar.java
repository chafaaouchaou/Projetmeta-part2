import java.util.*;

// class Sac {
//     int nom;
//     int Capasite;

//     public Sac(int nom, int capasite) {
//         this.nom = nom;
//         Capasite = capasite;
//     }

// }

// class Sacwheight {
//     Sac sac;
//     int wheight;

//     public Sacwheight(Sac sac) {
//         this.sac = sac;
//         this.wheight = 0;
//     }
// }

// class Item {
//     int nom;
//     int poids;
//     int valeur;

//     public Item(int nom, int poids, int valeur) {
//         this.nom = nom;
//         this.poids = poids;
//         this.valeur = valeur;
//     }

// }

// class Obj1 {
//     Sac sac;
//     Item item;
//     int g;
//     int h;
//     int profondeur;
//     List<Obj1> chemain;

//     public Obj1(Sac sac, Item item, int g, int profondeur, int h) {

//         this.sac = sac;
//         this.item = item;
//         this.g = g;
//         this.h = h;
//         this.profondeur = profondeur;
//         this.chemain = new ArrayList<>();
//     }

//     public void addNodeToChemain(Obj1 node) {
//         chemain.add(node);
//     }

//     public List<Obj1> getChemain() {
//         return chemain;
//     }

//     public void updateChemain(List<Obj1> newPath) {
//         chemain = new ArrayList<>(newPath);
//     }
// }

// class Retresult1 {
//     int value;
//     List<Obj1> path;

//     public Retresult1(int value, List<Obj1> path) {
//         this.value = value;
//         this.path = path;
//     }
// }

public class MkpAstar {

    // public static void main(String[] args)
    // {//////////////////////////////////////////////////////////////
    // int Bestbenef = 0;
    // List<Obj1> bestcombo = new ArrayList<>();
    // List<Sac> sacs = new ArrayList<>();
    // List<Item> items = new ArrayList<>();
    // sacs = Data.getSacs(4);
    // items = Data.getItems(7);
    // Obj1 node = new Obj1(null, null, 0,0,0);
    // long startTime = System.nanoTime();
    // Retresult1 result =
    // Starsolver(node,items.size(),sacs,items,bestcombo,Bestbenef);
    // long endTime = System.nanoTime();
    // long executionTime = (endTime - startTime) / 1000;
    // System.out.println("Bestbenef "+result.value);
    // System.out.println("Execution time: " + executionTime + " microseconds");
    // for (Obj1 obj : result.path) {
    // if (obj.sac.nom!=sacs.size()-1) {
    // System.out.println("S " +obj.sac.nom +" - "+"I " +obj.item.nom );
    // }
    // }
    // }///////////////////////////////////////////////////////////////////////////////////////////////////////

    static Retresult1 Starsolver(Obj1 startNode, int limit, List<Sac> sacs, List<Item> items, List<Obj1> bestcombo,
            int Bestbenef) {
        Comparator<Obj1> objComparator = Comparator.comparingInt(o -> -(o.g + o.h));
        PriorityQueue<Obj1> queue = new PriorityQueue<>(objComparator);
        int fin = 2;

        queue.offer(startNode);

        while (!queue.isEmpty()) {
            Obj1 currentNode = queue.poll();
            List<Obj1> currentPath = currentNode.getChemain();

            if (currentNode.profondeur == limit) {
                int valeur = verifySolution(currentPath, sacs, items);

                if (valeur > Bestbenef) {
                    Bestbenef = valeur;
                    bestcombo.clear();
                    bestcombo.addAll(currentPath);
                    fin--;
                }
                if (fin == 0) {

                    return new Retresult1(Bestbenef, bestcombo);
                }

            }

            if (currentNode.profondeur < limit) {

                for (Sac sac : sacs) {

                    // Obj1 node = new Obj1(sac,items.get(currentNode.profondeur) ,
                    // currentNode.g+items.get(currentNode.profondeur).poids,currentNode.profondeur+1,items.size()-currentNode.profondeur);
                    // Obj1 node = new Obj1(sac,items.get(currentNode.profondeur)
                    // ,currentNode.g+items.get(currentNode.profondeur).poids,currentNode.profondeur+1,items.get(currentNode.profondeur).poids/sac.Capasite);
                    // Obj1 node = new Obj1(sac,items.get(currentNode.profondeur) ,currentNode.g+
                    // items.get(currentNode.profondeur).poids,currentNode.profondeur+1,items.get(currentNode.profondeur).valeur/items.get(currentNode.profondeur).poids);
                    // WITH -
                    // Obj1 node = new Obj1(sac,items.get(currentNode.profondeur)
                    // ,currentNode.g+1,currentNode.profondeur+1,sac.Capasite-items.get(currentNode.profondeur).poids);
                    Obj1 node = new Obj1(sac, items.get(currentNode.profondeur),
                            currentNode.g + items.get(currentNode.profondeur).poids, currentNode.profondeur + 1,
                            sac.Capasite - items.get(currentNode.profondeur).poids);
                    List<Obj1> newPath = new ArrayList<>(currentPath);
                    newPath.add(node);
                    node.updateChemain(newPath);
                    queue.offer(node);

                }

            }

        }
        return new Retresult1(Bestbenef, bestcombo);

    }

    static int verifySolution(List<Obj1> currentPath, List<Sac> sacs, List<Item> items) {
        Sacwheight[] sacswheit = new Sacwheight[sacs.size()];

        for (Sac sac : sacs) {
            sacswheit[sac.nom] = new Sacwheight(sac);
        }

        for (Obj1 obj : currentPath) {
            sacswheit[obj.sac.nom].wheight += obj.item.poids;
        }

        for (int i = 0; i < sacs.size() - 1; i++) {
            if (sacswheit[i].wheight > sacs.get(i).Capasite) {
                return -1;
            }
        }
        int totalValue = 0;

        for (Obj1 obj : currentPath) {
            if (obj.sac.nom != sacs.size() - 1) {

                totalValue = totalValue + obj.item.valeur;
            }

        }

        return totalValue;

    }

}
