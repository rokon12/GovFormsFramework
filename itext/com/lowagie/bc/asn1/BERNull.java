package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * A BER NULL object.
 * @author Bazlur Rahman Rokon
 * @version $Revision: 1.0 $
 */
public class BERNull
    extends DERNull
{
    public BERNull()
    {
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
        if (out instanceof ASN1OutputStream || out instanceof BEROutputStream)
        {
            out.write(NULL);
            out.write(0);
            out.write(0);
        }
        else
        {
            super.encode(out);
        }
    }
}
