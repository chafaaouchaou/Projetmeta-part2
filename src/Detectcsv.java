import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Detectcsv {
    public static List<File> listerFichiersParMotCle(String cheminRepertoire, String motCle) {
        List<File> fichiers = new ArrayList<>();

        File repertoire = new File(cheminRepertoire);

        // Vérifier si le chemin spécifié est un répertoire
        if (repertoire.isDirectory()) {
            // Parcourir tous les fichiers du répertoire
            File[] fichiersDansRepertoire = repertoire.listFiles();
            if (fichiersDansRepertoire != null) {
                for (File fichier : fichiersDansRepertoire) {
                    // Vérifier si le fichier contient le mot clé dans son nom
                    if (fichier.isFile() && fichier.getName().toLowerCase().contains(motCle)) {
                        fichiers.add(fichier);
                    }
                }
            }
        } else {
            System.out.println("Le chemin spécifié n'est pas un répertoire.");
        }

        return fichiers;
    }

    public static void main(String[] args) {
        String cheminRepertoire = "src/csv";
        
        // Liste des fichiers contenant "sacs" dans leur nom
        List<File> fichiersSacs = listerFichiersParMotCle(cheminRepertoire, "sacs");
        
        // Liste des fichiers contenant "items" dans leur nom
        List<File> fichiersItems = listerFichiersParMotCle(cheminRepertoire, "items");

        if (fichiersSacs.isEmpty()) {
            System.out.println("Aucun fichier contenant 'sacs' trouvé dans le répertoire spécifié.");
        } else {
            System.out.println("Fichiers contenant 'sacs' trouvés dans le répertoire spécifié:");
            for (File fichier : fichiersSacs) {
                System.out.println(fichier.getName());
            }
        }

        if (fichiersItems.isEmpty()) {
            System.out.println("Aucun fichier contenant 'items' trouvé dans le répertoire spécifié.");
        } else {
            System.out.println("Fichiers contenant 'items' trouvés dans le répertoire spécifié:");
            for (File fichier : fichiersItems) {
                System.out.println(fichier.getName());
            }
        }
    }
}
