package com.financiera.core.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.hibernate.Session;

import com.dto.ClienteDTO;
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.bean.ClienteBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DisparoEntidadesBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.ClienteService;
import com.financiera.domain.service.DelegacionService;

public class CacheManager {
	private DelegacionService delegacionService = (DelegacionService) ServiceLocator.getInstance().getService(DelegacionService.class);
	private ClienteService    clienteService  	= (ClienteService)    ServiceLocator.getInstance().getService(ClienteService.class);

	static CacheManager singletonInstance;
	public static final String Administrador = new String("Administrador");
	
	private Session 			 sesionGeneral = null;
	private List<DelegacionBean> delegAll	 = null;
	private List<DelegacionBean> delegDisp	 = null;
	private List<DelegacionBean> delegBapro  = null;
	private List<DelegacionBean> delegNacion = null;
	private List<DisparoEntidadesBean> dispEnt = null;
	
	private HashMap<String, Session> sesiones = null;

	static public CacheManager getInstance() {
		if(singletonInstance==null) {
			singletonInstance = new CacheManager();
		}
		return singletonInstance;
	}
	private CacheManager(){
		this.sesiones = new HashMap<String, Session>(); 
		this.clearUsuarioSesion(Administrador);
		sesionGeneral = this.getUsuarioSesion(Administrador);
		this.inicializar();
	}
	public synchronized void resetearDisparoEntidades() {
		if(this.dispEnt != null) {
			this.dispEnt.clear();
		}
		this.dispEnt = delegacionService.getDisparoEntidades(sesionGeneral);
	}
	public synchronized void resetearDisparoEntidades(Session sesion) {
		if(this.dispEnt != null) {
			this.dispEnt.clear();
		}
		this.dispEnt = delegacionService.getDisparoEntidades(sesion);
	}

	public String[] getDelegacionStrAllOrderDescripcion() {
		return this.toVector(this.delegAll);
	}
	public String[] getDelegacionStrDispOrderDescripcion() {
		return this.toVector(this.delegDisp);
	}
	public String[] getDelegacionStrDispOrderDescripcion(String delegDescrip) {
		List<DelegacionBean> todas = this.delegDisp;
		List<DelegacionBean> deleg = new ArrayList<DelegacionBean>();
		Iterator<DelegacionBean> ii = todas.iterator();
		while(ii.hasNext()) {
			DelegacionBean db = (DelegacionBean) ii.next();
			if(db.getDescripcion().compareTo(delegDescrip)==0) {
				deleg.add(0, db);
			} else {
				deleg.add(db);
			}
		}
		return this.toVector(deleg);
	}
	public String[] getDelegacionStrBaproSVilleOrderDescripcion() {
		return this.toVector(this.delegBapro);
	}
	public String[] getDelegacionStrNacionOrderDescripcion() {
		return this.toVector(this.delegNacion);
	}
	public long getIdDelegacion(String descripcion) {
		long j = 0;
		for(int i=0; i<delegAll.size(); i++) {
			if(delegAll.get(i).getDescripcion().compareTo(descripcion)==0) {
				j= delegAll.get(i).getId();
			}
		}
		return j;
	}
	public synchronized DisparoEntidadesBean getDisparoEntidades(Long id, String tipo) {
		DisparoEntidadesBean de = null;
		Iterator<DisparoEntidadesBean> it = dispEnt.iterator();
		boolean enc = false;
		while(it.hasNext() && !enc) {
			de = (DisparoEntidadesBean)it.next();
			if(de.getTipo().compareTo(tipo)==0)
				if(de.getIdentificador().compareTo(id)==0) {
					enc = true;
			}
		}
		if(!enc) {
			de = null;
		}
		return de;
	}
	private String[] toVector(List<DelegacionBean> delegacion){
		String[] deleg = new String[delegacion.size()];
		int i = 0;
		Iterator<DelegacionBean> dI = delegacion.iterator();
		while(dI.hasNext()) {
			DelegacionBean delega = (DelegacionBean)dI.next();

			BancoBean banco = delega.getBanco();
			String d = delega.toString();
			deleg[i++] = new String(d.concat(banco.getDescripcion()));
		}
		return deleg;
	}

	@SuppressWarnings("unchecked")
	public void inicializar() {
		if(this.delegAll != null) {
			this.delegAll.clear();			
			this.delegDisp.clear();
			this.delegBapro.clear();
			this.delegNacion.clear();
		}
		this.delegAll    = delegacionService.getDelegacionAllOrderDescripcion(sesionGeneral);
		this.delegDisp   = delegacionService.getDelegacionDispOrderDescripcion(sesionGeneral);
		this.delegBapro  = delegacionService.getDelegacionBaproSVilleOrderDescripcion(sesionGeneral);
		this.delegNacion = delegacionService.getDelegacionNacionOrderDescripcion(sesionGeneral);
	}
	
	public List<DelegacionBean> getDelegAll() {
		return delegAll;
	}
	public List<DelegacionBean> getDelegDisp() {
		return delegDisp;
	}
	public List<DelegacionBean> getDelegBapro() {
		return delegBapro;
	}
	public List<DelegacionBean> getDelegNacion() {
		return delegNacion;
	}
	public List<DisparoEntidadesBean> getDispEnt() {
		return dispEnt;
	}
	public boolean veoTodosLosDatos(String cb, boolean esArca) {
//	cb es un ClienteBean.toString()
		try {
			if(esArca) {
				ClienteDTO c = new ClienteDTO(cb);
				if(c.esSoloServina()) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			return true;
		}
	}
	public boolean veoTodosLosDatos(Long cliente, boolean esArca, Session sesion) {
		ClienteBean c = null;
		try{
			c = clienteService.getClienteById(cliente, sesion);
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return veoTodosLosDatos(c.toString(), esArca);
	}
	public boolean esCompartido(String cb, boolean esArca) {
		try {
			if(esArca) {
				ClienteDTO c = new ClienteDTO(cb);
				if(c.esCompartido()) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	public Vector<Long> getServiciosVisibles(Long cliente, boolean esArca, Session sesion) {
		Vector<Long> v = new Vector<Long>();
		try{
			ClienteBean c = clienteService.getClienteById(cliente, sesion);
			Iterator<ServicioBean> it = c.getServicios().iterator();
			while(it.hasNext()) {
				ServicioBean sb = (ServicioBean)it.next();
				if(esArca) {
					if(sb.getVendedor().getVendedorArca()<9) {
						v.add(sb.getId());
					}
				} else {
					v.add(sb.getId());
				}
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		}
		return v;
	}
	public synchronized void clearUsuarioSesion(String usuario) {
		Session s = sesiones.get(usuario);
		if(s != null) {
			s.flush();
			s.close();
		}
		sesiones.remove(usuario);
		s = Persistencia.abrirSesion();
		sesiones.put(usuario, s);	
	}
	public synchronized Session getUsuarioSesion(String usuario) {
		return sesiones.get(usuario);
	}
	public synchronized Session restart(String usuario) {
		this.clearUsuarioSesion(usuario);
		return this.getUsuarioSesion(usuario);
	}
}