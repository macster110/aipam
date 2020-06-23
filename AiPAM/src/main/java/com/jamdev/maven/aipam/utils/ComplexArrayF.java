package com.jamdev.maven.aipam.utils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Class for handling arrays of Complex data. 
 * <p>
 * This class should be used wherever possible in preference to 
 * arrays of Complex objects since this class realised purely 
 * on an array of primitives, assumed to be in real, imaginary
 * order which should give a speed improvement over previous methods. 
 * <p>
 * functions exist within this to convert to an array of Complex
 * objects to support current PAMGUARD functionality but it is hoped
 * that modules will gradually be rewritten so they don't need this. 
 * <p>
 * This version of COmplexArray stores numbers as floats...perhaps a little dangerous
 * but halves memory usage. 
 * @author Doug Gillespie 
 *
 */
public class ComplexArrayF implements Cloneable, Serializable {


	private static final long serialVersionUID = 1L;

	/**
	 * Main data array of interleaved real / complex data. 
	 */
	private float[] data;

	/**
	 * Construct a complex array. Length of allocated
	 * data array will be 2*n
	 * @param n Number of real / imaginary pairs. 
	 */
	public ComplexArrayF(int n) {
		data = new float[n*2];
	}

	/**
	 * Construct a complex array from an existing array of 
	 * float data. Data must be interleaved real / imaginary pairs
	 * so the length of data MUST be even. 
	 * @param complexData interleaved real and imaginary data. 
	 */
	public ComplexArrayF(float[] complexData) {
		this.data = complexData;
	}
	
	/**
	 * Construct a complex array from two existing arrays of 
	 * float data, one with real, the other with imaginary 
	 * parts. Make sure imagData is same length as realData or else... 
	 * @param complexData interleaved real and imaginary data. 
	 */
	public ComplexArrayF(float[] realData, float[] imagData) {
		this.data = new float[realData.length*2];
		for (int i = 0, j = 0; i < realData.length; i++, j+=2) {
			this.data[j] = realData[i];
			this.data[j+1] = imagData[i];
		}
	}

	/**
	 * Construct a complex array from an array of Apache Complex objects. 
	 * @param complexArr  an array of Apache Complex objects. 
	 */
	public ComplexArrayF(org.apache.commons.math3.complex.Complex[] complexArr) {
		this.data = new float[complexArr.length*2];
		for (int i = 0, j = 0; i < complexArr.length; i++, j+=2) {
			this.data[j] = (float) complexArr[i].getReal();
			this.data[j+1] = (float) complexArr[i].getImaginary();
		}
	}
	
	/**
	 * Make a copy of the complex array with a new length. 
	 * Wraps around the Arrays.copyof function
	 * @param newLength new number of complex objects. 
	 * @return Complex array with the new length. 
	 */
	public ComplexArrayF copyOf(int newLength) {
		return new ComplexArrayF(Arrays.copyOf(data, newLength*2));
	}

	/**
	 * Set a single complex number in the array
	 * @param i index of the complex number
	 * @param re real part
	 * @param im imaginary part
	 */
	public void set(int i, float re, float im) {
		i<<=1;
		data[i++] = re;
		data[i] = im;
	}
	
	public void set(int i, Complex complex) {
		i<<=1;
		data[i++] = (float) complex.real;
		data[i] = (float) complex.imag;
	}
	
	public Complex get(int i) {
		return new Complex(data[i*2], data[i*2+1]);
	}

