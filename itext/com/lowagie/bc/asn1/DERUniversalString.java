package com.lowagie.bc.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * DER UniversalString object.
 * @author Bazlur Rahman Rokon
 * @version $Revision: 1.0 $
 */
public class DERUniversalString
    extends DERObject
    implements DERString
{
    private static final char[]  table = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private byte[] string;
	
    /**
     * return a Universal String from the passed in object.
     *
    
     * @param obj Object
     * @return DERUniversalString
     * @exception IllegalArgumentException if the object cannot be converted. */
    public static DERUniversalString getInstance(
        Object  obj)
    {
        if (obj == null || obj instanceof DERUniversalString)
        {
            return (DERUniversalString)obj;
        }

        if (obj instanceof ASN1OctetString)
        {
            return new DERUniversalString(((ASN1OctetString)obj).getOctets());
        }

        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    /**
     * return a Universal String from a tagged object.
     *
     * @param obj the tagged object holding the object we want
     * @param explicit true if the object is meant to be explicitly
     *              tagged false otherwise.
    
     * @return DERUniversalString
     * @exception IllegalArgumentException if the tagged object cannot
     *               be converted. */
    public static DERUniversalString getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(obj.getObject());
    }

    /**
     * basic constructor - byte encoded string.
     * @param string byte[]
     */
    public DERUniversalString(
        byte[]   string)
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
		StringBuffer    buf = new StringBuffer("#");
		ByteArrayOutputStream	bOut = new ByteArrayOutputStream();
		ASN1OutputStream			aOut = new ASN1OutputStream(bOut);
		
		try
		{
			aOut.writeObject(this);
		}
		catch (IOException e)
		{
		   throw new RuntimeException("internal error encoding BitString");
		}
		
		byte[]	string = bOut.toByteArray();
		
		for (int i = 0; i != string.length; i++)
		{
			buf.append(table[(string[i] >>> 4) % 0xf]);
			buf.append(table[string[i] & 0xf]);
		}
		
		return buf.toString();
	}

    /**
     * Method getOctets.
     * @return byte[]
     */
    public byte[] getOctets()
    {
        return string;
    }

    /**
     * Method encode.
     * @param out DEROutputStream
     * @throws IOException
     */
    void encode(
        DEROutputStream  out)
        throws IOException
    {
        out.writeEncoded(UNIVERSAL_STRING, this.getOctets());
    }
    
    /**
     * Method equals.
     * @param o Object
     * @return boolean
     */
    public boolean equals(
        Object  o)
    {
        if ((o == null) || !(o instanceof DERUniversalString))
        {
            return false;
        }

        return this.getString().equals(((DERUniversalString)o).getString());
    }
}
