package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * ASN.1 TaggedObject - in ASN.1 nottation this is any object proceeded by
 * a [n] where n is some number - these are assume to follow the construction
 * rules (as with sequences).
 * @author Bazlur Rahman Rokon
 * @version $Revision: 1.0 $
 */
public abstract class ASN1TaggedObject
    extends DERObject
{
    int             tagNo;
    boolean         empty = false;
    boolean         explicit = true;
    DEREncodable    obj = null;

    /**
     * Method getInstance.
     * @param obj ASN1TaggedObject
     * @param explicit boolean
     * @return ASN1TaggedObject
     */
    static public ASN1TaggedObject getInstance(
        ASN1TaggedObject    obj,
        boolean             explicit)
    {
        if (explicit)
        {
            return (ASN1TaggedObject)obj.getObject();
        }

        throw new IllegalArgumentException("implicitly tagged tagged object");
    }

    /**
     * @param tagNo the tag number for this object.
     * @param obj the tagged object.
     */
    public ASN1TaggedObject(
        int             tagNo,
        DEREncodable    obj)
    {
        this.explicit = true;
        this.tagNo = tagNo;
        this.obj = obj;
    }

    /**
     * @param explicit true if the object is explicitly tagged.
     * @param tagNo the tag number for this object.
     * @param obj the tagged object.
     */
    public ASN1TaggedObject(
        boolean         explicit,
        int             tagNo,
        DEREncodable    obj)
    {
        this.explicit = explicit;
        this.tagNo = tagNo;
        this.obj = obj;
    }
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	public boolean equals(
		Object o)
	{
        if (o == null || !(o instanceof ASN1TaggedObject))
        {
            return false;
        }
        
        ASN1TaggedObject other = (ASN1TaggedObject)o;
        
        if (tagNo != other.tagNo || empty != other.empty || explicit != other.explicit)
        {
			return false;
		}
		
		if(obj == null)
		{
			if(other.obj != null)
			{
				return false;
			}
		}
		else
		{
			if(!(obj.equals(other.obj)))
			{
				return false;
			}
		}
		
		return true;
	}
	
    /**
     * Method hashCode.
     * @return int
     */
    public int hashCode()
    {
        int code = tagNo;

        if (obj != null)
        {
            code ^= obj.hashCode();
        }

        return code;
    }

    /**
     * Method getTagNo.
     * @return int
     */
    public int getTagNo()
    {
        return tagNo;
    }

    /**
     * return whether or not the object may be explicitly tagged. 
     * <p>
     * Note: if the object has been read from an input stream, the only
     * time you can be sure if isExplicit is returning the true state of
     * affairs is if it returns false. An implicitly tagged object may appear
     * to be explicitly tagged, so you need to understand the context under
     * which the reading was done as well, see getObject below.
     * @return boolean
     */
    public boolean isExplicit()
    {
        return explicit;
    }

    /**
     * Method isEmpty.
     * @return boolean
     */
    public boolean isEmpty()
    {
        return empty;
    }

    /**
     * return whatever was following the tag.
     * <p>
     * Note: tagged objects are generally context dependent if you're
     * trying to extract a tagged object you should be going via the
     * appropriate getInstance method.
     * @return DERObject
     */
    public DERObject getObject()
    {
        if (obj != null)
        {
            return obj.getDERObject();
        }

        return null;
    }

    /**
     * Method encode.
     * @param out DEROutputStream
     * @throws IOException
     */
    abstract void encode(DEROutputStream  out)
        throws IOException;
}
