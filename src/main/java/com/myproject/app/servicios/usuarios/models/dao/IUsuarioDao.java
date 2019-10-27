package com.myproject.app.servicios.usuarios.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.myproject.app.servicios.usuarios.models.entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long>{
	
	@Query("select u from Usuario u where u.correo=?1")
	public Usuario findByCorreo(String correo);

}
