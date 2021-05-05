package programacion.avanzada.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.criteria.Path;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.MultipartStream.MalformedStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.*;

import programacion.avanzada.model.entity.Equipo;
import programacion.avanzada.model.entity.Futbolista;
import programacion.avanzada.model.entity.Pais;
import programacion.avanzada.services.IFutbolistaService;



@CrossOrigin(origins= {"http://localhost:4200","http://localhost:3000"})
@RestController
@RequestMapping("/api")
public class FutbolistaController {
	@Autowired
	private IFutbolistaService futbolistaservice;
	
	/*
	 * Get para obtener datos
	 * Post para guardar
	 * Put para editar datos
	 * Delete para borrar
	 */
	@GetMapping("/futbolista/paises")
	public List<Pais> listarpaises(){
		return futbolistaservice.findAllPaises();
	}
	@GetMapping("/futbolista")
	public List<Futbolista> index(){
		return futbolistaservice.listarFutbolistas();
	}
	@GetMapping("/futbolista/{id}")
	public ResponseEntity<?> jugador(@PathVariable Long id){
		Futbolista w= futbolistaservice.encontrar(id);
		Map<String,String> response=new HashMap<>();
		if(w==null) {
			response.put("mensaje", "El futbolista ID: "+id+" no existe en la bd");
			return new ResponseEntity<Map<String,String>>
			(response,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Futbolista>(w,HttpStatus.OK);
		
	}
	
	
	@PostMapping("/futbolista")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> save(@Valid @RequestBody Futbolista e, 
			BindingResult result) {
		
		Map<String, Object> response=new HashMap<>();
		Futbolista newfut=null;
		if(result.hasErrors()) {//Por si faltan campos por enviar
			List<String> error=new ArrayList<>();
			for(FieldError err: result.getFieldErrors()) {
				error.add("El campo "+err.getField()+" "+err.getDefaultMessage());
			}
			response.put("errors", error);
			return new ResponseEntity<Map<String,Object>>
			(response,HttpStatus.BAD_REQUEST);
		}
		try {
			//e.setFechaNac(new Date());
			e.setFechaNac(new Date());
			newfut=futbolistaservice.guardar(e);
		}
		catch(Exception e1) {
			response.put("mensaje", "Error al insertar");
			return new ResponseEntity<Map<String,Object>>
			(response,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Futbolista>(newfut,HttpStatus.OK);
		
	}
	@PutMapping("/futbolista/{id}")
	public Futbolista update(@Valid @RequestBody Futbolista e, @PathVariable Long id) {
		Futbolista actual=futbolistaservice.encontrar(id);
		System.out.println(actual.getId());
		actual.setApellido(e.getApellido());
		actual.setNombre(e.getNombre());
		actual.setGoles(e.getGoles());
		actual.setPais(e.getPais());
		return futbolistaservice.guardar(actual);
	}
	@DeleteMapping("/futbolista/{id}")
	public void borrar(@PathVariable Long id) {
		Futbolista actual=futbolistaservice.encontrar(id);
		String fotoAnterior=actual.getFoto();
		if(fotoAnterior!=null && fotoAnterior.length()>0) {
			java.nio.file.Path rutaFotoAnterior=
					 Paths.get("uploads").resolve(fotoAnterior).toAbsolutePath();
			File archivoAnterior=rutaFotoAnterior.toFile();
			if(archivoAnterior.canRead() && archivoAnterior.exists()) {
				archivoAnterior.delete();
			}
		}
		futbolistaservice.borrar(id);
	}
	@PostMapping("/futbolista/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo,
			@RequestParam("id") Long id){
		Map<String,Object> response=new HashMap<>();
		Futbolista actual=futbolistaservice.encontrar(id);
		if(!archivo.isEmpty()) {
			String nombre=UUID.randomUUID()+"_"
					+archivo.getOriginalFilename().replaceAll(" ", "");
			java.nio.file.Path rutaArchivo=
					Paths.get("uploads").resolve(nombre).toAbsolutePath();
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			}
			catch(Exception e) {
				response.put("Mensaje", "Error al subir la imagen");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			String fotoActual=actual.getFoto();
			if(fotoActual!=null && fotoActual.length()>0) {
				java.nio.file.Path rutaFotoAnterior=
						Paths.get("uploads").resolve(fotoActual).toAbsolutePath();
				File fotoAnterior=rutaFotoAnterior.toFile();
				if(fotoAnterior.canRead() && fotoAnterior.exists()) {
					fotoAnterior.delete();
				}
			}
			actual.setFoto(nombre);
			futbolistaservice.guardar(actual);
			response.put("Mensaje", "Se subio la imagen");
			response.put("Futbolista", actual);
		}
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
	@GetMapping("/futbolista/upload/img/{nombrefoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombrefoto){
		java.nio.file.Path rutaFoto=
				 Paths.get("uploads").resolve(nombrefoto).toAbsolutePath();
		Resource recurso=null;
		try {
			recurso=new UrlResource(rutaFoto.toUri());
		}
		catch(Exception e) {
			
		}
		HttpHeaders cabecera=new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""
		+recurso.getFilename()+"\"");
		return new ResponseEntity<Resource>(recurso, cabecera,HttpStatus.OK);
	}
}
