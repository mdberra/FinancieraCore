package com.financiera.core.util;


public final class Cuit {
	
	private static int prefijo;
	private static int dni;
	private static int digito;
	
// Sexo	
	public static String FEMENINO = "Femenino";
	public static String MASCULINO = "Masculino";
	
// Tipo de Persona
	public static String FISICA = "Fisica";
	public static String JURIDICA = "Juridica";
	
/**
 * Validar un numero de CUIT/CUIL.
 *  @param cuit N de CUIT/CUIL como String
 *  @return Boolean: true si el CUIT/CUIL es correcto, false en caso contrario
 **/
	public static boolean validarPersonaFisica(String cuit, String sexo) {
		return validar(cuit, sexo, Cuit.FISICA);
	}
	public static boolean validarPersonaJuridica(String cuit) {
		return validar(cuit, Cuit.MASCULINO, Cuit.JURIDICA);
	}
	public static boolean validar(String cuit, String sexo, String tipo) {
		if(Util.isBlank(cuit) || cuit.length() < 3)
			return false;
		
		if(sexo.equals("F"))
			sexo = Cuit.FEMENINO;
		else
			sexo = Cuit.MASCULINO;
		
		if(tipo.equals(Cuit.FISICA)) {
			if(sexo.equals(Cuit.FEMENINO)) {
				if(!(cuit.substring(0,2).equals("23") || cuit.substring(0,2).equals("27")))
					return false;
			} else {
		    	if(sexo.equals(Cuit.MASCULINO))
		    		if(!(cuit.substring(0,2).equals("20") || cuit.substring(0,2).equals("23")))
		    			return false;
			}
		} else {
			if(tipo.equals(Cuit.JURIDICA)) {
				if(!(cuit.substring(0,2).equals("28") || cuit.substring(0,2).equals("30") ||
					 cuit.substring(0,2).equals("31") || cuit.substring(0,2).equals("33") ||
					 cuit.substring(0,2).equals("99")))
	    			return false;
			}
		}
		
	
		String prefijoStr, dniStr, digitoStr;
		int digitoTmp;
		int n = cuit.lastIndexOf("-");

		prefijoStr = cuit.substring(0, 2);
		dniStr     = cuit.substring(cuit.indexOf("-") + 1, n);
		digitoStr  = cuit.substring(n + 1, n + 2);
		
		if(prefijoStr.length() != 2 || dniStr.length() > 8 || digitoStr.length() != 1)
			return false;
		try {
			prefijo   = Integer.parseInt(prefijoStr);
			dni       = Integer.parseInt(dniStr);
			digitoTmp = Integer.parseInt(digitoStr);
			
		} catch (NumberFormatException e) {
			return false;
		}
		
	
		calcular();
		
		if(digito == digitoTmp)
			return true;
		else
			return false;
	}
/**
 * Metodo privado q calcula el CUIT
 **/
	private static void calcular() {
		long tmp1, tmp2;
		long acum = 0;
		int n = 2;
		
		tmp1 = prefijo * 100000000L + dni;
		
		for (int i = 0; i < 10; i++) {
			tmp2 = tmp1 / 10;
			acum += (tmp1 - tmp2 * 10L) * n;
			tmp1 = tmp2;
			if (n < 7)
				n++;
			else
				n = 2;
		}
		n = (int)(11 - acum % 11);
		if (n == 10) {
			if (prefijo == 20 || prefijo == 27 || prefijo == 24)
				prefijo = 23;
			else
				prefijo = 33;
/**
 * No es necesario hacer la llamada recursiva a calcular(),
 * se puede poner el digito en 9 si el prefijo original era
 * 23 o 33 o poner el dijito en 4 si el prefijo era 27
 **/
			calcular();
		} else {
			if (n == 11)
				digito = 0;
			else
				digito = n;
			}
	}
}