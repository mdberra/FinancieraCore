package com.financiera.domain.service;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.ProductoBean;

public interface ProductoService extends Service {

	public void agregar(ProductoBean p, Session sesion);
	public ProductoBean getProducto(Long id);
	public ProductoBean getProducto(Long id, Session sesion);
}