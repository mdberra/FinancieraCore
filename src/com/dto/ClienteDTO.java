package com.dto;



public class ClienteDTO {
	public static final Long NUEVO = new Long(999999999);
	
	private Parser p = new Parser();
	
	private Long		id;
	private String		tipoDoc;
	private int			nroDoc;
	private String		cbu;
	private String		sucursalCA;
	private String		nroCA;
	private String		nombre;
	private String		apellido;
	private String		cantCuotas;

	public static final int SERVINA = 1;
	public static final int ARCA = 2;
	public static final int COMPARTIDO = 3;
	private int			esClienteDe;
	
	private String		fechaIngreso;
	private String		comentarios;
	private String		locParticular;
	private String		locLaboral;
	private String		locInformado;
	private Long		idDelegacion;
	private String		delegDescripcion;
	private String		bancoDescripcion;
	private String[]	servicios;
	private String[]	movimientos;
	private String		telefonosParticular = new String();
	private String		telefonosLaboral = new String();
	private String		telefonosInformado = new String();
	
	public static final int ACTIVO = 0;
	public static final String ACTIVO_STR = "Activo";
	public static final int EMBARGO = 1;
	public static final String EMBARGO_STR = "Embargo";
	public static final int FINALIZADO = 2;
	public static final String FINALIZADO_STR = "Finalizado";
	public static final int ENVIADOJUAN = 3;
	public static final String ENVIADOJUAN_STR = "Enviado a Juan";
	public static final int INCOBRABLE = 4;
	public static final String INCOBRABLE_STR = "Incobrable";
	private int			estado;
	private String		fechaEstado;
	
	public static final int OK = 0;
	public static final int YA_VIENE_EMBARGADO = 1;
	private int			estadoAnterior;
	private String		aux;

	public ClienteDTO() {}
	public ClienteDTO(String cliente) {
		p.parsear(cliente);

		this.id					= p.getTokenLong();
		this.tipoDoc			= p.getTokenString();
		this.nroDoc				= p.getTokenInteger();
		this.cbu				= p.getTokenString();
		this.sucursalCA			= p.getTokenString();
		this.nroCA				= p.getTokenString();
		this.nombre				= p.getTokenString();
		this.apellido			= p.getTokenString();
		this.cantCuotas			= p.getTokenString();
		this.fechaIngreso		= p.getTokenString();
		this.comentarios		= p.getTokenString();
		
		String sb = new String();
		for(int i=0; i<13; i++) {
			aux = p.getTokenString().concat(";");
			sb += aux;
			if(i==6 || i==7) {
				telefonosParticular += aux;
				telefonosParticular.concat(";");
			}
		}
		this.locParticular = sb;
		
		sb = new String();
		for(int i=0; i<13; i++) {
			aux = p.getTokenString().concat(";");
			sb += aux;
			if(i==6 || i==7) {
				telefonosLaboral += aux;
				telefonosLaboral.concat(";");
			}
		}
		this.locLaboral = sb;
		
		sb = new String();
		for(int i=0; i<13; i++) {
			aux = p.getTokenString().concat(";");
			sb += aux;
			if(i==6 || i==7) {
				telefonosInformado += aux;
				telefonosInformado.concat(";");
			}
		}
		this.locInformado = sb;

		this.estado				= p.getTokenInteger();
		this.fechaEstado		= p.getTokenString();
		this.estadoAnterior		= p.getTokenInteger();
		
		this.idDelegacion		= p.getTokenLong();
		this.delegDescripcion	= p.getTokenString();
		this.bancoDescripcion	= p.getTokenString();
	
		this.esClienteDe = 0;
		if(p.hayMas()) {
			int n = p.getTokenInteger();
			this.servicios = new String[n];
			for(int j=0; j<n; j++) {
				sb = new String();
				for(int i=0; i<25; i++) {
					sb += p.getTokenString().concat(";");
				}
				this.servicios[j] = sb;
				ServicioDTO sss = new ServicioDTO(sb);
				
				if(this.esClienteDe == 0) {
					if(sss.getVendedor().esArca())
						this.setEsClienteDe(ClienteDTO.ARCA);
					else
						this.setEsClienteDe(ClienteDTO.SERVINA);
				} else {
					if(this.esClienteDe == ClienteDTO.SERVINA) {
						if(sss.getVendedor().esArca())
							this.setEsClienteDe(ClienteDTO.COMPARTIDO);
						else
							this.setEsClienteDe(ClienteDTO.SERVINA);

					} else {
						if(this.esClienteDe == ClienteDTO.ARCA) {
							if(sss.getVendedor().esArca())
								this.setEsClienteDe(ClienteDTO.ARCA);
							else
								this.setEsClienteDe(ClienteDTO.COMPARTIDO);
						}
					}
				}
					
			}
			servicios = Varios.ordenar(servicios);
		}
		try {
			if(p.hayMas()) {
				int n = p.getTokenInteger();
				this.movimientos = new String[n];
				for(int j=0; j<n; j++) {
					if(!p.esNull()) {
						sb = new String();
						for(int i=0; i<11; i++) {
							sb += p.getTokenString().concat(";");
						}
						this.movimientos[j] = sb;
					}
				}
				movimientos = Varios.ordenar(movimientos);
			}
		} catch(Exception e) {
			p.restarK();
		}
	}
	public int getK() {
		return p.getK();
	}
	
