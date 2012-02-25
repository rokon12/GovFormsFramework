package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * DER NumericString object - this is an ascii string of characters {0,1,2,3,4,5,6,7,8,9, }.
 * @author Bazlur Rahman Rokon
 * @version $Revision: 1.0 $
 */
public class DERNumericString
    extends DERObject
    implements DERString
{
    String  string;

    /**
     * return a Numeric string from the passed in object
     *
    
     * @param obj Object
     * @return DERNumericString
     * @exception IllegalArgumentException if the object cannot be converted. */
    public static DERNumericString getInstance(
        Object  obj)
    {
        if (obj == null || obj instanceof DERNumericString)
        {
            return (DERNumericString)obj;
        }

        if (obj instanceof ASN1OctetString)
        {
            return new DERNumericString(((ASN1OctetString)obj).getOctets());
        }

        if (obj instanceof ASN1TaggedObject)
        {
            return getInstance(((ASN1TaggedObject)obj).getObject());
        }

        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    /**
     * return an Numeric String from a tagged object.
     *
     * @param obj the tagged object holding the object we want
     * @param explicit true if the object is meant to be explicitly
     *              tagged false otherwise.
    
     * @return DERNumericString
     * @exception IllegalArgumentException if the tagged object cannot
     *               be converted. */
    public static DERNumericString getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(obj.getObject());
    }

    /**
     * basic constructor - with bytes.
     * @param string byte[]
     */
    public DERNumericString(
        byte[]   string)
    {
        char[]  cs = new char[string.length];

        for (int i = 0; i != cs.length; i++)
        {
            cs[i] = (char)(string[i] & 0xff);
        }

        this.string = new String(cs);
    }

    /**
     * basic constructor - with string.
     * @param string String
     */
    public DERNumericString(
        String   string)
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
        char[]  cs = string.toCharArray();
        byte[]  bs = new byte[cs.length];

        for (int i = 0; i != cs.length; i++)
        {
            bs[i] = (byte)cs[i];
        }

        return bs; 
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
        out.writeEncoded(NUMERIC_STRING, this.getOctets());
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
    public boolean equals(
        Object  o)
    {
        if (!(o instanceof DERNumericString))
        {
            return false;
        }

        DERNumericString  s = (DERNumericString)o;

        return this.getString().equals(s.getString());
    }
}
