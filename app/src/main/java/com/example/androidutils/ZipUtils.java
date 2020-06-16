package com.example.androidutils;

import com.example.androidutils.customLog.LogManager;
import com.example.androidutils.customLog.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.example.androidutils.MainActivity.MODULE_NAME;

/**
 * Created by Bingo on 2018/6/6.
 */
public class ZipUtils {
    private static final String TAG = ZipUtils.class.getSimpleName();
    private static Logger LOGGER = LogManager.getLogger(MODULE_NAME);
    private static final int BUFF_SIZE = 1024 * 1024;

    static String pattern2 = "(.*):(.*):(.*)";
    static String pattern3 = "(.*)/(.*)/(.*)";


    /**
     * 解压zip到指定的路径
     * @param zipFileString  ZIP的名称
     * @param outPathString   要解压缩路径
     * @throws Exception
     */
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {

        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                LOGGER.e(TAG,outPathString + File.separator + szName);
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()){
                    LOGGER.e(TAG, "Create the file:" + outPathString + File.separator + szName);
                    file.getParentFile().mkdirs(); file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }


    public static void UnZipFolder(String zipFileString, String outPathString, String szName) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            //szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                LOGGER.e(TAG,outPathString + File.separator + szName);
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()){
                    LOGGER.e(TAG, "Create the file:" + outPathString + File.separator + szName);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                } out.close();
            }
        }
        inZip.close();
    }

    /**
     * 压缩文件和文件夹
     * @param srcFileString   要压缩的文件或文件夹
     * @param zipFileString   解压完成的Zip路径
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        //创建文件
        File file = new File(srcFileString);
        //压缩
        LOGGER.i(TAG,"getParent =" + file.getParent() + " separator=" + File.separator);

        if(outZip == null){
            LOGGER.e(" outZip is error");
        }else {
            String fileList[] = file.list();
            for (int i = 0; i < fileList.length; i++) {
                LOGGER.i(TAG,"filename =" + srcFileString + File.separator +fileList[i]);
                File subFile = new File(srcFileString + File.separator +fileList[i]);
                if (subFile.isFile()){
                    ZipEntry zipEntry = new ZipEntry(fileList[i]);
                    FileInputStream inputStream = new FileInputStream(subFile);
                    outZip.putNextEntry(zipEntry);
                    int len;
                    byte[] buffer = new byte[4096];
                    while((len=inputStream.read(buffer)) != -1) {
                        outZip.write(buffer, 0, len);
                    }
                    outZip.closeEntry();
                }else{
                    ZipFiles(subFile.getParent() + subFile.separator, subFile.getName(), outZip);
                }
            }
            //完成和关闭
            outZip.finish();
            outZip.close();
        }
    }

    /**
     * 压缩文件
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        if(zipOutputSteam == null) return;

        File file = new File(folderString+fileString);

        LOGGER.i(TAG,"folderString= " + folderString + "fileString= " + fileString);

        if (file.isFile()) {
            LOGGER.i(TAG,"zip file");
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while((len=inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String fileList[] = file.list();

            LOGGER.i(TAG,"fileList[0]= " + fileList[0]);
            //没有子文件和压缩cat
            if (fileList.length <= 0 ) {
                LOGGER.e(TAG,"fileList length is error");
                ZipEntry zipEntry = new ZipEntry(fileString+ File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString, fileString+ File.separator+fileList[i], zipOutputSteam);
            }
        }

        LOGGER.i("leave");
    }

    /**
     * 返回zip的文件输入流
     * @param zipFileString  zip的名称
     * @param fileString     ZIP的文件名
     * @return InputStream
     * @throws Exception
     */
    public static InputStream UpZip(String zipFileString, String fileString) throws Exception {
        ZipFile zipFile = new ZipFile(zipFileString);
        ZipEntry zipEntry = zipFile.getEntry(fileString);
        return zipFile.getInputStream(zipEntry);
    }


    /**
     * 返回ZIP中的文件列表（文件和文件夹）
     * @param zipFileString     ZIP的名称
     * @param bContainFolder    是否包含文件夹
     * @param bContainFile      是否包含文件
     * @return
     * @throws Exception
     */

    public static List<File> GetFileList(String zipFileString, boolean bContainFolder, boolean bContainFile) throws Exception {
        List<File> fileList = new ArrayList<File>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry; String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // 获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(szName);
                if (bContainFolder) {
                    fileList.add(folder);
                }
            } else {
                File file = new File(szName);
                if (bContainFile) {
                    fileList.add(file);
                }
            }
        }
        inZip.close();
        return fileList;
    }



    /**
     * 无需解压直接读取Zip文件和文件内容
     *
     * @param file
     * @throws Exception
     */
    public static void readZipFile(String file) throws Exception {
        ZipFile zf = new ZipFile(file);
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.isDirectory()) {
                //Do nothing
            } else {
                LOGGER.e(TAG, "file - " + ze.getName() + " : " + ze.getSize() + " bytes");
                if (ze.getName().equals("sbl1.mbn")) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(zf.getInputStream(ze)));
                    String line;
                    while ((line = br.readLine()) != null) {
                        LOGGER.e(TAG, "readZipFile line: " + line);
                        if (line.contains("OK")) {
                        }
                    }
                    br.close();
                }
            }
        }
        zin.closeEntry();
    }

    /**
     * 读取zip包中的某个条目
     *
     * @param zipPathName
     * @throws Exception
     */
    public static String getZipFileContent(String zipPathName, String entryName, String startStr) {
        ZipFile zipFile = null;
        String line = "";
        BufferedReader br = null;
        try {
            zipFile = new ZipFile(zipPathName);
            ZipEntry zipEntry = zipFile.getEntry(entryName);
            br = new BufferedReader(
                    new InputStreamReader(zipFile.getInputStream(zipEntry)));
            while ((line = br.readLine()) != null) {
                LOGGER.i(TAG, "readZipFile line: " + line);
                if (line.startsWith(startStr)) {
                    break;
                }
            }

        } catch (IOException e) {
            LOGGER.e(TAG, " getZipFileContent error: " + e.getMessage());
            e.printStackTrace();
        } finally {

            if(zipFile != null) {
                try {
                    zipFile.close();
                    LOGGER.i(TAG, " getZipFileContent zipFile close success...");
                } catch (IOException e) {
                    LOGGER.e(TAG, " getZipFileContent zipFile close error msg: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                    LOGGER.i(TAG, " getZipFileContent br close success...");
                } catch (IOException e) {
                    LOGGER.e(TAG, " getZipFileContent close error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return line;
    }


    /**
     * 解压到指定目录
     * @param zipPath 即将被解压的文件的绝对路径
     * @param descDir 文件被解压到哪个目录下，也就是父目录
     */
    public static boolean unZipFiles(String zipPath, String descDir) {
        return unZipFiles(new File(zipPath), descDir);
    }

    /**
     * 解压文件到指定目录
     */
    @SuppressWarnings("rawtypes")
    public static boolean unZipFiles(File zipFile, String descDir) {
        boolean success = true;
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            if(!pathFile.mkdirs()) {
                return false;
            }
        }
        //解决zip文件中有中文目录或者中文文件
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile, ZipFile.OPEN_READ);
        } catch (IOException e) {
            LOGGER.e(TAG ," new ZipFile error zipFile: " + zipFile + ", msg: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = zip.getInputStream(entry);
                String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
                ;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    if(!file.mkdirs()){
                        continue;
                    }
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //输出文件路径信息
                LOGGER.i(TAG, " outPath: " + outPath);
                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
            } catch (FileNotFoundException e) {
                success = false;
                LOGGER.e(TAG, " unZipFiles file not found error msg: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                success = false;
                LOGGER.e(TAG, " unZipFiles io error msg: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if(in != null) {
                        in.close();
                    }
                    if(out != null) {
                        out.close();
                    }
                    zip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     *
     * @param zipFile    即将要被解压的文件
     * @param folderPath 解压后的文件存放的位置
     * @return
     */
    public static boolean unZipFile(File zipFile, String folderPath) {
        boolean isSuccess = true;
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            isSuccess = desDir.mkdirs();
        }

        if (!isSuccess) {
            LOGGER.e(TAG, " unZipFile mkdirs error desDir: " + desDir.getName());
            return isSuccess;
        }

        ZipFile zf = null;
        InputStream in = null;

        try {
            zf = new ZipFile(zipFile);

            for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
                OutputStream out = null;
                try {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    in = zf.getInputStream(entry);
                    String str = folderPath + File.separator + entry.getName();
                    str = new String(str.getBytes("8859_1"), "GB2312");
                    File desFile = new File(str);
                    if (!desFile.exists()) {
                        File fileParentDir = desFile.getParentFile();
                        if (!fileParentDir.exists()) {
                            isSuccess = fileParentDir.mkdirs();

                            if (!isSuccess) {
                                LOGGER.e(TAG, " unZipFile mkdirs error fileParentDir: "
                                        + fileParentDir.getName());
                            }
                        }

                        isSuccess = desFile.createNewFile();
                        if (!isSuccess) {
                            LOGGER.e(TAG, " unZipFile createNewFile error desFile: " + desFile.getName());
                        }
                    }
                    out = new FileOutputStream(desFile);
                    byte buffer[] = new byte[BUFF_SIZE];
                    int realLength;
                    while ((realLength = in.read(buffer)) > 0) {
                        out.write(buffer, 0, realLength);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception exception) {
                    LOGGER.e(TAG, " unZipFile fail, exception: " + exception.getMessage());
                    exception.printStackTrace();

                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            LOGGER.e(TAG, " unZipFile ArrayIndexOutOfBoundsException, e: " + e.getMessage());
            e.printStackTrace();
            isSuccess = false;
        } catch (Exception e) {
            LOGGER.e(TAG, " unZipFile fail, e: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (zf != null) {
                    zf.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * 按存储方式压缩
     * @param zipFileName
     * @param inputFile
     * @throws Exception
     */
    public static void zipStored(String zipFileName, File inputFile) throws Exception {
        LOGGER.e(TAG,"enter zipStored");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zipStored(out, inputFile, "");
        LOGGER.e(TAG,"zipStored ok");
        out.close();
    }

    /**
     * 按存储方式压缩
     * @param out
     * @param f
     * @param base
     * @throws Exception
     */
    private static void zipStored(ZipOutputStream out, File f, String base) throws Exception {
        out.setMethod(ZipOutputStream.STORED);
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                if (fl[i].getName().indexOf(".zip") == -1) {
                    zipStored(out, fl[i], base + fl[i].getName());
                }
            }
        } else {
            ZipEntry entry = new ZipEntry(base);
            entry.setMethod(ZipEntry.STORED);
            entry.setSize(f.length());
            long crc = 0;
            crc = calFileCRC32(f);
            entry.setCrc(crc);
            out.putNextEntry(entry);
            FileInputStream in = new FileInputStream(f);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            out.closeEntry();
            in.close();
        }
    }

    public static long calFileCRC32(File file) throws IOException {
        FileInputStream fi = new FileInputStream(file);
        CheckedInputStream checksum = new CheckedInputStream(fi, new CRC32());
        while (checksum.read() != -1) { }
        long temp = checksum.getChecksum().getValue();
        fi.close();
        checksum.close();
        return temp;
    }

}

