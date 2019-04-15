package com.financiera.domain.service.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.AbstractService;
import com.financiera.core.server.Persistencia;
import com.financiera.domain.bean.LocalizacionBean;
import com.financiera.domain.service.LocalizacionService;

public class LocalizacionServiceImpl extends AbstractService implements LocalizacionService {

	public LocalizacionServiceImpl(){
		super();
		this.setDescription("Servicio para Localizacion");
		this.setName("LocalizacionService");
	}

	public void agregar(LocalizacionBean d, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			sesion.save(d);
		} finally {
			transaction.commit();
		}
	}
	
	public LocalizacionBean getLocalizacion(Long id, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			return (LocalizacionBean)sesion.get(LocalizacionBean.class, id);
		} finally {
			transaction.commit();
		}
	}
	
	public List getLocalizacion(Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Criteria messagesCriteria = sesion.createCriteria(LocalizacionBean.class);
			return messagesCriteria.list();
		} finally {
			transaction.commit();
		}
	}
}