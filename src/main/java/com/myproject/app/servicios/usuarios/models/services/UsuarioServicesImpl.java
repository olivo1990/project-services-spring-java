package com.myproject.app.servicios.usuarios.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myproject.app.servicios.usuarios.models.dao.IUsuarioDao;
import com.myproject.app.servicios.usuarios.models.dao.IUsuarioRoleDao;
import com.myproject.app.servicios.usuarios.models.entity.Usuario;
import com.myproject.app.servicios.usuarios.models.entity.UsuarioRol;
import com.myproject.app.servicios.usuarios.models.services.interfaces.IUsuarioServices;

@Service
public class UsuarioServicesImpl implements IUsuarioServices, UserDetailsService {

	@Autowired
	@Qualifier("passwordEncoderApplication")
	private BCryptPasswordEncoder passwordEncoder;

	private Logger logger = LoggerFactory.getLogger(UsuarioServicesImpl.class);
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IUsuarioRoleDao usuarioRoleDao;

	@SuppressWarnings("null")
	@Override
	@Transactional
	public Usuario registrar(Usuario usuario) {
		
		UsuarioRol usuarioRol = new UsuarioRol();
		
		Long idRol = (long) 5;
		
		String passwordBcrypt = passwordEncoder.encode(usuario.getPassword());
	
		usuario.setPassword(passwordBcrypt);
		
		Usuario usuarioGuardado = usuarioDao.save(usuario);
		
		Long idUsuario = usuarioGuardado.getId();
		
		usuarioRol.setIdUsuario(idUsuario);
		usuarioRol.setIdRol(idRol);
		
		System.out.println(usuarioRol);
		
		usuarioRoleDao.save(usuarioRol);
		
		return usuarioGuardado;
	}
	
	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
		
		Usuario usuario = usuarioDao.findByCorreo(correo);
		
		if(usuario == null) {
			logger.error("Error en el login: no existe el correo '"+correo+"' en el sistema!");
			throw new UsernameNotFoundException("Error en el login: no existe el correo '"+correo+"' en el sistema!");
		}
		
		List<GrantedAuthority> authorities = usuario.getRol()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> logger.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		
		return new User(usuario.getCorreo(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}

	@Override
	@Transactional(readOnly=true)
	public Usuario buscarUsuarioCorreo(String correo) {
		return usuarioDao.findByCorreo(correo);
	}

	
}
