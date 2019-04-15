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
import com.financiera.core.util.DateTimeUtil;
import com.financiera.domain.bean.DelegacionBean;
import com.financiera.domain.bean.DiasCobroBean;
import com.financiera.domain.service.DiasCobroService;


public class DiasCobroServiceImpl extends AbstractService implements DiasCobroService {
	
	public DiasCobroServiceImpl(){
		super();
		this.setDescription("Servicio para Dias Cobro");
		this.setName("DiasCobroService");
	}

	public List<DiasCobroBean> getDiasCobro(Long idDelegacion, Session sesion) {
		List<DiasCobroBean> dc = new ArrayList<DiasCobroBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			Query q = sesion.getNamedQuery("DiasCobroBean.findByDelegacion");
			q.setLong("idDelegacion", idDelegacion);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				dc.add(i++, (DiasCobroBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("DelegacionService : " + e.getMessage());
		}
		return dc;
	}
	public String[] getDiasCobroStr(Long idDelegacion, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<DiasCobroBean> dc = this.getDiasCobro(idDelegacion, sesion);
		
		String[] dcStr = new String[dc.size()];
		int i = 0;
		Iterator dI = dc.iterator();
		while(dI.hasNext()) {
			DiasCobroBean diaCobr = (DiasCobroBean)dI.next();
			dcStr[i++] = new String(diaCobr.getFechaDisparoStr());
		}
		CacheManager.getInstance().restart(usuario);
		return dcStr;
	}

	public void eliminar(DelegacionBean delegacion, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Iterator diasCobroIt = delegacion.getDiasCobro(sesion).iterator();
			while(diasCobroIt.hasNext()) {
				DiasCobroBean dcBean = (DiasCobroBean)diasCobroIt.next();
				sesion.delete(dcBean);
			}
		} catch(Exception e) {
			System.out.println("Error al delete : " + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}

	public void agregarDiaCobro(DiasCobroBean d, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();

			sesion.save(d);

		} catch (Exception e) {
			System.out.println("Error al persistir : " + e.getMessage());
		}
		finally {
			transaction.commit();
			sesion.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
	public void reemplazar(Long idDelegacion, String[] fechas, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();

			Query q = sesion.getNamedQuery("DelegacionBean.findById");
			q.setLong("id", idDelegacion);
			DelegacionBean deleg = (DelegacionBean)q.list().iterator().next();

			q = sesion.getNamedQuery("DiasCobroBean.findByDelegacion");
			q.setLong("idDelegacion", idDelegacion);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				sesion.delete((DiasCobroBean)cI.next());
			}
			
			for(int i=0; i< fechas.length; i++) {
				if(!com.financiera.core.util.Util.isBlank(fechas[i])) {
					DiasCobroBean dc = new DiasCobroBean();
					dc.setIdDelegacion(idDelegacion);
					dc.setDelegacion(deleg);
					dc.setFechaDisparo(DateTimeUtil.getDate(fechas[i]));
					sesion.save(dc);
				}
			}
		} catch(Exception e) {
			System.out.println("Error DiasCobro.reemplazar : " + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
		CacheManager.getInstance().restart(usuario);
	}
}