
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


/**
 * Encrypt and decrypt the data key and value SharedPreferences
 * @param context the context application
 * @param nameFileShared Name file SharedPreference
 * @return SharedPreferences use normally would
 */
fun  sharedPreferencesCrypt(context: Context, nameFileShared:String):SharedPreferences{
    return EncryptedSharedPreferences.create(
        context,
        nameFileShared,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
