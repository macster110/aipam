package com.jamdev.maven.aipam.utils;

import org.apache.commons.math3.complex.Complex;

public class ComplexArray extends ComplexArrayF implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public ComplexArray(Complex[] complexArr) {
		super(complexArr);
		// TODO Auto-generated constructor stub
	}

	public ComplexArray(float[] realData, float[] imagData) {
		super(realData, imagData);
		// TODO Auto-generated constructor stub
	}

	public ComplexArray(float[] complexData) {
		super(complexData);
		// TODO Auto-generated constructor stub
	}

	public ComplexArray(int n) {
		super(n);
		// TODO Auto-generated constructor stub
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ComplexArray clone() {
		ComplexArray newOne;
		try {
			newOne = (ComplexArray) super.clone();
			newOne.data = data.clone();
			return newOne;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}




}
