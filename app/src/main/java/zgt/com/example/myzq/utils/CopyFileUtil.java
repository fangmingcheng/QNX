package zgt.com.example.myzq.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFileUtil {
    private static final String DEAFAULT_FILE_PATH = getSdCardPath() + File.separator;
    private CopyFileUtil() {
    }
    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
//    public static Bitmap readBitMap(Context context, int resId) {
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//        opt.inPreferredConfig = Bitmap.Config.RGB_565;
//        opt.inPurgeable = true;
//        opt.inInputShareable = true;
//        // 获取资源图片
//        InputStream is = context.getResources().openRawResource(resId);
//        return BitmapFactory.decodeStream(is, null, opt);
//    }
//    Bitmap bmp = CopyFileUtil.getDiskBitmap(path);
//        if(bmp != null) {
//        mHeaderIv.setImageBitmap(bmp);
//    }
    /**
     * 读取本地图片
     * @param path 图片路径
     * */
    public static Bitmap getDiskBitmap(String path) {
        Bitmap bitmap = null;
        if(TextUtils.isEmpty(path)) {
            return bitmap;
        }
        try {
            File file = new File(path);
            if (file.exists()) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                bitmap = BitmapFactory.decodeFile(path, opt);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 保存图片到本地 第一个参数是图片名称 第二个参数为需要保存的bitmap
     * */
    public static void saveImgToDisk(String imgName, Bitmap bitmap) {
        File file = new File(DEAFAULT_FILE_PATH, imgName);
        if(file == null) {
            return;
        }
        if(isFileExists(file.getPath())) {
            return;
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            String path = file.getPath();
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 判断文件路径是否已经存在
     * @param filePath 文件路径
     * */
    private static boolean isFileExists(String filePath) {
        try {
            File file = new File( filePath );
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 返回SD卡跟目录 <br>
     *
     * @return SD卡跟目录
     */
    public static String getSdCardPath() {
        File sdDir ;
        boolean sdCardExist = isSdCardExist(); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        } else {
            return null;
        }
    }
    /**
     * 判断SD卡是否存在 <br>
     *
     * @return SD卡存在返回true，否则返回false
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }
}
