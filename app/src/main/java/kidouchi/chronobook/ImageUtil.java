package kidouchi.chronobook;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by iuy407 on 2/11/16.
 */
public class ImageUtil {

    public static String getRealPathFromURI(Context context, Uri uri) {
        String filePath = "";

        // Extract the COLUMN_DOCUMENT_ID from the given URI.
        String docID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        // ex. image:90 --> retrieve 90
        String id = docID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String selection = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column,
                selection,
                new String[]{id},
                null);

        // Get first result
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }

}
