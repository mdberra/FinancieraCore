package com.financiera.domain.bean;

import java.io.Serializable;

public class EstadoMovBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;
// Estado
	public static final int E0_MES_CUOTA = 1;
	public static final int E0_A_DISPARAR = 100; // son los que se generan en forma manual
	public static final int E1_DISPARADO = 2;
	public static final int E2_DEBITADO = 3;
	public static final int E4_REVERSION = 5;
	public static final int E5_DEVUELTO = 6;
	public static final int E0_DIVIDIDO = 102;
	
// Color
	public static final int C1_NARANJA = 1;
	public static final int C2_AZUL = 2;
	public static final int C3_VERDE = 3;
	public static final int C4_ROJO = 4;
	public static final int C5_NEGRO = 5;
	public static final int C6_AMARILLO = 6;
	
	private Long	id;
	private int		estado;
	private int  	color;
	private String  codRechazo;
	
	public boolean esDebitado() {
		return (this.estado == E2_DEBITADO);
	}
	public boolean esReversion() {
		return (this.estado == E4_REVERSION);
	}
	public boolean esDevolucion() {
		return (this.estado == E5_DEVUELTO);
	}
	public boolean esRechazo() {
		return (this.color == C4_ROJO);
	}
	public boolean esProcesableEstad() {
		return (this.esDebitado() || this.esRechazo() || this.esReversion() || this.esDevolucion());
	}
	public boolean eliminable() {
		if(this.estado != EstadoMovBean.E0_MES_CUOTA  &&
		   this.estado != EstadoMovBean.E0_A_DISPARAR &&
		   this.estado != EstadoMovBean.E1_DISPARADO  &&
		   this.estado != EstadoMovBean.E2_DEBITADO   &&
		   this.estado != EstadoMovBean.E4_REVERSION  &&
		   this.estado != EstadoMovBean.E5_DEVUELTO  &&
		   this.estado != EstadoMovBean.E0_DIVIDIDO)
			return true;
		else
			return false;
	}
	public static final int PENDIENTE = 0;
	public static final int DEBITO    = 1;
	public static final int RECHAZO   = 2;
	public static final int REVERSION = 3;
	public static final int DEVUELTO  = 4;
	public int analisisEstado() {
		int out = 0;
		switch(this.color) {
			case EstadoMovBean.C1_NARANJA:  out = 0; break;
			case EstadoMovBean.C2_AZUL:		out = 0; break;
			case EstadoMovBean.C3_VERDE:	out = 1; break;
			case EstadoMovBean.C4_ROJO:		out = 2; break;
			case EstadoMovBean.C5_NEGRO:	out = 3; break;
			case EstadoMovBean.C6_AMARILLO: out = 4; break;
		}
		return out;
	}
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("estadoMov");	sb.append(";");
			sb.append(id);			sb.append(";");
			sb.append(estado);		sb.append(";");
			sb.append(color); 		sb.append(";");
			sb.append(codRechazo);	sb.append(";");
		} catch(Exception e) {
			throw new Exception("EstadoMovBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodRechazo() {
		return codRechazo;
	}
	public void setCodRechazo(String codRechazo) {
		this.codRechazo = codRechazo;
	}
}