package org.cryptimeleon.craco.kem;

import org.cryptimeleon.craco.enc.CipherText;
import org.cryptimeleon.craco.enc.DecryptionKey;
import org.cryptimeleon.craco.enc.EncryptionKey;
import org.cryptimeleon.math.serialization.Representation;
import org.cryptimeleon.math.serialization.StandaloneRepresentable;
import org.cryptimeleon.math.serialization.annotations.RepresentationRestorer;

import java.lang.reflect.Type;

/**
 * A mechanism that is able to generate random symmetric keys and
 * encrypt/decrypt them to build hybrid encryption schemes.
 * For this purpose, encrypt and decrypt are called encaps and decaps, respectively.
 * <p>
 * The usual use case is:
 * <ol>
 * <li> call {@link #encaps(EncryptionKey)} with the public key of the receiver to get a random symmetric key k
 * and an encrypted version c
 * <li> hand c to the receiver
 * <li> the receiver calls {@link #decaps(CipherText, DecryptionKey)} on c and their secret decryption key,
 * which yields k
 * <li> the receiver now has the same random key k as you
 * </ol>
 * Cramer, Ronald, and Victor Shoup. Design and Analysis of Practical Public-Key Encryption Schemes Secure against
 * Adaptive Chosen Ciphertext Attack. SIAM Journal on Computing 33, no. 1 (2003): 167-226.
 * Page 201.
 *
 * @param <T> type of the encapsulated key
 */
public interface KeyEncapsulationMechanism<T> extends StandaloneRepresentable, RepresentationRestorer {
    class KeyAndCiphertext<T> {
        public T key;
        public CipherText encapsulatedKey;
    }

    /**
     * Randomly chooses a key k and encrypts it using the given {@code pk}.
     * The result is {@code (key, encapsulatedKey)}.
     *
     * @param pk public key used for encrypting k
     */
    KeyAndCiphertext<T> encaps(EncryptionKey pk);

    /**
     * Takes an encapsulated key that was created by {@code encaps()} and decrypts it with {@code sk}.
     * That is, if {@code (key, encapsulatedKey) = encaps(pk)}, then {@code decrypt(encapsulatedKey, sk) == key}.
     */
    T decaps(CipherText encapsulatedKey, DecryptionKey sk) throws IllegalArgumentException;

    CipherText restoreEncapsulatedKey(Representation repr);

    EncryptionKey restoreEncapsulationKey(Representation repr);

    DecryptionKey restoreDecapsulationKey(Representation repr);

    default Object restoreFromRepresentation(Type type, Representation repr) {
        if (type instanceof Class) {
            if (EncryptionKey.class.isAssignableFrom((Class) type)) {
                return this.restoreEncapsulationKey(repr);
            } else if (DecryptionKey.class.isAssignableFrom((Class) type)) {
                return this.restoreDecapsulationKey(repr);
            } else if (CipherText.class.isAssignableFrom((Class) type)) {
                return this.restoreEncapsulatedKey(repr);
            }
        }
        throw new IllegalArgumentException("Cannot restore object of type: " + type.getTypeName());
    }
}
