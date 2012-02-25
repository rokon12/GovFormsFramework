package com.lowagie.bc.asn1;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 */
public class DEROutputStream
    extends FilterOutputStream implements DERTags
{
    /**
     * Constructor for DEROutputStream.
     * @param os OutputStream
     */
    public DEROutputStream(
        OutputStream    os)
    {
        super(os);
    }

    /**
     * Method writeLength.
     * @param length int
     * @throws IOException
     */
    private void writeLength(
        int length)
        throws IOException
    {
        if (length > 127)
        {
            int size = 1;
            int val = length;

            while ((val >>>= 8) != 0)
            {
                size++;
            }

            write((byte)(size | 0x80));

            for (int i = (size - 1) * 8; i >= 0; i -= 8)
            {
                write((byte)(length >> i));
            }
        }
        else
        {
            write((byte)length);
        }
    }

    /**
     * Method writeEncoded.
     * @param tag int
     * @param bytes byte[]
     * @throws IOException
     */
    void writeEncoded(
        int     tag,
        byte[]  bytes)
        throws IOException
    {
        write(tag);
        writeLength(bytes.length);
        write(bytes);
    }

    /**
     * Method writeNull.
     * @throws IOException
     */
    protected void writeNull()
        throws IOException
    {
        write(NULL);
        write(0x00);
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
            throw new IOException("object not DEREncodable");
        }
    }
}
