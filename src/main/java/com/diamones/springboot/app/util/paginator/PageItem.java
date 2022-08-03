package com.diamones.springboot.app.util.paginator;

public class PageItem {
	
	public PageItem(int numero, boolean actual) {

		this.numero = numero;
		this.actual = actual;
	}
	
	public int getNumero() {
		return numero;
	}
	public boolean isActual() {
		return actual;
	}
	
	private int numero;
	
	private boolean actual;

}
