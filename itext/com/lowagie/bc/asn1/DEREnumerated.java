package com.lowagie.bc.asn1;

import java.io.IOException;
import java.math.BigInteger;

/**
 */
public class DEREnumerated
    extends DERObject
{
    byte[]      bytes;

    /**
     * return an integer from the passed in object
     *
    
     * @param obj Object
     * @return DEREnumerated
     * @exception IllegalArgumentException if the object cannot be converted. */
    public static DEREnumerated getInstance(
        Object  obj)
    {
        if (obj == null || obj instanceof DEREnumerated)
        {
            return (DEREnumerated)obj;
        }

        if (obj instanceof ASN1OctetString)
        {
            return new DEREnumerated(((ASN1OctetString)obj).getOctets());
        }

        if (obj instanceof ASN1TaggedObject)
        {
            return getInstance(((ASN1TaggedObject)obj).getObject());
        }

        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    /**
     * return an Enumerated from a tagged object.
     *
     * @param obj the tagged object holding the object we want
     * @param explicit true if the object is meant to be explicitly
     *              tagged false otherwise.
    
     * @return DEREnumerated
     * @exception IllegalArgumentException if the tagged object cannot
     *               be converted. */
    public static DEREnumerated getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(obj.getObject());
    }

    /**
     * Constructor for DEREnumerated.
     * @param value int
     */
    public DEREnumerated(
        int         value)
    {
        bytes = BigInteger.valueOf(value).toByteArray();
    }

    /**
     * Constructor for DEREnumerated.
     * @param value BigInteger
     */
    public DEREnumerated(
        BigInteger   value)
    {
        bytes = value.toByteArray();
    }

    /**
     * Constructor for DEREnumerated.
     * @param bytes byte[]
     */
    public DEREnumerated(
        byte[]   bytes)
    {
        this.bytes = bytes;
    }

    /**
     * Method getValue.
     * @return BigInteger
     */
    public BigInteger getValue()
    {
        return new BigInteger(bytes);
    }

    /**
     * Method encode.
     * @param out DEROutputStream
     * @throws IOException
     */
    void encode(
        DEROutputStream out)
        throws IOException
    {
        out.writeEncoded(ENUMERATED, bytes);
    }
    
    /**
     * Method equals.
     * @param o Object
     * @return boolean
     */
    public boolean equals(
        Object  o)
    {
        if (o == null || !(o instanceof DEREnumerated))
        {
            return false;
        }

        DEREnumerated other = (DEREnumerated)o;

        if (bytes.length != other.bytes.length)
        {
            return false;
        }

        for (int i = 0; i != bytes.length; i++)
        {
            if (bytes[i] != other.bytes[i])
            {
                return false;
            }
        }

        return true;
    }
}
