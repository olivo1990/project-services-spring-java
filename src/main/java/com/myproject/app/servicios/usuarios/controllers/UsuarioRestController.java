package com.myproject.app.servicios.usuarios.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myproject.app.servicios.usuarios.models.entity.Usuario;
import com.myproject.app.servicios.usuarios.models.services.interfaces.IUsuarioServices;

@CrossOrigin(origins = { "http://localhost:4200", "*" })
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
	
	@Autowired
	private IUsuarioServices usuarioService;
	
	@PostMapping("/registrar")
	public Usuario registrarUsuario(@RequestBody Usuario usuario) {
		return usuarioService.registrar(usuario);
	}

}
