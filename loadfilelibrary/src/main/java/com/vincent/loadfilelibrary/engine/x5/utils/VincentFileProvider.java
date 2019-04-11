package com.vincent.loadfilelibrary.engine.x5.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *  共享app文件数据的内容提供者
 */
public class VincentFileProvider extends ContentProvider {

    public static final String AUTHORITY = "com.vincent.fileprovider";


    //匹配成功后的匹配码
    private static final int MATCH_CODE = 100;

    private static final UriMatcher mUriMatcher ;


    static {

        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY,"",MATCH_CODE);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static Uri getFileFromUri(File f){

        PathStrategyImpl impl = new PathStrategyImpl();
        return impl.getUriForFile(f);
    }


    @Nullable
    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {
        PathStrategyImpl impl = new PathStrategyImpl();
        File file = impl.getFileForUri(uri);
        System.out.println("file.exists() = " + file.exists());
        int fileMode = modeToMode(mode);
        return ParcelFileDescriptor.open(file, fileMode);
    }

    private static int modeToMode(String mode) {
        int modeBits;
        if ("r".equals(mode)) {
            modeBits = 268435456;
        } else if (!"w".equals(mode) && !"wt".equals(mode)) {
            if ("wa".equals(mode)) {
                modeBits = 704643072;
            } else if ("rw".equals(mode)) {
                modeBits = 939524096;
            } else {
                if (!"rwt".equals(mode)) {
                    throw new IllegalArgumentException("Invalid mode: " + mode);
                }

                modeBits = 1006632960;
            }
        } else {
            modeBits = 738197504;
        }

        return modeBits;
    }

    private static class PathStrategyImpl implements PathStrategy{

        @Override
        public Uri getUriForFile(File file) {

            String path;
            try {
                path = file.getCanonicalPath();
            } catch (IOException var7) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file);
            }

            return (new Uri.Builder()).scheme("content").authority(AUTHORITY).encodedPath(path).build();
        }

        @Override
        public File getFileForUri(Uri uri) {

            String path = uri.getEncodedPath();
            path = Uri.decode(path);
           return new File(path);

        }
    }

    private interface PathStrategy {
        Uri getUriForFile(File file);

        File getFileForUri(Uri uri);
    }
}
