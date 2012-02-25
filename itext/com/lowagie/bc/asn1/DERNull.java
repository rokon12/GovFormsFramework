package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * A NULL object.
 * @author Bazlur Rahman Rokon
 * @version $Revision: 1.0 $
 */
public class DERNull
    extends ASN1Null
{
    byte[]  zeroBytes = new byte[0];

    public DERNull()
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
        out.writeEncoded(NULL, zeroBytes);
    }
    
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	public boolean equals(
		Object o)
	{
        if ((o == null) || !(o instanceof DERNull))
        {
            return false;
        }
        
		return true;
	}
}
