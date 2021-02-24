package de.upb.crypto.craco.commitment.pedersen;

import de.upb.crypto.craco.commitment.Commitment;
import de.upb.crypto.math.hash.ByteAccumulator;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.annotations.ReprUtil;
import de.upb.crypto.math.structures.groups.GroupElement;

import java.util.Objects;

public class PedersenCommitment implements Commitment {
    private final GroupElement commitment;

    public PedersenCommitment(GroupElement commitment) {
        this.commitment = commitment;
    }

    public GroupElement get() {
        return commitment;
    }

    @Override
    public Representation getRepresentation() {
        return new ReprUtil(this)
                .register(commitment.getStructure(), "G")
                .serialize();
    }

    @Override
    public ByteAccumulator updateAccumulator(ByteAccumulator byteAccumulator) {
        return commitment.updateAccumulator(byteAccumulator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PedersenCommitment that = (PedersenCommitment) o;
        return Objects.equals(commitment, that.commitment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commitment);
    }
}