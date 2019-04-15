package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.financiera.core.util.DateTimeUtil;

public class ClienteBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long				id;
	private String		 		tipoDoc;
	private int			 		nroDoc;
	private String		 		cbu;
	private String				sucursalCA;
	private String				nroCA;
	private String		 		nombre;
	private String		 		apellido;
	private List<String>		cantCuotas;
	private Date				fechaIngreso;
	private String				comentarios;
	
	private LocalizacionBean	locParticular;
	private LocalizacionBean 	locLaboral;
	private LocalizacionBean 	locInformado;

	private Long				idDelegacion;
	private DelegacionBean		delegacion;
	private Set<ServicioBean>	servicios;
	private Set<MovimientoBean> movimientos;
	
	public static final int ACTIVO = 0;
	public static final int EMBARGO = 1;
	public static final int FINALIZADO = 2;
	public static final int ENVIADOJUAN = 3;
	public static final int INCOBRABLE = 4;
	private int					estado;
	private Date				fechaEstado;
	
	public static final int OK = 0;
	public static final int YA_VIENE_EMBARGADO = 1;
	private int					estadoAnterior;
	private ServicioBean sv; 	
	
	public String getDatosCliente() {
		return new String(tipoDoc + " " + nroDoc + "   Nombre: " + this.getNyA() +
			"   Delegacion: " + delegacion.getDescripcion() + "   Banco: " + delegacion.getBanco().getDescripcion() + "   Estado: " + this.getEstadoString());
	}
	public boolean isActivo() {
		if(this.estado == ClienteBean.ACTIVO)
			return true;
		else
			return false;
	}
	public String getNyA() {
		return apellido.concat(", ").concat(nombre);
	}
	public String getEstadoString() {
		String est = null;
		switch(estado) {
		case ACTIVO:      est = "Activo";     break;
		case EMBARGO:     est = "Embargo";    break;
		case FINALIZADO:  est = "Finalizado"; break;
		case ENVIADOJUAN: est = "Enviado a Juan"; break;
		case INCOBRABLE:  est = "Incobrable"; break;
		}
		return est;
	}
	public boolean esquemaBapro() {
		BancoBean b = this.getDelegacion().getBanco();
		
		if(b.getBancoRecaudador().compareTo(BancoBean.BAPRO)==0)
			return true;
		else
			return false;
	}

	public boolean esquemaSVille() {
		BancoBean b = this.getDelegacion().getBanco();
		
		if(b.getBancoRecaudador().compareTo(BancoBean.SUPERVILLE)==0)
			return true;
		else
			return false;
	}
	public boolean esquemaNacion() {
		BancoBean b = this.getDelegacion().getBanco();
		
		if(b.getBancoRecaudador().compareTo(BancoBean.NACION)==0)
			return true;
		else
			return false;
	}
	public boolean esquemaItau() {
		BancoBean b = this.getDelegacion().getBanco();
		
		if(b.getBancoRecaudador().compareTo(BancoBean.ITAU)==0)
			return true;
		else
			return false;
	}
	public boolean esquemaBica() {
		BancoBean b = this.getDelegacion().getBanco();
		
		if(b.getBancoRecaudador().compareTo(BancoBean.BICA)==0)
			return true;
		else
			return false;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("cliente"); 				sb.append(";");
		sb.append(id);						sb.append(";");
		sb.append(tipoDoc);					sb.append(";");
		sb.append(nroDoc); 					sb.append(";");
		sb.append(cbu);						sb.append(";");
		sb.append(sucursalCA);				sb.append(";");
		sb.append(nroCA);					sb.append(";");
		sb.append(nombre);					sb.append(";");
		sb.append(apellido);				sb.append(";");
		cantCuotas = new ArrayList<String>();
		Iterator it = servicios.iterator();
		while(it.hasNext()) {
			sv = (ServicioBean)it.next();
			if(!cantCuotas.contains(String.valueOf(sv.getCantCuota()))) {
				cantCuotas.add(String.valueOf(sv.getCantCuota()));
			}
		}
		StringBuffer sb1 = new StringBuffer();
		it = cantCuotas.iterator();
		while(it.hasNext()) {
			String ss = (String)it.next();
			sb1.append(ss);  sb1.append(",");
		}
		if(sb1.length()==0) {
			sb.append("NO tiene servicios;");
		} else {
			sb1.replace(sb1.length()-1, sb1.length(), ";");
			sb.append(sb1.toString());
		}
		if(fechaIngreso == null) {
			sb.append(" ;");
		} else {
			sb.append(DateTimeUtil.formatDate(fechaIngreso)); sb.append(";");
		}
		sb.append(comentarios); sb.append(";");
		sb.append(locParticular.toString());
		sb.append(locLaboral.toString());
		try {
			sb.append(locInformado.toString());
		} catch (Exception e) {
			sb.append(" ; ; ; ; ; ; ; ; ; ; ; ; ;");
		}
		sb.append(estado);					sb.append(";");
		if(fechaEstado == null) {
			sb.append(" ;");
		} else {
			sb.append(DateTimeUtil.formatDate(fechaEstado)); sb.append(";");
		}
		sb.append(estadoAnterior);			sb.append(";");
		sb.append(idDelegacion);			sb.append(";");
		sb.append(delegacion.getDescripcion()); sb.append(";");
		sb.append(delegacion.getBanco().getDescripcion());	sb.append(";");
		sb.append(servicios.size()); sb.append(";");
		it = servicios.iterator();
		while(it.hasNext()) {
			sv = (ServicioBean)it.next();
			sb.append(sv.toString());
		}
		return super.agregarBlancoANull(sb.toString());
	}
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("cliente"); 				sb.append(";");
			sb.append(id);						sb.append(";");
			sb.append(tipoDoc);					sb.append(";");
			sb.append(nroDoc); 					sb.append(";");
			sb.append(cbu);						sb.append(";");
			sb.append(sucursalCA);				sb.append(";");
			sb.append(nroCA);					sb.append(";");
			sb.append(nombre);					sb.append(";");
			sb.append(apellido);				sb.append(";");
			if(fechaIngreso == null) {
				sb.append(" ;");
			} else {
				sb.append(DateTimeUtil.formatDate(fechaIngreso)); sb.append(";");
			}
