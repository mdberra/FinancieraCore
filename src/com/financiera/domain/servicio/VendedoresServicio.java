package com.financiera.domain.servicio;

import com.financiera.core.server.Service;

public interface VendedoresServicio extends Service {

	public void actualizarVendedor(Long idVendedor, String codigo, String nombre, int utilizar, String usuario);
}