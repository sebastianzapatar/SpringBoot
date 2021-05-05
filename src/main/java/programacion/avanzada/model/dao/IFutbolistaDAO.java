package programacion.avanzada.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import programacion.avanzada.model.entity.Futbolista;
import programacion.avanzada.model.entity.Pais;

public interface IFutbolistaDAO extends JpaRepository<Futbolista, Long> {
//ORM de JPA
	@Query("from Pais")//select * from paises
	public List<Pais> findAllPaises();
}
