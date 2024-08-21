package com.digi.digihello.testdigi.controleurs;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.digi.digihello.testdigi.service.Ville;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/villes")
public class VilleControleur {

    private List<Ville> villes = new ArrayList<>();
    private int nextId = 0; // Otomatik artan ID için başlangıç değeri

    // Initialisation de quelques villes
    public VilleControleur() {
        villes.add(new Ville(nextId++, "Istanbul", 15636243));
        villes.add(new Ville(nextId++, "Ankara", 5310000));
        villes.add(new Ville(nextId++, "Izmir", 3056000));
    }

    // Récupérer toutes les villes
    @GetMapping
    public List<Ville> getVilles() {
        // ID'ye göre sıralama
        villes.sort(Comparator.comparingInt(Ville::getId));
        return villes;
    }

    // Trouver une ville par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleById(@PathVariable int id) {
        Optional<Ville> ville = villes.stream().filter(v -> v.getId() == id).findFirst();
        if (ville.isPresent()) {
            return new ResponseEntity<>(ville.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Ajouter une nouvelle ville avec un PUT (Vérification de l'unicité de l'ID)
    @PutMapping("/ajouter")
    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) {
        // ID kontrolü
        for (Ville ville : villes) {
            if (ville.getId() == nouvelleVille.getId()) {
                return new ResponseEntity<>("L'identifiant existe déjà", HttpStatus.CONFLICT);
            }
        }

        // Yeni ID ataması
        nouvelleVille.setId(nextId++);
        villes.add(nouvelleVille);
        return new ResponseEntity<>("Ville insérée avec succès", HttpStatus.CREATED);
    }

    // Modifier une ville avec un POST basé sur l'ID
    @PostMapping("/modifier/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @RequestBody Ville updatedVille) {
        for (Ville ville : villes) {
            if (ville.getId() == id) {
                ville.setNom(updatedVille.getNom());
                ville.setNbHabitants(updatedVille.getNbHabitants());
                return new ResponseEntity<>("Ville modifiée avec succès", HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Ville non trouvée", HttpStatus.NOT_FOUND);
    }

    // Supprimer une ville par son ID
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) {
        boolean removed = villes.removeIf(v -> v.getId() == id);

        if (removed) {
            return new ResponseEntity<>("Ville supprimée avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ville non trouvée", HttpStatus.NOT_FOUND);
        }
    }
}
