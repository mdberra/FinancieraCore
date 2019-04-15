package com.dto;

public class Parser {
	private char separador = ';';
	private int k = 1;
	private String[] salida = new String[700];
	private int cantidad;
	
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public void restarK() {
		this.k--;
	}
	public void parsear(String entrada) {
		cantidad = 0;
		salida[cantidad] = "";
		char[] chars = entrada.toCharArray();
		for(int i=0; i< chars.length; i++) {
			if(chars[i] == separador) {
				cantidad++;
				salida[cantidad] = "";
			} else {
				salida[cantidad] += chars[i];
			}
		}
	}
	public int getCantidad() {
		return cantidad;
	}
	public boolean hayMas() {
		if(k < cantidad)
			return true;
		else
			return false;
	}
	public boolean hayMasIg() {
		if(k <= cantidad)
			return true;
		else
			return false;
	}
	public boolean esNull() {
		return (salida[k].indexOf("null") == 0 || salida[k].trim().equals(""));		
	}
	public String getTokenString() {
		return new String(salida[k++]);
	}
	public Long getTokenLong() {
		return new Long(salida[k++]);
	}
	public int getTokenInteger() {
		return Integer.parseInt(salida[k++]);
	}
	public double getTokenDouble() {
		return Double.parseDouble(salida[k++]);
	}
	public void changeSeparador(char c) {
		separador = c;
	}
}