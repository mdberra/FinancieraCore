package com.financiera.domain.bean;

import java.io.Serializable;

public class BancoBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String BAPRO = "Bapro";
	public static final String SUPERVILLE = "SuperVille";
	public static final String NACION = "Nacion";
	public static final String ITAU = "Banco Itau";
	public static final String BICA = "Banco Bica";
	
	private Long		 id;
	private String		 codigo;
	private String		 descripcion;
	private LocalizacionBean localizacion;
	private int			 codigoDebito;
	private String		 descripPrestacion;
	private String		 bancoRecaudador;

	public boolean isBaproSVille() {
		if(this.bancoRecaudador.compareTo(BancoBean.BAPRO) == 0 ||
		   this.bancoRecaudador.compareTo(BancoBean.SUPERVILLE) == 0)
			return true;
		else
			return false;
	}
	public boolean isNacion() {
		if(this.bancoRecaudador.compareTo(BancoBean.NACION) == 0)
			return true;
		else
			return false;
	}
	public boolean isITAU() {
		if(this.bancoRecaudador.compareTo(BancoBean.ITAU) == 0)
			return true;
		else
			return false;
	}
	public boolean isBICA() {
		if(this.bancoRecaudador.compareTo(BancoBean.BICA) == 0)
			return true;
		else
			return false;
	}

	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("banco"); 				sb.append(";");
			sb.append(id);						sb.append(";");
			sb.append(codigo);					sb.append(";");
			sb.append(descripcion); 			sb.append(";");
			sb.append(localizacion.getId());	sb.append(";");
			sb.append(codigoDebito);			sb.append(";");
			sb.append(descripPrestacion);		sb.append(";");
			sb.append(bancoRecaudador);			sb.append(";");
		} catch(Exception e) {
			throw new Exception("BancoBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
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
	public int getCodigoDebito() {
		return codigoDebito;
	}
	public void setCodigoDebito(int codigoDebito) {
		this.codigoDebito = codigoDebito;
	}
	public LocalizacionBean getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(LocalizacionBean localizacion) {
		this.localizacion = localizacion;
	}
	public String getDescripPrestacion() {
		return descripPrestacion;
	}
	public void setDescripPrestacion(String descripPrestacion) {
		this.descripPrestacion = descripPrestacion;
	}
	public String getBancoRecaudador() {
		return bancoRecaudador;
	}
	public void setBancoRecaudador(String bancoRecaudador) {
		this.bancoRecaudador = bancoRecaudador;
	}
}