package com.financiera.domain.servicio.impl;

import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.ServiceLocator;
import com.financiera.domain.bean.VendedorBean;
import com.financiera.domain.service.VendedorService;
import com.financiera.domain.servicio.VendedoresServicio;

public class VendedoresServicioImpl extends AbstractService implements VendedoresServicio {
	private VendedorService vendedor = (VendedorService) ServiceLocator.getInstance().getService(VendedorService.class);
	
	public VendedoresServicioImpl(){
		super();
		this.setDescription("Servicio para Vendedores");
		this.setName("VendedoresServicio");
	}

	public void actualizarVendedor(Long idVendedor, String codigo, String nombre, int utilizar, String usuario) {
		VendedorBean d = new VendedorBean();
		d.setId(idVendedor);
		d.setCodigo(codigo);
		d.setNombre(nombre);
		d.setUtilizar(utilizar);
		vendedor.actualizar(d, usuario);
	}
}