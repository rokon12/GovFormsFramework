/*
 * Copyright 1999-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lowagie.text.pdf.hyphenation;

import java.io.Serializable;

/**
 * This class implements a simple byte vector with access to the
 * underlying array.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 * @version $Revision: 1.0 $
 */
public class ByteVector implements Serializable {

    /**
     * Capacity increment size
     */
    private static final int DEFAULT_BLOCK_SIZE = 2048;
    private int blockSize;

    /**
     * The encapsulated array
     */
    private byte[] array;

    /**
     * Points to next free item
     */
    private int n;

    public ByteVector() {
        this(DEFAULT_BLOCK_SIZE);
    }

    /**
     * Constructor for ByteVector.
     * @param capacity int
     */
    public ByteVector(int capacity) {
        if (capacity > 0) {
            blockSize = capacity;
        } else {
            blockSize = DEFAULT_BLOCK_SIZE;
        }
        array = new byte[blockSize];
        n = 0;
    }

    /**
     * Constructor for ByteVector.
     * @param a byte[]
     */
    public ByteVector(byte[] a) {
        blockSize = DEFAULT_BLOCK_SIZE;
        array = a;
        n = 0;
    }

    /**
     * Constructor for ByteVector.
     * @param a byte[]
     * @param capacity int
     */
    public ByteVector(byte[] a, int capacity) {
        if (capacity > 0) {
            blockSize = capacity;
        } else {
            blockSize = DEFAULT_BLOCK_SIZE;
        }
        array = a;
        n = 0;
    }

    /**
     * Method getArray.
     * @return byte[]
     */
    public byte[] getArray() {
        return array;
    }

    /**
     * return number of items in array
     * @return int
     */
    public int length() {
        return n;
    }

    /**
     * returns current capacity of array
     * @return int
     */
    public int capacity() {
        return array.length;
    }

    /**
     * Method put.
     * @param index int
     * @param val byte
     */
    public void put(int index, byte val) {
        array[index] = val;
    }

    /**
     * Method get.
     * @param index int
     * @return byte
     */
    public byte get(int index) {
        return array[index];
    }

    /**
     * This is to implement memory allocation in the array. Like malloc().
     * @param size int
     * @return int
     */
    public int alloc(int size) {
        int index = n;
        int len = array.length;
        if (n + size >= len) {
            byte[] aux = new byte[len + blockSize];
            System.arraycopy(array, 0, aux, 0, len);
            array = aux;
        }
        n += size;
        return index;
    }

    public void trimToSize() {
        if (n < array.length) {
            byte[] aux = new byte[n];
            System.arraycopy(array, 0, aux, 0, n);
            array = aux;
        }
    }

}
