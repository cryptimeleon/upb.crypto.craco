package org.cryptimeleon.craco.kem;

import org.cryptimeleon.craco.enc.SymmetricKey;
import org.cryptimeleon.craco.common.ByteArrayImplementation;
import org.cryptimeleon.math.hash.HashFunction;
import org.cryptimeleon.math.hash.impl.SHA256HashFunction;
import org.cryptimeleon.math.serialization.Representation;
import org.cryptimeleon.math.serialization.annotations.ReprUtil;
import org.cryptimeleon.math.serialization.annotations.Represented;

import java.util.Arrays;
import java.util.Objects;

/**
 * A basic implementation of a {@link KeyDerivationFunction} using a hash function.
 * This class can be used to generate key derivation functions that are not provably secure
 * (e.g. using {@link SHA256HashFunction} as a hash function),
 * or it can be used to generate provably secure key derivation functions.
 *
 */
public class HashBasedKeyDerivationFunction implements KeyDerivationFunction<SymmetricKey> {

    @Represented
    private HashFunction hashFunction;

    public HashBasedKeyDerivationFunction(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
    }

    /**
     * Initializes this key derivation function based on the SHA256 hash function.
     * Does not yield a provably secure key derivation function.
     */
    public HashBasedKeyDerivationFunction() {
        this(new SHA256HashFunction());
    }

    public HashBasedKeyDerivationFunction(Representation repr) {
        new ReprUtil(this).deserialize(repr);
    }

    @Override
    public Representation getRepresentation() {
        return ReprUtil.serialize(this);
    }

    @Override
    public SymmetricKey deriveKey(KeyMaterial material) {
        byte[] hashedPlainText = hashFunction.hash(material);
        return new ByteArrayImplementation(Arrays.copyOfRange(hashedPlainText, 0, 128 / 8));
    }


    /**
     * Returns the output length in number of bytes
     *
     * @return the output length in number of bytes
     */
    public int bitSize() {
        return hashFunction.getOutputLength();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hashFunction == null) ? 0 : hashFunction.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HashBasedKeyDerivationFunction other = (HashBasedKeyDerivationFunction) obj;
        return Objects.equals(hashFunction, other.hashFunction);
    }

}
