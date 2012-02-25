package com.lowagie.bc.asn1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base class for an application specific object
 * @author Bazlur Rahman Rokon
 * @version $Revision: 1.0 $
 */
public class DERApplicationSpecific 
	extends DERObject
{
	private int		tag;
	private byte[]	octets;
	
	/**
	 * Constructor for DERApplicationSpecific.
	 * @param tag int
	 * @param octets byte[]
	 */
	public DERApplicationSpecific(
		int		tag,
		byte[]	octets)
	{
		this.tag = tag;
		this.octets = octets;
	}
	
	/**
	 * Constructor for DERApplicationSpecific.
	 * @param tag int
	 * @param object DEREncodable
	 * @throws IOException
	 */
	public DERApplicationSpecific(
		int 							tag, 
		DEREncodable 		object) 
		throws IOException 
	{
		this.tag = tag | DERTags.CONSTRUCTED;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DEROutputStream dos = new DEROutputStream(baos);
		
		dos.writeObject(object);
		
		this.octets = baos.toByteArray();
	}
	
	/**
	 * Method isConstructed.
	 * @return boolean
	 */
	public boolean isConstructed()
	{
		return (tag & DERTags.CONSTRUCTED) != 0;
	}
	
	/**
	 * Method getContents.
	 * @return byte[]
	 */
	public byte[] getContents()
	{
		return octets;
	}
	
	/**
	 * Method getApplicationTag.
	 * @return int
	 */
	public int getApplicationTag() 
	{
		return tag & 0x1F;
	}
 	
	/**
	 * Method getObject.
	 * @return DERObject
	 * @throws IOException
	 */
	public DERObject getObject() 
		throws IOException 
	{
		return new ASN1InputStream(new ByteArrayInputStream(getContents())).readObject();
	}
	
    /* (non-Javadoc)
     * @see org.bouncycastle.asn1.DERObject#encode(org.bouncycastle.asn1.DEROutputStream)
     */
    void encode(DEROutputStream out) throws IOException
    {
        out.writeEncoded(DERTags.APPLICATION | tag, octets);
    }
}
