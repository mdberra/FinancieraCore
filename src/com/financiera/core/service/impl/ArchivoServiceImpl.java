package com.financiera.core.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.financiera.core.server.AbstractService;
import com.financiera.core.service.ArchivoService;

public class ArchivoServiceImpl extends AbstractService implements ArchivoService{
	Collection<File> c = new ArrayList<File>();
	private int ii = 0;
	
	public ArchivoServiceImpl(){
		super();
		this.setDescription("Servicio para Archivos");
		this.setName("Archivo");
	}

	public void clearFiles() {
		c.clear();
	}
	
	public void buscarFiles(File directorio, String extension) {
		File files[] = directorio.listFiles();
		for(int i=0; i<files.length; i++) {
			File f = files[i];
			if(f.isDirectory()) {
				this.buscarFiles(f, extension);
			} else {
				if(f.isFile()) {
					String s = f.getName();
					if(s.indexOf(";")>0) {
						String s1 = s.substring(s.indexOf("."),s.indexOf(";"));
						if(s1.toUpperCase().compareTo(extension)==0) {
							this.agregar(f);
						}
					}
				} else {
					System.out.println("no es directorio ni file");
				}
			}
		}
	}
	public void buscarFilesCommon(File directorio, String extension) {
		File files[] = directorio.listFiles();
		for(int i=0; i<files.length; i++) {
			File f = files[i];
			if(f.isDirectory()) {
				this.buscarFiles(f, extension);
			} else {
				if(f.isFile()) {
					this.agregar(f);
				} else {
					System.out.println("no es directorio ni file");
				}
			}
		}
	}
	private void agregar(File f) {
		c.add(f);
	}
	
	public void recorrerFiles() throws Exception {
		Collection<File> fuentes = this.getFiles();
		Iterator fuente = fuentes.iterator();
		while(fuente.hasNext()) {
			File f = (File)fuente.next();
			if(++ii % 100 == 0)
				System.out.println("Procesando " + ii + " File : " + f.getName());
			procesarFile(f);
		}
	}
	public Collection<File> getFiles() {
		return c;
	}
// Se debe sobrescribir
	public void procesarFile(File f) throws Exception {
// Por cada file se recorren las lineas
		Collection lineas = this.getData(f);
		Iterator linea = lineas.iterator();
		while(linea.hasNext()) {
			String l = (String)linea.next();
			procesarLinea(l);
		}
	}
	
//	 Se debe sobrescribir
	public void procesarLinea(String linea) throws Exception {
	}
	

// Devuelve una Collection<String> con el contenido de un file
	public Collection<String> getData(String inputFileName) throws Exception {
		File f = new File(inputFileName);
		if (!f.exists())
	    	throw new Exception("No existe el Archivo de Datos " + inputFileName);
	    else {
	    	return this.getData(f);
	    }
	}
	public Collection<String> getData(File f) throws Exception {
		Collection<String> cData = new ArrayList<String>();

		String inputFileName= f.getAbsolutePath();
		BufferedReader entrada = this.abrirFile(inputFileName);
	    String linea = entrada.readLine();
	    while(linea != null) {
	    	cData.add(linea);
	    	linea = entrada.readLine();
	    } 
		return cData;
	}
	
	public BufferedReader abrirFile(String inputFileName) throws Exception {
		File f = new File(inputFileName);
		if (!f.exists())
	    	throw new Exception("No existe el Archivo de Datos " + inputFileName);
	    else {
	    	BufferedReader entrada = new BufferedReader( new FileReader( f ) );
	     	return entrada;
	    }
	}
	public PrintWriter abrirFileSalida(String outputFileName) throws Exception {
		return new PrintWriter( new BufferedWriter(new FileWriter(outputFileName)));
	}
	public boolean eliminarFile(String fileName) throws Exception {
		File f = new File(fileName);
		if (f.exists())
			return f.delete();
		else
			return false;
	}
	
	public void copiar(File fOrig, String pathDestino) throws Exception {
		Files.copy(fOrig.toPath(),  (new File(pathDestino + fOrig.getName())).toPath(),  StandardCopyOption.REPLACE_EXISTING);
	}
}