package programacion.avanzada.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name="futbolistas")
public class Futbolista implements Serializable{
	/**
	 * Que sea serializable quiere decir que los datos 
	 * Se puedan transformar en un json
	 * 
	 */
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)//Decir que es indice
	private Long id;
	@NotNull
	@Size(min=3)
	@Column(unique=true)
	private String nombre;
	@NotNull
	@Size(min=3)
	private String apellido;
	@NotNull
	private Long goles;
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date FechaNac;
	
	private String foto;
	
	@ManyToOne(fetch=FetchType.LAZY)//Restapi con Json
	@JoinColumn(name="pais_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
	@NotNull
	private Pais pais;
	
	
	

	public Pais getPais() {
		return pais;
	}



	public void setPais(Pais pais) {
		this.pais = pais;
	}



	public String getFoto() {
		return foto;
	}



	public void setFoto(String foto) {
		this.foto = foto;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getApellido() {
		return apellido;
	}



	public void setApellido(String apellido) {
		this.apellido = apellido;
	}



	public Long getGoles() {
		return goles;
	}



	public void setGoles(Long goles) {
		this.goles = goles;
	}



	public Date getFechaNac() {
		return FechaNac;
	}



	public void setFechaNac(Date fechaNac) {
		FechaNac = fechaNac;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	private static final long serialVersionUID = 1L;
	
}
