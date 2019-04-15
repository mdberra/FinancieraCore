package com.financiera.domain.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.domain.bean.BancoBean;
import com.financiera.domain.service.BancoService;

public class BancoServiceImpl extends AbstractService implements BancoService {
	
	public BancoServiceImpl(){
		super();
		this.setDescription("Servicio para Bancos");
		this.setName("BancoService");
	}

	public void actualizar(BancoBean b, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			if(b.getId() == 0) {
				sesion.save(b);
			} else {
				BancoBean b1 = this.getBanco(b.getId(), sesion);
				b1.setCodigo(b.getCodigo());
				b1.setDescripcion(b.getDescripcion());
				b1.setLocalizacion(b.getLocalizacion());
				b1.setCodigoDebito(b.getCodigoDebito());
				b1.setDescripPrestacion(b.getDescripPrestacion());
				b1.setBancoRecaudador(b.getBancoRecaudador());
				sesion.update(b1);
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
		}
	}

	public BancoBean getBanco(Long id, Session sesion) {
		BancoBean b = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("BancoBean.findById");
			q.setLong("id", id);
			b = (BancoBean)q.list().iterator().next();
		} catch(Exception e) {
			System.out.println("DelegacionService : " + e.getMessage());
		}
		return b;
	}
	
	public String getBancoStr(Long id, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		BancoBean b = this.getBanco(id, sesion);
		return b.toString();
	}
	
	public List<BancoBean> getBancoAllOrderDescripcion(Session sesion) {
		List<BancoBean> banco = new ArrayList<BancoBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("BancoBean.getAllOrderDescripcion");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				banco.add(i++, (BancoBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("BancoService : " + e.getMessage());
		}
		return banco;
	}
	public String[] getBancoStrAllOrderDescripcion(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<BancoBean> banco = this.getBancoAllOrderDescripcion(sesion);
		
		String[] ban = new String[banco.size()];
		int i = 0;
		Iterator dI = banco.iterator();
		while(dI.hasNext()) {
			BancoBean b = (BancoBean)dI.next();

			ban[i++] = b.toString();
		}
		return ban;
	}
}