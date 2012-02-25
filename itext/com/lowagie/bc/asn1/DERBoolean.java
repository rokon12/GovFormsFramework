package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 */
public class DERBoolean
    extends DERObject
{
    byte         value;

	public static final DERBoolean FALSE = new DERBoolean(false);
	public static final DERBoolean TRUE  = new DERBoolean(true);

    /**
     * return a boolean from the passed in object.
     *
    
     * @param obj Object
     * @return DERBoolean
     * @exception IllegalArgumentException if the object cannot be converted. */
    public static DERBoolean getInstance(
        Object  obj)
    {
        if (obj == null || obj instanceof DERBoolean)
        {
            return (DERBoolean)obj;
        }

        if (obj instanceof ASN1OctetString)
        {
            return new DERBoolean(((ASN1OctetString)obj).getOctets());
        }

        if (obj instanceof ASN1TaggedObject)
        {
            return getInstance(((ASN1TaggedObject)obj).getObject());
        }

        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    /**
     * return a DERBoolean from the passed in boolean.
     * @param value boolean
     * @return DERBoolean
     */
    public static DERBoolean getInstance(
        boolean  value)
    {
        return (value ? TRUE : FALSE);
    }

    /**
     * return a Boolean from a tagged object.
     *
     * @param obj the tagged object holding the object we want
     * @param explicit true if the object is meant to be explicitly
     *              tagged false otherwise.
    
     * @return DERBoolean
     * @exception IllegalArgumentException if the tagged object cannot
     *               be converted. */
    public static DERBoolean getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(obj.getObject());
    }
    
    /**
     * Constructor for DERBoolean.
     * @param value byte[]
     */
    public DERBoolean(
        byte[]       value)
    {
        this.value = value[0];
    }

    /**
     * Constructor for DERBoolean.
     * @param value boolean
     */
    public DERBoolean(
        boolean     value)
    {
        this.value = (value) ? (byte)0xff : (byte)0;
    }

    /**
     * Method isTrue.
     * @return boolean
     */
    public boolean isTrue()
    {
        return (value != 0);
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
        byte[]  bytes = new byte[1];

        bytes[0] = value;

        out.writeEncoded(BOOLEAN, bytes);
    }
    
    /**
     * Method equals.
     * @param o Object
     * @return boolean
     */
    public boolean equals(
        Object  o)
    {
        if ((o == null) || !(o instanceof DERBoolean))
        {
            return false;
        }

        return (value == ((DERBoolean)o).value);
    }

}
