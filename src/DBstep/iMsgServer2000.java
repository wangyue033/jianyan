package DBstep;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class iMsgServer2000
{
  private String _$287 = "DBSTEP V3.0";
  private String _$288 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
  private File _$289;
  private String _$290 = new String();
  private String _$291 = new String();
  private String _$292 = new String();
  private int _$293 = 0;
  private String _$294;
  public int BuffSize = 100;
  public boolean CryptFile = false;
  public String Charset = "GB2312";
  static final int S11 = 7;
  static final int S12 = 12;
  static final int S13 = 17;
  static final int S14 = 22;
  static final int S21 = 5;
  static final int S22 = 9;
  static final int S23 = 14;
  static final int S24 = 20;
  static final int S31 = 4;
  static final int S32 = 11;
  static final int S33 = 16;
  static final int S34 = 23;
  static final int S41 = 6;
  static final int S42 = 10;
  static final int S43 = 15;
  static final int S44 = 21;
  static final byte[] PADDING = { -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
  private long[] _$315 = new long[4];
  private long[] _$316 = new long[2];
  private byte[] _$317 = new byte[64];
  private String _$318;
  private byte[] _$319 = new byte[16];
  
  private String _$320(String inbuf)
  {
    _$322();
    _$323(inbuf.getBytes(), inbuf.length());
    _$325();
    this._$318 = "";
    for (int i = 0; i < 16; i++) {
      this._$318 += _$327(this._$319[i]);
    }
    return this._$318;
  }
  
  private void _$322()
  {
    this._$316[0] = 0L;
    this._$316[1] = 0L;
    

    this._$315[0] = 1732584193L;
    this._$315[1] = 4023233417L;
    this._$315[2] = 2562383102L;
    this._$315[3] = 271733878L;
  }
  
  private long _$328(long x, long y, long z)
  {
    return x & y | (x ^ 0xFFFFFFFF) & z;
  }
  
  private long _$332(long x, long y, long z)
  {
    return x & z | y & (z ^ 0xFFFFFFFF);
  }
  
  private long _$333(long x, long y, long z)
  {
    return x ^ y ^ z;
  }
  
  private long _$334(long x, long y, long z)
  {
    return y ^ (x | z ^ 0xFFFFFFFF);
  }
  
  private long _$335(long a, long b, long c, long d, long x, long s, long ac)
  {
    a += _$328(b, c, d) + x + ac;
    a = (int)a << (int)s | (int)a >>> (int)(32L - s);
    a += b;
    return a;
  }
  
  private long _$342(long a, long b, long c, long d, long x, long s, long ac)
  {
    a += _$332(b, c, d) + x + ac;
    a = (int)a << (int)s | (int)a >>> (int)(32L - s);
    a += b;
    return a;
  }
  
  private long _$343(long a, long b, long c, long d, long x, long s, long ac)
  {
    a += _$333(b, c, d) + x + ac;
    a = (int)a << (int)s | (int)a >>> (int)(32L - s);
    a += b;
    return a;
  }
  
  private long _$344(long a, long b, long c, long d, long x, long s, long ac)
  {
    a += _$334(b, c, d) + x + ac;
    a = (int)a << (int)s | (int)a >>> (int)(32L - s);
    a += b;
    return a;
  }
  
  private void _$323(byte[] inbuf, int inputLen)
  {
    byte[] block = new byte[64];
    int index = (int)(this._$316[0] >>> 3) & 0x3F;
    if ((this._$316[0] += (inputLen << 3)) < (inputLen << 3)) {
      this._$316[1] += 1L;
    }
    this._$316[1] += (inputLen >>> 29);
    
    int partLen = 64 - index;
    int i;
    if (inputLen >= partLen)
    {
      _$349(this._$317, inbuf, index, 0, partLen);
      _$350(this._$317);
      for (i = partLen; i + 63 < inputLen; i += 64)
      {
        _$349(block, inbuf, 0, i, 64);
        _$350(block);
      }
      index = 0;
    }
    else
    {
      i = 0;
    }
    _$349(this._$317, inbuf, index, i, inputLen - i);
  }
  
  private void _$325()
  {
    byte[] bits = new byte[8];
    


    _$353(bits, this._$316, 8);
    

    int index = (int)(this._$316[0] >>> 3) & 0x3F;
    int padLen = index < 56 ? 56 - index : 120 - index;
    _$323(PADDING, padLen);
    

    _$323(bits, 8);
    

    _$353(this._$319, this._$315, 16);
  }
  
  private void _$349(byte[] output, byte[] input, int outpos, int inpos, int len)
  {
    for (int i = 0; i < len; i++) {
      output[(outpos + i)] = input[(inpos + i)];
    }
  }
  
  private void _$350(byte[] block)
  {
    long a = this._$315[0];long b = this._$315[1];long c = this._$315[2];long d = this._$315[3];
    long[] x = new long[16];
    
    _$359(x, block, 64);
    

    a = _$335(a, b, c, d, x[0], 7L, 3614090360L);
    d = _$335(d, a, b, c, x[1], 12L, 3905402710L);
    c = _$335(c, d, a, b, x[2], 17L, 606105819L);
    b = _$335(b, c, d, a, x[3], 22L, 3250441966L);
    a = _$335(a, b, c, d, x[4], 7L, 4118548399L);
    d = _$335(d, a, b, c, x[5], 12L, 1200080426L);
    c = _$335(c, d, a, b, x[6], 17L, 2821735955L);
    b = _$335(b, c, d, a, x[7], 22L, 4249261313L);
    a = _$335(a, b, c, d, x[8], 7L, 1770035416L);
    d = _$335(d, a, b, c, x[9], 12L, 2336552879L);
    c = _$335(c, d, a, b, x[10], 17L, 4294925233L);
    b = _$335(b, c, d, a, x[11], 22L, 2304563134L);
    a = _$335(a, b, c, d, x[12], 7L, 1804603682L);
    d = _$335(d, a, b, c, x[13], 12L, 4254626195L);
    c = _$335(c, d, a, b, x[14], 17L, 2792965006L);
    b = _$335(b, c, d, a, x[15], 22L, 1236535329L);
    

    a = _$342(a, b, c, d, x[1], 5L, 4129170786L);
    d = _$342(d, a, b, c, x[6], 9L, 3225465664L);
    c = _$342(c, d, a, b, x[11], 14L, 643717713L);
    b = _$342(b, c, d, a, x[0], 20L, 3921069994L);
    a = _$342(a, b, c, d, x[5], 5L, 3593408605L);
    d = _$342(d, a, b, c, x[10], 9L, 38016083L);
    c = _$342(c, d, a, b, x[15], 14L, 3634488961L);
    b = _$342(b, c, d, a, x[4], 20L, 3889429448L);
    a = _$342(a, b, c, d, x[9], 5L, 568446438L);
    d = _$342(d, a, b, c, x[14], 9L, 3275163606L);
    c = _$342(c, d, a, b, x[3], 14L, 4107603335L);
    b = _$342(b, c, d, a, x[8], 20L, 1163531501L);
    a = _$342(a, b, c, d, x[13], 5L, 2850285829L);
    d = _$342(d, a, b, c, x[2], 9L, 4243563512L);
    c = _$342(c, d, a, b, x[7], 14L, 1735328473L);
    b = _$342(b, c, d, a, x[12], 20L, 2368359562L);
    

    a = _$343(a, b, c, d, x[5], 4L, 4294588738L);
    d = _$343(d, a, b, c, x[8], 11L, 2272392833L);
    c = _$343(c, d, a, b, x[11], 16L, 1839030562L);
    b = _$343(b, c, d, a, x[14], 23L, 4259657740L);
    a = _$343(a, b, c, d, x[1], 4L, 2763975236L);
    d = _$343(d, a, b, c, x[4], 11L, 1272893353L);
    c = _$343(c, d, a, b, x[7], 16L, 4139469664L);
    b = _$343(b, c, d, a, x[10], 23L, 3200236656L);
    a = _$343(a, b, c, d, x[13], 4L, 681279174L);
    d = _$343(d, a, b, c, x[0], 11L, 3936430074L);
    c = _$343(c, d, a, b, x[3], 16L, 3572445317L);
    b = _$343(b, c, d, a, x[6], 23L, 76029189L);
    a = _$343(a, b, c, d, x[9], 4L, 3654602809L);
    d = _$343(d, a, b, c, x[12], 11L, 3873151461L);
    c = _$343(c, d, a, b, x[15], 16L, 530742520L);
    b = _$343(b, c, d, a, x[2], 23L, 3299628645L);
    

    a = _$344(a, b, c, d, x[0], 6L, 4096336452L);
    d = _$344(d, a, b, c, x[7], 10L, 1126891415L);
    c = _$344(c, d, a, b, x[14], 15L, 2878612391L);
    b = _$344(b, c, d, a, x[5], 21L, 4237533241L);
    a = _$344(a, b, c, d, x[12], 6L, 1700485571L);
    d = _$344(d, a, b, c, x[3], 10L, 2399980690L);
    c = _$344(c, d, a, b, x[10], 15L, 4293915773L);
    b = _$344(b, c, d, a, x[1], 21L, 2240044497L);
    a = _$344(a, b, c, d, x[8], 6L, 1873313359L);
    d = _$344(d, a, b, c, x[15], 10L, 4264355552L);
    c = _$344(c, d, a, b, x[6], 15L, 2734768916L);
    b = _$344(b, c, d, a, x[13], 21L, 1309151649L);
    a = _$344(a, b, c, d, x[4], 6L, 4149444226L);
    d = _$344(d, a, b, c, x[11], 10L, 3174756917L);
    c = _$344(c, d, a, b, x[2], 15L, 718787259L);
    b = _$344(b, c, d, a, x[9], 21L, 3951481745L);
    
    this._$315[0] += a;
    this._$315[1] += b;
    this._$315[2] += c;
    this._$315[3] += d;
  }
  
  private void _$353(byte[] output, long[] input, int len)
  {
    int i = 0;
    for (int j = 0; j < len; j += 4)
    {
      output[j] = ((byte)(int)(input[i] & 0xFF));
      output[(j + 1)] = ((byte)(int)(input[i] >>> 8 & 0xFF));
      output[(j + 2)] = ((byte)(int)(input[i] >>> 16 & 0xFF));
      output[(j + 3)] = ((byte)(int)(input[i] >>> 24 & 0xFF));i++;
    }
  }
  
  private void _$359(long[] output, byte[] input, int len)
  {
    int i = 0;
    for (int j = 0; j < len; j += 4)
    {
      output[i] = (_$361(input[j]) | _$361(input[(j + 1)]) << 8 | _$361(input[(j + 2)]) << 16 | _$361(input[(j + 3)]) << 24);i++;
    }
  }
  
  private static long _$361(byte b)
  {
    return b < 0 ? b & 0xFF : b;
  }
  
  private static String _$327(byte ib)
  {
    char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    
    char[] ob = new char[2];
    ob[0] = Digit[(ib >>> 4 & 0xF)];
    ob[1] = Digit[(ib & 0xF)];
    String s = new String(ob);
    return s;
  }
  
  private String _$365(byte[] Value)
  {
    _$322();
    _$323(Value, Value.length);
    _$325();
    this._$318 = "";
    for (int i = 0; i < 16; i++) {
      this._$318 += _$327(this._$319[i]);
    }
    return this._$318;
  }
  
  public iMsgServer2000()
  {
    this._$290 = "";
    this._$291 = "";
    this._$292 = "DBSTEP V3.0";
    try
    {
      this._$289 = File.createTempFile("~GG", ".tmp");
      this._$294 = this._$289.getName();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    this._$289.deleteOnExit();
  }
  
  protected void finalize()
  {
    try
    {
      if (this._$294.matches(this._$289.getName())) {
        this._$289.delete();
      }
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
  }
  
  protected String FormatHead(String vString)
  {
    if (vString.length() > 16) {
      return vString.substring(0, 16);
    }
    for (int i = vString.length() + 1; i < 17; i++) {
      vString = vString.concat(" ");
    }
    return vString;
  }
  
  private byte[] _$378(byte[] mStream)
  {
    int HeadSize = 64;
    int BodySize = 0;
    int ErrorSize = 0;
    int FileSize = 0;
    int Position = 0;
    int BlockSize = 1024 * this.BuffSize;
    byte[] BlockBuf = new byte[BlockSize];
    ByteArrayOutputStream mWite = new ByteArrayOutputStream();
    
    int CurSize = 0;
    int CurRead = 0;
    try
    {
      BodySize = this._$290.getBytes(this.Charset).length;
      ErrorSize = this._$291.getBytes(this.Charset).length;
      FileSize = (int)this._$289.length();
      this._$293 = FileSize;
      if ((FileSize > 0) && 
        (!this.CryptFile))
      {
        FileInputStream mRead = new FileInputStream(this._$289);
        
        CurSize = BlockSize;
        while (FileSize > 0)
        {
          if (FileSize - BlockSize < BlockSize)
          {
            CurSize = FileSize;
            BlockBuf = new byte[CurSize];
          }
          CurRead = 0;
          while (CurRead < CurSize) {
            CurRead += mRead.read(BlockBuf, CurRead, CurSize - CurRead);
          }
          mWite.write(BlockBuf, 0, CurSize);
          FileSize -= CurSize;
        }
        this._$293 = mWite.size();
        mRead.close();
        mWite.close();
      }
      ByteArrayOutputStream mBuffer = new ByteArrayOutputStream();
      






      String HeadString = FormatHead(this._$292) + FormatHead(String.valueOf(BodySize)) + FormatHead(String.valueOf(ErrorSize)) + FormatHead(String.valueOf(this._$293));
      


      mBuffer.write(HeadString.getBytes(), 0, HeadSize);
      Position += HeadSize;
      if (BodySize > 0) {
        mBuffer.write(this._$290.getBytes());
      }
      Position += BodySize;
      if (ErrorSize > 0) {
        mBuffer.write(this._$291.getBytes(this.Charset));
      }
      Position += ErrorSize;
      if (this._$293 > 0) {
        mBuffer.write(mWite.toByteArray());
      }
      mBuffer.close();
      

      return mBuffer.toByteArray();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return null;
  }
  
  private boolean _$378(HttpServletResponse response)
  {
    int HeadSize = 64;
    int BodySize = 0;
    int ErrorSize = 0;
    int FileSize = 0;
    int Position = 0;
    int BlockSize = 1024 * this.BuffSize;
    byte[] BlockBuf = new byte[BlockSize];
    ByteArrayOutputStream mWite = new ByteArrayOutputStream();
    
    int CurSize = 0;
    int CurRead = 0;
    try
    {
      ServletOutputStream out = response.getOutputStream();
      
      BodySize = this._$290.getBytes(this.Charset).length;
      ErrorSize = this._$291.getBytes(this.Charset).length;
      FileSize = (int)this._$289.length();
      this._$293 = FileSize;
      if ((FileSize > 0) && 
        (!this.CryptFile))
      {
        FileInputStream mRead = new FileInputStream(this._$289);
        
        CurSize = BlockSize;
        while (FileSize > 0)
        {
          if (FileSize - BlockSize < BlockSize)
          {
            CurSize = FileSize;
            BlockBuf = new byte[CurSize];
          }
          CurRead = 0;
          while (CurRead < CurSize) {
            CurRead += mRead.read(BlockBuf, CurRead, CurSize - CurRead);
          }
          mWite.write(BlockBuf, 0, CurSize);
          FileSize -= CurSize;
        }
        this._$293 = mWite.size();
        mRead.close();
        mWite.close();
      }
      String HeadString = FormatHead(this._$292) + FormatHead(String.valueOf(BodySize)) + FormatHead(String.valueOf(ErrorSize)) + FormatHead(String.valueOf(this._$293));
      


      out.write(HeadString.getBytes());
      

      Position += HeadSize;
      if (BodySize > 0) {
        out.write(this._$290.getBytes());
      }
      Position += BodySize;
      if (ErrorSize > 0) {
        out.write(this._$291.getBytes(this.Charset));
      }
      Position += ErrorSize;
      if (this._$293 > 0) {
        out.write(mWite.toByteArray());
      }
      out.flush();
      return true;
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return false;
  }
  
  public byte[] MsgVariant()
  {
    byte[] mStream = null;
    return _$378(mStream);
  }
  
  private static int _$402(byte[] b)
  {
    int s = 0;
    for (int i = 3; i > 0; i--)
    {
      if (b[i] >= 0) {
        s += b[i];
      } else {
        s = s + 256 + b[i];
      }
      s *= 256;
    }
    if (b[0] >= 0) {
      s += b[0];
    } else {
      s = s + 256 + b[0];
    }
    return s;
  }
  
  public byte[] ToDocument(byte[] Value)
  {
    byte[] mIntBuf = { 0, 0, 0, 0 };
    byte[] mFlagBuf = { 68, 73, 82, 71 };
    byte[] mOutBuf = null;
    int HeadFlag = 0;
    int Signature = 0;
    int WordSize = 0;
    int PageSize = 0;
    int FlagSize = 0;
    try
    {
      HeadFlag = _$402(mFlagBuf);
      
      ByteArrayInputStream mStream = new ByteArrayInputStream(Value);
      
      mStream.read(mIntBuf, 0, 4);
      Signature = _$402(mIntBuf);
      
      mStream.read(mIntBuf, 0, 4);
      WordSize = _$402(mIntBuf);
      mStream.read(mIntBuf, 0, 4);
      PageSize = _$402(mIntBuf);
      mStream.read(mIntBuf, 0, 4);
      FlagSize = _$402(mIntBuf);
      if (Signature != HeadFlag)
      {
        mStream.reset();
        WordSize = mStream.available();
      }
      mOutBuf = new byte[WordSize];
      mStream.read(mOutBuf, 0, WordSize);
      
      return mOutBuf;
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return mOutBuf;
  }
  
  private boolean _$413(byte[] mStream)
  {
    int HeadSize = 64;
    int BodySize = 0;
    int ErrorSize = 0;
    int FileSize = 0;
    int Position = 0;
    int CurRead = 0;
    String Md5Value = "";
    String Md5Calcu = "";
    try
    {
      Position = 0;
      String HeadString = new String(mStream, Position, HeadSize);
      this._$292 = HeadString.substring(0, 15);
      BodySize = Integer.parseInt(HeadString.substring(16, 31).trim());
      ErrorSize = Integer.parseInt(HeadString.substring(32, 47).trim());
      FileSize = Integer.parseInt(HeadString.substring(48, 63).trim());
      this._$293 = FileSize;
      
      Position += HeadSize;
      if (BodySize > 0) {
        this._$290 = new String(mStream, Position, BodySize);
      }
      Position += BodySize;
      if (ErrorSize > 0) {
        this._$291 = new String(mStream, Position, ErrorSize);
      }
      Position += ErrorSize;
      if (FileSize > 0)
      {
        if (!this.CryptFile)
        {
          ByteArrayInputStream mRead = new ByteArrayInputStream(mStream, Position, FileSize);
          
          FileOutputStream mWite = new FileOutputStream(this._$289);
          
          int BlockSize = 1024 * this.BuffSize;
          int CurSize = 0;
          
          byte[] BlockBuf = new byte[BlockSize];
          
          CurSize = BlockSize;
          while (FileSize > 0)
          {
            if (FileSize < BlockSize)
            {
              CurSize = FileSize;
              BlockBuf = new byte[CurSize];
            }
            CurRead = 0;
            while (CurRead < CurSize) {
              CurRead += mRead.read(BlockBuf, CurRead, CurSize - CurRead);
            }
            mWite.write(BlockBuf, 0, CurSize);
            FileSize -= CurSize;
          }
          mWite.close();
          mRead.close();
        }
        this._$293 = ((int)this._$289.length());
      }
      return true;
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return false;
  }
  
  private boolean _$413(HttpServletRequest request)
  {
    int HeadSize = 64;
    int BodySize = 0;
    int ErrorSize = 0;
    int FileSize = 0;
    int CurRead = 0;
    try
    {
      ServletInputStream mRead = request.getInputStream();
      byte[] BlockBuf = new byte[HeadSize];
      mRead.read(BlockBuf, 0, HeadSize);
      
      String HeadString = new String(BlockBuf, 0, HeadSize);
      
      this._$292 = HeadString.substring(0, 15);
      BodySize = Integer.parseInt(HeadString.substring(16, 31).trim());
      ErrorSize = Integer.parseInt(HeadString.substring(32, 47).trim());
      this._$293 = Integer.parseInt(HeadString.substring(48, 63).trim());
      FileSize = this._$293;
      if (BodySize > 0)
      {
        BlockBuf = new byte[BodySize];
        CurRead = 0;
        while (CurRead < BodySize) {
          CurRead += mRead.read(BlockBuf, CurRead, BodySize - CurRead);
        }
        this._$290 = new String(BlockBuf, 0, BodySize);
      }
      if (ErrorSize > 0)
      {
        BlockBuf = new byte[ErrorSize];
        mRead.read(BlockBuf, 0, ErrorSize);
        this._$291 = new String(BlockBuf, 0, ErrorSize);
      }
      if (FileSize > 0)
      {
        if (!this.CryptFile)
        {
          int BlockSize = 1024 * this.BuffSize;
          int CurSize = 0;
          FileOutputStream mWite = new FileOutputStream(this._$289);
          


          BlockBuf = new byte[BlockSize];
          
          CurSize = BlockSize;
          while (FileSize > 0)
          {
            if (FileSize < BlockSize)
            {
              CurSize = FileSize;
              BlockBuf = new byte[CurSize];
            }
            CurRead = 0;
            while (CurRead < CurSize) {
              CurRead += mRead.read(BlockBuf, CurRead, CurSize - CurRead);
            }
            mWite.write(BlockBuf, 0, CurSize);
            FileSize -= CurSize;
          }
          mWite.close();
          mRead.close();
        }
        this._$293 = ((int)this._$289.length());
      }
      return true;
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return false;
  }
  
  public void MsgVariant(byte[] mStream)
  {
    _$413(mStream);
  }
  
  public boolean SavePackage(String FileName, ServletRequest request)
  {
    byte[] BlockBuf = new byte[1024 * this.BuffSize];
    try
    {
      int mLength = request.getContentLength();
      int mIndex = 0;
      ServletInputStream mRead = request.getInputStream();
      FileOutputStream mFile = new FileOutputStream(FileName);
      while (mLength > 0)
      {
        mIndex = mRead.read(BlockBuf);
        mFile.write(BlockBuf, 0, mIndex);
        mLength -= mIndex;
      }
      mFile.close();
      return true;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public boolean MsgFileSave(String FileName)
  {
    long FileSize = 0L;
    
    int BlockSize = 1024 * this.BuffSize;
    FileSize = this._$289.length();
    try
    {
      FileOutputStream mFile = new FileOutputStream(FileName);
      FileInputStream mThis = new FileInputStream(this._$289);
      byte[] BlockBuf = new byte[BlockSize];
      while (FileSize > 0L)
      {
        int CurSize = mThis.read(BlockBuf, 0, BlockSize);
        
        mFile.write(BlockBuf, 0, CurSize);
        FileSize -= CurSize;
      }
      mThis.close();
      mFile.close();
      return true;
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return false;
  }
  
  public boolean MsgFileLoad(String FileName)
  {
    try
    {
      if (this._$294.matches(this._$289.getName())) {
        this._$289.delete();
      }
      this._$289 = new File(FileName);
      this._$293 = ((int)this._$289.length());
      






      return true;
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return false;
  }
  
  public int FileSizeByName(String FileName)
  {
    try
    {
      File mFile = new File(FileName);
      return (int)mFile.length();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return 0;
  }
  
  public String MsgTextBody()
  {
    return this._$290;
  }
  
  public byte[] MsgFileBody()
  {
    byte[] mBuffer = new byte[this._$293];
    try
    {
      FileInputStream mFile = new FileInputStream(this._$289);
      mFile.read(mBuffer, 0, this._$293);
      mFile.close();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    return mBuffer;
  }
  
  public String MsgError()
  {
    return this._$291;
  }
  
  public String MsgVersion()
  {
    return this._$292;
  }
  
  public void MsgTextBody(String Value)
  {
    this._$290 = Value;
  }
  
  public void MsgFileBody(byte[] Value)
  {
    MsgFileClear();
    try
    {
      FileOutputStream mFile = new FileOutputStream(this._$289);
      mFile.write(Value);
      mFile.close();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(this._$291);
    }
    this._$293 = Value.length;
  }
  
  public void MsgError(String Value)
  {
    this._$291 = Value;
  }
  
  public int MsgFileSize()
  {
    return this._$293;
  }
  
  public void MsgFileSize(int value)
  {
    this._$293 = value;
  }
  
  public void MsgFileClear()
  {
    this._$293 = 0;
    if (this._$294.matches(this._$289.getName())) {
      this._$289.delete();
    }
    try
    {
      this._$289 = File.createTempFile("~GG", ".tmp");
      this._$294 = this._$289.getName();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
  }
  
  public void MsgTextClear()
  {
    this._$290 = "";
  }
  
  public void MsgErrorClear()
  {
    this._$291 = "";
  }
  
  public String DecodeBase64(String Value)
  {
    ByteArrayOutputStream o = new ByteArrayOutputStream();
    String m = "";
    

    byte[] d = new byte[4];
    try
    {
      int count = 0;
      byte[] x = Value.getBytes();
      while (count < x.length)
      {
        for (int n = 0; n <= 3; n++)
        {
          if (count >= x.length)
          {
            d[n] = 64;
          }
          else
          {
            int y = this._$288.indexOf(x[count]);
            if (y < 0) {
              y = 65;
            }
            d[n] = ((byte)y);
          }
          count++;
        }
        o.write((byte)(((d[0] & 0x3F) << 2) + ((d[1] & 0x30) >> 4)));
        if (d[2] != 64)
        {
          o.write((byte)(((d[1] & 0xF) << 4) + ((d[2] & 0x3C) >> 2)));
          if (d[3] != 64) {
            o.write((byte)(((d[2] & 0x3) << 6) + (d[3] & 0x3F)));
          }
        }
      }
    }
    catch (StringIndexOutOfBoundsException e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    try
    {
      m = o.toString(this.Charset);
    }
    catch (UnsupportedEncodingException ea)
    {
      System.out.println(ea.toString());
    }
    return m;
  }
  
  public String EncodeBase64(String Value)
  {
    ByteArrayOutputStream o = new ByteArrayOutputStream();
    


    byte[] d = new byte[4];
    try
    {
      int count = 0;
      
      byte[] x = Value.getBytes(this.Charset);
      while (count < x.length)
      {
        byte c = x[count];
        count++;
        d[0] = ((byte)((c & 0xFC) >> 2));
        d[1] = ((byte)((c & 0x3) << 4));
        if (count < x.length)
        {
          c = x[count];
          count++;
          d[1] = ((byte)(d[1] + (byte)((c & 0xF0) >> 4)));
          d[2] = ((byte)((c & 0xF) << 2));
          if (count < x.length)
          {
            c = x[count];
            count++;
            d[2] = ((byte)(d[2] + ((c & 0xC0) >> 6)));
            d[3] = ((byte)(c & 0x3F));
          }
          else
          {
            d[3] = 64;
          }
        }
        else
        {
          d[2] = 64;
          d[3] = 64;
        }
        for (int n = 0; n <= 3; n++) {
          o.write(this._$288.charAt(d[n]));
        }
      }
    }
    catch (StringIndexOutOfBoundsException e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
    catch (UnsupportedEncodingException ea)
    {
      System.out.println(ea.toString());
    }
    return o.toString();
  }
  
  public int GetFieldCount()
  {
    int i = 0;
    int j = 0;
    i = this._$290.indexOf("\r\n", i + 1);
    while (i != -1)
    {
      j++;
      i = this._$290.indexOf("\r\n", i + 1);
    }
    return j;
  }
  
  public String GetFieldName(int Index)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int n = 0;
    String mFieldString = "";
    String mFieldName = "";
    String mReturn = "";
    while ((i != -1) && (j < Index))
    {
      i = this._$290.indexOf("\r\n", i + 1);
      if (i != -1) {
        j++;
      }
    }
    k = this._$290.indexOf("\r\n", i + 1);
    if ((i != -1) && (k != -1))
    {
      if (i == 0) {
        mFieldString = this._$290.substring(i, k);
      } else {
        mFieldString = this._$290.substring(i + 2, k);
      }
      n = mFieldString.indexOf("=", 0);
      if (n != -1)
      {
        mFieldName = mFieldString.substring(0, n);
        mReturn = mFieldName;
      }
    }
    return mReturn;
  }
  
  public String GetFieldValue(int Index)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int n = 0;
    String mFieldString = "";
    String mFieldValue = "";
    String mReturn = "";
    while ((i != -1) && (j < Index))
    {
      i = this._$290.indexOf("\r\n", i + 1);
      if (i != -1) {
        j++;
      }
    }
    k = this._$290.indexOf("\r\n", i + 1);
    if ((i != -1) && (k != -1))
    {
      if (i == 0) {
        mFieldString = this._$290.substring(i, k);
      } else {
        mFieldString = this._$290.substring(i + 2, k);
      }
      n = mFieldString.indexOf("=", 0);
      if (n != -1)
      {
        mFieldValue = mFieldString.substring(n + 1, mFieldString.length());
        
        mReturn = DecodeBase64(mFieldValue);
      }
    }
    return mReturn;
  }
  
  public String GetFieldText()
  {
    return this._$290.toString();
  }
  
  public String GetMsgByName(String FieldName)
  {
    int i = 0;
    int j = 0;
    String mReturn = "";
    
    String mFieldName = FieldName.trim().concat("=");
    








    i = this._$290.indexOf(mFieldName);
    if (i != -1)
    {
      j = this._$290.indexOf("\r\n", i + 1);
      i += mFieldName.length();
      if (j != -1)
      {
        String mFieldValue = this._$290.substring(i, j);
        mReturn = DecodeBase64(mFieldValue);
        return mReturn;
      }
      return mReturn;
    }
    return mReturn;
  }
  
  public void SetMsgByName(String FieldName, String FieldValue)
  {
    String mFieldText = "";
    String mFieldHead = "";
    String mFieldNill = "";
    
    int i = 0;
    int j = 0;
    boolean f = false;
    
    String mFieldName = FieldName.trim().concat("=");
    String mFieldValue = EncodeBase64(FieldValue);
    
    mFieldText = mFieldName + mFieldValue + "\r\n";
    
    i = this._$290.indexOf(mFieldName);
    if (i != -1)
    {
      j = this._$290.indexOf("\r\n", i + 1);
      if (j != -1)
      {
        mFieldHead = this._$290.substring(0, i);
        mFieldNill = this._$290.substring(j + 2);
        f = true;
      }
    }
    if (f) {
      this._$290 = new StringBuffer().append(mFieldHead).append(mFieldText).append(mFieldNill).toString();
    } else {
      this._$290 = this._$290.concat(mFieldText);
    }
  }
  
  public boolean MakeDirectory(String FilePath)
  {
    File mFile = new File(FilePath);
    mFile.mkdirs();
    return mFile.isDirectory();
  }
  
  public boolean MKDirectory(String FilePath)
  {
    File mFile = new File(FilePath);
    mFile.mkdirs();
    return mFile.isDirectory();
  }
  
  public boolean RMDirectory(String FilePath)
  {
    File mFile = new File(FilePath);
    if (mFile.isDirectory()) {
      mFile.delete();
    }
    return true;
  }
  
  public boolean DelFile(String FileName)
  {
    File mFile = new File(FileName);
    if (mFile.exists()) {
      mFile.delete();
    }
    return true;
  }
  
  public boolean DelTree(String FilePath)
  {
    File mFile = new File(FilePath);
    if (mFile.isDirectory()) {
      mFile.delete();
    }
    return true;
  }
  
  public int LoadFilePoint(String FileName)
  {
    int i = 0;
    int j = 0;
    int mSize = 0;
    
    String mText = "";
    String mReturn = "-1";
    String mFieldName = "INDEX=";
    try
    {
      File mFile = new File(FileName + ".fp");
      mSize = (int)mFile.length();
      byte[] mBuffer = new byte[mSize];
      FileInputStream mStream = new FileInputStream(mFile);
      mStream.read(mBuffer, 0, mSize);
      mStream.close();
      mText = new String(mBuffer);
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      
      return Integer.parseInt(mReturn);
    }
    i = mText.indexOf(mFieldName);
    if (i != -1)
    {
      j = mText.indexOf("\r\n", i + 1);
      i += mFieldName.length();
      if (j != -1)
      {
        mReturn = mText.substring(i, j - i);
        
        return Integer.parseInt(mReturn);
      }
      return Integer.parseInt(mReturn);
    }
    return Integer.parseInt(mReturn);
  }
  
  public boolean SaveFilePoint(String FileName, int FCount)
  {
    int i = 0;
    int j = 0;
    int mSize = 0;
    
    String mFieldName = "INDEX=";
    String mCount = "";
    try
    {
      FileOutputStream mFile = new FileOutputStream(FileName);
      mCount = mFieldName + FCount + "\r\n";
      byte[] mBuffer = mCount.getBytes();
      mSize = mBuffer.length;
      mFile.write(mBuffer, 0, mSize);
      mFile.close();
      return true;
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println("SaveFilePoint:" + this._$291);
    }
    return false;
  }
  
  public boolean LoadStreamFromFile(String FileName, int Index)
  {
    int mPosition = 0;
    int mSize = 0;
    int mLength = 0;
    try
    {
      File mLocal = new File(FileName);
      mSize = (int)mLocal.length();
      
      FileInputStream mFile = new FileInputStream(mLocal);
      
      FileOutputStream mThis = new FileOutputStream(this._$289);
      
      mPosition = Index * this.BuffSize * 1024;
      if (mPosition + this.BuffSize * 1024 < mSize) {
        mLength = this.BuffSize * 1024;
      } else {
        mLength = mSize - mPosition;
      }
      mFile.skip(mPosition);
      
      byte[] mBuffer = new byte[mLength];
      mFile.read(mBuffer);
      mFile.close();
      mThis.write(mBuffer);
      mThis.close();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println("LoadStreamFromFile:" + this._$291);
      return false;
    }
    return true;
  }
  
  public boolean SaveStreamToFile(String FileName, int Index)
  {
    if (Index == 0) {
      DelFile(FileName);
    }
    try
    {
      RandomAccessFile mFile = new RandomAccessFile(FileName, "rw");
      FileInputStream mThis = new FileInputStream(this._$289);
      byte[] mBuffer = new byte[this._$293];
      mThis.read(mBuffer);
      mThis.close();
      mFile.seek(mFile.length());
      mFile.write(mBuffer);
      mFile.close();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println("SaveStreamToFile:" + this._$291);
      return false;
    }
    return true;
  }
  
  public boolean SaveFromStream(String FileName, int Index)
  {
    if (Index == 0) {
      DelFile(FileName);
    }
    try
    {
      RandomAccessFile mFile = new RandomAccessFile(FileName, "rw");
      FileInputStream mThis = new FileInputStream(this._$289);
      byte[] mBuffer = new byte[this._$293];
      mThis.read(mBuffer);
      mThis.close();
      mFile.seek(mFile.length());
      mFile.write(mBuffer);
      mFile.close();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println("SaveFromStream:" + this._$291);
      return false;
    }
    return true;
  }
  
  public boolean DecodeBase64ToFile(String Value, String FileName)
  {
    ByteArrayOutputStream o = new ByteArrayOutputStream();
    
    boolean mResult = false;
    
    byte[] d = new byte[4];
    try
    {
      int count = 0;
      byte[] x = Value.getBytes();
      while (count < x.length)
      {
        for (int n = 0; n <= 3; n++)
        {
          if (count >= x.length)
          {
            d[n] = 64;
          }
          else
          {
            int y = this._$288.indexOf(x[count]);
            if (y < 0) {
              y = 65;
            }
            d[n] = ((byte)y);
          }
          count++;
        }
        o.write((byte)(((d[0] & 0x3F) << 2) + ((d[1] & 0x30) >> 4)));
        if (d[2] != 64)
        {
          o.write((byte)(((d[1] & 0xF) << 4) + ((d[2] & 0x3C) >> 2)));
          if (d[3] != 64) {
            o.write((byte)(((d[2] & 0x3) << 6) + (d[3] & 0x3F)));
          }
        }
      }
      FileOutputStream mFile = new FileOutputStream(FileName);
      byte[] mBuffer = o.toByteArray();
      int mSize = mBuffer.length;
      mFile.write(mBuffer, 0, mSize);
      mFile.close();
      mResult = true;
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      mResult = false;
      System.out.println(e.toString());
    }
    return mResult;
  }
  
  public boolean SaveFromFile(String FileName, int FileCount)
  {
    int mIndex = 0;
    
    String mPkName = "";
    


    mPkName = FileName + ".fp";
    DelFile(mPkName);
    try
    {
      FileOutputStream mFile = new FileOutputStream(FileName);
      for (mIndex = 0; mIndex <= FileCount; mIndex++)
      {
        mPkName = FileName + "." + mIndex;
        File nTemp = new File(mPkName);
        FileInputStream mTemp = new FileInputStream(nTemp);
        byte[] mBuffer = new byte[(int)nTemp.length()];
        mTemp.read(mBuffer, 0, (int)nTemp.length());
        mFile.write(mBuffer, 0, (int)nTemp.length());
        mTemp.close();
        nTemp.delete();
      }
      mFile.close();
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println("SaveFromFile:" + this._$291);
      return false;
    }
    return true;
  }
  
  public void Load(HttpServletRequest request)
  {
    this.Charset = request.getHeader("charset");
    if (this.Charset == null) {
      this.Charset = "GB2312";
    }
    if (!_$413(request)) {
      System.out.println("StreamToMsg Error");
    }
  }
  
  public void Send(HttpServletResponse response)
  {
    if (!_$378(response)) {
      System.out.println("MsgToStream Error");
    }
    try
    {
      if (this._$294.matches(this._$289.getName())) {
        this._$289.delete();
      }
    }
    catch (Exception e)
    {
      this._$291 += e.toString();
      System.out.println(e.toString());
    }
  }
  
  public byte[] ReadPackage(HttpServletRequest request)
  {
    byte[] mStream = null;
    int totalRead = 0;
    int readBytes = 0;
    int totalBytes = 0;
    
    this.Charset = request.getHeader("charset");
    if (this.Charset == null) {
      this.Charset = "GB2312";
    }
    try
    {
      totalBytes = request.getContentLength();
      mStream = new byte[totalBytes];
      while (totalRead < totalBytes)
      {
        readBytes = request.getInputStream().read(mStream, totalRead, totalBytes - totalRead);
        
        totalRead += readBytes;
      }
      MsgVariant(mStream);
    }
    catch (Exception e)
    {
      System.out.println("ReadPackage:" + e.toString());
    }
    return mStream;
  }
  
  public void SendPackage(HttpServletResponse response)
  {
    try
    {
      ServletOutputStream OutBinarry = response.getOutputStream();
      OutBinarry.write(MsgVariant());
      OutBinarry.flush();
      OutBinarry.close();
    }
    catch (Exception e)
    {
      System.out.println("SendPackage:" + e.toString());
    }
  }
  
  public static String Version()
  {
    return "8,1,0,12";
  }
  
  public static String Version(String SoftwareName)
  {
    String mVersion = "0,0,0,0";
    if ((SoftwareName.equalsIgnoreCase("HandWrite")) || (SoftwareName.equalsIgnoreCase(""))) {
      mVersion = "4,0,0,8";
    }
    if (SoftwareName.equalsIgnoreCase("iWebSignature")) {
      mVersion = "5,8,0,0";
    }
    return mVersion;
  }
  
  public static String VersionEx()
  {
    return "高级版本";
  }
  
  public static String VersionEx(String SoftwareName)
  {
    String mVersionEx = "错误版本";
    if ((SoftwareName.equalsIgnoreCase("HandWrite")) || (SoftwareName.equalsIgnoreCase(""))) {
      mVersionEx = "高级版本";
    }
    if (SoftwareName.equalsIgnoreCase("iWebSignature")) {
      mVersionEx = "标准版本";
    }
    return mVersionEx;
  }
  
  public static String CopyRight(String SoftwareName)
  {
    String mVersionEx = "错误版本";
    if ((SoftwareName.equalsIgnoreCase("HandWrite")) || (SoftwareName.equalsIgnoreCase(""))) {
      mVersionEx = "高级版本 江西金格科技股份有限公司，所权所有";
    }
    if (SoftwareName.equalsIgnoreCase("iWebSignature")) {
      mVersionEx = "标准版本 江西金格科技股份有限公司，所权所有";
    }
    return mVersionEx;
  }
  
  public static String VersionDesc()
  {
    String mVersionDesc = "www.kinggrid.com KingGrid-iMsgServer2000";
    return mVersionDesc;
  }
  
  public static String ProdDesc()
  {
    String mProdDescX = " 金格科技自主创新开发的实时安全传输技术——DBPacketTM通讯协议包，采用前端和后端方式设计，实现客户端与服务器端的数据安全可靠传递、交互。金格科技是专注于“可信应用软件”研究与开发的自主创新型高新技术企业，凭借完全自主创新的核心技术和强大的研发力量，为社会提供安全可靠的可信应用软件产品、技术和服务，日益成长为中国可信应用产业的积极推动者和可信应用软件领域的领军企业。";
    String mProdDesc = "Kinggrid:iWebOffice2000/iWebOffice2003/iWebOffice2003/iWebOffice2009/iWebOffice2012/";
    mProdDesc = mProdDesc + "iWebRevision/iWebPDF/iWebPicture/iWebFile2005/iWebBarcode/";
    mProdDesc = mProdDesc + "iSignature/iSolutions";
    return mProdDesc;
  }
}
