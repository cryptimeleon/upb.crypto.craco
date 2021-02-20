package org.cryptimeleon.craco.accumulator;

import org.cryptimeleon.craco.common.PublicParameters;

import java.math.BigInteger;
import java.util.List;

/**
 * Interface for public parameters of 'Accumulators' reflecting the theoretical properties of 'Accumulators' in
 * combination with the org.cryptimeleon.craco.interfaces in accumulators.org.cryptimeleon.craco.interfaces.
 * Thus, it contains the universe of {@link AccumulatorIdentity} that can be accumulated ait contains the upper bound
 * of {@link AccumulatorIdentity} that can be accumulated using this pp.
 */
public interface AccumulatorPublicParameters extends PublicParameters {

    /**
     * @return Upper bound for {@link AccumulatorIdentity}s that can be Accumulated in the Accumulator
     */
    BigInteger getUpperBoundForAccumulatableIdentities();

    /**
     * @return List of {@link AccumulatorIdentity}s defining the universe of {@link AccumulatorIdentity}s that can be
     * accumulated
     */
    List<? extends AccumulatorIdentity> getUniverse();

}