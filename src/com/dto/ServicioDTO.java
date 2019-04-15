package com.dto;


public class ServicioDTO {
	public static final Long NUEVO = new Long(999999999);
	public static final String APROBADO			= "Aprobado";
	public static final String RECHAZADO		= "Rechazado";
	public static final String FINALIZADO		= "Finalizado";
	public static final String DIVIDIDO			= "Dividido";
	
	private Long		id;
	private Long		idCliente;
	private ProductoDTO	producto;
	private double		importeTotal;
	private int			cantCuota;
	private double		importeCuota;
	private VendedorDTO	vendedor;
	private double		comision;
	private String		fechaVenta;
	private int			perPrimerDisparo;
	private int			ultCuotaDebitada;
	private String		estado;
	private String		motivo;
	private String		fechaIngreso;
	private String		ultFechaConMovimientos;
	private Long		contId;
	
	public ServicioDTO() {
		this.producto = new ProductoDTO();
		this.vendedor = new VendedorDTO();
	}
	public void armarPartirCliDTO(ClienteDTO clienteDTO) {
		this.setId(ServicioDTO.NUEVO);
		this.setIdCliente(clienteDTO.getId());
		this.setEstado(APROBADO);
	}
	public ServicioDTO(String servicio) {
		Parser p = new Parser();
		p.parsear(servicio);

		if(p.esNull())
			p.getTokenString();
		else
			this.id	= p.getTokenLong();
		
		if(p.esNull())
			p.getTokenString();
		else
			this.idCliente = p.getTokenLong();
// tarifa
		String sb = new String();
		for(int i=0; i<5; i++) {
			sb += p.getTokenString().concat(";");
		}
		this.producto = new ProductoDTO(sb);
		this.importeTotal		= p.getTokenDouble();
		this.cantCuota			= p.getTokenInteger();
		this.importeCuota		= p.getTokenDouble();
//vendedor		
		sb = new String();
		for(int i=0; i<5; i++) {
			sb += p.getTokenString().concat(";");
		}
		this.vendedor	= new VendedorDTO(sb);
		
		this.comision			= p.getTokenDouble();
		this.fechaVenta			= p.getTokenString();
		this.perPrimerDisparo	= p.getTokenInteger();
		this.ultCuotaDebitada	= p.getTokenInteger();
		this.estado				= p.getTokenString();
		this.motivo				= p.getTokenString();
		this.fechaIngreso		= p.getTokenString();
		this.ultFechaConMovimientos = p.getTokenString();
		this.contId				= p.getTokenLong();
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("servicio"); 			sb.append(";");
		sb.append(id);					sb.append(";");
		sb.append(idCliente);			sb.append(";");
		sb.append(producto.toString());
		sb.append(importeTotal);		sb.append(";");
		sb.append(cantCuota);			sb.append(";");
		sb.append(importeCuota);		sb.append(";");
		sb.append(vendedor.toString());
		sb.append(comision);			sb.append(";");
		
		if(this.fechaVenta == null) {
			sb.append(" "); sb.append(";");
		}else{
			sb.append(this.fechaVenta.toString()); sb.append(";");
		}
		sb.append(perPrimerDisparo);	sb.append(";");
		sb.append(ultCuotaDebitada);	sb.append(";");
		sb.append(estado);				sb.append(";");
		sb.append(motivo);				sb.append(";");
		
		if(this.fechaIngreso == null) {
			sb.append(" "); sb.append(";");
		}else{
			sb.append(this.fechaIngreso.toString()); sb.append(";");
		}
		
		if(this.ultFechaConMovimientos == null) {
			sb.append(" "); sb.append(";");
		}else{
			sb.append(this.ultFechaConMovimientos.toString()); sb.append(";");
		}
		sb.append(contId);				sb.append(";");
		
		return sb.toString();
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
	public String getFechaVenta() {
		return fechaVenta;
	}
	public void setFechaVenta(String fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
	public VendedorDTO getVendedor() {
		return vendedor;
	}
	public void setVendedor(VendedorDTO vendedor) {
		this.vendedor = vendedor;
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
	public ProductoDTO getProducto() {
		return producto;
	}
	public void setProducto(ProductoDTO producto) {
		this.producto = producto;
	}
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public String getUltFechaConMovimientos() {
		return ultFechaConMovimientos;
	}
	public void setUltFechaConMovimientos(String ultFechaConMovimientos) {
		this.ultFechaConMovimientos = ultFechaConMovimientos;
	}
	public Long getContId() {
		return contId;
	}
	public void setContId(Long contId) {
		this.contId = contId;
	}
}