package cn.edu.buct.se.cs1808.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileEntity {
    /**
     * 参数名称
     */
    public String mName;
    /**
     * 上传的文件名
     */
    public String mFileName;
    /**
     * 需要上传的文件
     */
    public File mFile;
    /**
     * 文件的 mime，需要根据文档查询<br/>
     * 默认使用 application/octet-stream  任意的二进制数据
     */
    public String mMime = "application/octet-stream";


    public FileEntity(String mName, String mFileName, File mFile, String mMime) {
        this.mName = mName;
        this.mFileName = mFileName;
        this.mFile = mFile;
        this.mMime = mMime;
    }

    public byte[] getFileBytes() {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(mFile);
            outputStream = new ByteArrayOutputStream();
            int len;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
