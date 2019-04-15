package com.financiera.domain.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.dto.ServicioDTO;
import com.financiera.core.server.AbstractService;
import com.financiera.core.server.CacheManager;
import com.financiera.core.server.Persistencia;
import com.financiera.domain.bean.Persistible;
import com.financiera.domain.bean.ProductoBean;
import com.financiera.domain.bean.ServicioBean;
import com.financiera.domain.service.ServicioService;


public class ServicioServiceImpl extends AbstractService implements ServicioService {
	private ServicioBean servicioBean;
	
	public ServicioServiceImpl(){
		super();
		this.setDescription("Servicio para Servicio");
		this.setName("ServicioService");
	}

	public void persistirStr(ServicioBean s, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		this.persistir(s, sesion);
	}
	public void persistir(ServicioBean s, Session session) {
//		Transaction transaction = session.beginTransaction();
		try{
			if(s.getAccion() == Persistible.UPDATE) {
				servicioBean = (ServicioBean)session.get(ServicioBean.class, s.getId());
				
				this.movesServicio(s);

				session.update(servicioBean);
			}
			if(s.getAccion() == Persistible.INSERT) {
				servicioBean = new ServicioBean();
				
				this.movesServicio(s);
				session.save(servicioBean);
			}
		} catch (Exception e) {
			System.out.println("Error al persistir : " + e.getMessage());
		}
	}
	private void movesServicio(ServicioBean input) {
		servicioBean.setCliente(input.getCliente());
		servicioBean.setProducto(input.getProducto());
		servicioBean.setImporteTotal(input.getImporteTotal());
		servicioBean.setCantCuota(input.getCantCuota());
		servicioBean.setImporteCuota(input.getImporteCuota());
		servicioBean.setVendedor(input.getVendedor());
		servicioBean.setComision(input.getComision());
		servicioBean.setFechaVenta(input.getFechaVenta());
		servicioBean.setPerPrimerDisparo(input.getPerPrimerDisparo());
		servicioBean.setUltCuotaDebitada(input.getUltCuotaDebitada());
		servicioBean.setEstado(input.getEstado());
		servicioBean.setMotivo(input.getMotivo());
		servicioBean.setFechaIngreso(input.getFechaIngreso());
		servicioBean.setUltFechaConMovimientos(input.getUltFechaConMovimientos());
		servicioBean.setContId(input.getContId());
	}
	
	public ServicioBean getServicioById(Long id, Session sesion) {
		ServicioBean servicio = null;
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ServicioBean.getId");
			q.setLong("id", id);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				 servicio = (ServicioBean)cI.next();
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
		return servicio;
	}
	public String getServicioByIdStr(Long id, String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		return this.getServicioById(id, sesion).toString();
	}
	
	public List<ProductoBean> getProductos(Session sesion) {
		List<ProductoBean> productos =  new ArrayList<ProductoBean>();
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ProductoBean.getAll");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				productos.add((ProductoBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
		return productos;
	}
	public String[] getProductosStr(String usuario) {
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		List<ProductoBean> producto = this.getProductos(sesion);
		String[] prod = new String[producto.size() + 1];
		int j = 0;
		Iterator cI = producto.iterator();
		while(cI.hasNext()) {
			ProductoBean pp = (ProductoBean)cI.next();
			prod[j++] = pp.toString();
		}
		prod[j++] = new String("total;"+ j);
		return prod;
	}
	
