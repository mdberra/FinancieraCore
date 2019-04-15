package com.financiera.domain.service;

import java.util.List;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.VendedorBean;

public interface VendedorService extends Service {
	public void actualizar(VendedorBean v, String usuario);
	public VendedorBean getVendedor(Long id, Session sesion);
	public VendedorBean getVendedor(Long id);
	public VendedorBean getVendedor(String codigo, Session sesion);
	public String getVendedorStr(Long id, String usuario);
	public List<VendedorBean> getVendedorAll(Session sesion);
	public String[] getVendedorAllStr(String usuario);
	public String[] getVendedorActStr(String usuario);
}