	/**
	 * The length of the complex array, i.e. the number of 
	 * complex numbers. This is half the length of the internal data array. 
	 * @return The number of complex numbers in the array
	 */
	public int length() {
		if (data == null){
			return 0;
		}
		return data.length/2;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ComplexArrayF clone() {
		ComplexArrayF newOne;
		try {
			newOne = (ComplexArrayF) super.clone();
			newOne.data = data.clone();
			return newOne;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the angle of all complex numbers in the array. 
	 * @return angles in radians
	 */
	public float[] ang() {
		if (data == null) {
			return null;
		}
		float[] angles = new float[data.length/2];
		for (int re = 0, im = 1, o = 0; re < data.length; re+=2, im+=2, o++) {
			if (data[re] == 0 && data[im] == 0) {
				angles[o] = 0;
			}
			else {
				angles[0] = (float) Math.atan2(data[im], data[re]);
			}
		}
		return angles;
	}

	/**
	 * Gets the angle of a single complex number in the array
	 * @param i index in the array
	 * @return angle in radians. 
	 */
	public float ang(int i) {
		float re = data[i*2];
		float im = data[i*2+1];
		if (re == 0 && im == 0) {
			return 0;
		}
		return (float) Math.atan2(im, re);
	}

	/**
	 * Gets the squared magnitude of a complex array
	 * @return squared magnitudes
	 */
	public float[] magsq() {
		if (data == null) {
			return null;
		}
		float[] out = new float[data.length/2];
		for (int re = 0, im = 1, o = 0; re < data.length; re+=2, im+=2, o++) {
			out[o] = data[re]*data[re] + data[im]*data[im];
		}
		return out;
	}
	
	/**
	 * Gets the squared magnitude of a complex number
	 * @param i index of the complex number
	 * @return squared magnitude
	 */
	public float magsq(int i) {
		i<<=1;
		float re = data[i++];
		float im = data[i];
		return re * re + im * im;
	}


	/**
	 * Gets the magnitude of all complex numbers in the array. 
	 * @return Array of magnitudes
	 */
	public float[] mag() {
		float[] out = magsq();
		for (int i = 0; i < out.length; i++) {
			out[i] = (float) Math.sqrt(out[i]);
		}
		return out;
	}
	/**
	 * Gets the magnitude of a single complex number
	 * @param i index of the complex number
	 * @return magnitude magnitude
	 */
	public float mag(int i) {
		return (float) Math.sqrt(magsq(i));
	}

	/**
	 * Calculates the square root of all complex numbers in the array
	 * @return square roots of complex numbers. 
	 */
	public ComplexArrayF sqrt() {
		if (data == null) {
			return null;
		}
		ComplexArrayF s = new ComplexArrayF(length());
		for (int o = 0; o < data.length/2; o++) {
			float newmag = (float) Math.sqrt(mag(o));
			float newang = (float) (ang(o) / 2.0);
			s.set(o, (float) (newmag * Math.cos(newang)), (float) (newmag * Math.sin(newang)));
		}
		return s;
	}
	/**
	 * Gets the square root of a Complex number
	 * @param i index of the complex number
	 * @return square root of the Complex number
	 */
	public Complex sqrt(int i) {
		float newmag = (float) Math.sqrt(mag(i));
		float newang = (float) (ang(i) / 2.0);
		return new Complex(newmag * Math.cos(newang), newmag * Math.sin(newang));
	}

	/**
	 * Raises the complex array to the power f
	 * @param f power factor
	 * @return array of complex numbers raised to the power f. 
	 */
	public ComplexArrayF pow(float f) {
		if (data == null) {
			return null;
		}
		ComplexArrayF s = new ComplexArrayF(length());
		for (int o = 0; o < data.length/2; o++) {
			float newmag = (float) Math.pow(mag(o), f);
			float newang = ang(o) * f;
			s.set(o, (float) (newmag * Math.cos(newang)), (float) (newmag * Math.sin(newang)));
		}
		return s;
	}

	/**
	 * Raises a complex number to a scalar power.
	 * @param i index of the complex number 
	 * @param f power to raise number to
	 * @return new Complex number
	 */
	public Complex pow(int i, float f) {
		float newmag = (float) Math.pow(mag(i), f);
		float newang = ang(i) * f;
		return new Complex( (float) ( newmag * Math.cos(newang)),  (float)  (newmag * Math.sin(newang)));
	}

	/**
	 * Add a complex array to the current array
	 * @param c complex array to add
	 * @return sum of this and c.
	 */
	public ComplexArrayF plus(ComplexArrayF c) {
		if (data == null) {
			return null;
		}
		ComplexArrayF s = clone();
		for (int i = 0; i < c.data.length; i++) {
			s.data[i] += c.data[i];
		}
		return s;
	}

	/**
	 * Subtract a complex array from this array. 
	 * @param c complex array to subtract. 
	 * @return this minus c.
	 */
	public ComplexArrayF minus(ComplexArrayF c) {
		if (data == null) {
			return null;
		}
		ComplexArrayF s = clone();
		for (int i = 0; i < c.data.length; i++) {
			s.data[i] -= c.data[i];
		}
		return s;
	}

	/**
	 * Multiply a complex array by a scaler factor
	 * @param f multiplication factor. 
	 * @return new complex array
	 */
	public ComplexArrayF times(float f) {
		if (data == null) {
			return null;
		}
		float[] tData = new float[data.length];
		for (int i = 0; i < data.length; i++) {
			tData[i] = data[i]*f;
		}
		return new ComplexArrayF(tData);
	}
	
	public Complex times(int i, float f) {
		return new Complex(getReal(i)*f, getImag(i)*f);
	}

	/**
	 * Calculate the complex conjugate of the complex array
	 * @return complex conjugate of the complex array. 
	 */
	public ComplexArrayF conj() {
		if (data == null) {
			return null;
		}
		float[] tData = data.clone();
		for (int i = 1; i < tData.length; i+=2) {
			tData[i] = -tData[i];
		}
		return new ComplexArrayF(tData);
	}

	/**
	 * Multiply this array by the complex conjugate of Array s;
	 * @param s Array to multiply by
	 * @return new array = this*conj(other)
	 */
	public ComplexArrayF conjTimes(ComplexArrayF s) {
		if (data == null) {
			return null;
		}
		float[] sData = s.data;
		float[] tData = new float[data.length];
		for (int re = 0, im = 1; re < data.length; re+=2, im+=2) {
			tData[re] = data[re]*sData[re] + data[im]*sData[im];
			tData[im] = -data[re]*sData[im] + data[im]*sData[re];
		}
		return new ComplexArrayF(tData);
	}
	
	/**
	 * Multiply this array by the complex conjugate of Array s
	 * but only using data within the complex bin range >= binRange[0] to < binRange[1]
	 * @param s Array to multiply be
	 * @param binRange range of frequency bins to include. 
	 * @return new array = this*conj(other)
	 */
	public ComplexArrayF conjTimes(ComplexArrayF s, int[] binRange) {
		if (data == null) {
			return null;
		}
		float[] sData = s.data;
		float[] tData = new float[data.length];
		for (int re = binRange[0]*2, im = binRange[0]*2+1; re < binRange[1]*2; re+=2, im+=2) {
			tData[re] = data[re]*sData[re] + data[im]*sData[im];
			tData[im] = -data[re]*sData[im] + data[im]*sData[re];
		}
		return new ComplexArrayF(tData);		
	}
	
	/**
	 * Dot product (aka Inner Product) of this array and another complex array 's'.
	 * The order of the equation is this&#8901s.</p>
	 * <p>For example, if this ComplexArrayF u = [u0 u1 u2] and the passed array is
	 * v = [v0 v1 v2], the number returned would be:
	 * <ul>
	 * <li>Complex newVal = u0<u>v0</u> + u1<u>v1</u> + u2<u>v2</u></li>
	 * </ul>
	 * <p>Where the underlined variables indicate the complex conjugate.  This method
	 * is similar to the conjTimes method, but adds Complex Numbers together
	 * and returns the resultant Complex number</p>
	 * @param s the ComplexArrayF to perform the dot product with
	 * @return Complex number = this&#8901s 
	 */
	public Complex dotProduct(ComplexArrayF s) {
		if (data == null) {
			return null;
		}
		float[] sData = s.data;
		float realVal = 0;
		float imVal = 0;
		for (int re = 0, im = 1; re < data.length; re+=2, im+=2) {
			realVal += data[re]*sData[re] + data[im]*sData[im];
			imVal += -data[re]*sData[im] + data[im]*sData[re];
		}
		return new Complex(realVal,imVal);
	}
	
	/**
	 * <p>Calculate the Cross-Spectral Density Matrix (CSDM) from this complex array.
	 * This array is assumed to be a column vector with number of rows = length().
	 * The returned object will be a ComplexArrayF column vector with length() number
	 * of rows, and each row will be a ComplexArrayF object containing a row
	 * vector with length() number of columns.</p>
	 * <p>For example, if this ComplexArrayF u = [u0 u1 u2], then CSDM(u) = ComplexArrayF[] of
	 * length 3, where:
	 * <ul>
	 * <li>ComplexArrayF[0] = [u0<u>u0</u> u0<u>u1</u> u0<u>u2</u>]</li>
	 * <li>ComplexArrayF[1] = [u1<u>u0</u> u1<u>u1</u> u1<u>u2</u>]</li>
	 * <li>ComplexArrayF[2] = [u2<u>u0</u> u2<u>u1</u> u2<u>u2</u>]</li>
	 * </ul>
	 * <p>Where the underlined variables indicate the complex conjugate</p>
	 * @return
	 */
	public ComplexArrayF[] calcCSDM() {
		ComplexArrayF[] csdm = new ComplexArrayF[this.length()];
		for (int row=0; row<this.length(); row++) {
			ComplexArrayF prelim = new ComplexArrayF(this.length());
			for (int col=0; col<this.length(); col++) {
//				prelim.set(col, this.get(row).times(this.get(col).conj())); to save time and not continuously create new Complex objects, work on the data vector directly
				prelim.data[col*2] = data[row*2]*data[col*2] + data[row*2+1]*data[col*2+1];	// real value
				prelim.data[col*2+1] = -data[row*2]*data[col*2+1] + data[row*2+1]*data[col*2];	// imaginary value
			}
			csdm[row]=prelim;
		}
		return csdm;
	}

	/**
	 * 
	 * @return the entire real part of the array
	 */
	public float[] getReal() {
		float[] r = new float[data.length/2];
		for (int i = 0, j = 0; j < data.length; i++, j+=2) {
			r[i] = data[j];
		}
		return r;
	}
	
	/**
	 * Get a real element from the array
	 * @param i index of the complex number
	 * @return single real element
	 */
	public float getReal(int i) {
		return data[i<<1];
	}

	/**
	 * Set a single real element in the array
	 * @param i index of the complex number
	 * @param re real value to set. 
	 */
	public void setReal(int i, float re) {
		data[i<<1] = re;
	}

	/**
	 * Get a single imaginary element from the array
	 * @param i index of the complex number
	 * @return single imaginary number
	 */
	public float getImag(int i) {
		i<<=1;
		return data[i+1];
	}
	
	/**
	 * 
	 * @return the entire imag part of the array
	 */
	public float[] getImag() {
		float[] r = new float[data.length/2];
		for (int i = 0, j = 1; j < data.length; i++, j+=2) {
			r[i] = data[j];
		}
		return r;
	}
	
	/**
	 * Set a single imaginary element in the array
	 * @param i index of the complex number
	 * @param re imaginary value to set. 
	 */
	public void setImag(int i, float im) {
		data[i*2+1] = im;
	}

	/**
	 * Get the data array of interleaved real / complex elements
	 * @return data array
	 */
	public float[] getData() {
		return data;
	}

	/**
	 * Set the data array
	 * @param data array of interleaved real / imaginary pairs. 
	 */
	public void setData(float[] data) {
		this.data = data;
	}

	/**
	 * Is either the real or imaginary part of a specifiec element NaN
	 * @param i index of the complex number
	 * @return true if either the real or imaginary part is NaN
	 */
	public boolean isNaN(int i) {
		return (Float.isNaN(data[i<<1]) || Float.isNaN(data[(i<<1)+1]));
	}

	/**
	 * Multiply the array internally by a scalar number
	 * @param d scalar multiplier
	 */
	public void internalTimes(float d) {
		for (int i = 0; i < data.length; i++) {
			data[i] *= d;
		}
	}
	
    /**
     *  return a new ComplexArrayF whose value is (this * b)
     * @param b
     * @return
     */
    public ComplexArrayF times(Complex b) {
    	float[] newData = data.clone();
    	for(int i = 0; i<newData.length; i+=2) {
    		Complex a = new Complex(newData[i],newData[i+1]);
    		a = a.times(b);
    		newData[i] = (float) a.real;
    		newData[i+1] = (float) a.imag;
    	}
    	return new ComplexArrayF(newData);
    }
	
	/**
	 * Multiple a single element of the array by a scalar
	 * @param i index of the complex number
	 * @param d scalar factor
	 */
	public void internalTimes(int i, float d) {
		i<<=1;
		data[i++] *= d;
		data[i] *= d;
	}

	/**
	 * Set the entire data array to zero. 
	 */
	public void setZero() {
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
	}
	
	/**
	 * FFT's of real data often only contain the first half of the data since the second
	 * half is just a flipped complex conjugate of the first half. This function will 
	 * fill back in the second half of the data.  
	 * @return float length array with the second half being the complex cunjugate of the first. 
	 */
	public ComplexArrayF fillConjugateHalf() {
		float[] fData = Arrays.copyOf(data, data.length*2);
		for (int re1 = 0, im1 = 1, re2 = fData.length-2, im2 = fData.length-1; re1 < data.length; re1+=2, im1+=2, re2-=2, im2-=2) {
			fData[re2] = data[re1];
			fData[im2] = -data[im1];
		}
		return new ComplexArrayF(fData);
	}

	/**
	 * Create a complex array from a real array. 
	 * @param realArray array of real data. 
	 * @return a complex array - twice the length of realArray with imag parts = 0;
	 */
	public static ComplexArrayF realToComplex(float[] realArray) {
		if (realArray == null) {
			return null;
		}
		int n = realArray.length;
		float[] cData = new float[n*2];
		for (int i = 0, j = 0; i < n; i++, j+=2) {
			cData[j] = realArray[i];
		}
		return new ComplexArrayF(cData);
	}

	/**
	 * @return sum of squares of all data in the array. 
	 */
	public float sumSquared() {
		float s = 0;
		for (int ir = 0, im = 1; ir < data.length; ir+=2, im+=2) {
			float rV = data[ir];
			float iV = data[im];
			s += rV*rV+iV*iV;
		}
		return s;
	}

	/**
	 * 
	 * @param complexData
	 * @return sum of squares of the complex array
	 */
	public static float sumSquared(float[] complexData) {
		float s = 0;
		for (int ir = 0, im = 1; ir < complexData.length; ir+=2, im+=2) {
			float rV = complexData[ir];
			float iV = complexData[im];
			s += rV*rV+iV*iV;
		}
		return s;
	}

}
