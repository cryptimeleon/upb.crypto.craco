package de.upb.crypto.craco.kem;

import de.upb.crypto.craco.interfaces.CipherText;
import de.upb.crypto.craco.interfaces.DecryptionKey;
import de.upb.crypto.craco.interfaces.EncryptionKey;
import de.upb.crypto.craco.interfaces.SymmetricKey;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.annotations.v2.ReprUtil;
import de.upb.crypto.math.serialization.annotations.v2.Represented;

/**
 * A KEM that is implemented by the composition of a {@link KeyEncapsulationMechanism} providing {@link KeyMaterial} and
 * a {@link KeyDerivationFunction} that derives a {@link SymmetricKey} from the {@link KeyMaterial} produced by the KEM.
 * <p>
 * This should be used in combination with an symmetric encryption scheme to implement the standard hybrid encryption
 * technique.
 *
 * @author Denis Diemert
 */
public abstract class SymmetricKeyKEM implements KeyEncapsulationMechanism<SymmetricKey> {


    /**
     * Underlying KEM that generates the key material of this KEM.
     */
    @Represented
    protected KeyEncapsulationMechanism<? extends KeyMaterial> kem;

    /**
     * KDF that derives a symmetric key from the key material generated by {@link #kem}
     */
    @Represented
    protected KeyDerivationFunction<? extends SymmetricKey> kdf;

    public SymmetricKeyKEM(KeyEncapsulationMechanism<? extends KeyMaterial> kem,
                           KeyDerivationFunction<? extends SymmetricKey> kdf) {
        this.kem = kem;
        this.kdf = kdf;
    }

    public SymmetricKeyKEM(Representation repr) {
        new ReprUtil(this).deserialize(repr);
    }

    /**
     * {@inheritDoc}
     * <p>
     * It basically outputs the derived key from the key material given by the KEM's encaps and the corresponding
     * encapsulation.
     */
    @Override
    public KeyAndCiphertext<SymmetricKey> encaps(EncryptionKey pk) {
        KeyAndCiphertext<SymmetricKey> output = new KeyAndCiphertext<>();
        KeyAndCiphertext<? extends KeyMaterial> temp = kem.encaps(pk);

        output.key = kdf.deriveKey(temp.key);
        output.encapsulatedKey = temp.encapsulatedKey;

        return output;
    }

    /**
     * {@inheritDoc}
     * <p>
     * It outputs the image of the {@link #kdf} on the decapsulation of the given {@code encapsulatedKey}.
     */
    @Override
    public SymmetricKey decaps(CipherText encapsulatedKey, DecryptionKey sk) {
        return kdf.deriveKey(kem.decaps(encapsulatedKey, sk));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CipherText getEncapsulatedKey(Representation repr) {
        return kem.getEncapsulatedKey(repr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EncryptionKey getEncapsulationKey(Representation repr) {
        return kem.getEncapsulationKey(repr);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DecryptionKey getDecapsulationKey(Representation repr) {
        return kem.getDecapsulationKey(repr);
    }

    public KeyEncapsulationMechanism<? extends KeyMaterial> getKem() {
        return kem;
    }

    public KeyDerivationFunction<? extends SymmetricKey> getKdf() {
        return kdf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SymmetricKeyKEM that = (SymmetricKeyKEM) o;

        if (kem != null ? !kem.equals(that.kem) : that.kem != null)
            return false;
        return kdf != null ? kdf.equals(that.kdf) : that.kdf == null;
    }

    @Override
    public int hashCode() {
        int result = kem != null ? kem.hashCode() : 0;
        result = 31 * result + (kdf != null ? kdf.hashCode() : 0);
        return result;
    }

    @Override
    public Representation getRepresentation() {
        return ReprUtil.serialize(this);

    }

}
