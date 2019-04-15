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
import com.financiera.domain.bean.VendedorBean;
import com.financiera.domain.service.VendedorService;


public class VendedorServiceImpl extends AbstractService implements VendedorService {
	
	public VendedorServiceImpl(){
		super();
		this.setDescription("Servicio para Vendedor");
		this.setName("VendedorService");
	}

	public void actualizar(VendedorBean v, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try {
			transaction.begin();
			if(v.getId() == 0) {
				sesion.save(v);
			} else {
				VendedorBean v1 = this.getVendedor(v.getId(), sesion);
				v1.setCodigo(v.getCodigo());
				v1.setNombre(v.getNombre());
				v1.setUtilizar(v.getUtilizar());
				sesion.update(v1);
			}
		} catch(Exception e) {
			System.out.println("error" + e.getMessage());
		} finally {
			transaction.commit();
			sesion.flush();
			CacheManager.getInstance().restart(usuario);
		}
	}
	
	public VendedorBean getVendedor(Long id, Session sesion) {
		VendedorBean b = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("VendedorBean.findById");
			q.setLong("id", id);
			b = (VendedorBean)q.list().iterator().next();
		} catch(Exception e) {
			System.out.println("VendedorService : " + e.getMessage());
		}
		return b;
	}
	public VendedorBean getVendedor(String nombre, Session sesion) {
		boolean seguir = true;
		VendedorBean b = null;

		System.out.println("Vendedor Nombre: " + nombre);
		
		List<VendedorBean> vendedor = this.getVendedorAll(sesion);
		Iterator<VendedorBean> dI = vendedor.iterator();
		while(dI.hasNext() && seguir) {
			b = (VendedorBean)dI.next();
			if(b.usar()) {
				if(b.getNombre().compareTo(nombre)==0) {
					seguir = false;
				}
			}
		}
		if(seguir){
			return null;
		}else{
			return b;
		}
	}
	public String getVendedorStr(Long id, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		VendedorBean v = this.getVendedor(id, sesion);
		return v.toString();
	}
	public VendedorBean getVendedor(Long id) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(new String("Administrador"));
		VendedorBean v = this.getVendedor(id, sesion);
		return v;
	}
	
	public List<VendedorBean> getVendedorAll(Session sesion) {
		List<VendedorBean> vendedor = new ArrayList<VendedorBean>();
		int i = 0;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("VendedorBean.getAll");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				vendedor.add(i++, (VendedorBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("VendedorService : " + e.getMessage());
		}
		return vendedor;
	}
	public String[] getVendedorAllStr(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<VendedorBean> vendedor = this.getVendedorAll(sesion);
		
		String[] vend = new String[vendedor.size()];
		int i = 0;
		Iterator dI = vendedor.iterator();
		while(dI.hasNext()) {
			VendedorBean v = (VendedorBean)dI.next();
			vend[i++] = new String(v.toString());
		}
		return vend;
	}
	public String[] getVendedorActStr(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<VendedorBean> vendedor = this.getVendedorAll(sesion);
		
		String[] vend = new String[vendedor.size()];
		int i = 0;
		Iterator dI = vendedor.iterator();
		while(dI.hasNext()) {
			VendedorBean v = (VendedorBean)dI.next();
			if(v.usar()) {
				vend[i++] = new String(v.toString());
			}
		}
		return vend;
	}
}