package org.cryptimeleon.craco.common.attributes;

import org.cryptimeleon.craco.common.policies.PolicyFact;
import org.cryptimeleon.math.hash.ByteAccumulator;
import org.cryptimeleon.math.hash.annotations.AnnotatedUbrUtil;
import org.cryptimeleon.math.hash.annotations.UniqueByteRepresented;
import org.cryptimeleon.math.serialization.BigIntegerRepresentation;
import org.cryptimeleon.math.serialization.ObjectRepresentation;
import org.cryptimeleon.math.serialization.Representation;

import java.math.BigInteger;
import java.util.Collection;

/**
 * An attribute in form of a {@link BigInteger}.
 */
public class BigIntegerAttribute implements Attribute {
    @UniqueByteRepresented
    private BigInteger element;

    public BigIntegerAttribute(Integer element) {
        this.element = BigInteger.valueOf(element);
    }

    public BigIntegerAttribute(BigInteger element) {
        this.element = element;
    }

    public BigIntegerAttribute(Representation repr) {
        this.element = repr.obj().get("elem").bigInt().get();
    }

    /**
     * Returns the element representing the attributes value
     *
     * @return this attributes element/value
     */
    public BigInteger getAttribute() {
        return element;
    }

    @Override
    public Representation getRepresentation() {
        ObjectRepresentation result = new ObjectRepresentation();
        result.put("elem", new BigIntegerRepresentation(element));

        return result;
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BigIntegerAttribute other = (BigIntegerAttribute) obj;
        if (element == null) {
            if (other.element != null)
                return false;
        } else if (!element.equals(other.element))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return element.toString();
    }

    @Override
    public boolean isFulfilled(Collection<? extends PolicyFact> facts) {
        return facts.contains(this);
    }

    @Override
    public ByteAccumulator updateAccumulator(ByteAccumulator accumulator) {
        return AnnotatedUbrUtil.autoAccumulate(accumulator, this);
    }


}
