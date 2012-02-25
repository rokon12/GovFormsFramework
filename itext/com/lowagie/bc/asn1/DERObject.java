package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 */
public abstract class DERObject
    implements DERTags, DEREncodable
{
    /**
     * Method getDERObject.
     * @return DERObject
     * @see com.lowagie.bc.asn1.DEREncodable#getDERObject()
     */
    public DERObject getDERObject()
    {
        return this;
    }

    /**
     * Method encode.
     * @param out DEROutputStream
     * @throws IOException
     */
    abstract void encode(DEROutputStream out)
        throws IOException;
}
