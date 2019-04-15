package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import com.financiera.core.server.Persistencia;
import com.financiera.core.util.DateTimeUtil;

public class DelegacionBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int K_UTILIZAR = 0;
	public static final int K_NO_UTILIZAR = 1;
	
	private Long				id;
	private String				codigo;
	private String				descripcion;
	private LocalizacionBean	localizacion;
	private EntidadBean 		entidad;
	private BancoBean			banco;
	private Date				f_alta;
	private int					utilizar;
	
	public Collection<DiasCobroBean> getDiasCobro(Session sesion) {
		Collection<DiasCobroBean> c = new ArrayList<DiasCobroBean>();
		
		Query q = sesion.getNamedQuery("DiasCobroBean.findByDelegacion");
		q.setLong("idDelegacion", this.getId());
		Collection dias = q.list();
		Iterator dI = dias.iterator();
		while(dI.hasNext()) {
			c.add((DiasCobroBean)dI.next());
		}
		return c;
	}
	public boolean usar() {
		boolean usar = true;
		
		switch(this.getUtilizar()) {
		case K_UTILIZAR:    usar = true; break;
		case K_NO_UTILIZAR: usar = false; break;
		}
		return usar;
	}
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("delegacion");	sb.append(";");
			sb.append(id);				sb.append(";");
			sb.append(codigo);			sb.append(";");
			sb.append(descripcion); 	sb.append(";");
			sb.append(DateTimeUtil.formatDate(f_alta)); sb.append(";");
			sb.append(localizacion.getId());	sb.append(";");
			sb.append(entidad.getId());	sb.append(";");
			sb.append(banco.getId());	sb.append(";");
			sb.append(utilizar);		sb.append(";");
		} catch(Exception e) {
			throw new Exception("DelegacionBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}
	
	public String getDatosDelegacion(Session sesion) {
		StringBuffer sb = new StringBuffer();
		
		DiasCobroBean dcBean;
		Iterator dcBeanIt = this.getDiasCobro(sesion).iterator();
		if(dcBeanIt.hasNext()) {
			sb.append("Dias de Cobro: ");
		}
		while(dcBeanIt.hasNext()) {
			dcBean = (DiasCobroBean)dcBeanIt.next();
			sb.append(dcBean.getFechaDisparoStr() + " ");
		}
		
		return new String(descripcion + " - " + banco.getDescripcion() +
			" - " + sb.toString()); 
	}

	public BancoBean getBanco() {
		return banco;
	}
	public void setBanco(BancoBean banco) {
		this.banco = banco;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public EntidadBean getEntidad() {
		return entidad;
	}
	public void setEntidad(EntidadBean entidad) {
		this.entidad = entidad;
	}
	public LocalizacionBean getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(LocalizacionBean localizacion) {
		this.localizacion = localizacion;
	}
	public Date getF_alta() {
		return f_alta;
	}
	public void setF_alta(Date f_alta) {
		this.f_alta = f_alta;
	}
	public void setF_alta(String f_alta) {
		try {
			this.f_alta = DateTimeUtil.getDate(f_alta);
		} catch(Exception e) { }
	}
	public int getUtilizar() {
		return utilizar;
	}
	public void setUtilizar(int utilizar) {
		this.utilizar = utilizar;
	}
}