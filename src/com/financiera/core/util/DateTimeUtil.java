package com.financiera.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class DateTimeUtil {
	private static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final String DATE_FORMAT1 = "ddMMyyyy";
	private static final String DATE_FORMAT2 = "yyyyMMdd";
	private static final String DATE_FORMAT3 = "ddMM";
	private static final String DATE_FORMAT4 = "yyyy-MM-dd";
	private static final String DATETIME_FORMAT1 = "ddMMyyyyHHmmss";
	/**
	 * Le da a una fecha el formato correcto usado
	 */
	public static String formatDate(Date fecha) {
		String dateFormated = "";
		if (fecha != null) {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			dateFormated = df.format(fecha);
		}
		return dateFormated;
	}
	public static String formatDateDD_MM_AAAA(Date fecha) {
		String dateFormated = "";
		if (fecha != null) {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			dateFormated = df.format(fecha);
		}
		return dateFormated;
	}
	public static String formatDateDDMMAAAA(Date fecha) {
		String dateFormated = "";
		if (fecha != null) {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT1);
			dateFormated = df.format(fecha);
		}
		return dateFormated;
	}
	
	public static String formatDateDDMM(Date fecha) {
		String dateFormated = "";
		if (fecha != null) {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT3);
			dateFormated = df.format(fecha);
		}
		return dateFormated;
	}

	public static String formatPeriodoAAAAMMDD(Date fecha) {
		String dateFormated = "";
		if (fecha != null) {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT2);
			dateFormated = df.format(fecha);
		}
		return dateFormated.substring(0, 6);
	}
	public static String formatDateAAAAMMDD(Date fecha) {
		String dateFormated = "";
		if (fecha != null) {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT2);
			dateFormated = df.format(fecha);
		}
		return dateFormated;
	}
	/**
	 * Le da a una fecha y hora el formato correcto usado
	 */
	public static String formatDateTime(Date fecha) {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat(DATETIME_FORMAT);
		sb.append(formatter.format(fecha));

		return sb.toString();
	}
	public static String formatDateTime1(Date fecha) {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat(DATETIME_FORMAT1);
		sb.append(formatter.format(fecha));

		return sb.toString();
	}

	/**
	 * Convierte un string a una fecha
	 */
	public static Date getDate(String fecha) throws ParseException {
		if (Util.isBlank(fecha)) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(DATE_FORMAT);

		return df.parse(fecha);
	}
	public static Date getDateDDMMAAAA(String fecha) throws ParseException {
		if (Util.isBlank(fecha)) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(DATE_FORMAT1);

		return df.parse(fecha);
	}
	public static Date getDateAAAAMMDD(String fecha) throws ParseException {
		if (Util.isBlank(fecha)) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(DATE_FORMAT2);

		return df.parse(fecha);
	}
	public static Date getDateAAAAbMMbDD(String fecha) throws ParseException {
		if (Util.isBlank(fecha)) {
			return null;
		}
		DateFormat df = new SimpleDateFormat(DATE_FORMAT4);

		return df.parse(fecha);
	}
	public static int getPeriodoAAAMMDD(Date fecha) {
		String f = formatDateAAAAMMDD(fecha);
		
		return Integer.parseInt(f.substring(0,6));
	}
	public static int getDiaAAAAMMDD(Date fecha) {
		String f = formatDateAAAAMMDD(fecha);
		
		return Integer.parseInt(f.substring(6,8));
	}
	public static String getPeriodoDD_MM_AAAA(Date fecha) {
		String f = formatDateDD_MM_AAAA(fecha);
		String anio = f.substring(6,10);
		String mes = f.substring(3,5);
		String periodo = anio + mes;
		return periodo;
	}
	public static String Sub1Periodo(String periodo) throws ParseException {
		int anio = Integer.parseInt(periodo.substring(0, 4));
		int mes  = Integer.parseInt(periodo.substring(4, 6));

		if(mes == 1) {
			anio--;
			mes = 12;
		} else {
			mes--;
		}
		
		String smes  = Util.formateo(Util.NUMERICO, 2, String.valueOf(mes), false);
		String sanio = Util.formateo(Util.NUMERICO, 4, String.valueOf(anio), false);
		String speriodo = sanio.concat(smes);
		
		return speriodo;
	}
	
	public static int getDiaActual() {
		 String s = formatDateAAAAMMDD(getDate());
		 String s1 = s.substring(7, 8);
		 return Integer.valueOf(s1);
	}
	public static int getPeriodoActual() {
		 String s = formatDateAAAAMMDD(getDate());
		 String s1 = s.substring(0, 6);
		 return Integer.valueOf(s1);
	}
	public static int getPeriodoSig() {
		String s1 = null;
		try {
			String s = formatDateAAAAMMDD(add1Mes(getDate()));
			s1 = s.substring(0, 6);
		} catch(Exception e) {}
		
		return Integer.valueOf(s1);
	}
	public static Date add1Mes(Date fecha) throws ParseException {
		String f = formatDateDDMMAAAA(fecha);
		int dia  = Integer.parseInt(f.substring(0, 2));
		int mes  = Integer.parseInt(f.substring(2, 4));
		int anio = Integer.parseInt(f.substring(4, 8));
		
		if(mes == 12) {
			anio++;
			mes = 0;
		}
		
		mes++;
		
		String sdia  = Util.formateo(Util.NUMERICO, 2, String.valueOf(dia), false);
		String smes  = Util.formateo(Util.NUMERICO, 2, String.valueOf(mes), false);
		String sanio = Util.formateo(Util.NUMERICO, 4, String.valueOf(anio), false);
		String f1 = sdia.concat(smes).concat(sanio);
		
		return getDateDDMMAAAA(f1);
	}
	public static Date sub1Mes(Date fecha) throws ParseException {
		String f = formatDateDDMMAAAA(fecha);
		int dia  = Integer.parseInt(f.substring(0, 2));
		int mes  = Integer.parseInt(f.substring(2, 4));
		int anio = Integer.parseInt(f.substring(4, 8));
		
		if(mes == 1) {
			anio--;
			mes = 13;
		}
		
		mes--;
		
		String sdia  = Util.formateo(Util.NUMERICO, 2, String.valueOf(dia), false);
		String smes  = Util.formateo(Util.NUMERICO, 2, String.valueOf(mes), false);
		String sanio = Util.formateo(Util.NUMERICO, 4, String.valueOf(anio), false);
		String f1 = sdia.concat(smes).concat(sanio);
		
		return getDateDDMMAAAA(f1);
	}
	public static Date get2MesesAtras() throws ParseException {
		String fechaHoy = DateTimeUtil.formatDateAAAAMMDD(DateTimeUtil.getDate());
		String periodo1 = DateTimeUtil.Sub1Periodo(fechaHoy.substring(0, 6));
		String periodo2 = DateTimeUtil.Sub1Periodo(periodo1);
		String fecha = periodo2 + fechaHoy.substring(6, 8);
		return DateTimeUtil.getDateAAAAMMDD(fecha);
	}
	public static Date getNMesesAtras(int meses) throws ParseException {
		String periodo = String.valueOf(getPeriodoActual());
		for(int i=0;i<meses;i++) {
			periodo = DateTimeUtil.Sub1Periodo(periodo);
		}
		String fecha = periodo + "20";
		return DateTimeUtil.getDateAAAAMMDD(fecha);
	}
	public static Date getFechaInicio(int diaCorte) throws ParseException {
		if(getDiaActual() > diaCorte) {
			return getFechaDia1(getPeriodoSig());
		} else {
			return getFechaDia1(getPeriodoActual());
		}
	}
	public static Date getFechaDia1(int periodo) throws ParseException {
		return getDateAAAAMMDD(new String(String.valueOf(periodo).concat("01")));
	}
	public static Date getFechaDia28(int periodo) throws ParseException {
		return getDateAAAAMMDD(new String(String.valueOf(periodo).concat("28")));
	}
	public static Date getFechaDia1(Date d) throws ParseException {
		return getFechaDia1(getPeriodoAAAMMDD(d));
	}
	public static Date getFechaDia28(Date d) throws ParseException {
		return getFechaDia28(getPeriodoAAAMMDD(d));
	}
	public static boolean esDia01(Date d) throws ParseException {
		if(getDiaAAAAMMDD(d) == 1)
			return true;
		else
			return false;
	}
	/**
	 * Obtiene la fecha actual
	 */
	public static Date getDate() {
		// return
		// getDate(formatDate(GregorianCalendar.getInstance().getTime()));
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	/**
	 * Obtiene la fecha actual, en formato String y formateada
	 */
	public static String getStringFormatDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND, 0);
		return formatDate(c.getTime());
	}
	
	/**
	 * Obtiene la fecha y hora actual, en formato String y formateada
	 */
	public static String getStringFormatDateTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND, 0);
		return formatDateTime(c.getTime());
	}
	public static String getPeriodoNacion(Date fecha) {
		String periodo = null;
		String f = DateTimeUtil.formatDateDDMMAAAA(fecha);
		int mes = Integer.parseInt(f.substring(2, 4));
		int dia = Integer.parseInt(f.substring(0, 2));
		int anio = Integer.parseInt(f.substring(4, 8));
		periodo = mesToString(mes);
		return new String(periodo).concat(String.valueOf(anio));
	}
	public static String getPeriodoNacionSort(Date fecha) {
		String f = DateTimeUtil.formatDateDDMMAAAA(fecha);
		return new String(f.substring(4, 8) + f.substring(2, 4));
	}
	public static String getPeriodo(Date fecha) {
		String periodo = null;
		String f = DateTimeUtil.formatDateDDMMAAAA(fecha);
		int mes = Integer.parseInt(f.substring(2, 4));
		int dia = Integer.parseInt(f.substring(0, 2));
		if(dia > 19) {
			mes += 1;
		}
		int anio = Integer.parseInt(f.substring(4, 8));
		if(mes == 13) {
			mes = 1;
			anio += 1;
		}
		periodo = mesToString(mes);
		return new String(periodo).concat(String.valueOf(anio));
	}
	public static String getPeriodoSort(Date fecha) {
		String f = DateTimeUtil.formatDateDDMMAAAA(fecha);
		int mes = Integer.parseInt(f.substring(2, 4));
		int dia = Integer.parseInt(f.substring(0, 2));
		if(dia > 19) {
			mes += 1;
		}
		int anio = Integer.parseInt(f.substring(4, 8));
		if(mes == 13) {
			mes = 1;
			anio += 1;
		}
		String smes  = Util.formateo(Util.NUMERICO, 2, String.valueOf(mes), false);
		return new String(String.valueOf(anio) + smes);
	}
	private static String mesToString(int mes) {
		String periodo = null;
		switch(mes) {
			case 1  : periodo = "Enero-"; break; 
			case 2  : periodo = "Febrero-"; break;
			case 3  : periodo = "Marzo-"; break;
			case 4  : periodo = "Abril-"; break;
			case 5  : periodo = "Mayo-"; break;
			case 6  : periodo = "Junio-"; break;
			case 7  : periodo = "Julio-"; break;
			case 8  : periodo = "Agosto-"; break;
			case 9  : periodo = "Septiembre-"; break;
			case 10 : periodo = "Octubre-"; break;
			case 11 : periodo = "Noviembre-"; break;
			case 12 : periodo = "Diciembre-"; break;
		}
		return periodo;
	}
	public static String getNroPeriodo(String periodo) {
		StringTokenizer st = new StringTokenizer(periodo, "-");
		st.nextToken();
		String anio = st.nextToken();
		return anio.concat("-").concat(getNroMesStr(periodo));
	}
	public static String getFecha1_Periodo(String periodo) {
		return getNroPeriodo(periodo).concat("-01");
	}
	public static int getNroMes(String periodo) {
		int mes = 1;
		
		if(periodo.startsWith("Enero-")) {
			mes = 1;
		}
		if(periodo.startsWith("Febrero-")) {
			mes = 2;
		}
		if(periodo.startsWith("Marzo-")) {
			mes = 3;
		}
		if(periodo.startsWith("Abril-")) {
			mes = 4;
		}
		if(periodo.startsWith("Mayo-")) {
			mes = 5;
		}
		if(periodo.startsWith("Junio-")) {
			mes = 6;
		}
		if(periodo.startsWith("Julio-")) {
			mes = 7;
		}
		if(periodo.startsWith("Agosto-")) {
			mes = 8;
		}
		if(periodo.startsWith("Septiembre-")) {
			mes = 9;
		}
		if(periodo.startsWith("Octubre-")) {
			mes = 10;
		}
		if(periodo.startsWith("Noviembre-")) {
			mes = 11;
		}
		if(periodo.startsWith("Diciembre-")) {
			mes = 12;
		}
		return mes;
	}
	public static String getNroMesStr(String periodo) {
		String mes = "01";
		
		if(periodo.startsWith("Enero-")) {
			mes = "01";
		}
		if(periodo.startsWith("Febrero-")) {
			mes = "02";
		}
		if(periodo.startsWith("Marzo-")) {
			mes = "03";
		}
		if(periodo.startsWith("Abril-")) {
			mes = "04";
		}
		if(periodo.startsWith("Mayo-")) {
			mes = "05";
		}
		if(periodo.startsWith("Junio-")) {
			mes = "06";
		}
		if(periodo.startsWith("Julio-")) {
			mes = "07";
		}
		if(periodo.startsWith("Agosto-")) {
			mes = "08";
		}
		if(periodo.startsWith("Septiembre-")) {
			mes = "09";
		}
		if(periodo.startsWith("Octubre-")) {
			mes = "10";
		}
		if(periodo.startsWith("Noviembre-")) {
			mes = "11";
		}
		if(periodo.startsWith("Diciembre-")) {
			mes = "12";
		}
		return mes;
	}
}