package programacion.avanzada.services;

import java.util.List;

import programacion.avanzada.model.entity.Equipo;
import programacion.avanzada.model.entity.Futbolista;
import programacion.avanzada.model.entity.Pais;

public interface IFutbolistaService {
	public List<Futbolista> listarFutbolistas();
	public Futbolista guardar(Futbolista a);
	public Futbolista encontrar(Long id);
	public void borrar(Long id);
	public List<Pais> findAllPaises();
}
