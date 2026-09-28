package com.example.demo;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personas")
public class PersonaController {

	private final ConcurrentHashMap<Long, Persona> personas = new ConcurrentHashMap<>();
	private final AtomicLong siguienteId = new AtomicLong(1);

    // obtener la lista de personas
	@GetMapping
	public List<Persona> listar() {
		return personas.values().stream()
				.sorted((primera, segunda) -> primera.id().compareTo(segunda.id()))
				.toList();
	}

    // obtener una persona por su id
	@GetMapping("/{id}")
	public ResponseEntity<Persona> obtener(@PathVariable Long id) {
		Persona persona = personas.get(id);
		return persona == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(persona);
	}

    // crear una nueva persona
	@PostMapping
	public ResponseEntity<Persona> crear(@RequestBody Persona datos) {
		Long id = siguienteId.getAndIncrement();
		Persona persona = new Persona(id, datos.nombre(), datos.apellido(), datos.email());
		personas.put(id, persona);
		return ResponseEntity.created(URI.create("/api/personas/" + id)).body(persona);
	}

    // actualizar una persona existente
	@PutMapping("/{id}")
	public ResponseEntity<Persona> actualizar(@PathVariable Long id, @RequestBody Persona datos) {
		if (!personas.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}

		Persona persona = new Persona(id, datos.nombre(), datos.apellido(), datos.email());
		personas.put(id, persona);
		return ResponseEntity.ok(persona);
	}

    // eliminar una persona
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		if (personas.remove(id) == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.noContent().build();
	}
}