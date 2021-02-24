package de.upb.crypto.craco.accumulator;

/**
 * Interface for 'setup()-method' of 'Accumulators', generating the {@link AccumulatorPublicParameters} and
 * reflecting the theoretical properties of 'Accumulators' in combination with the de.upb.crypto.craco.interfaces in
 * accumulators.de.upb.crypto.craco.interfaces.
 * <p>
 * It contains the following methods of the theoretical definition:
 * Setup
 */
public interface AccumulatorPublicParametersGen {

    /**
     * Setup of {@link AccumulatorPublicParameters}
     *
     * @param securityParameter security parameter
     * @param size              upper bound for the number of accumulated {@link AccumulatorIdentity}
     * @return {@link AccumulatorPublicParameters} containing the universe of accumulatable {@link AccumulatorIdentity}
     */
    AccumulatorPublicParameters setup(int securityParameter, int size);
}