package com.dto;

public class Varios {
	public static String imageItemHTML(String imageUrl, String title) {
	    return "<span><img style='margin-right:4px' src='" + imageUrl.toLowerCase()
	        + "'>" + title + "</span>";
	}
	
	public static String[] ordenar(String[] input) {
		String aux;
		for(int x = 0; x < input.length; x++) {
			aux = input[x];
			for(int y = x+1; y < input.length; y++) {
				if(input[y].compareTo(aux) < 0) {
					input[x] = input[y];
					input[y] = aux;
					aux = input[x];
				}
			}
		}
		return input;
	}
}
