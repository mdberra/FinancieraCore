package com.financiera.domain.service.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.Persistencia;
import com.financiera.domain.bean.ProductoBean;
import com.financiera.domain.bean.VendedorBean;
import com.financiera.domain.service.ProductoService;


public class ProductoServiceImpl extends AbstractService implements ProductoService {
	
	public ProductoServiceImpl(){
		super();
		this.setDescription("Servicio para Producto");
		this.setName("ProducoService");
	}

	public void agregar(ProductoBean p, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			sesion.save(p);
		}finally{
			transaction.commit();
		}
	}
	
	public ProductoBean getProducto(Long id, Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			return (ProductoBean)sesion.get(ProductoBean.class, id);
		} finally {
			transaction.commit();
		}
	}
	public ProductoBean getProducto(Long id) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(new String("Administrador"));
		ProductoBean p = this.getProducto(id, sesion);
		return p;
	}
}