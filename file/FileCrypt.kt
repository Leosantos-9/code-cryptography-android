
import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets

/**
 * Encrypts content from a given file
 * @param context Context application
 * @param nameDirectory Name directory where store file
 * @param nameFile Name file. The file name cannot contain path separators. and put your extension
 * @param contentFile The content that will be inside the file
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
fun encryptFile(@NonNull context:Context,
                @NonNull nameDirectory:String,
                @NonNull nameFile:String,
                @NonNull contentFile:String){
    nameFile.forEach {
        if (it==' ')
            throw IllegalArgumentException("nameFile cannot contain space  \"$nameFile\"")
    }
    try {
    val encryptedFile = EncryptedFile.Builder(
        context,
        File(nameDirectory, nameFile),
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
    ).build()

    encryptedFile.openFileOutput().apply {
        write(contentFile.toByteArray(StandardCharsets.UTF_8))
        flush()
        close()
    }
    }catch (e: Exception){
        e.stackTrace
    }
}

/**
 * Decrypt the contents of a given file
 * @param context Context application
 * @param nameDirectory Name of the directory where you will store the file
 * @param nameFile Stored file name
 * @return if the return is a String Byte, use String(ByteArray) to convert to String
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
fun decrypFile(@NonNull context:Context, @NonNull nameDirectory:String, @NonNull nameFile:String):ByteArray?{
    nameFile.forEach {
        if (it==' ')
            throw IllegalArgumentException("nameFile cannot contain space \"$nameFile\"")
    }
    try {
        val encryptedFile = EncryptedFile.Builder(
            context,
            File(nameDirectory, nameFile),
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        val inputStream = encryptedFile.openFileInput()
        val byteArrayOutputStream = ByteArrayOutputStream()
        var nextByte: Int = inputStream.read()
        while (nextByte != -1) {
            byteArrayOutputStream.write(nextByte)
            nextByte = inputStream.read()
        }
        return byteArrayOutputStream.toByteArray()
    }catch (e: FileNotFoundException){
        e.stackTrace
        return null
    }

}