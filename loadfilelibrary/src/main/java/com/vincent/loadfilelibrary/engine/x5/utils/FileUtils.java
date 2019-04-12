package com.vincent.loadfilelibrary.engine.x5.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件操作工具类
 */
public class FileUtils {

    public static final String ROOT = Environment.getExternalStorageDirectory().getPath() + "/nicehair/";
    public static final String CAMERA = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
    public static final String CACHE_IMG = "/cache/images/";

    /**
     * 十六进制值对应文件类型
     */
    private static final Map<String, String> HEX_FILE_TYPES = new HashMap<>();
    /**
     * 未知类型
     */
    public static final String FILE_TYPE_UNKNOW = "file_type_unknow";

    /**
     * 应用日志目录文件
     */
    public static String APP_LOG_PATH = ROOT + "log/";

    /**
     * 日志文件路径
     */
    public static String LOGFILE = APP_LOG_PATH + "log.txt";

    static int FileHashDefaultChunkSizeForReadingData = 1024;

    static {
        HEX_FILE_TYPES.put("FFD8FF", "jpg");
        HEX_FILE_TYPES.put("89504E47", "png");
        HEX_FILE_TYPES.put("89504E", "png");
        HEX_FILE_TYPES.put("474946", "gif");
        HEX_FILE_TYPES.put("47494638", "gif");
        HEX_FILE_TYPES.put("49492A00", "tif");
        HEX_FILE_TYPES.put("424D", "bmp");

        HEX_FILE_TYPES.put("41433130", "dwg"); //CAD
        HEX_FILE_TYPES.put("38425053", "psd");
        HEX_FILE_TYPES.put("7B5C727466", "rtf"); //日记本
        HEX_FILE_TYPES.put("3C3F786D6C", "xml");
        HEX_FILE_TYPES.put("68746D6C3E", "html");
        HEX_FILE_TYPES.put("44656C69766572792D646174653A", "eml"); //邮件
        HEX_FILE_TYPES.put("D0CF11E0", "doc");
        HEX_FILE_TYPES.put("5374616E64617264204A", "mdb");
        HEX_FILE_TYPES.put("252150532D41646F6265", "ps");
        HEX_FILE_TYPES.put("255044462D312E", "pdf");
        HEX_FILE_TYPES.put("504B0304", "zip");
        HEX_FILE_TYPES.put("52617221", "rar");
        HEX_FILE_TYPES.put("57415645", "wav");
        HEX_FILE_TYPES.put("41564920", "avi");
        HEX_FILE_TYPES.put("2E524D46", "rm");
        HEX_FILE_TYPES.put("000001BA", "mpg");
        HEX_FILE_TYPES.put("000001B3", "mpg");
        HEX_FILE_TYPES.put("6D6F6F76", "mov");
        HEX_FILE_TYPES.put("3026B2758E66CF11", "asf");
        HEX_FILE_TYPES.put("4D546864", "mid");
        HEX_FILE_TYPES.put("1F8B08", "gz");
    }

    /**
     * 获取文件的md5
     *
     * @param toCalculateFile 文件
     * @return
     * @throws FileNotFoundException
     */
    public static String getMD5ByFilePath(File toCalculateFile) {

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e("FileUtils", "Exception while getting digest", e);
            return null;
        }
        if (!toCalculateFile.exists()) {
            return "";
        }
        InputStream is = null;
        try {
            if (toCalculateFile != null) {
                is = new FileInputStream(toCalculateFile);
            } else {
                return "";
            }
        } catch (FileNotFoundException e) {
            Log.e("FileUtils", "Exception while getting FileInputStream", e);
            return null;
        }

        boolean hasMoreData = true;
        int chunkSizeForReadingData = FileHashDefaultChunkSizeForReadingData;

