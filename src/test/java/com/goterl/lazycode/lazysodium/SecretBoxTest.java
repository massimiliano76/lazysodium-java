/*
 * Copyright (c) Terl Tech Ltd • 14/06/19 17:54 • goterl.com
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v2.0. If a copy of the MPL was not distributed with this
 * file, you can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.goterl.lazycode.lazysodium;/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import com.goterl.lazycode.lazysodium.exceptions.SodiumException;
import com.goterl.lazycode.lazysodium.interfaces.SecretBox;
import com.goterl.lazycode.lazysodium.utils.Key;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class SecretBoxTest extends BaseTest {


    private SecretBox.Native secretBoxNative;
    private SecretBox.Lazy secretBoxLazy;

    @Before
    public void before() {
        secretBoxLazy = (SecretBox.Lazy) lazySodium;
        secretBoxNative = (SecretBox.Native) lazySodium;
    }


    @Test
    public void encrypt() throws SodiumException {
        String message = "This is a super secret message.";

        // Generate a symmetric key to encrypt the message.
        Key key = secretBoxLazy.cryptoSecretBoxKeygen();

        // Generate a random nonce.
        byte[] nonce = lazySodium.nonce(SecretBox.NONCEBYTES);
        String cipher = secretBoxLazy.cryptoSecretBoxEasy(message, nonce, key);
        String decrypted = secretBoxLazy.cryptoSecretBoxOpenEasy(cipher, nonce, key);

        TestCase.assertEquals(decrypted, message);
    }

    @Test
    public void encryptXSalsa20Poly1305() {
        String message = "This message has to be greater than 32 characters.";
        byte[] messageBytes = lazySodium.bytes(message);
        byte[] cipherBytes = new byte[SecretBox.MACBYTES + messageBytes.length];

        byte[] key = new byte[SecretBox.KEYBYTES];
        lazySodium.getSodium().crypto_secretbox_xsalsa20poly1305_keygen(key);
        byte[] nonce = lazySodium.nonce(SecretBox.NONCEBYTES);
        int ok = lazySodium.getSodium().crypto_secretbox_xsalsa20poly1305(cipherBytes, messageBytes, messageBytes.length, nonce, key);
        if (ok != 0) {
            throw new RuntimeException("The encryption was unsuccessful. Ensure your message is greater than 32 chars.");
        }

        byte[] resultBytes = new byte[messageBytes.length];
        int ok2 = lazySodium.getSodium().crypto_secretbox_xsalsa20poly1305_open(resultBytes, cipherBytes, cipherBytes.length, nonce, key);

        TestCase.assertEquals(message, lazySodium.str(resultBytes));
    }




}