	public String[] getServicioByFechaIngresoOrderVendedor(Date fechaDesde, Date fechaHasta, String usuario) {
		String[] s = null;
		int i = 0;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ServicioBean.getByFechaIngresoOrderVendedor");
			q.setDate("fDesde", fechaDesde);
			q.setDate("fHasta", fechaHasta);
			Iterator cI = q.list().iterator();
			s = new String[q.list().size()];
			while(cI.hasNext()) {
				s[i++] = ((ServicioBean)cI.next()).toString();
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
		return s;
	}
	public String[] getServicioByFechaVentaOrderFecha(Date fechaDesde, Date fechaHasta, String usuario) {
		String[] s = null;
		int i = 0;
		Session sesion = CacheManager.getInstance().getUsuarioSesion(usuario);
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ServicioBean.getByFechaVentaOrderFecha");
			q.setDate("fDesde", fechaDesde);
			q.setDate("fHasta", fechaHasta);
			Iterator cI = q.list().iterator();
			s = new String[q.list().size()];
			while(cI.hasNext()) {
				s[i++] = ((ServicioBean)cI.next()).toString();
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
		return s;
	}
	public void actualizarDivididos(Session sesion) {
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ServicioBean.getDivididos");
			q.setString("estado", ServicioDTO.DIVIDIDO);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				ServicioBean s = ((ServicioBean)cI.next());
				s.setEstado(ServicioDTO.APROBADO);
				sesion.update(s);
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
	}
	public Collection<ServicioBean> getServiciosHist(Session sesion) {
		Collection<ServicioBean> s = new ArrayList<ServicioBean>();
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ServicioBean.getDivididos");
			q.setString("estado", ServicioDTO.FINALIZADO);
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				s.add(((ServicioBean)cI.next()));
			}
			
			q.setString("estado", ServicioDTO.RECHAZADO);
			cI = q.list().iterator();
			while(cI.hasNext()) {
				s.add(((ServicioBean)cI.next()));
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
		return s;
	}
	public Vector<Long> getServiciosHistId(Session sesion) {
		Vector<Long> v = new Vector<Long>();
		Collection<ServicioBean> s = this.getServiciosHist(sesion);
		Iterator<ServicioBean> si = s.iterator();
		while(si.hasNext()) {
			ServicioBean serv = (ServicioBean)si.next();
			if(!serv.isAprobado()){
				v.add(serv.getId());
			}
		}
		return v;
	}
	public Hashtable<Long, ServicioBean> getServiciosAprobados(Session sesion) {
		Hashtable<Long, ServicioBean> s = new Hashtable<Long, ServicioBean>();
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ServicioBean.getAll");
			Iterator<ServicioBean> cI = q.list().iterator();
			while(cI.hasNext()) {
				ServicioBean serv = (ServicioBean)cI.next();
				if(serv.isAprobado()){
					s.put(serv.getId(), serv);
				}
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
		return s;
	}

	public List<ServicioBean> getServicios(Long idCliente, Session sesion) {
		List<ServicioBean> servicios =  new ArrayList<ServicioBean>();
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ServicioBean.getCliente");
			q.setLong("idCliente", idCliente);
			Iterator<ServicioBean> cI = q.list().iterator();
			while(cI.hasNext()) {
				servicios.add((ServicioBean)cI.next());
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
		return servicios;
	}
	public Collection<ServicioBean> getServiciosArca(Session sesion) {
		Collection<ServicioBean> s = new ArrayList<ServicioBean>();
		
		Transaction transaction = sesion.beginTransaction();
		try{
			transaction.begin();
			Query q = sesion.getNamedQuery("ServicioBean.getAll");
			Iterator cI = q.list().iterator();
			while(cI.hasNext()) {
				ServicioBean ss = (ServicioBean)cI.next();
				if(ss.getVendedor().vendedorArca()) {
					s.add(ss);
				}
			}
		} catch(Exception e) {
			System.out.println("ServicioService : " + e.getMessage());
		}
		return s;
	}
	public Vector<Long> getServiciosArcaId(Session sesion) {
		Vector<Long> v = new Vector<Long>();
	
		Collection<ServicioBean> s = this.getServiciosArca(sesion);
		Iterator<ServicioBean> si = s.iterator();
		while(si.hasNext()) {
			ServicioBean ss = (ServicioBean)si.next();
			v.add(ss.getId());
		}
		return v;
	}
}