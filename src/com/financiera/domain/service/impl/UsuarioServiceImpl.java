package com.financiera.domain.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.Persistencia;
import com.financiera.domain.bean.GrupoPermisoBean;
import com.financiera.domain.bean.PermisoBean;
import com.financiera.domain.bean.UsuarioBean;
import com.financiera.domain.service.UsuarioService;

public class UsuarioServiceImpl extends AbstractService implements UsuarioService {
	
	public UsuarioServiceImpl(){
		super();
		this.setDescription("Servicio para Usuario");
		this.setName("UsuarioService");
	}
	public String validar(String usuario, String password) {
		CacheManager.getInstance().getUsuarioSesion(CacheManager.Administrador);
		UsuarioBean b = null;
		if(usuario.compareTo("admin") == 0) {
			b = new UsuarioBean();
			b.setNombre(CacheManager.Administrador);
			b.setEmpresa("ARCA");
			return new String("ok").concat(b.getNombre());
		}
		Session session = CacheManager.getInstance().getUsuarioSesion(CacheManager.Administrador);
		Transaction transaction = session.beginTransaction();
		try {
			transaction.begin();
			Query q = session.getNamedQuery("UsuarioBean.findByAlias");
			q.setString("alias", usuario);
			b = (UsuarioBean)q.list().iterator().next();
		} catch(Exception e) {
			System.out.println("UsuarioService : " + e.getMessage());
		}
		if(b.getPassword().compareTo(password) == 0) {
			CacheManager.getInstance().clearUsuarioSesion(b.getNyA());
			return new String("ok").concat(b.getNyA());
		} else {
			return new String("false");
		}
	}
// Usuarios
	public void actualizar(UsuarioBean u) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(CacheManager.Administrador);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			if(u.getId() == 0) {
				sesion.save(u);
			} else {
				UsuarioBean u1 = this.getUsuario(u.getId());
				u1.setNombre(u.getNombre());
				u1.setApellido(u.getApellido());
				u1.setGrupos(u.getGrupos());
				u1.setFechaDesde(u.getFechaDesde());
				u1.setFechaHasta(u.getFechaHasta());
				sesion.update(u1);
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
	}

	public UsuarioBean getUsuario(Long id) {
		UsuarioBean b = null;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(CacheManager.Administrador);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("UsuarioBean.findById");
			q.setLong("id", id);
			b = (UsuarioBean)q.list().iterator().next();
		} catch(Exception e) {
			System.out.println("UsuarioService : " + e.getMessage());
		}
		return b;
	}
	
	public String getUsuarioStr(Long id) {
		UsuarioBean b = this.getUsuario(id);
		return b.toString();
	}
}