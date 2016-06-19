
package com.petsay.utile;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.petsay.constants.Constants;
import com.petsay.vo.petalk.PhotoAlbumItem;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author wangw
 * 文件工具类
 */
public class FileUtile {

    /**
     * 检查SD卡状态是否可用
     * @return
     */
    public static boolean checkSDCard(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        return false;
    }

    /**
     * 获取文件路径
     * @param context
     * @param dir
     * @return
     */
    public static String getPath(Context context,String dir){
        String path = "";
        if(checkSDCard()){
            path = Environment.getExternalStorageDirectory().getAbsoluteFile()+File.separator+dir+File.separator;
        }else{
            path = context.getCacheDir().getAbsolutePath()+File.separator+dir;
        }
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }

    /**
     * 将图片保存到SD卡中(压缩质量默认50)
     * @param path
     * @param bmp
     * @return
     */
    public static boolean saveImage(String path,Bitmap bmp){
        return saveImage(path, bmp, 50);
    }

    /**
     * 将图片保存到SD卡中
     * @param path
     * @param bmp
     * @param quality
     * @return
     */
    public static boolean saveImage(String path,Bitmap bmp,int quality){
        OutputStream out = null;

        try {
            out = new FileOutputStream(path);
            //参数1：压缩格式
            //参数2：压缩质量：质量从1-100,1为最差，100为最好
            //参数3：输出流
            bmp.compress(getImageFormat(path), quality,out );
            return true;
        } catch (Exception e) {
            Log.e("sample", "[saveImage]Error:"+e.getMessage());
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 保存图片到系统相册
     * @param context
     * @param bmp
     * @return
     */
    public static String saveImageToSysAlbum(Context context,Bitmap bmp,String name){
        if(bmp == null || (bmp != null && bmp.isRecycled()))
            return "";
        String path = "";
        try {
            path = MediaStore.Images.Media.insertImage(context.getContentResolver(),bmp,name,"petsay");
        }catch (Exception e){
            path = getPath(context,"Camera")+name;
            boolean flag = saveImage(path,bmp,100);
            if(!flag)
                return "";
        }
        return path;
    }

    /**
     * 创建一个系统相册的相册文件
     * @return
     */
    public static File createSysAlbumFile(String fileName)
    {
        File mediaStorageDir = null;
        try
        {
            mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                    "petsay");
            if (!mediaStorageDir.exists())
            {
                if (!mediaStorageDir.mkdirs())
                {
                    return null;
                }
            }
            return new File(mediaStorageDir.getPath() + File.separator+fileName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取SD卡图片并按照指定压缩比例压缩图片
     * @param sampleRate 压缩比例，不压缩时输入1，此时按照最大的宽高值输出
     * @param maxW  图片最大宽度，屏幕的宽度即可
     * @param maxH  图片最大高度，屏幕的高度即可
     * @param path  图片在SD卡的绝对路径
     * @return
     */
    public static Bitmap loadImageBySdCard(int sampleRate,int maxW,int maxH,String path){
        return loadImageBySdCard(null,sampleRate,maxW,maxH,path);
    }

    /**
     * 读取SD卡图片并按照指定压缩比例压缩图片
     * @param config 图片解码方式(默认可以为空)： ARGB_4444      代表16位ARGB位图
    ARGB_8888     代表32位ARGB位图
    RGB_565         代表8位RGB位图
     * @param sampleRate 压缩比例，不压缩时输入1，此时按照最大的宽高值输出
     * @param maxW  图片最大宽度，屏幕的宽度即可
     * @param maxH  图片最大高度，屏幕的高度即可
     * @param path  图片在SD卡的绝对路径
     * @return
     */
    public static Bitmap loadImageBySdCard(Bitmap.Config config,int sampleRate,int maxW,int maxH,String path){
        File file = new File(path);
        if(!file.exists() || file.isDirectory())
            return null;
        //pre:先采集图片信息
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = config;
        BitmapFactory.decodeFile(path,options);
        //1.获取图片的宽高
        int w = options.outWidth;
        int h = options.outHeight;
        //2.判断是否读取到图片，如果为0则代表没有读取到图片
        if(w == 0 || h == 0)
            return null;
        //3.初始化压缩比例
        options.inSampleSize = sampleRate;
        if(w > maxW || h > maxH) {
            if (w > h) {
                options.inSampleSize = w/maxW;
            }else {
                options.inSampleSize = h/maxH;
            }
        }
        //4.开始真正读取图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
    }

    /**
     * 读取并压缩Assets文件夹中的图片
     * @param config 图片解码方式(默认可以为空)： ARGB_4444      代表16位ARGB位图
    ARGB_8888     代表32位ARGB位图
    RGB_565         代表8位RGB位图
     * @param sampleRate 压缩比例，不压缩时输入1，此时按照最大的宽高值输出
     * @param maxW  图片最大宽度，屏幕的宽度即可
     * @param maxH  图片最大高度，屏幕的高度即可
     * @param manager   AssetManager
     * @param FilePath  Assets中文件路径及名称
     * @return
     */
    public static Bitmap loadImageByAssets(Bitmap.Config config,int sampleRate,int maxW,int maxH,AssetManager manager,String FilePath){
        InputStream in = null;
        try {
            in = manager.open(FilePath);
            if(in == null)
                return null;
            //pre:先采集图片信息
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
//        BitmapFactory.decodeFile(path,options);
            BitmapFactory.decodeStream(in, null, options);
            //1.获取图片的宽高
            int w = options.outWidth;
            int h = options.outHeight;
            //2.判断是否读取到图片，如果为0则代表没有读取到图片
            if(w == 0 || h == 0)
                return null;
            //3.初始化压缩比例
            options.inSampleSize = 99;
            if(w > maxW || h > maxH) {
                if (w > h) {
                    options.inSampleSize = w/maxW;
                }else {
                    options.inSampleSize = h/maxH;
                }
            }
            //4.开始真正读取图片
            options.inJustDecodeBounds = false;
            in = manager.open(FilePath);
            return BitmapFactory.decodeStream(in, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 压缩Bitmap，并输出byte[]数组
     * @param bmp   要压缩的图片
     * @param maxSize 最大Size，单位：kb
     * @return 输出压缩后的Byte[]数组（一律输出jpg格式）
     */
    public static byte[] compressBitmapOutputByte(Bitmap bmp,int maxSize){
        if(bmp == null || maxSize <= 10){
            return null;
        }

        //压缩率，默认100是不压缩的
        int qu = 100;
        //1.将bitmap转换成ByteArrayOutputStream
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //2.将Bitma转换成BtyeArray
        bmp.compress(CompressFormat.JPEG,qu,stream);
        while (stream.toByteArray().length/1024 > maxSize){
            qu -= 10;
            stream.reset();
            bmp.compress(CompressFormat.JPEG,qu,stream);
        }
        byte[] data = stream.toByteArray();
        IOUtils.closeQuietly(stream);
        return data;
    }

    /**
     * 压缩Bitmap，并输出Bitmap
     * @param bmp   要压缩的图片
     * @param maxSize 最大Size，单位：kb
     * @return 输出压缩后的Byte[]数组（一律输出jpg格式）
     */
    public static Bitmap compressBitmap(Bitmap bmp,int maxSize){
        byte[] data = compressBitmapOutputByte(bmp, maxSize);
        if(data != null) {
            return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
        }
        return bmp;
    }

    @SuppressLint("NewApi")
    private static CompressFormat getImageFormat(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        String fromat = path.substring(path.lastIndexOf("."), path.length());
        fromat = fromat.toUpperCase();
        if("WEBP".equals(fromat))
            return CompressFormat.WEBP;
        else if("JPEG".equals(fromat) || "JPG".equals(fromat)){
            return CompressFormat.JPEG;
        }else{
            return CompressFormat.PNG;
        }
    }

    /**
     * 检查SD卡文件是否存在
     * @param path
     * @param fileName
     */
    public static boolean sdCardHasFile(String path,String fileName){
        File file = new File(path,fileName);
        return file.exists();
    }

    /**
     * 检查assets文件夹下文件是否存在
     * @param context
     * @param path
     * @param fileName
     * @return
     */
    public static boolean assetsHasFile(Context context,String path,String fileName){
        try {
            //			path = path.substring(0,path.lastIndexOf(File.separator));
            String[] files = context.getAssets().list(path);
            for (String str : files) {
                if(str.equals(fileName))
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 创建缩放BitMap
     * @param bmp
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createScaledBitmap(Bitmap bmp,int width,int height){
        if(bmp == null)
            return null;
        if(bmp.getWidth() == width && bmp.getHeight() == height){
            return bmp;
        }else {
            Bitmap temp = Bitmap.createScaledBitmap(bmp, width, height, true);
            bmp.recycle();
            bmp = null;
            return temp;
        }
    }

    /**
     * 从assets读取图片
     * @param fileName
     * @return
     */
    public static Bitmap getImageFromAssetsFile(Context context,String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            PublicMethod.log_d("从Assets读取图片失败");
        }
        return image;
    }

    /**
     * 根据URL获取文件名带扩展名
     * @param url
     * @return
     */
    public static String getFileNameByUrl(String url){
        if(TextUtils.isEmpty(url))
            return "";
        return url.substring(url.lastIndexOf("/")+1);
    }

    /**
     * 利用递归删除目录及目录下的所有文件夹
     * @param dir
     */
    public static void deleteDir(File dir){
        if(dir.isFile() && dir.exists()){
            dir.delete();
            return;
        }

        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            if(files == null || files.length == 0){
                dir.delete();
                return;
            }

            for (int i = 0; i < files.length; i++) {
                File f= files[i];
                deleteDir(f);
            }
            dir.delete();
        }
    }

    public static boolean deleteFile(String path){
        if(TextUtils.isEmpty(path))
            return false;
        File File = new File(path);
        if (File.exists()) {
            return File.delete();
        }
        return true;
    }

    /**
     * 获取指定文件夹的大小
     * @param file
     * @return 以M为单位
     * @throws Exception
     */
    public static float getFolderSize(File file){
        float size = 0.0f;
        if(!file.exists())
            return size;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++){
            File f = fileList[i];
            if(!f.exists())
                continue;
            if (f.isDirectory()){
                size = size + getFolderSize(f);
            } else {
                size = size + f.length();
            }
        }
        return size*100/1024/1024/100;
    }

    /**
     * (线程同步方式，增加线程锁)解压zip包
     * @param inputStream
     * @param target
     * @return
     */
    public synchronized static boolean unZip(InputStream inputStream,File target){

        //创建解压后的文件夹
        if(!target.exists())
            target.mkdirs();

        //初始化zipStream数据流
        ZipInputStream zipStream = new ZipInputStream(inputStream);
        //获取一个文件实体
        try {
            ZipEntry entry = zipStream.getNextEntry();
            //初始化缓存区对象
            byte[] buffer = new byte[512*1024];
            int readCount = 0;
            //遍历压缩包内的文件
            while (entry != null) {
                //判断是否为目录
                if(!entry.isDirectory()){
                    File temp = new File(target.getAbsolutePath(),FileUtile.getFileNameByUrl(entry.getName()));
                    if(!temp.exists()){
                        temp.createNewFile();
                        FileOutputStream outputStream = new FileOutputStream(temp);
                        while((readCount = zipStream.read(buffer)) > 0){
                            outputStream.write(buffer,0,readCount);
                        }
                        outputStream.flush();
                        outputStream.close();
                        outputStream = null;
                    }
                }
                entry = zipStream.getNextEntry();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            try {
                zipStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 获取解压后的文件路径
     * @param context
     * @param fileName
     * @return
     */
    public static String getUnzipFilePath(Context context,String fileName){
        return  FileUtile.getPath(context, Constants.SDCARD_DECORATE_UNZIP+fileName);
    }

    /**
     * 根据ContentProvider获取图片地址
     * @param context
     * @param maxCount 获取最大数量，如果值为-1则代表获取所有
     * @return
     */
    public static List<String> getImagePathsByContentProvider(Context context,int maxCount){
        Uri imgURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String key_type = MediaStore.Images.Media.MIME_TYPE;
        String key_data = MediaStore.Images.Media.DATA;

        Cursor cursor = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(imgURI,new String[]{key_data},
                    key_type+"=? or "+key_type+"=? or "+key_type+"=?",
                    new String[]{"image/jpg","image/jpeg","image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(cursor != null){
            List<String> paths = new ArrayList<String>();
            if(cursor.moveToLast()){
                while (true){
                    String path = cursor.getString(0);
                    paths.add(path);
                    if(maxCount > 0){
                        if(paths.size()>maxCount || !cursor.moveToPrevious())
                            break;
                    }else{
                        if(!cursor.moveToPrevious())
                            break;
                    }
                }
            }
            cursor.close();
            return paths;
        }else {
            return null;
        }


    }

    /**
     * 获取图片文件夹列表
     * @param context
     * @return
     */
    public static List<PhotoAlbumItem> getImagePathsByContentProvider(Context context){
        Uri imgURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String key_image_type =MediaStore.Images.Media.MIME_TYPE;
        String key_data = MediaStore.Images.Media.DATA;

        Cursor cursor = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            cursor = resolver.query(imgURI,new String[]{key_data},
                    key_image_type +"=? or "+key_image_type+"=? or "+key_image_type+"=?",
                    new String[]{"image/png","image/jpg","image/jpeg"},MediaStore.Images.Media.DATE_MODIFIED);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(cursor != null){
            List<PhotoAlbumItem> patahs = new ArrayList<PhotoAlbumItem>();
            if(cursor.moveToLast()){
                Set<String> cache = new HashSet<String>();
                while (true){
                    String path = cursor.getString(0);
                    File file = new File(path);
                    File parent = file.getParentFile();
                    String parentPath = parent.getAbsolutePath();
                    if(!cache.contains(parentPath)){
                        patahs.add(new PhotoAlbumItem(parentPath,getImageCount(parent),getFirstImagePath(parent)));
                        cache.add(parentPath);
                    }
                    if(!cursor.moveToPrevious())
                        break;
                }
            }
            cursor.close();
            return patahs;
        }else {
            return null;
        }
    }

    public static int getImageCount(File floder){
        int count = 0;
        if(floder != null){
            File[] files = floder.listFiles();
            int length = files.length;
            for (int i=0;i<length;i++){
                if(isImage(files[i].getName()))
                    count++;
            }
        }

        return count;
    }

    /**
     * 获取目录中最新的一张图片的绝对路径。
     */
    private static String getFirstImagePath(File folder) {
        File[] files = folder.listFiles();
        for (int i = files.length - 1; i >= 0; i--) {
            File file = files[i];
            if (isImage(file.getName())) {
                return file.getAbsolutePath();
            }
        }

        return null;
    }

    /**判断该文件是否是一个图片。*/
    public static boolean isImage(String fileName) {
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    /**
     * 获取Bitmap大小
     * @param bmp
     * @return
     */
    public static int getBitmapSize(Bitmap bmp){
        if(bmp == null)
            return 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1){
            return bmp.getByteCount();
        }else {
            return bmp.getRowBytes() * bmp.getHeight();
        }
    }

    public static String getStringFromFile(Context context, String strFilePath) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(strFilePath));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
