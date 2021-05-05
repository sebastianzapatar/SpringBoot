package programacion.avanzada.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import programacion.avanzada.model.dao.IFutbolistaDAO;
import programacion.avanzada.model.entity.Equipo;
import programacion.avanzada.model.entity.Futbolista;
import programacion.avanzada.model.entity.Pais;

@Service
public class FutbolistaService implements IFutbolistaService {
	
	@Autowired
	private IFutbolistaDAO futbolistaDAO;
	@Override
	public List<Futbolista> listarFutbolistas() {
		// TODO Auto-generated method stub
		return  futbolistaDAO.findAll();
	}
	@Override
	public Futbolista guardar(Futbolista e) {
		return futbolistaDAO.save(e);
		/*
		 * Para guardar y editar se utiliza el save
		 */
	}
	@Override
	public Futbolista encontrar(Long id) {
		return futbolistaDAO.findById(id).orElse(null);
	}
	
	@Override
	public void borrar(Long id) {
		futbolistaDAO.deleteById(id);
	}
	@Override
	@Transactional(readOnly=true)
	public List<Pais> findAllPaises(){
		return futbolistaDAO.findAllPaises();
	}
}
