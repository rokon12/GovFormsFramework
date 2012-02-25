/*
 * $Id: RandomAccessFileOrArray.java,v 1.48 2005/09/11 08:38:18 blowagie Exp $
 * $Name:  $
 *
 * Copyright 2001, 2002 Paulo Soares
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.pdf;

import java.io.DataInputStream;
import java.io.DataInput;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
/** An implementation of a RandomAccessFile for input only
 * that accepts a file or a byte array as data source.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 * @version $Revision: 1.0 $
 */
public class RandomAccessFileOrArray implements DataInput {
    
    RandomAccessFile rf;
    String filename;
    byte arrayIn[];
    int arrayInPtr;
    byte back;
    boolean isBack = false;
    
    /** Holds value of property startOffset. */
    private int startOffset = 0;

    /**
     * Constructor for RandomAccessFileOrArray.
     * @param filename String
     * @throws IOException
     */
    public RandomAccessFileOrArray(String filename) throws IOException {
    	this(filename, false);
    }
    
    /**
     * Constructor for RandomAccessFileOrArray.
     * @param filename String
     * @param forceRead boolean
     * @throws IOException
     */
    public RandomAccessFileOrArray(String filename, boolean forceRead) throws IOException {
        File file = new File(filename);
        if (!file.canRead()) {
            if (filename.startsWith("file:/") || filename.startsWith("http://") || filename.startsWith("https://") || filename.startsWith("jar:")) {
                InputStream is = new URL(filename).openStream();
                try {
                    this.arrayIn = InputStreamToArray(is);
                    return;
                }
                finally {
                    try {is.close();}catch(IOException ioe){}
                }
            }
            else {
                InputStream is = BaseFont.getResourceStream(filename);
                if (is == null)
                    throw new IOException(filename + " not found as file or resource.");
                try {
                    this.arrayIn = InputStreamToArray(is);
                    return;
                }
                finally {
                    try {is.close();}catch(IOException ioe){}
                }
            }
        }
        else if (forceRead) {
            InputStream s = null;
            try {
                s = new FileInputStream(file);
                this.arrayIn = InputStreamToArray(s);
            }
            finally {
                try {s.close();}catch(Exception e){}
            }
        	return;
        }
        this.filename = filename;
        rf = new RandomAccessFile(filename, "r");
    }

    /**
     * Constructor for RandomAccessFileOrArray.
     * @param url URL
     * @throws IOException
     */
    public RandomAccessFileOrArray(URL url) throws IOException {
        InputStream is = url.openStream();
        try {
            this.arrayIn = InputStreamToArray(is);
        }
        finally {
            try {is.close();}catch(IOException ioe){}
        }
    }

    /**
     * Constructor for RandomAccessFileOrArray.
     * @param is InputStream
     * @throws IOException
     */
    public RandomAccessFileOrArray(InputStream is) throws IOException {
        this.arrayIn = InputStreamToArray(is);
    }
    
    /**
     * Method InputStreamToArray.
     * @param is InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStreamToArray(InputStream is) throws IOException {
        byte b[] = new byte[8192];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (true) {
            int read = is.read(b);
            if (read < 1)
                break;
            out.write(b, 0, read);
        }
        return out.toByteArray();
    }

    /**
     * Constructor for RandomAccessFileOrArray.
     * @param arrayIn byte[]
     */
    public RandomAccessFileOrArray(byte arrayIn[]) {
        this.arrayIn = arrayIn;
    }
    
    /**
     * Constructor for RandomAccessFileOrArray.
     * @param file RandomAccessFileOrArray
     */
    public RandomAccessFileOrArray(RandomAccessFileOrArray file) {
        filename = file.filename;
        arrayIn = file.arrayIn;
        startOffset = file.startOffset;
    }
    
    /**
     * Method pushBack.
     * @param b byte
     */
    public void pushBack(byte b) {
        back = b;
        isBack = true;
    }
    