        long bigFileLength = 1024 * 1024 * 32;
        long filelength = toCalculateFile.length();
        if (filelength > bigFileLength) {
            // 计算大文件md5采用跳跃计算
            byte[] buffer = new byte[chunkSizeForReadingData];
            int readBytesCount;
            try {
                while (hasMoreData) {

                    readBytesCount = is.read(buffer);
                    if (readBytesCount == 0)
                        break;
                    if (readBytesCount == -1) {
                        hasMoreData = false;
                        continue;
                    }
                    digest.update(buffer, 0, readBytesCount);

                    byte[] bufferSkip = new byte[chunkSizeForReadingData * 31];
                    readBytesCount = is.read(bufferSkip);
                    if (readBytesCount == 0)
                        break;
                    if (readBytesCount == -1) {
                        hasMoreData = false;
                        continue;
                    }
                }

                byte[] md5sum = digest.digest();
                BigInteger bigInt = new BigInteger(1, md5sum);
                String output = bigInt.toString(16);
                // Fill to 32 chars
                output = String.format("%32s", output).replace(' ', '0');
                return output;
            } catch (IOException e) {
                throw new RuntimeException("Unable to process file for MD5", e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e("FileUtils", "Exception on closing MD5 input stream", e);
                }
            }

        } else {
            // 小文件
            byte[] buffer = new byte[chunkSizeForReadingData];
            int read;
            try {
                while ((read = is.read(buffer)) > 0) {
                    digest.update(buffer, 0, read);
                }
                byte[] md5sum = digest.digest();
                BigInteger bigInt = new BigInteger(1, md5sum);
                String output = bigInt.toString(16);
                // Fill to 32 chars
                output = String.format("%32s", output).replace(' ', '0');
                return output;
            } catch (IOException e) {
                throw new RuntimeException("Unable to process file for MD5", e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e("FileUtils", "Exception on closing MD5 input stream", e);
                }
            }
        }

    }


    /**
     * 创建文件
     *
     * @param filePath 文件路径
     */
    public static void createFileAtPath(String filePath) {
        File dir = new File(filePath);
        if (!dir.exists()) {
            try {
                //在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹路径
     */
    public static void createFolderAtPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                //按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return
     */
    public static boolean isFileExistAtPath(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取文件大小
     *
     * @param path 文件路径
     * @return
     */
    public static long fileSizeOfPath(String path) {
        File file = new File(path);
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
                Log.e("获取文件大小", "文件不存在!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 返回格式化后的文件大小
     *
     * @param path 文件路径
     * @return
     */
    public static String stringFileSizeOfPath(String path) {
        long size = fileSizeOfPath(path);
        return convertFileSize(size);
    }

    /**
     * 换算文件大小
     *
     * @param size 文件总字节数
     * @return 格式化后的文件大小
     */
    public static String convertFileSize(long size) {
        if (size <= 0)
            return "0.0B";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) size / 1024;
        if (temp >= 1024) {
            return df.format(temp / 1024) + "MB";
        } else {
            return df.format(temp) + "KB";
        }
    }

    /**
     * 读取输入流数据
     *
     * @param inStream 输入流
     * @return
     */
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 判断SD是否可以
     *
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 创建根目录
     *
     * @param path 目录路径
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    public static File getRootDir(Context context) {
        if (isSdcardExist()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return context.getDir("ExternalStorage", Context.MODE_PRIVATE);
        }
    }

    /**
     * 判断是是否为文件夹
     *
     * @param filePath
     * @return
     */
    public static boolean isDir(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isDirectory();
    }

    /**
     * 查找文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getExspansion(String fileName) {
        if (TextUtils.isEmpty(fileName))
            return "";
        int index = fileName.lastIndexOf(".");
        if (-1 == index || index == (fileName.length() - 1))
            return "";
        return fileName.substring(index);
    }

    /**
     * 读取assets目录下的内容
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssetsDirContent(final Context context, final String fileName) {
        InputStream inputStream = null;
        String content = "";
        try {
            inputStream = context.getAssets().open(fileName);
            int size = inputStream.available();
            int len = -1;
            byte[] bytes = new byte[size];
            inputStream.read(bytes);
            inputStream.close();
            content = new String(bytes, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }

    /**
     * 复制asset文件到指定目录
     *
     * @param oldPath asset下的路径
     * @param newPath SD卡下保存路径
     */
    public static void copyAssets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压assets的zip压缩文件到指定目录
     *
     * @param context         上下文对象
     * @param assetName       压缩文件名
     * @param outputDirectory 输出目录
     * @param isReWrite       是否覆盖
     * @throws IOException
     */
    public static void unZip(Context context, String assetName, String outputDirectory, boolean isReWrite) throws IOException {
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 打开压缩文件
        InputStream inputStream = context.getAssets().open(assetName);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    /**
     * 文件是否是图片
     */
    public static boolean fileIsImage(String filePath) {
        String fileType = obtainFileTypeByHead(filePath);
        return "jpg".equals(fileType)
                || "png".equals(fileType)
                || "gif".equals(fileType)
                || "tif".equals(fileType)
                || "bmp".equals(fileType);
    }

    /**
     * 根据文件头获取文件类型(暂只支持识别图片类型)<br/>
     * ps:缺少文件头的文件无法识别，如：txt
     */
    public static String obtainFileTypeByHead(String filePath) {
        String type = HEX_FILE_TYPES.get(getFileHeader(filePath, 0, 3));
        if (TextUtils.isEmpty(type)) type = FILE_TYPE_UNKNOW;
        return type;
    }

    /**
     * 获取文件头信息
     */
    public static String getFileHeader(String filePath, int skip, int bufferLength) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            long skipResult = is.skip(skip);
            byte[] b = new byte[bufferLength];
            int isRead = is.read(b, 0, b.length);
            if (isRead > 0) value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    /**
     * 将byte字节转换为十六进制字符串
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte b : src) {
            hv = Integer.toHexString(b & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }


    /**
     * //删除文件夹和文件夹里面的文件
     * @param dir
     */
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

}
