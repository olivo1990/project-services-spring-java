package com.myproject.app.servicios.usuarios.models.services.interfaces;

import com.myproject.app.servicios.usuarios.models.entity.Usuario;

public interface IUsuarioServices {

	public Usuario registrar(Usuario usuario);
	
	public Usuario buscarUsuarioCorreo(String correo);
}
