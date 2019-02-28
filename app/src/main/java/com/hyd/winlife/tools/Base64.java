//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hyd.winlife.tools;

public class Base64 extends BaseNCodec {
    private static final int BITS_PER_ENCODED_BYTE = 6;
    private static final int BYTES_PER_UNENCODED_BLOCK = 3;
    private static final int BYTES_PER_ENCODED_BLOCK = 4;
    static final byte[] CHUNK_SEPARATOR = new byte[]{13, 10};
    private static final byte[] STANDARD_ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    private static final int MASK_6BITS = 63;
    private final byte[] mEncodeTable;
    private final byte[] mDecodeTable;
    private final byte[] mLineSeparator;
    private final int mDecodeSize;
    private final int mEncodeSize;
    private int mBitWorkArea;

    public Base64() {
        this(0);
    }

    public Base64(boolean var1) {
        this(76, CHUNK_SEPARATOR, var1);
    }

    public Base64(int var1) {
        this(var1, CHUNK_SEPARATOR);
    }

    public Base64(int var1, byte[] var2) {
        this(var1, var2, false);
    }

    public Base64(int var1, byte[] var2, boolean var3) {
        super(3, 4, var1, var2 == null?0:var2.length);
        this.mDecodeTable = DECODE_TABLE;
        if(var2 != null) {
            if(this.containsAlphabetOrPad(var2)) {
                String var4 = AesHelper.newStringUtf8(var2);
                throw new IllegalArgumentException("lineSeparator must not contain base64 characters: [" + var4 + "]");
            }

            if(var1 > 0) {
                this.mEncodeSize = 4 + var2.length;
                this.mLineSeparator = new byte[var2.length];
                System.arraycopy(var2, 0, this.mLineSeparator, 0, var2.length);
            } else {
                this.mEncodeSize = 4;
                this.mLineSeparator = null;
            }
        } else {
            this.mEncodeSize = 4;
            this.mLineSeparator = null;
        }

        this.mDecodeSize = this.mEncodeSize - 1;
        this.mEncodeTable = var3?URL_SAFE_ENCODE_TABLE:STANDARD_ENCODE_TABLE;
    }

    void encode(byte[] var1, int var2, int var3) {
        if(!this.mEof) {
            int var4;
            if(var3 < 0) {
                this.mEof = true;
                if(0 == this.mModulus && this.mLineLength == 0) {
                    return;
                }

                this.ensureBufferSize(this.mEncodeSize);
                var4 = this.mPos;
                switch(this.mModulus) {
                case 1:
                    this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea >> 2 & 63];
                    this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea << 4 & 63];
                    if(this.mEncodeTable == STANDARD_ENCODE_TABLE) {
                        this.mBuffer[this.mPos++] = 61;
                        this.mBuffer[this.mPos++] = 61;
                    }
                    break;
                case 2:
                    this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea >> 10 & 63];
                    this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea >> 4 & 63];
                    this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea << 2 & 63];
                    if(this.mEncodeTable == STANDARD_ENCODE_TABLE) {
                        this.mBuffer[this.mPos++] = 61;
                    }
                }

                this.mCurrentLinePos += this.mPos - var4;
                if(this.mLineLength > 0 && this.mCurrentLinePos > 0) {
                    System.arraycopy(this.mLineSeparator, 0, this.mBuffer, this.mPos, this.mLineSeparator.length);
                    this.mPos += this.mLineSeparator.length;
                }
            } else {
                for(var4 = 0; var4 < var3; ++var4) {
                    this.ensureBufferSize(this.mEncodeSize);
                    this.mModulus = (this.mModulus + 1) % 3;
                    int var5 = var1[var2++];
                    if(var5 < 0) {
                        var5 += 256;
                    }

                    this.mBitWorkArea = (this.mBitWorkArea << 8) + var5;
                    if(0 == this.mModulus) {
                        this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea >> 18 & 63];
                        this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea >> 12 & 63];
                        this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea >> 6 & 63];
                        this.mBuffer[this.mPos++] = this.mEncodeTable[this.mBitWorkArea & 63];
                        this.mCurrentLinePos += 4;
                        if(this.mLineLength > 0 && this.mLineLength <= this.mCurrentLinePos) {
                            System.arraycopy(this.mLineSeparator, 0, this.mBuffer, this.mPos, this.mLineSeparator.length);
                            this.mPos += this.mLineSeparator.length;
                            this.mCurrentLinePos = 0;
                        }
                    }
                }
            }

        }
    }

    void decode(byte[] var1, int var2, int var3) {
        if(!this.mEof) {
            if(var3 < 0) {
                this.mEof = true;
            }

            for(int var4 = 0; var4 < var3; ++var4) {
                this.ensureBufferSize(this.mDecodeSize);
                byte var5 = var1[var2++];
                if(var5 == 61) {
                    this.mEof = true;
                    break;
                }

                if(var5 >= 0 && var5 < DECODE_TABLE.length) {
                    byte var6 = DECODE_TABLE[var5];
                    if(var6 >= 0) {
                        this.mModulus = (this.mModulus + 1) % 4;
                        this.mBitWorkArea = (this.mBitWorkArea << 6) + var6;
                        if(this.mModulus == 0) {
                            this.mBuffer[this.mPos++] = (byte)(this.mBitWorkArea >> 16 & 255);
                            this.mBuffer[this.mPos++] = (byte)(this.mBitWorkArea >> 8 & 255);
                            this.mBuffer[this.mPos++] = (byte)(this.mBitWorkArea & 255);
                        }
                    }
                }
            }

            if(this.mEof && this.mModulus != 0) {
                this.ensureBufferSize(this.mDecodeSize);
                switch(this.mModulus) {
                case 2:
                    this.mBitWorkArea >>= 4;
                    this.mBuffer[this.mPos++] = (byte)(this.mBitWorkArea & 255);
                    break;
                case 3:
                    this.mBitWorkArea >>= 2;
                    this.mBuffer[this.mPos++] = (byte)(this.mBitWorkArea >> 8 & 255);
                    this.mBuffer[this.mPos++] = (byte)(this.mBitWorkArea & 255);
                }
            }

        }
    }

    public static String encodeBase64String(byte[] var0) {
        return AesHelper.newStringUtf8(encodeBase64(var0, false));
    }

    public static byte[] encodeBase64(byte[] var0, boolean var1) {
        return encodeBase64(var0, var1, false);
    }

    public static byte[] encodeBase64(byte[] var0, boolean var1, boolean var2) {
        return encodeBase64(var0, var1, var2, 2147483647);
    }

    public static byte[] encodeBase64(byte[] var0, boolean var1, boolean var2, int var3) {
        if(var0 != null && var0.length != 0) {
            Base64 var4 = var1?new Base64(var2):new Base64(0, CHUNK_SEPARATOR, var2);
            long var5 = var4.getEncodedLength(var0);
            if(var5 > (long)var3) {
                throw new IllegalArgumentException("Input array too big, the output array would be bigger (" + var5 + ") than the specified maximum size of " + var3);
            } else {
                return var4.encode(var0);
            }
        } else {
            return var0;
        }
    }

    public static byte[] decodeBase64(String var0) {
        return (new Base64()).decode(var0);
    }

    protected boolean isInAlphabet(byte var1) {
        return var1 >= 0 && var1 < this.mDecodeTable.length && this.mDecodeTable[var1] != -1;
    }
}
