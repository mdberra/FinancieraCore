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
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DisparoEntidadesBean;
import com.financiera.domain.service.DelegacionService;


public class DelegacionServiceImpl extends AbstractService implements DelegacionService {

	public DelegacionServiceImpl(){
		super();
		this.setDescription("Servicio para Delegacion");
		this.setName("DelegacionService");
	}

	public void actualizar(DelegacionBean d, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			if(d.getId() == 0) {
				sesion.save(d);
			} else {
				DelegacionBean d1 = this.getDelegacion(d.getId(), sesion);
				d1.setCodigo(d.getCodigo());
				d1.setDescripcion(d.getDescripcion());
				d1.setLocalizacion(d.getLocalizacion());
				d1.setEntidad(d.getEntidad());
				d1.setBanco(d.getBanco());
				d1.setUtilizar(d.getUtilizar());
//				d1.setF_alta(d.getF_alta());
				sesion.update(d1);
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
			CacheManager.getInstance().inicializar();
		}
	}
	public DelegacionBean getDelegacion(Long id, Session sesion) {
		DelegacionBean d = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DelegacionBean.findById");
			q.setLong("id", id);
			d = (DelegacionBean)q.list().iterator().next();
		} catch(Exception e) {
			System.out.println("DelegacionService getDelegacion: " + e.getMessage());
		}
		return d;
	}
	public DelegacionBean getDelegacion(Long id) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(new String("Administrador"));
		DelegacionBean deleg = this.getDelegacion(id, sesion);
		return deleg;
	}
	public String getDelegacionStr(Long id, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		DelegacionBean deleg = this.getDelegacion(id, sesion);
		BancoBean banco = deleg.getBanco();
		return deleg.toString().concat(banco.getDescripcion());
	}

	
	public List getDelegacionAll(Session sesion) {
		List<DelegacionBean> delegacion = new ArrayList<DelegacionBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DelegacionBean.getAll");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				delegacion.add(i++, (DelegacionBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDelegacionAll: " + e.getMessage());
		}
		return delegacion;
	}
	public List<DelegacionBean> getDelegacionAllOrderDescripcion(Session sesion) {
		List<DelegacionBean> delegacion = new ArrayList<DelegacionBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		DelegacionBean db;
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DelegacionBean.getAllOrderDescripcion");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				db = (DelegacionBean)cI.next();
				delegacion.add(i++, db);
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDelegacionAllOrderDescripcion: " + e.getMessage());
		}
		return delegacion;
	}

	public List<DelegacionBean> getDelegacionDispOrderDescripcion(Session sesion) {
		List<DelegacionBean> delegacion = new ArrayList<DelegacionBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		DelegacionBean db;
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DelegacionBean.getAllOrderDescripcion");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				db = (DelegacionBean)cI.next();
				if(db.usar()) {
					delegacion.add(i++, db);
				}
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDelegacionAllOrderDescripcion: " + e.getMessage());
		}
		return delegacion;
	}

	public List<DelegacionBean> getDelegacionBaproSVilleOrderDescripcion(Session sesion) {
		List<DelegacionBean> delegacion = new ArrayList<DelegacionBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		DelegacionBean db;
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DelegacionBean.getAllOrderDescripcion");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				db = (DelegacionBean)cI.next();
				if(db.usar()) {
					if(db.getBanco().isBaproSVille()) {
						delegacion.add(i++, db);
					}
				}
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDelegacionAllOrderDescripcion: " + e.getMessage());
		}
		return delegacion;
	}
	public List<DelegacionBean> getDelegacionNacionOrderDescripcion(Session sesion) {
		List<DelegacionBean> delegacion = new ArrayList<DelegacionBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		DelegacionBean db;
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DelegacionBean.getAllOrderDescripcion");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				db = (DelegacionBean)cI.next();
				if(db.usar()) {
					if(db.getBanco().isNacion()) {
						delegacion.add(i++, db);
					}
				}
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDelegacionAllOrderDescripcion: " + e.getMessage());
		}
		return delegacion;
	}

	public String[] getDelegacionStrAllOrderDescripcion(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<DelegacionBean> delegacion = this.getDelegacionAllOrderDescripcion(sesion);
		
		String[] deleg = new String[delegacion.size()];
		int i = 0;
		Iterator dI = delegacion.iterator();
		while(dI.hasNext()) {
			DelegacionBean delega = (DelegacionBean)dI.next();

			BancoBean banco = delega.getBanco();
			String d = delega.toString();
			deleg[i++] = new String(d.concat(banco.getDescripcion()));
		}
		return deleg;
	}
	
	public String[] getDelegacionStrDispOrderDescripcion(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<DelegacionBean> delegacion = this.getDelegacionDispOrderDescripcion(sesion);
		
		String[] deleg = new String[delegacion.size()];
		int i = 0;
		Iterator dI = delegacion.iterator();
		while(dI.hasNext()) {
			DelegacionBean delega = (DelegacionBean)dI.next();

			BancoBean banco = delega.getBanco();
			String d = delega.toString();
			deleg[i++] = new String(d.concat(banco.getDescripcion()));
		}
		return deleg;
	}
	
	public String[] getDelegacionStrBaproSVilleOrderDescripcion(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<DelegacionBean> delegacion = this.getDelegacionBaproSVilleOrderDescripcion(sesion);
		
		String[] deleg = new String[delegacion.size()];
		int i = 0;
		Iterator dI = delegacion.iterator();
		while(dI.hasNext()) {
			DelegacionBean delega = (DelegacionBean)dI.next();

			BancoBean banco = delega.getBanco();
			String d = delega.toString();
			deleg[i++] = new String(d.concat(banco.getDescripcion()));
		}
		return deleg;
	}
	public String[] getDelegacionStrNacionOrderDescripcion(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<DelegacionBean> delegacion = this.getDelegacionNacionOrderDescripcion(sesion);
		
		String[] deleg = new String[delegacion.size()];
		int i = 0;
		Iterator dI = delegacion.iterator();
		while(dI.hasNext()) {
			DelegacionBean delega = (DelegacionBean)dI.next();

			BancoBean banco = delega.getBanco();
			String d = delega.toString();
			deleg[i++] = new String(d.concat(banco.getDescripcion()));
		}
		return deleg;
	}
