package com.myproject.app.servicios.usuarios.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myproject.app.servicios.usuarios.models.entity.UsuarioRol;

public interface IUsuarioRoleDao extends JpaRepository<UsuarioRol, Long> {

}
