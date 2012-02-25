package com.lowagie.bc.asn1;

import java.io.IOException;
import java.io.OutputStream;

/**
 */
public class ASN1OutputStream
    extends DEROutputStream
{
    /**
     * Constructor for ASN1OutputStream.
     * @param os OutputStream
     */
    public ASN1OutputStream(
        OutputStream    os)
    {
        super(os);
    }

    /**
     * Method writeObject.
     * @param obj Object
     * @throws IOException
     */
    public void writeObject(
        Object    obj)
        throws IOException
    {
        if (obj == null)
        {
            writeNull();
        }
        else if (obj instanceof DERObject)
        {
            ((DERObject)obj).encode(this);
        }
        else if (obj instanceof DEREncodable)
        {
            ((DEREncodable)obj).getDERObject().encode(this);
        }
        else
        {
            throw new IOException("object not ASN1Encodable");
        }
    }
}
