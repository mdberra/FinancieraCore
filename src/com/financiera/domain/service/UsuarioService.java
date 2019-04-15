package com.financiera.domain.service;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.UsuarioBean;

public interface UsuarioService extends Service {
	
	public String validar(String usuario, String password);
	public void actualizar(UsuarioBean u);
	public UsuarioBean getUsuario(Long id);
	public String getUsuarioStr(Long id);
	/**
	public List<UsuarioBean> getUsuarioAll();
	public String[] getUsuarioStrAll();

	public void actualizar(GrupoPermisoBean g);
	public GrupoPermisoBean getGrupoPermiso(Long id);
	public String getGrupoPermisoStr(Long id);
	public List<GrupoPermisoBean> getGrupoPermisoAll();
	public String[] getGrupoPermisoStrAll();

	public void actualizar(PermisoBean g);
	public PermisoBean getPermiso(Long id);
	public String getPermisoStr(Long id);
	public List<PermisoBean> getPermisoAll();
	public String[] getPermisoStrAll();
	*/
}