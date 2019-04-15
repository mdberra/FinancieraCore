package com.financiera.domain.bean;

import java.io.Serializable;
import java.util.Date;

import com.dto.ServicioDTO;
import com.financiera.core.util.DateTimeUtil;

public class ServicioBean extends Persistible implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long				id;
	private ClienteBean			cliente;
	private ProductoBean		producto;
	private double				importeTotal;
	private int					cantCuota;
	private double				importeCuota;
	private VendedorBean		vendedor;
	private double				comision;
	private Date				fechaVenta;
	private int					perPrimerDisparo;
	private int					ultCuotaDebitada;
	private String				estado;
	private String				motivo;
	private Date				fechaIngreso;
	private Date				ultFechaConMovimientos;
	private Long				contId;

	public boolean servicioDisponible() {
		if(this.ultCuotaDebitada < this.cantCuota)
			return true;
		else
			return false;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("servicio"); 			sb.append(";");
		sb.append(id);					sb.append(";");
		sb.append(cliente.getId());		sb.append(";");
		sb.append(producto.toString());
		sb.append(importeTotal);		sb.append(";");
		sb.append(cantCuota);			sb.append(";");
		sb.append(importeCuota);		sb.append(";");
		sb.append(vendedor.toString());
		sb.append(comision);			sb.append(";");
		
		String fecha = DateTimeUtil.formatDate(fechaVenta);
		if(fecha == null || fecha.compareTo("") == 0) {
			sb.append(" "); sb.append(";");
		}else{
			sb.append(fecha); sb.append(";");
		}
		sb.append(perPrimerDisparo);	sb.append(";");
		sb.append(ultCuotaDebitada);	sb.append(";");
		sb.append(estado);				sb.append(";");
		sb.append(motivo);				sb.append(";");

		fecha = DateTimeUtil.formatDate(fechaIngreso);
		if(fecha == null || fecha.compareTo("") == 0) {
			sb.append(" "); sb.append(";");
		}else{
			sb.append(fecha); sb.append(";");
		}

		fecha = DateTimeUtil.formatDate(ultFechaConMovimientos);
		if(fecha == null || fecha.compareTo("") == 0) {
			sb.append(" "); sb.append(";");
		}else{
			sb.append(fecha); sb.append(";");
		}
		sb.append(contId);				sb.append(";");
		
		return super.agregarBlancoANull(sb.toString());
	}
	public String backupBajar() throws Exception {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("servicio"); 			sb.append(";");
			sb.append(id);					sb.append(";");
			sb.append(cliente.getId());		sb.append(";");
			sb.append(producto.getId());	sb.append(";");
			sb.append(importeTotal); 		sb.append(";");
			sb.append(cantCuota);			sb.append(";");
			sb.append(importeCuota);		sb.append(";");
			sb.append(vendedor.getId());	sb.append(";");
			sb.append(comision);			sb.append(";");
			
			String fecha = DateTimeUtil.formatDate(fechaVenta);
			if(fecha == null || fecha.compareTo("") == 0) {
				sb.append(" "); sb.append(";");
			}else{
				sb.append(fecha); sb.append(";");
			}
			sb.append(perPrimerDisparo);	sb.append(";");
			sb.append(ultCuotaDebitada);	sb.append(";");
			sb.append(estado);				sb.append(";");
			sb.append(motivo);				sb.append(";");
	
			fecha = DateTimeUtil.formatDate(fechaIngreso);
			if(fecha == null || fecha.compareTo("") == 0) {
				sb.append(" "); sb.append(";");
			}else{
				sb.append(fecha); sb.append(";");
			}
			fecha = DateTimeUtil.formatDate(ultFechaConMovimientos);
			if(fecha == null || fecha.compareTo("") == 0) {
				sb.append(" "); sb.append(";");
			}else{
				sb.append(fecha); sb.append(";");
			}
			sb.append(contId);				sb.append(";");
		} catch(Exception e) {
			throw new Exception("ServicioBean " + e.getMessage());
		}
		return super.agregarBlancoANull(sb.toString());
	}

	public int getPeriodoComenzarCuotas() {
		return DateTimeUtil.getPeriodoSig();
	}

	public boolean isAprobado() {
		return (this.getEstado().compareTo(ServicioDTO.APROBADO) == 0);
	}
	public ClienteBean getCliente() {
		return cliente;
	}
	public void setCliente(ClienteBean cliente) {
		this.cliente = cliente;
	}
	public Date getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	public void setFechaVenta(String fechaVenta) {
		try {
			this.fechaVenta = DateTimeUtil.getDate(fechaVenta);
		} catch(Exception e) {
			this.fechaVenta = null;
		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getPerPrimerDisparo() {
		return perPrimerDisparo;
	}
	public void setPerPrimerDisparo(int perPrimerDisparo) {
		this.perPrimerDisparo = perPrimerDisparo;
	}
	public int getUltCuotaDebitada() {
		return ultCuotaDebitada;
	}
	public void setUltCuotaDebitada(int ultCuotaDebitada) {
		this.ultCuotaDebitada = ultCuotaDebitada;
	}
	public VendedorBean getVendedor() {
		return vendedor;
	}
	public void setVendedor(VendedorBean vendedor) {
		this.vendedor = vendedor;
	}
	public double getComision() {
		return comision;
	}
	public void setComision(double comision) {
		this.comision = comision;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public int getCantCuota() {
		return cantCuota;
	}
	public void setCantCuota(int cantCuota) {
		this.cantCuota = cantCuota;
	}
	public double getImporteCuota() {
		return importeCuota;
	}
	public void setImporteCuota(double importeCuota) {
		this.importeCuota = importeCuota;
	}
	public double getImporteTotal() {
		return importeTotal;
	}
	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public ProductoBean getProducto() {
		return producto;
	}
	public void setProducto(ProductoBean producto) {
		this.producto = producto;
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
		} catch(Exception e) {
			this.fechaIngreso = null;
		}
	}
	public Date getUltFechaConMovimientos() {
		return ultFechaConMovimientos;
	}
	public void setUltFechaConMovimientos(Date ultFechaConMovimientos) {
		this.ultFechaConMovimientos = ultFechaConMovimientos;
	}
	public void setUltFechaConMovimientos(String ultFechaConMovimientos) {
		try {
			this.ultFechaConMovimientos = DateTimeUtil.getDate(ultFechaConMovimientos);
		} catch(Exception e) {
			this.ultFechaConMovimientos = null;
		}
	}
	public Long getContId() {
		return contId;
	}
	public void setContId(Long contId) {
		this.contId = contId;
	}	
}