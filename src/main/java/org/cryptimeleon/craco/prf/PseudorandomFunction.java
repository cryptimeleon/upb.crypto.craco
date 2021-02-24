package org.cryptimeleon.craco.prf;

import org.cryptimeleon.craco.common.plaintexts.PlainText;
import org.cryptimeleon.craco.enc.CipherText;
import org.cryptimeleon.craco.enc.DecryptionKey;
import org.cryptimeleon.craco.enc.EncryptionKey;
import org.cryptimeleon.math.serialization.Representation;
import org.cryptimeleon.math.serialization.StandaloneRepresentable;
import org.cryptimeleon.math.serialization.annotations.RepresentationRestorer;

import java.lang.reflect.Type;

/**
 * A pseudorandom function is a family of functions \((f_k)\)
 * so that for random choice of the key k, \(f_k: X \rightarrow Y\) is computationally
 * indistinguishable from a uniformly random function \(F: X \rightarrow Y\).
 * <p>
 * The way to use this interface is:
 * <ol>
 * <li> Generate a key k using {@link #generateKey()}
 * <li> Call {@link #evaluate(PrfKey, PrfPreimage)} using key k and input x to receive \(y = f_k(x)\)
 * </ol>
 */
public interface PseudorandomFunction extends StandaloneRepresentable, RepresentationRestorer {
    /**
     * Generates a key k for use with this PRF.
     */
    PrfKey generateKey();

    /**
     * Maps a preimage x to its image using key k.
     *
     * @return output of \(f_k(x)\)
     */
    PrfImage evaluate(PrfKey k, PrfPreimage x);

    //below this, there are only serialization-related methods

    /**
     * Recreates (deserializes) a key k from its representation.
     */
    PrfKey getKey(Representation repr);

    /**
     * Recreates (deserializes) a preimage x from its representation.
     */
    PrfPreimage getPreimage(Representation repr);

    /**
     * Recreates (deserializes) an image y from its representation.
     */
    PrfImage getImage(Representation repr);

    default Object recreateFromRepresentation(Type type, Representation repr) {
        if (type instanceof Class) {
            if (PrfKey.class.isAssignableFrom((Class) type)) {
                return this.getKey(repr);
            } else if (PrfPreimage.class.isAssignableFrom((Class) type)) {
                return this.getPreimage(repr);
            } else if (PrfImage.class.isAssignableFrom((Class) type)) {
                return this.getImage(repr);
            }
        }
        throw new IllegalArgumentException("Cannot recreate object of type: " + type.getTypeName());
    }
}