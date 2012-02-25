package com.lowagie.bc.asn1;

import java.util.Vector;

/**
 * a general class for building up a vector of DER encodable objects -
 * this will eventually be superceded by ASN1EncodableVector so you should
 * use that class in preference.
 * @author Bazlur Rahman Rokon
 * @version $Revision: 1.0 $
 */
public class DEREncodableVector
{
    private Vector  v = new Vector();

    /**
     * Method add.
     * @param obj DEREncodable
     */
    public void add(
        DEREncodable   obj)
    {
        v.addElement(obj);
    }

    /**
     * Method get.
     * @param i int
     * @return DEREncodable
     */
    public DEREncodable get(
        int i)
    {
        return (DEREncodable)v.elementAt(i);
    }

    /**
     * Method size.
     * @return int
     */
    public int size()
    {
        return v.size();
    }
}
