package com.dto;

import com.financiera.domain.bean.DelegacionBean;

public class TotalesDesgloseDTO {

	private DelegacionBean delegacion;
	private String periodo;
	
	private int  pendienteCant;
	private double pendienteImporte;
	
	private int debitoCant;
	private double debitoImporte;
	
	private int rechazoCant;
	private double rechazoImporte;
	
	private int reversionCant;
	private double reversionImporte;

	private int devueltoCant;
	private double devueltoImporte;

	public void incrDebitoCant() {
		debitoCant += 1;
	}
	public void incrPendienteCant() {
		pendienteCant += 1;
	}
	public void incrRechazoCant() {
		rechazoCant += 1;
	}
	public void incrReversionCant() {
		reversionCant += 1;
	}
	public void incrDevueltoCant() {
		devueltoCant += 1;
	}
	
	public void addDebitoCant(int c) {
		debitoCant += c;
	}
	public void addPendienteCant(int c) {
		pendienteCant += c;
	}
	public void addRechazoCant(int c) {
		rechazoCant += c;
	}
	public void addReversionCant(int c) {
		reversionCant += c;
	}
	public void addDevueltoCant(int c) {
		devueltoCant += c;
	}
	
	public void addDebitoImporte(double importe) {
		debitoImporte += importe;
	}
	public void addPendienteImporte(double importe) {
		pendienteImporte += importe;
	}
	public void addRechazoImporte(double importe) {
		rechazoImporte += importe;
	}
	public void addReversionImporte(double importe) {
		reversionImporte += importe;
	}
	public void addDevueltoImporte(double importe) {
		devueltoImporte += importe;
	}
	
	public DelegacionBean getDelegacion() {
		return delegacion;
	}
	public void setDelegacion(DelegacionBean delegacion) {
		this.delegacion = delegacion;
	}
	public int getDebitoCant() {
		return debitoCant;
	}
	public double getDebitoImporte() {
		return debitoImporte;
	}
	public int getPendienteCant() {
		return pendienteCant;
	}
	public double getPendienteImporte() {
		return pendienteImporte;
	}
	public int getRechazoCant() {
		return rechazoCant;
	}
	public double getRechazoImporte() {
		return rechazoImporte;
	}
	public int getReversionCant() {
		return reversionCant;
	}
	public double getReversionImporte() {
		return reversionImporte;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public int getDevueltoCant() {
		return devueltoCant;
	}
	public double getDevueltoImporte() {
		return devueltoImporte;
	}
	public void setRechazoCant(int rechazoCant) {
		this.rechazoCant = rechazoCant;
	}
	public void setRechazoImporte(double rechazoImporte) {
		this.rechazoImporte = rechazoImporte;
	}
	
}