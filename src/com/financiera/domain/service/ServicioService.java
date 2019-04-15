package com.financiera.domain.service;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.hibernate.Session;

import com.financiera.core.server.Service;
import com.financiera.domain.bean.ProductoBean;
import com.financiera.domain.bean.ServicioBean;

public interface ServicioService extends Service {

	public void persistirStr(ServicioBean s, String usuario);
	public void persistir(ServicioBean s, Session session);
	
	public ServicioBean getServicioById(Long id, Session sesion);
	public String getServicioByIdStr(Long id, String usuario);
	
	public List<ProductoBean> getProductos(Session sesion);
	public String[] getProductosStr(String usuario);
	
	public String[] getServicioByFechaIngresoOrderVendedor(Date fechaDesde, Date fechaHasta, String usuario);
	public String[] getServicioByFechaVentaOrderFecha(Date fechaDesde, Date fechaHasta, String usuario);
	public void actualizarDivididos(Session sesion);
	public Collection<ServicioBean> getServiciosHist(Session sesion);
	public Vector<Long> getServiciosHistId(Session sesion);
	public Hashtable<Long, ServicioBean> getServiciosAprobados(Session sesion);
	public List<ServicioBean> getServicios(Long idCliente, Session sesion);
	public Collection<ServicioBean> getServiciosArca(Session sesion);
	public Vector<Long> getServiciosArcaId(Session sesion);
}