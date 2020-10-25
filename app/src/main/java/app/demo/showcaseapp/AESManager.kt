package app.demo.showcaseapp

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object AESManager {

    private val password = "aesPassword"
    private val salt = "aesSalt"
    private val ivParam = "aesIVParam123456"

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeySpecException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        UnsupportedEncodingException::class
    )
    fun encrypt(plainText: String?): String? {
        if (plainText == null) return null
        val ivParameterSpec = IvParameterSpec(ivParam.toByteArray())
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(), ivParameterSpec)
        val cipherBytes: ByteArray = cipher.doFinal(plainText.toByteArray())
        return Base64.encodeToString(cipherBytes, Base64.DEFAULT)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        InvalidKeySpecException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class,
        UnsupportedEncodingException::class
    )
    fun decrypt(cipherText: String?): String? {
        if (cipherText == null) return null
        val ivParameterSpec = IvParameterSpec(ivParam.toByteArray())
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, getKeySpec(), ivParameterSpec)
        val plainMessageBytes = cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT))
        return String(plainMessageBytes)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getKeySpec(): SecretKeySpec? {
        val pbeKeySpec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 128, 256)
        val secretKeyFactory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val secretKey: SecretKey = secretKeyFactory.generateSecret(pbeKeySpec)
        return SecretKeySpec(secretKey.encoded, "AES")
    }
}