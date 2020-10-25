package app.demo.showcaseapp

import android.util.Base64
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

object RSAManager {

    private var publicKey: PublicKey
    private var privateKey: PrivateKey

    init {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        val keyPair = keyPairGenerator.genKeyPair()
        privateKey = keyPair.private
        publicKey = keyPair.public
    }

    fun publicKeyString(): String = Base64.encodeToString(publicKey.encoded, Base64.DEFAULT)

    fun decrypt(cipherText: String): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val messageBytes = cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT))
        return String(messageBytes)
    }
}