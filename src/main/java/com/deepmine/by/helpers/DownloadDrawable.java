package com.deepmine.by.helpers;

/**
 * Created by zyr3x on 09.10.13.
 */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadDrawable {
    private static final String LOG_TAG = DownloadDrawable.class.getName();

    public static Bitmap createBitmapFromUrl(String url) {
        Log.d(LOG_TAG, "Starting download");
        Bitmap bitmap;

        bitmap = readBitmapFromNetwork(url);

        Log.d(LOG_TAG, "Download complete");
        return bitmap;
    }


    /**
     * Convenience method to retrieve a bitmap image from
     * a URL over the network. The built-in methods do
     * not seem to work, as they return a FileNotFound
     * exception.
     *
     * Note that this does not perform any threading --
     * it blocks the call while retrieving the data.
     *
     * @param urlString The URL to read the bitmap from.
     * @return A Bitmap image or null if an error occurs.
     */
    private static Bitmap readBitmapFromNetwork(String urlString) {
        InputStream is = null;
        FlushedInputStream bis = null;
        Bitmap bmp = null;
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new FlushedInputStream(is);
            bmp = BitmapFactory.decodeStream(bis);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Bad image URL", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not get remote image", e);
        } finally {
            try {
                if( is != null )
                    is.close();
                if( bis != null )
                    bis.close();
            } catch (IOException e) {
                Log.w(LOG_TAG, "Error closing stream.");
            }
        }
        Log.d(LOG_TAG, "Got bitmap");
        return bmp;
    }

    // This fixes the Image sometimes null bug
    // See http://android-developers.blogspot.com/2010/07/multithreading-for-performance.html
    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int bytes = read();
                    if (bytes < 0){
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}