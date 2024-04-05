import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Data {


 


///////////////////////////////////////////////////////////

public static List<Sac> getSacs(int count, String cheminFichier) {
    List<Sac> sacs = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
        String ligne;
        int sacCount = 0; // Counter for the number of sacs read
        boolean premiereLigne = true;
        while ((ligne = br.readLine()) != null && sacCount < count) {
            if (premiereLigne) { // Ignorer l'en-tête
                premiereLigne = false;
                continue;
            }
            String[] donnees = ligne.split(",");
            int id = Integer.parseInt(donnees[0]);
            int capacite = Integer.parseInt(donnees[1]);
            Sac sac = new Sac(id, capacite);
            sacs.add(sac);
            sacCount++; // Increment sac counter
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return sacs;
}

public static List<Item> getItems(int count, String cheminFichier) {
    List<Item> items = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
        String ligne;
        int itemCount = 0; // Counter for the number of items read
        boolean premiereLigne = true;
        while ((ligne = br.readLine()) != null && itemCount < count) {
            if (premiereLigne) { // Ignorer l'en-tête
                premiereLigne = false;
                continue;
            }
            String[] donnees = ligne.split(",");
            int id = Integer.parseInt(donnees[0]);
            int poids = Integer.parseInt(donnees[1]);
            int valeur = Integer.parseInt(donnees[2]);
            Item item = new Item(id, poids, valeur);
            items.add(item);
            itemCount++; // Increment item counter
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return items;
}
}
