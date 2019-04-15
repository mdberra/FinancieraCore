package com.financiera.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.AbstractService;
import com.financiera.core.server.Persistencia;
import com.financiera.core.service.PersistenciaService;
import com.financiera.core.util.DateTimeUtil;
import com.financiera.domain.bean.Persistible;

public class PersistenciaServiceImpl extends AbstractService implements PersistenciaService {
	
	public PersistenciaServiceImpl(){
		super();
		this.setDescription("Servicio para Movics");
		this.setName("MovicsService");
	}

	public void persistirMasivo(Collection<Object> c, Collection<String> informe, Session sesion) {
		informe.add(new String("Persistencia Comienzo : " + DateTimeUtil.getStringFormatDateTime()));
		Collection<Object> aux = new ArrayList<Object>();
		int count = 0;
		int count1 = 0;

		Iterator i = c.iterator();
		while(i.hasNext()) {
			Object t = (Object)i.next();
			aux.add(t);
			if(count++ > 100) {
				this.persistir(aux, informe, sesion);
				count = 0;
			}
			count1++;
			if(count1 % 500 == 0) 
				informe.add(new String("Persistiendo Objecto nro : " + count1 + " " + DateTimeUtil.getStringFormatDateTime()));
		}
		if(!aux.isEmpty()) {
			this.persistir(aux, informe, sesion);
		}

		informe.add(new String("Persistencia Finalizacion : " + count1 + " " + DateTimeUtil.getStringFormatDateTime()));
		c.clear();
	}
	
	private void persistir(Collection<Object> c, Collection<String> informe, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Iterator i = c.iterator();
			while(i.hasNext()) {
				Persistible p = (Persistible)i.next();
				p.persistir(sesion, p);
			}
		} catch (Exception e) {
			informe.add(new String("Error al persistir : " + e.getMessage()));
		}
		finally {
			transaction.commit();
			sesion.flush();
			c.clear();			
		}
	}
	
	public String[] informeToString(Collection<String> informe) {
		int j = 0;
		String[] s = new String[informe.size()];
		Iterator i = informe.iterator();
		while(i.hasNext()) {
			s[j++] = (String)i.next();
		}
		return s;
	}
}