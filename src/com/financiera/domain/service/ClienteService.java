package com.financiera.domain.service;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.LocalizacionBean;
import com.financiera.domain.bean.ParametroBean;

public interface ClienteService extends Service {

	public void agregar(ClienteBean c, String usuario);
	public void persistir(ClienteBean c, String usuario);
	public List<ClienteBean> getClienteByDelegacion(Session sesion);
	
	public List<ClienteBean> getClientes(String usuario, Session sesion);
	public String[] getClientesStr(String usuario);
	
	public List<ClienteBean> getClienteByApellido(String apellido, Session sesion);
	public String[] getClienteByApellidoStr(String apellido, int desde, int hasta, String usuario);
	
	public List<ClienteBean> getClienteFindDelegacion(Long idDelegacion, Session sesion);
	public List<ClienteBean> getClienteFindDelegacionEstado(Long idDelegacion, int idEstado, Session sesion);
	public List<ClienteBean> getClienteFindDelegacion(Collection<DelegacionBean> delegaciones, Session sesion);
	public String[] getClienteByDelegacionStr(Long idDelegacion, String usuario);
	public String[] getClienteByDelegacionStr(Long idDelegacion, int desde, int hasta, String usuario);
	public String[] getClienteByDelegacionEstadoStr(Long idDelegacion, int idEstado, int desde, int hasta, String usuario);
	

	public ClienteBean getClienteById(Long id);
	public ClienteBean getClienteById(Long id, Session sesion);
	public String getClienteByIdStr(Long id, String usuario);
	
	public ClienteBean getClienteByNroDoc(int nroDoc, Session sesion) throws Exception;
	public ClienteBean getClienteByNroDocActivo(int nroDoc, Session sesion) throws Exception;
	public String getClienteByNroDocStr(int nroDoc, String usuario);
	public String[] getClienteByNroDocStr(int nrodoc, int desde, int hasta, String usuario);
	
	public void persistir(LocalizacionBean l, String usuario);
	
	public ClienteBean getClienteBySucNroCA(String sucursalCA, String	nroCA, Session sesion) throws Exception;
	public String[] getClienteByNroCAStr(String nroCA, int desde, int hasta, String usuario);

	public ParametroBean getParametro();
	public ParametroBean getParametro(Session sesion);
}