package com.google.appinventor.components.runtime.util;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Environment;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.ReplForm;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MediaUtil {
    private static final String LOG_TAG = "MediaUtil";
    private static final String REPL_ASSET_DIR = "/sdcard/AppInventor/assets/";
    private static ConcurrentHashMap<String, String> pathCache = new ConcurrentHashMap(2);
    private static final Map<String, File> tempFileMap = new HashMap();

    private static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0;
            while (totalBytesSkipped < n) {
                long bytesSkipped = this.in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0) {
                    if (read() < 0) {
                        break;
                    }
                    bytesSkipped = 1;
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    private enum MediaSource {
        ASSET,
        REPL_ASSET,
        SDCARD,
        FILE_URL,
        URL,
        CONTENT_URI,
        CONTACT_URI
    }

    private MediaUtil() {
    }

    private static String replAssetPath(String assetName) {
        return REPL_ASSET_DIR + assetName;
    }

    static String fileUrlToFilePath(String mediaPath) throws IOException {
        try {
            return new File(new URL(mediaPath).toURI()).getAbsolutePath();
        } catch (IllegalArgumentException e) {
            throw new IOException("Unable to determine file path of file url " + mediaPath);
        } catch (Exception e2) {
            throw new IOException("Unable to determine file path of file url " + mediaPath);
        }
    }

    private static MediaSource determineMediaSource(Form form, String mediaPath) {
        if (mediaPath.startsWith("/sdcard/") || mediaPath.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())) {
            return MediaSource.SDCARD;
        }
        if (mediaPath.startsWith("content://contacts/")) {
            return MediaSource.CONTACT_URI;
        }
        if (mediaPath.startsWith("content://")) {
            return MediaSource.CONTENT_URI;
        }
        try {
            URL url = new URL(mediaPath);
            if (mediaPath.startsWith("file:")) {
                return MediaSource.FILE_URL;
            }
            return MediaSource.URL;
        } catch (MalformedURLException e) {
            if (!(form instanceof ReplForm)) {
                return MediaSource.ASSET;
            }
            if (((ReplForm) form).isAssetsLoaded()) {
                return MediaSource.REPL_ASSET;
            }
            return MediaSource.ASSET;
        }
    }

    private static String findCaseinsensitivePath(Form form, String mediaPath) throws IOException {
        if (!pathCache.containsKey(mediaPath)) {
            String newPath = findCaseinsensitivePathWithoutCache(form, mediaPath);
            if (newPath == null) {
                return null;
            }
            pathCache.put(mediaPath, newPath);
        }
        return (String) pathCache.get(mediaPath);
    }

    private static String findCaseinsensitivePathWithoutCache(Form form, String mediaPath) throws IOException {
        String[] mediaPathlist = form.getAssets().list("");
        int l = Array.getLength(mediaPathlist);
        for (int i = 0; i < l; i++) {
            String temp = mediaPathlist[i];
            if (temp.equalsIgnoreCase(mediaPath)) {
                return temp;
            }
        }
        return null;
    }

    private static InputStream getAssetsIgnoreCaseInputStream(Form form, String mediaPath) throws IOException {
        try {
            return form.getAssets().open(mediaPath);
        } catch (IOException e) {
            String path = findCaseinsensitivePath(form, mediaPath);
            if (path != null) {
                return form.getAssets().open(path);
            }
            throw e;
        }
    }

    private static InputStream openMedia(Form form, String mediaPath, MediaSource mediaSource) throws IOException {
        switch (mediaSource) {
            case ASSET:
                return getAssetsIgnoreCaseInputStream(form, mediaPath);
            case REPL_ASSET:
                return new FileInputStream(replAssetPath(mediaPath));
            case SDCARD:
                return new FileInputStream(mediaPath);
            case FILE_URL:
            case URL:
                return new URL(mediaPath).openStream();
            case CONTENT_URI:
                return form.getContentResolver().openInputStream(Uri.parse(mediaPath));
            case CONTACT_URI:
                InputStream is;
                if (SdkLevel.getLevel() >= 12) {
                    is = HoneycombMR1Util.openContactPhotoInputStreamHelper(form.getContentResolver(), Uri.parse(mediaPath));
                } else {
                    is = People.openContactPhotoInputStream(form.getContentResolver(), Uri.parse(mediaPath));
                }
                if (is != null) {
                    return is;
                }
                throw new IOException("Unable to open contact photo " + mediaPath + ".");
            default:
                throw new IOException("Unable to open media " + mediaPath + ".");
        }
    }

    public static InputStream openMedia(Form form, String mediaPath) throws IOException {
        return openMedia(form, mediaPath, determineMediaSource(form, mediaPath));
    }

    public static File copyMediaToTempFile(Form form, String mediaPath) throws IOException {
        return copyMediaToTempFile(form, mediaPath, determineMediaSource(form, mediaPath));
    }

    private static File copyMediaToTempFile(Form form, String mediaPath, MediaSource mediaSource) throws IOException {
        InputStream in = openMedia(form, mediaPath, mediaSource);
        File file = null;
        try {
            file = File.createTempFile("AI_Media_", null);
            file.deleteOnExit();
            FileUtil.writeStreamToFile(in, file.getAbsolutePath());
            in.close();
            return file;
        } catch (IOException e) {
            if (file != null) {
                Log.e(LOG_TAG, "Could not copy media " + mediaPath + " to temp file " + file.getAbsolutePath());
                file.delete();
            } else {
                Log.e(LOG_TAG, "Could not copy media " + mediaPath + " to temp file.");
            }
            throw e;
        } catch (Throwable th) {
            in.close();
        }
    }

    private static File cacheMediaTempFile(Form form, String mediaPath, MediaSource mediaSource) throws IOException {
        File tempFile = (File) tempFileMap.get(mediaPath);
        if (tempFile != null && tempFile.exists()) {
            return tempFile;
        }
        Log.i(LOG_TAG, "Copying media " + mediaPath + " to temp file...");
        tempFile = copyMediaToTempFile(form, mediaPath, mediaSource);
        Log.i(LOG_TAG, "Finished copying media " + mediaPath + " to temp file " + tempFile.getAbsolutePath());
        tempFileMap.put(mediaPath, tempFile);
        return tempFile;
    }

    public static BitmapDrawable getBitmapDrawable(Form form, String mediaPath) throws IOException {
        if (mediaPath == null || mediaPath.length() == 0) {
            return null;
        }
        MediaSource mediaSource = determineMediaSource(form, mediaPath);
        try {
            InputStream is1 = openMedia(form, mediaPath, mediaSource);
            try {
                Options options = getBitmapOptions(form, is1, mediaPath);
                InputStream is2 = openMedia(form, mediaPath, mediaSource);
                try {
                    Log.d(LOG_TAG, "mediaPath = " + mediaPath);
                    BitmapDrawable originalBitmapDrawable = new BitmapDrawable(decodeStream(is2, null, options));
                    originalBitmapDrawable.setTargetDensity(form.getResources().getDisplayMetrics());
                    if (options.inSampleSize != 1 || form.deviceDensity() == 1.0f) {
                        if (is2 != null) {
                            is2.close();
                        }
                        return originalBitmapDrawable;
                    }
                    int scaledWidth = (int) (form.deviceDensity() * ((float) originalBitmapDrawable.getIntrinsicWidth()));
                    int scaledHeight = (int) (form.deviceDensity() * ((float) originalBitmapDrawable.getIntrinsicHeight()));
                    Log.d(LOG_TAG, "form.deviceDensity() = " + form.deviceDensity());
                    Log.d(LOG_TAG, "originalBitmapDrawable.getIntrinsicWidth() = " + originalBitmapDrawable.getIntrinsicWidth());
                    Log.d(LOG_TAG, "originalBitmapDrawable.getIntrinsicHeight() = " + originalBitmapDrawable.getIntrinsicHeight());
                    BitmapDrawable scaledBitmapDrawable = new BitmapDrawable(Bitmap.createScaledBitmap(originalBitmapDrawable.getBitmap(), scaledWidth, scaledHeight, false));
                    scaledBitmapDrawable.setTargetDensity(form.getResources().getDisplayMetrics());
                    System.gc();
                    if (is2 == null) {
                        return scaledBitmapDrawable;
                    }
                    is2.close();
                    return scaledBitmapDrawable;
                } catch (Throwable th) {
                    if (is2 != null) {
                        is2.close();
                    }
                }
            } finally {
                is1.close();
            }
        } catch (IOException e) {
            if (mediaSource == MediaSource.CONTACT_URI) {
                return new BitmapDrawable(BitmapFactory.decodeResource(form.getResources(), 17301606, null));
            }
            throw e;
        }
    }

    private static Bitmap decodeStream(InputStream is, Rect outPadding, Options opts) {
        return BitmapFactory.decodeStream(new FlushedInputStream(is), outPadding, opts);
    }

    private static Options getBitmapOptions(Form form, InputStream is, String mediaPath) {
        int maxWidth;
        int maxHeight;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        decodeStream(is, null, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;
        Display display = ((WindowManager) form.getSystemService("window")).getDefaultDisplay();
        if (Form.getCompatibilityMode()) {
            maxWidth = 720;
            maxHeight = 840;
        } else {
            maxWidth = (int) (((float) display.getWidth()) / form.deviceDensity());
            maxHeight = (int) (((float) display.getHeight()) / form.deviceDensity());
        }
        int sampleSize = 1;
        while (imageWidth / sampleSize > maxWidth && imageHeight / sampleSize > maxHeight) {
            sampleSize *= 2;
        }
        options = new Options();
        Log.d(LOG_TAG, "getBitmapOptions: sampleSize = " + sampleSize + " mediaPath = " + mediaPath + " maxWidth = " + maxWidth + " maxHeight = " + maxHeight + " display width = " + display.getWidth() + " display height = " + display.getHeight());
        options.inSampleSize = sampleSize;
        return options;
    }

    private static AssetFileDescriptor getAssetsIgnoreCaseAfd(Form form, String mediaPath) throws IOException {
        try {
            return form.getAssets().openFd(mediaPath);
        } catch (IOException e) {
            String path = findCaseinsensitivePath(form, mediaPath);
            if (path != null) {
                return form.getAssets().openFd(path);
            }
            throw e;
        }
    }

    public static int loadSoundPool(SoundPool soundPool, Form form, String mediaPath) throws IOException {
        MediaSource mediaSource = determineMediaSource(form, mediaPath);
        switch (mediaSource) {
            case ASSET:
                return soundPool.load(getAssetsIgnoreCaseAfd(form, mediaPath), 1);
            case REPL_ASSET:
                return soundPool.load(replAssetPath(mediaPath), 1);
            case SDCARD:
                return soundPool.load(mediaPath, 1);
            case FILE_URL:
                return soundPool.load(fileUrlToFilePath(mediaPath), 1);
            case URL:
            case CONTENT_URI:
                return soundPool.load(cacheMediaTempFile(form, mediaPath, mediaSource).getAbsolutePath(), 1);
            case CONTACT_URI:
                throw new IOException("Unable to load audio for contact " + mediaPath + ".");
            default:
                throw new IOException("Unable to load audio " + mediaPath + ".");
        }
    }

    public static void loadMediaPlayer(MediaPlayer mediaPlayer, Form form, String mediaPath) throws IOException {
        switch (determineMediaSource(form, mediaPath)) {
            case ASSET:
                AssetFileDescriptor afd = getAssetsIgnoreCaseAfd(form, mediaPath);
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    return;
                } finally {
                    afd.close();
                }
            case REPL_ASSET:
                mediaPlayer.setDataSource(replAssetPath(mediaPath));
                return;
            case SDCARD:
                mediaPlayer.setDataSource(mediaPath);
                return;
            case FILE_URL:
                mediaPlayer.setDataSource(fileUrlToFilePath(mediaPath));
                return;
            case URL:
                mediaPlayer.setDataSource(mediaPath);
                return;
            case CONTENT_URI:
                mediaPlayer.setDataSource(form, Uri.parse(mediaPath));
                return;
            case CONTACT_URI:
                throw new IOException("Unable to load audio or video for contact " + mediaPath + ".");
            default:
                throw new IOException("Unable to load audio or video " + mediaPath + ".");
        }
    }

    public static void loadVideoView(VideoView videoView, Form form, String mediaPath) throws IOException {
        MediaSource mediaSource = determineMediaSource(form, mediaPath);
        switch (mediaSource) {
            case ASSET:
            case URL:
                videoView.setVideoPath(cacheMediaTempFile(form, mediaPath, mediaSource).getAbsolutePath());
                return;
            case REPL_ASSET:
                videoView.setVideoPath(replAssetPath(mediaPath));
                return;
            case SDCARD:
                videoView.setVideoPath(mediaPath);
                return;
            case FILE_URL:
                videoView.setVideoPath(fileUrlToFilePath(mediaPath));
                return;
            case CONTENT_URI:
                videoView.setVideoURI(Uri.parse(mediaPath));
                return;
            case CONTACT_URI:
                throw new IOException("Unable to load video for contact " + mediaPath + ".");
            default:
                throw new IOException("Unable to load video " + mediaPath + ".");
        }
    }
}