//			sb.append(" ;");// para hacer el cambio
			sb.append(comentarios);				sb.append(";");
			sb.append(locParticular.getId());	sb.append(";");
			sb.append(locLaboral.getId());		sb.append(";");
			sb.append(locInformado.getId());	sb.append(";");
			
			sb.append(estado);					sb.append(";");
			if(fechaEstado == null) {
				sb.append(" ;");
			} else {
				sb.append(DateTimeUtil.formatDate(fechaEstado)); sb.append(";");
			}
			sb.append(estadoAnterior);			sb.append(";");
	
			sb.append(idDelegacion);			sb.append(";");
		} catch(Exception e) {
			throw new Exception("ClienteBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}
	public String[] serviciosToString() {
		String[] servicios = new String[this.getServicios().size()];
		Iterator servI = this.getServicios().iterator();
		int i = 0;
		while(servI.hasNext()) {
			ServicioBean servicio = (ServicioBean)servI.next();
			
			servicios[i++] = servicio.toString();
			
		}
		return servicios;
	}
	
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getCbu() {
		return cbu;
	}
	public void setCbu(String cbu) {
		this.cbu = cbu;
	}
	public DelegacionBean getDelegacion() {
		return delegacion;
	}
	public void setDelegacion(DelegacionBean delegacion) {
		this.delegacion = delegacion;
	}
	public Date getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		try {
			this.fechaIngreso = DateTimeUtil.getDate(fechaIngreso);
		} catch(Exception e) { }
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalizacionBean getLocLaboral() {
		return locLaboral;
	}
	public void setLocLaboral(LocalizacionBean locLaboral) {
		this.locLaboral = locLaboral;
	}
	public LocalizacionBean getLocParticular() {
		return locParticular;
	}
	public void setLocParticular(LocalizacionBean locParticular) {
		this.locParticular = locParticular;
	}
	public LocalizacionBean getLocInformado() {
		return locInformado;
	}
	public void setLocInformado(LocalizacionBean locInformado) {
		this.locInformado = locInformado;
	}
	public Set<MovimientoBean> getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(Set<MovimientoBean> movimientos) {
		this.movimientos = movimientos;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getNroDoc() {
		return nroDoc;
	}
	public void setNroDoc(int nroDoc) {
		this.nroDoc = nroDoc;
	}
	public Set<ServicioBean> getServicios() {
		return servicios;
	}
	public void setServicios(Set<ServicioBean> servicios) {
		this.servicios = servicios;
	}
	public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public Long getIdDelegacion() {
		return idDelegacion;
	}
	public void setIdDelegacion(Long idDelegacion) {
		this.idDelegacion = idDelegacion;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public int getEstadoAnterior() {
		return estadoAnterior;
	}
	public void setEstadoAnterior(int estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}
	public Date getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public void setFechaEstado(String fechaEstado) {
		try {
			this.fechaEstado = DateTimeUtil.getDate(fechaEstado);
		} catch(Exception e) { }
	}
	public String getNroCA() {
		return nroCA;
	}
	public void setNroCA(String nroCA) {
		this.nroCA = nroCA;
	}
	public String getSucursalCA() {
		return sucursalCA;
	}
	public void setSucursalCA(String sucursalCA) {
		this.sucursalCA = sucursalCA;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
}