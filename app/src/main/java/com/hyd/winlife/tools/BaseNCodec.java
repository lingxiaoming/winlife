//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hyd.winlife.tools;

public abstract class BaseNCodec {
    public static final int MIME_CHUNK_SIZE = 76;
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    protected static final int MASK_8BITS = 255;
    protected static final byte PAD_DEFAULT = 61;
    protected static final byte PAD = 61;
    private final int mUnencodedBlockSize;
    private final int mEncodedBlockSize;
    protected final int mLineLength;
    private final int mChunkSeparatorLength;
    protected byte[] mBuffer;
    protected int mPos;
    private int mReadPos;
    protected boolean mEof;
    protected int mCurrentLinePos;
    protected int mModulus;

    protected BaseNCodec(int var1, int var2, int var3, int var4) {
        this.mUnencodedBlockSize = var1;
        this.mEncodedBlockSize = var2;
        this.mLineLength = var3 > 0 && var4 > 0?var3 / var2 * var2:0;
        this.mChunkSeparatorLength = var4;
    }

    boolean hasData() {
        return this.mBuffer != null;
    }

    int available() {
        return this.mBuffer != null?this.mPos - this.mReadPos:0;
    }

    protected int getDefaultBufferSize() {
        return 8192;
    }

    private void resizeBuffer() {
        if(this.mBuffer == null) {
            this.mBuffer = new byte[this.getDefaultBufferSize()];
            this.mPos = 0;
            this.mReadPos = 0;
        } else {
            byte[] var1 = new byte[this.mBuffer.length * 2];
            System.arraycopy(this.mBuffer, 0, var1, 0, this.mBuffer.length);
            this.mBuffer = var1;
        }

    }

    protected void ensureBufferSize(int var1) {
        if(this.mBuffer == null || this.mBuffer.length < this.mPos + var1) {
            this.resizeBuffer();
        }

    }

    int readResults(byte[] var1, int var2, int var3) {
        if(this.mBuffer != null) {
            int var4 = Math.min(this.available(), var3);
            System.arraycopy(this.mBuffer, this.mReadPos, var1, var2, var4);
            this.mReadPos += var4;
            if(this.mReadPos >= this.mPos) {
                this.mBuffer = null;
            }

            return var4;
        } else {
            return this.mEof?-1:0;
        }
    }

    protected static boolean isWhiteSpace(byte var0) {
        switch(var0) {
        case 9:
        case 10:
        case 13:
        case 32:
            return true;
        default:
            return false;
        }
    }

    private void reset() {
        this.mBuffer = null;
        this.mPos = 0;
        this.mReadPos = 0;
        this.mCurrentLinePos = 0;
        this.mModulus = 0;
        this.mEof = false;
    }

    public Object encode(Object var1) throws Exception {
        if(!(var1 instanceof byte[])) {
            throw new Exception("Parameter supplied to Base-N encode is not a byte[]");
        } else {
            return this.encode((byte[])((byte[])var1));
        }
    }

    public String encodeToString(byte[] var1) {
        return AesHelper.newStringUtf8(this.encode(var1));
    }

    public Object decode(Object var1) throws Exception {
        if(var1 instanceof byte[]) {
            return this.decode((byte[])((byte[])var1));
        } else if(var1 instanceof String) {
            return this.decode((String)var1);
        } else {
            throw new Exception("Parameter supplied to Base-N decode is not a byte[] or a String");
        }
    }

    public byte[] decode(String var1) {
        return this.decode(AesHelper.getBytesUtf8(var1));
    }

    public byte[] decode(byte[] var1) {
        this.reset();
        if(var1 != null && var1.length != 0) {
            this.decode(var1, 0, var1.length);
            this.decode(var1, 0, -1);
            byte[] var2 = new byte[this.mPos];
            this.readResults(var2, 0, var2.length);
            return var2;
        } else {
            return var1;
        }
    }

    public byte[] encode(byte[] var1) {
        this.reset();
        if(var1 != null && var1.length != 0) {
            this.encode(var1, 0, var1.length);
            this.encode(var1, 0, -1);
            byte[] var2 = new byte[this.mPos - this.mReadPos];
            this.readResults(var2, 0, var2.length);
            return var2;
        } else {
            return var1;
        }
    }

    public String encodeAsString(byte[] var1) {
        return AesHelper.newStringUtf8(this.encode(var1));
    }

    abstract void encode(byte[] var1, int var2, int var3);

    abstract void decode(byte[] var1, int var2, int var3);

    protected abstract boolean isInAlphabet(byte var1);

    public boolean isInAlphabet(byte[] var1, boolean var2) {
        for(int var3 = 0; var3 < var1.length; ++var3) {
            if(!this.isInAlphabet(var1[var3]) && (!var2 || var1[var3] != 61 && !isWhiteSpace(var1[var3]))) {
                return false;
            }
        }

        return true;
    }

    public boolean isInAlphabet(String var1) {
        return this.isInAlphabet(AesHelper.getBytesUtf8(var1), true);
    }

    protected boolean containsAlphabetOrPad(byte[] var1) {
        if(var1 == null) {
            return false;
        } else {
            for(int var2 = 0; var2 < var1.length; ++var2) {
                if(61 == var1[var2] || this.isInAlphabet(var1[var2])) {
                    return true;
                }
            }

            return false;
        }
    }

    public long getEncodedLength(byte[] var1) {
        long var2 = (long)((var1.length + this.mUnencodedBlockSize - 1) / this.mUnencodedBlockSize) * (long)this.mEncodedBlockSize;
        if(this.mLineLength > 0) {
            var2 += (var2 + (long)this.mLineLength - 1L) / (long)this.mLineLength * (long)this.mChunkSeparatorLength;
        }

        return var2;
    }
}