    /**
     * Method read.
     * @return int
     * @throws IOException
     */
    public int read() throws IOException {
        if(isBack) {
            isBack = false;
            return back & 0xff;
        }
        if (arrayIn == null)
            return rf.read();
        else {
            if (arrayInPtr >= arrayIn.length)
                return -1;
            return arrayIn[arrayInPtr++] & 0xff;
        }
    }
    
    /**
     * Method read.
     * @param b byte[]
     * @param off int
     * @param len int
     * @return int
     * @throws IOException
     */
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0)
            return 0;
        int n = 0;
        if (isBack) {
            isBack = false;
            if (len == 1) {
                b[off] = back;
                return 1;
            }
            else {
                n = 1;
                b[off++] = back;
                --len;
            }
        }
        if (arrayIn == null) {
            return rf.read(b, off, len) + n;
        }
        else {
            if (arrayInPtr >= arrayIn.length)
                return -1;
            if (arrayInPtr + len > arrayIn.length)
                len = arrayIn.length - arrayInPtr;
            System.arraycopy(arrayIn, arrayInPtr, b, off, len);
            arrayInPtr += len;
            return len + n;
        }
    }
    
    /**
     * Method read.
     * @param b byte[]
     * @return int
     * @throws IOException
     */
    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }
    
    /**
     * Method readFully.
     * @param b byte[]
     * @throws IOException
     * @see java.io.DataInput#readFully(byte[])
     */
    public void readFully(byte b[]) throws IOException {
        readFully(b, 0, b.length);
    }
    
    /**
     * Method readFully.
     * @param b byte[]
     * @param off int
     * @param len int
     * @throws IOException
     * @see java.io.DataInput#readFully(byte[], int, int)
     */
    public void readFully(byte b[], int off, int len) throws IOException {
        int n = 0;
        do {
            int count = read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        } while (n < len);
    }
    
    /**
     * Method skip.
     * @param n long
     * @return long
     * @throws IOException
     */
    public long skip(long n) throws IOException {
        return skipBytes((int)n);
    }
    
    /**
     * Method skipBytes.
     * @param n int
     * @return int
     * @throws IOException
     * @see java.io.DataInput#skipBytes(int)
     */
    public int skipBytes(int n) throws IOException {
        if (n <= 0) {
            return 0;
        }
        int adj = 0;
        if (isBack) {
            isBack = false;
            if (n == 1) {
                return 1;
            }
            else {
                --n;
                adj = 1;
            }
        }
        int pos;
        int len;
        int newpos;
        
        pos = getFilePointer();
        len = length();
        newpos = pos + n;
        if (newpos > len) {
            newpos = len;
        }
        seek(newpos);
        
        /* return the actual number of bytes skipped */
        return newpos - pos + adj;
    }
    
    /**
     * Method reOpen.
     * @throws IOException
     */
    public void reOpen() throws IOException {
        if (filename != null && rf == null)
            rf = new RandomAccessFile(filename, "r");
        seek(0);
    }
    
    /**
     * Method insureOpen.
     * @throws IOException
     */
    protected void insureOpen() throws IOException {
        if (filename != null && rf == null) {
            reOpen();
        }
    }
    
    /**
     * Method isOpen.
     * @return boolean
     */
    public boolean isOpen() {
        return (filename == null || rf != null);
    }
    
    /**
     * Method close.
     * @throws IOException
     */
    public void close() throws IOException {
        isBack = false;
        if (rf != null) {
            rf.close();
            rf = null;
        }
    }
    
    /**
     * Method length.
     * @return int
     * @throws IOException
     */
    public int length() throws IOException {
        if (arrayIn == null) {
            insureOpen();
            return (int)rf.length() - startOffset;
        }
        else
            return arrayIn.length - startOffset;
    }
    
    /**
     * Method seek.
     * @param pos int
     * @throws IOException
     */
    public void seek(int pos) throws IOException {
        pos += startOffset;
        isBack = false;
        if (arrayIn == null) {
            insureOpen();
            rf.seek(pos);
        }
        else
            arrayInPtr = pos;
    }
    
    /**
     * Method seek.
     * @param pos long
     * @throws IOException
     */
    public void seek(long pos) throws IOException {
        seek((int)pos);
    }
    
    /**
     * Method getFilePointer.
     * @return int
     * @throws IOException
     */
    public int getFilePointer() throws IOException {
        insureOpen();
        int n = isBack ? 1 : 0;
        if (arrayIn == null) {
            return (int)rf.getFilePointer() - n - startOffset;
        }
        else
            return arrayInPtr - n - startOffset;
    }
    
    /**
     * Method readBoolean.
     * @return boolean
     * @throws IOException
     * @see java.io.DataInput#readBoolean()
     */
    public boolean readBoolean() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return (ch != 0);
    }
    
    /**
     * Method readByte.
     * @return byte
     * @throws IOException
     * @see java.io.DataInput#readByte()
     */
    public byte readByte() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return (byte)(ch);
    }
    
    /**
     * Method readUnsignedByte.
     * @return int
     * @throws IOException
     * @see java.io.DataInput#readUnsignedByte()
     */
    public int readUnsignedByte() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return ch;
    }
    
    /**
     * Method readShort.
     * @return short
     * @throws IOException
     * @see java.io.DataInput#readShort()
     */
    public short readShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short)((ch1 << 8) + ch2);
    }
    
    /**
     * Reads a signed 16-bit number from this stream in little-endian order.
     * The method reads two
     * bytes from this stream, starting at the current stream pointer.
     * If the two bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where each of the two values is
     * between <code>0</code> and <code>255</code>, inclusive, then the
     * result is equal to:
     * <blockquote><pre>
     *     (short)((b2 &lt;&lt; 8) | b1)
     * </pre></blockquote>
     * <p>
     * This method blocks until the two bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
    
    
    
     * @return     the next two bytes of this stream, interpreted as a signed
     *             16-bit number. * @exception  IOException   if an I/O error occurs. * @exception  EOFException  if this stream reaches the end before reading
     *               two bytes. */
    public final short readShortLE() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short)((ch2 << 8) + (ch1 << 0));
    }
    
    /**
     * Method readUnsignedShort.
     * @return int
     * @throws IOException
     * @see java.io.DataInput#readUnsignedShort()
     */
    public int readUnsignedShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch1 << 8) + ch2;
    }
    
    /**
     * Reads an unsigned 16-bit number from this stream in little-endian order.
     * This method reads
     * two bytes from the stream, starting at the current stream pointer.
     * If the bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b2 &lt;&lt; 8) | b1
     * </pre></blockquote>
     * <p>
     * This method blocks until the two bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
    
    
    
     * @return     the next two bytes of this stream, interpreted as an
     *             unsigned 16-bit integer. * @exception  IOException   if an I/O error occurs. * @exception  EOFException  if this stream reaches the end before reading
     *               two bytes. */
    public final int readUnsignedShortLE() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch2 << 8) + (ch1 << 0);
    }
    
    /**
     * Method readChar.
     * @return char
     * @throws IOException
     * @see java.io.DataInput#readChar()
     */
    public char readChar() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char)((ch1 << 8) + ch2);
    }
    
    /**
     * Reads a Unicode character from this stream in little-endian order.
     * This method reads two
     * bytes from the stream, starting at the current stream pointer.
     * If the bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1,&nbsp;b2&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (char)((b2 &lt;&lt; 8) | b1)
     * </pre></blockquote>
     * <p>
     * This method blocks until the two bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
    
    
    
     * @return     the next two bytes of this stream as a Unicode character. * @exception  IOException   if an I/O error occurs. * @exception  EOFException  if this stream reaches the end before reading
     *               two bytes. */
    public final char readCharLE() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char)((ch2 << 8) + (ch1 << 0));
    }
    
    /**
     * Method readInt.
     * @return int
     * @throws IOException
     * @see java.io.DataInput#readInt()
     */
    public int readInt() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4);
    }
    
    /**
     * Reads a signed 32-bit integer from this stream in little-endian order.
     * This method reads 4
     * bytes from the stream, starting at the current stream pointer.
     * If the bytes read, in order, are <code>b1</code>,
     * <code>b2</code>, <code>b3</code>, and <code>b4</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2, b3, b4&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b4 &lt;&lt; 24) | (b3 &lt;&lt; 16) + (b2 &lt;&lt; 8) + b1
     * </pre></blockquote>
     * <p>
     * This method blocks until the four bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
    
    
    
     * @return     the next four bytes of this stream, interpreted as an
     *             <code>int</code>. * @exception  IOException   if an I/O error occurs. * @exception  EOFException  if this stream reaches the end before reading
     *               four bytes. */
    public final int readIntLE() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }
    
    /**
     * Reads an unsigned 32-bit integer from this stream. This method reads 4
     * bytes from the stream, starting at the current stream pointer.
     * If the bytes read, in order, are <code>b1</code>,
     * <code>b2</code>, <code>b3</code>, and <code>b4</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2, b3, b4&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b1 &lt;&lt; 24) | (b2 &lt;&lt; 16) + (b3 &lt;&lt; 8) + b4
     * </pre></blockquote>
     * <p>
     * This method blocks until the four bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
    
    
    
     * @return     the next four bytes of this stream, interpreted as a
     *             <code>long</code>. * @exception  IOException   if an I/O error occurs. * @exception  EOFException  if this stream reaches the end before reading
     *               four bytes. */
    public final long readUnsignedInt() throws IOException {
        long ch1 = this.read();
        long ch2 = this.read();
        long ch3 = this.read();
        long ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
    
    /**
     * Method readUnsignedIntLE.
     * @return long
     * @throws IOException
     */
    public final long readUnsignedIntLE() throws IOException {
        long ch1 = this.read();
        long ch2 = this.read();
        long ch3 = this.read();
        long ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }
    
    /**
     * Method readLong.
     * @return long
     * @throws IOException
     * @see java.io.DataInput#readLong()
     */
    public long readLong() throws IOException {
        return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
    }
    
    /**
     * Method readLongLE.
     * @return long
     * @throws IOException
     */
    public final long readLongLE() throws IOException {
        int i1 = readIntLE();
        int i2 = readIntLE();
        return ((long)i2 << 32) + (i1 & 0xFFFFFFFFL);
    }
    
    /**
     * Method readFloat.
     * @return float
     * @throws IOException
     * @see java.io.DataInput#readFloat()
     */
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }
    
    /**
     * Method readFloatLE.
     * @return float
     * @throws IOException
     */
    public final float readFloatLE() throws IOException {
        return Float.intBitsToFloat(readIntLE());
    }
    
    /**
     * Method readDouble.
     * @return double
     * @throws IOException
     * @see java.io.DataInput#readDouble()
     */
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }
    
    /**
     * Method readDoubleLE.
     * @return double
     * @throws IOException
     */
    public final double readDoubleLE() throws IOException {
        return Double.longBitsToDouble(readLongLE());
    }
    
    /**
     * Method readLine.
     * @return String
     * @throws IOException
     * @see java.io.DataInput#readLine()
     */
    public String readLine() throws IOException {
        StringBuffer input = new StringBuffer();
        int c = -1;
        boolean eol = false;
        
        while (!eol) {
            switch (c = read()) {
                case -1:
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    int cur = getFilePointer();
                    if ((read()) != '\n') {
                        seek(cur);
                    }
                    break;
                default:
                    input.append((char)c);
                    break;
            }
        }
        
        if ((c == -1) && (input.length() == 0)) {
            return null;
        }
        return input.toString();
    }
    
    /**
     * Method readUTF.
     * @return String
     * @throws IOException
     * @see java.io.DataInput#readUTF()
     */
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }
    
    /** Getter for property startOffset.
    
     *
     * @return Value of property startOffset. */
    public int getStartOffset() {
        return this.startOffset;
    }
    
    /** Setter for property startOffset.
     * @param startOffset New value of property startOffset.
     *
     */
    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }
    
}