//---------------------------------------------------------------------------------	
	public void inicializarDisparoEntidades(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			Iterator i = this.getDisparoEntidadesAll(sesion).iterator();
			while(i.hasNext()) {
				sesion.delete((DisparoEntidadesBean)i.next());
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void selectAllEntidadesBaproSVille(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		DelegacionBean d = null;
		DisparoEntidadesBean de = null;
		
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			Iterator<DelegacionBean> i = this.getDelegacionBaproSVilleOrderDescripcion(sesion).iterator();
			while(i.hasNext()) {
				d = (DelegacionBean) i.next();

				de = new DisparoEntidadesBean();
				de.setIdentificador(d.getId());
				de.setTipo(DisparoEntidadesBean.DELEGACION);
				sesion.save(de);
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void selectAllEntidadesNacion(String usuario) {
		DelegacionBean d = null;
		DisparoEntidadesBean de = null;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			Iterator<DelegacionBean> i = this.getDelegacionNacionOrderDescripcion(sesion).iterator();
			while(i.hasNext()) {
				d = (DelegacionBean) i.next();

				de = new DisparoEntidadesBean();
				de.setIdentificador(d.getId());
				de.setTipo(DisparoEntidadesBean.DELEGACION_NACION);
				sesion.save(de);
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void deletearDisparoEntidades(Long id, String tipo, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			DisparoEntidadesBean deBean = this.getDisparoEntidades(id, tipo, sesion);
			sesion.delete(deBean);
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public List<DisparoEntidadesBean> getDisparoEntidadesAll(Session sesion) {
		List<DisparoEntidadesBean> dispEnt = new ArrayList<DisparoEntidadesBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DisparoEntidadesBean.getAll");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				dispEnt.add(i++, (DisparoEntidadesBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDisparoEntidadesAll: " + e.getMessage());
		}
		return dispEnt;
	}
	public List<DisparoEntidadesBean> getDisparoEntidadesBaproSVille(Session sesion) {
		List<DisparoEntidadesBean> dispEnt = new ArrayList<DisparoEntidadesBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DisparoEntidadesBean.findByTipo");
			q.setString("tipo", DisparoEntidadesBean.DELEGACION);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				dispEnt.add(i++, (DisparoEntidadesBean)cI.next());
			}
			q.setString("tipo", DisparoEntidadesBean.CLIENTE);
			cI = q.list().iterator();
			while(cI.hasNext()) {
				dispEnt.add(i++, (DisparoEntidadesBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDisparoEntidadesBaproSVille: " + e.getMessage());
		}
		return dispEnt;
	}
	public List<DisparoEntidadesBean> getDisparoEntidadesNacion(Session sesion) {
		List<DisparoEntidadesBean> dispEnt = new ArrayList<DisparoEntidadesBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DisparoEntidadesBean.findByTipo");
			q.setString("tipo", DisparoEntidadesBean.DELEGACION_NACION);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				dispEnt.add(i++, (DisparoEntidadesBean)cI.next());
			}
			q.setString("tipo", DisparoEntidadesBean.CLIENTE);
			cI = q.list().iterator();
			while(cI.hasNext()) {
				dispEnt.add(i++, (DisparoEntidadesBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDisparoEntidadesNacion: " + e.getMessage());
		}
		return dispEnt;
	}
	public List<DisparoEntidadesBean> getDisparoEntidadesAll(int desde, int hasta, Session sesion) {
		int i = 0, j = 0;
		List<DisparoEntidadesBean> dispEnt = new ArrayList<DisparoEntidadesBean>();
		Iterator cI = this.getDisparoEntidadesAll(sesion).iterator();
		while(cI.hasNext()) {
			DisparoEntidadesBean dBean = (DisparoEntidadesBean)cI.next();
			if(i >=desde && i <= hasta) {
				dispEnt.add(j++, dBean);
			}
			i++;
		}
		return dispEnt;
	}
	public void persistirDisparoEntidades(DisparoEntidadesBean d, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		this.persistirDisparoEntidadesSesion(d, sesion);
	}	
	public void persistirDisparoEntidadesSesion(DisparoEntidadesBean d, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			
			if(d.getId() == 0) {
				sesion.save(d);
			}
			else {
				sesion.update(d);
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
	}
	public DisparoEntidadesBean getDisparoEntidades(Long id, String tipo, Session sesion) {
		DisparoEntidadesBean d = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DisparoEntidadesBean.findByIdTipo");
			q.setLong("identificador", id);
			q.setString("tipo", tipo);
			d = (DisparoEntidadesBean)q.list().iterator().next();
		} catch(Exception e) {
//			System.out.println("DelegacionService getDisparoEntidades: " + e.getMessage());
		}
		return d;
	}
	public List<DisparoEntidadesBean> getDisparoEntidades(Session sesion) {
		List<DisparoEntidadesBean> de = new ArrayList<DisparoEntidadesBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("DisparoEntidadesBean.findPendientes");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				de.add(i++, (DisparoEntidadesBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("DelegacionService getDisparoEntidades: " + e.getMessage());
		}
		return de;
	}
	public String getDisparoEntidadesStr(Long id, String tipo, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		return this.getDisparoEntidades(id, tipo, sesion).toString();
	}
}