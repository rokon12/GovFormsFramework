package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 */
public class DERGeneralString 
	extends DERObject implements DERString
{
	private String string;

	/**
	 * Method getInstance.
	 * @param obj Object
	 * @return DERGeneralString
	 */
	public static DERGeneralString getInstance(
		Object obj) 
	{
		if (obj == null || obj instanceof DERGeneralString) 
		{
			return (DERGeneralString) obj;
		}
		if (obj instanceof ASN1OctetString) 
		{
			return new DERGeneralString(((ASN1OctetString) obj).getOctets());
		}
		if (obj instanceof ASN1TaggedObject) 
		{
			return getInstance(((ASN1TaggedObject) obj).getObject());
		}
		throw new IllegalArgumentException("illegal object in getInstance: "
				+ obj.getClass().getName());
	}

	/**
	 * Method getInstance.
	 * @param obj ASN1TaggedObject
	 * @param explicit boolean
	 * @return DERGeneralString
	 */
	public static DERGeneralString getInstance(
		ASN1TaggedObject obj, 
		boolean explicit) 
	{
		return getInstance(obj.getObject());
	}

	/**
	 * Constructor for DERGeneralString.
	 * @param string byte[]
	 */
	public DERGeneralString(byte[] string) 
	{
		char[] cs = new char[string.length];
		for (int i = 0; i != cs.length; i++) {
			cs[i] = (char) (string[i] & 0xff);
		}
		this.string = new String(cs);
	}

	/**
	 * Constructor for DERGeneralString.
	 * @param string String
	 */
	public DERGeneralString(String string) 
	{
		this.string = string;
	}
	
	/**
	 * Method getString.
	 * @return String
	 * @see com.lowagie.bc.asn1.DERString#getString()
	 */
	public String getString() 
	{
		return string;
	}
	
	/**
	 * Method getOctets.
	 * @return byte[]
	 */
	public byte[] getOctets() 
	{
		char[] cs = string.toCharArray();
		byte[] bs = new byte[cs.length];
		for (int i = 0; i != cs.length; i++) 
		{
			bs[i] = (byte) cs[i];
		}
		return bs;
	}
	
	/**
	 * Method encode.
	 * @param out DEROutputStream
	 * @throws IOException
	 */
	void encode(DEROutputStream out) 
		throws IOException 
	{
		out.writeEncoded(GENERAL_STRING, this.getOctets());
	}
	
	/**
	 * Method hashCode.
	 * @return int
	 */
	public int hashCode() 
	{
		return this.getString().hashCode();
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	public boolean equals(Object o) 
	{
		if (!(o instanceof DERGeneralString)) 
		{
			return false;
		}
		DERGeneralString s = (DERGeneralString) o;
		return this.getString().equals(s.getString());
	}
}
