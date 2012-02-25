package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * A NULL object.
 * @author Bazlur Rahman Rokon
 * @version $Revision: 1.0 $
 */
public abstract class ASN1Null
    extends DERObject
{
    public ASN1Null()
    {
    }

    /**
     * Method hashCode.
     * @return int
     */
    public int hashCode()
    {
        return 0;
    }

	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	public boolean equals(
		Object o)
	{
        if ((o == null) || !(o instanceof ASN1Null))
        {
            return false;
        }
        
		return true;
	}

    /**
     * Method encode.
     * @param out DEROutputStream
     * @throws IOException
     */
    abstract void encode(DEROutputStream out)
        throws IOException;
}
