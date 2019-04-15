package com.financiera.domain.service.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.AbstractService;
import com.financiera.core.server.Persistencia;
import com.financiera.domain.bean.EntidadBean;
import com.financiera.domain.bean.EstadoMovBean;
import com.financiera.domain.bean.LocalizacionBean;
import com.financiera.domain.bean.ParametroBean;
import com.financiera.domain.service.EntidadService;


public class EntidadServiceImpl extends AbstractService implements EntidadService {
	
	public EntidadServiceImpl(){
		super();
		this.setDescription("Servicio para Entidades");
		this.setName("EntidadService");
	}

	public void agregar(EntidadBean e, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			sesion.save(e);
		}finally{
			transaction.commit();
		}
	}
	
	public EntidadBean getEntidad(Long id, Session sesion) {
		EntidadBean eb = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("EntidadBean.findById");
			q.setLong("id", id);
			eb = (EntidadBean)q.list().iterator().next();
		} catch(Exception e) {
			System.out.println("EntidadService : " + e.getMessage());
		}
		return eb;
	}
	
	public LocalizacionBean getLocalizacion(Long id, Session sesion) {
		LocalizacionBean eb = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("LocalizacionBean.findById");
			q.setLong("id", id);
			eb = (LocalizacionBean)q.list().iterator().next();
		} catch(Exception e) {
			System.out.println("EntidadService : " + e.getMessage());
		}
		return eb;
	}
	
	public ParametroBean getParametro(Session sesion) {
		ParametroBean p = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			p = ParametroBean.getParametro1(sesion);
		} catch(Exception e) {
		}
		return p;
	}
}