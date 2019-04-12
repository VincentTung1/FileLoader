package com.vincent.loadfilelibrary.engine.zip.manager;

import android.text.TextUtils;

import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.engine.zip.entity.MimeType;

import java.util.HashMap;
import java.util.Map;

public class ResourceTypeManager {

    private static ResourceTypeManager INSTANCE = new ResourceTypeManager();

    private static Map<String,MimeType> map = null;//mimeType集合

    private static Map<MimeType,Integer> resMap;//mimeType对应图片资源集合

    public static ResourceTypeManager get(){

        if (map == null) {
            map = new HashMap<String,MimeType>();
            map.put(".amr", MimeType.MUSIC);
            map.put(".mp3", MimeType.MUSIC);
            map.put(".ogg", MimeType.MUSIC);
            map.put(".wav", MimeType.MUSIC);
            map.put(".m4a", MimeType.MUSIC);
            map.put(".3gp", MimeType.VIDEO);
            map.put(".mp4", MimeType.VIDEO);
            map.put(".rmvb",MimeType.VIDEO);
            map.put(".mpeg",MimeType.VIDEO);
            map.put(".mpg", MimeType.VIDEO);
            map.put(".asf", MimeType.VIDEO);
            map.put(".avi", MimeType.VIDEO);
            map.put(".wmv", MimeType.VIDEO);
            map.put(".apk", MimeType.APK);
            map.put(".bmp", MimeType.IMAGE);
            map.put(".gif", MimeType.IMAGE);
            map.put(".jpeg",MimeType.IMAGE);
            map.put(".jpg", MimeType.IMAGE);
            map.put(".png", MimeType.IMAGE);
            map.put(".doc", MimeType.DOC);
            map.put(".docx",MimeType.DOC);
            map.put(".rtf", MimeType.DOC);
            map.put(".wps", MimeType.DOC);
            map.put(".xls", MimeType.XLS);
            map.put(".xlsx", MimeType.XLS);
            map.put(".gtar", MimeType.RAR);
            map.put(".gz", MimeType.RAR);
            map.put(".zip",MimeType.RAR);
            map.put(".tar",MimeType.RAR);
            map.put(".rar",MimeType.RAR);
            map.put(".jar", MimeType.RAR);
            map.put(".htm", MimeType.HTML);
            map.put(".html",MimeType.HTML);
            map.put(".xhtml", MimeType.HTML);
            map.put(".sql", MimeType.TXT);
            map.put(".java",MimeType.TXT);
            map.put(".txt", MimeType.TXT);
            map.put(".xml", MimeType.TXT);
            map.put(".log", MimeType.TXT);
            map.put(".pdf", MimeType.PDF);
            map.put(".ppt", MimeType.PPT);
            map.put(".pptx",MimeType.PPT);
        }

        if (resMap == null) {
            resMap = new HashMap<MimeType,Integer>();
            resMap.put(MimeType.APK, R.drawable.utils_filemanager_apk);
            resMap.put(MimeType.DOC, R.drawable.utils_filemanager_doc);
            resMap.put(MimeType.HTML, R.drawable.utils_filemanager_html);
            resMap.put(MimeType.IMAGE, R.drawable.utils_filemanager_jpg);
            resMap.put(MimeType.MUSIC, R.drawable.utils_filemanager_mp3);
            resMap.put(MimeType.VIDEO, R.drawable.utils_filemanager_video);
            resMap.put(MimeType.PDF, R.drawable.utils_filemanager_pdf);
            resMap.put(MimeType.PPT, R.drawable.utils_filemanager_ppt);
            resMap.put(MimeType.RAR, R.drawable.utils_filemanager_zip);
            resMap.put(MimeType.TXT, R.drawable.utils_filemanager_txt);
            resMap.put(MimeType.XLS, R.drawable.utils_filemanager_xls);
            resMap.put(MimeType.UNKNOWN, R.drawable.utils_filemanager_unknow);
        }

        return INSTANCE;
    }


    /**
     *
     * 创建者：ldh
     * 时间：2015年6月2日 下午2:15:20
     * 注释：获取文件类型
     */
    public MimeType getMimeType(String exspansion){
        if (!TextUtils.isEmpty(exspansion)){
            return map.get(exspansion.toLowerCase());
        }
        return MimeType.UNKNOWN;
    }


    /**
     * 获取文件类型对应的图片
     * @param type
     * @return
     */
    public Integer getMimeDrawable(MimeType type){
        return resMap.get(type);
    }

    /**
     *  获取文件类型对应的图片
     * @param exspansion
     * @return
     */
    public Integer getMimeDrawable(String exspansion){
        if (resMap == null || map == null) return -1;
        return resMap.get(map.get(exspansion.toLowerCase()));
    }

}
