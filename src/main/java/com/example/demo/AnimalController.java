package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/animales")
public class AnimalController {

    private final List<Animal> baseDeDatos = new ArrayList<>();
    private long contador = 1;

    // CREAR ANIMAL
    @PostMapping
    public ResponseEntity<Animal> crear(@RequestBody Animal animal) {
        animal.setId(contador++);
        baseDeDatos.add(animal);
        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }

    // RETORNAR
    @GetMapping
    public ResponseEntity<List<Animal>> listar() {
        return ResponseEntity.ok(baseDeDatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> obtenerPorId(@PathVariable long id) {
        Optional<Animal> encontrado = baseDeDatos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
        return encontrado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> actualizar(@PathVariable long id, @RequestBody Animal animal) {
        Optional<Animal> existente = baseDeDatos.stream()
                .filter(a -> a.getId() == id)
                .findFirst();
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Animal actualizado = existente.get();
        actualizado.setNombre(animal.getNombre());
        actualizado.setEspecie(animal.getEspecie());
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        boolean eliminado = baseDeDatos.removeIf(a -> a.getId() == id);
        return eliminado ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}