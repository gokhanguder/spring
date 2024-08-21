package com.digi.digihello.testdigi.controleurs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digi.digihello.testdigi.service.Ville;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
// Mapping de la classe sur l'URL /villes
public class VilleControleur {

     private List<Ville> villes = new ArrayList<>();

    // Exemple d'initialisation de quelques villes
    public VilleControleur() {
        villes.add(new Ville("Istanbul", 15636243));
        villes.add(new Ville("Ankara", 5310000));
        villes.add(new Ville("Izmir", 3056000));
    }

    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    // Méthode pour ajouter une nouvelle ville
    @PutMapping("/ajouter")
    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) {
        // Vérification si une ville avec le même nom existe déjà
        for (Ville ville : villes) {
            if (ville.getNom().equalsIgnoreCase(nouvelleVille.getNom())) {
                return new ResponseEntity<>("La ville existe déjà", HttpStatus.BAD_REQUEST);
            }
        }

        // Si la ville n'existe pas, elle est ajoutée à la liste
        villes.add(nouvelleVille);
        return new ResponseEntity<>("Ville insérée avec succès", HttpStatus.OK);
    }
}