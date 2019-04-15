package com.financiera.core.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;

import com.financiera.core.server.Service;

public interface ArchivoService extends Service {
	
	public void clearFiles();
	public void buscarFiles(File directorio, String extension);
	public void buscarFilesCommon(File directorio, String extension);
	public void recorrerFiles() throws Exception;
	public Collection<File> getFiles();
	
//	 Se debe sobrescribir
	public void procesarFile(File f) throws Exception;
//	 Se debe sobrescribir
	public void procesarLinea(String linea) throws Exception;
	
	public Collection<String> getData(String inputFileName) throws Exception;
	public Collection<String> getData(File f) throws Exception;
	public BufferedReader abrirFile(String inputFileName) throws Exception;
	public PrintWriter abrirFileSalida(String outputFileName) throws Exception;
	public boolean eliminarFile(String fileName) throws Exception;
	public void copiar(File fOrig, String pathDestino) throws Exception;
}