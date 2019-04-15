package com.financiera.domain.service;

import java.util.List;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DisparoEntidadesBean;

public interface DelegacionService extends Service {

	public void actualizar(DelegacionBean d, Session sesion);
	public DelegacionBean getDelegacion(Long id, Session sesion);
	public DelegacionBean getDelegacion(Long id);
	public String getDelegacionStr(Long id, String usuario);

	public List<DelegacionBean> getDelegacionAll(Session sesion);

	public List<DelegacionBean> getDelegacionAllOrderDescripcion(Session sesion);
	public List<DelegacionBean> getDelegacionDispOrderDescripcion(Session sesion);
	public List<DelegacionBean> getDelegacionBaproSVilleOrderDescripcion(Session sesion);
	public List<DelegacionBean> getDelegacionNacionOrderDescripcion(Session sesion);
	
	
	public String[] getDelegacionStrAllOrderDescripcion(String usuario);
	public String[] getDelegacionStrDispOrderDescripcion(String usuario);
	public String[] getDelegacionStrBaproSVilleOrderDescripcion(String usuario);
	public String[] getDelegacionStrNacionOrderDescripcion(String usuario);
	
	public void inicializarDisparoEntidades(String usuario);
	public void selectAllEntidadesBaproSVille(String usuario);
	public void selectAllEntidadesNacion(String usuario);
	public void deletearDisparoEntidades(Long id, String tipo, String usuario);

	public List<DisparoEntidadesBean> getDisparoEntidades(Session sesion);	
	public List<DisparoEntidadesBean> getDisparoEntidadesAll(Session sesion);
	public List<DisparoEntidadesBean> getDisparoEntidadesBaproSVille(Session sesion);
	public List<DisparoEntidadesBean> getDisparoEntidadesNacion(Session sesion);
	
	public List<DisparoEntidadesBean> getDisparoEntidadesAll(int desde, int hasta, Session sesion);
	public void persistirDisparoEntidades(DisparoEntidadesBean d, String usuario);
	public void persistirDisparoEntidadesSesion(DisparoEntidadesBean d, Session sesion);
	
	public DisparoEntidadesBean getDisparoEntidades(Long id, String tipo, Session sesion);
	public String getDisparoEntidadesStr(Long id, String tipo, String usuario);
}