	public String toString() {
		String fecha = " ";
		String fechaEstado = " ";
		String servicio = " ";
		String movimiento = " ";
		
		if(servicios != null) {
			servicio = new String(String.valueOf(servicios.length) + ";");
			for(int i=0; i<servicios.length; i++) {
				servicio += servicios[i];
			}
		}
		
		if(movimientos != null) {
			movimiento = new String(String.valueOf(movimientos.length) + ";");
			for(int i=0; i<movimientos.length; i++) {
				movimiento += movimientos[i];
			}
		}
		return new String("cliente;" + String.valueOf(this.id) + ";" + this.tipoDoc + ";" + String.valueOf(this.nroDoc) + ";" +
			this.cbu + ";" + this.sucursalCA + ";" + this.nroCA + ";" + this.nombre + ";" + this.apellido + ";" +
			this.cantCuotas + ";" + fecha + ";" + this.comentarios + ";" + this.locParticular +
			this.locLaboral + this.locInformado + this.estado + ";" + fechaEstado + ";" +
			this.estadoAnterior + ";" + String.valueOf(this.idDelegacion) + ";" + this.delegDescripcion + ";" +
			this.bancoDescripcion + ";" + servicio + movimiento);
	}
	
	public String getDatosCliente() {
		String est = null;

		switch(estado) {
		case ACTIVO:     est = ACTIVO_STR;     break;
		case EMBARGO:    est = EMBARGO_STR;    break;
		case FINALIZADO: est = FINALIZADO_STR; break;
		case ENVIADOJUAN: est = ENVIADOJUAN_STR; break;
		case INCOBRABLE: est = INCOBRABLE_STR; break;
		}
		return new String(id + " - " + tipoDoc + " " + nroDoc + "   Nombre: " +
				apellido.concat(", ").concat(nombre) +
				"   Delegacion: " + delegDescripcion +
				"   Banco: " + bancoDescripcion + "   Estado: " + est);
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdDelegacion() {
		return idDelegacion;
	}
	public void setIdDelegacion(Long idDelegacion) {
		this.idDelegacion = idDelegacion;
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
	public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getDelegDescripcion() {
		return delegDescripcion;
	}
	public void setDelegDescripcion(String delegDescripcion) {
		this.delegDescripcion = delegDescripcion;
	}
	public String getBancoDescripcion() {
		return bancoDescripcion;
	}
	public void setBancoDescripcion(String bancoDescripcion) {
		this.bancoDescripcion = bancoDescripcion;
	}
	public String getLocLaboral() {
		return locLaboral;
	}
	public void setLocLaboral(String locLaboral) {
		this.locLaboral = locLaboral;
	}
	public String getLocParticular() {
		return locParticular;
	}
	public void setLocParticular(String locParticular) {
		this.locParticular = locParticular;
	}
	public String getLocInformado() {
		return locInformado;
	}
	public void setLocInformado(String locInformado) {
		this.locInformado = locInformado;
	}
	public String[] getServicios() {
		servicios = Varios.ordenar(servicios);
		return servicios;
	}
	public void setServicios(String[] servicios) {
		this.servicios = servicios;
	}
	public String getApellidoyNombre() {
		return this.apellido + ", " + this.nombre;
	}
	public int getEstado() {
		return estado;
	}
	public String getEstadoStr() {
		switch(this.estado) {
		case 0: return ClienteDTO.ACTIVO_STR;
		case 1: return ClienteDTO.EMBARGO_STR;
		case 2: return ClienteDTO.FINALIZADO_STR;
		case 3: return ClienteDTO.ENVIADOJUAN_STR;
		case 4: return ClienteDTO.INCOBRABLE_STR;
		default : return "Estado desconocido";
		}
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
	public String[] getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(String[] movimientos) {
		this.movimientos = movimientos;
	}
	public String getFechaEstado() {
		return fechaEstado;
	}
	public void setFechaEstado(String fechaEstado) {
		this.fechaEstado = fechaEstado;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
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
	public String getTelefonosParticular() {
		return telefonosParticular;
	}
	public String getTelefonosLaboral() {
		return telefonosLaboral;
	}
	public String getTelefonosInformado() {
		return telefonosInformado;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	public String getCantCuotas() {
		return cantCuotas;
	}
	public void setCantCuotas(String cantCuotas) {
		this.cantCuotas = cantCuotas;
	}
	public int getEsClienteDe() {
		return esClienteDe;
	}
	public void setEsClienteDe(int esClienteDe) {
		this.esClienteDe = esClienteDe;
	}
	public boolean esSoloServina() {
		if(this.getEsClienteDe() == ClienteDTO.SERVINA)
			return true;
		else
			return false;
	}
	public boolean esCompartido() {
		if(this.getEsClienteDe() == ClienteDTO.COMPARTIDO)
			return true;
		else
			return false;
	}
}