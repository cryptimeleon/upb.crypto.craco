package org.cryptimeleon.craco.common.plaintexts;

import org.cryptimeleon.math.hash.ByteAccumulator;
import org.cryptimeleon.math.serialization.ObjectRepresentation;
import org.cryptimeleon.math.serialization.RepresentableRepresentation;
import org.cryptimeleon.math.serialization.Representation;
import org.cryptimeleon.math.structures.rings.Ring;
import org.cryptimeleon.math.structures.rings.RingElement;

import java.util.Objects;

/**
 * A plaintext consisting of a single ring element.
 */
public class RingElementPlainText implements PlainText {
    private final RingElement element;

    public RingElementPlainText(RingElement element) {
        this.element = element;
    }

    public RingElementPlainText(Representation repr) {
        Ring ring = (Ring) repr.obj().get("ring").repr().recreateRepresentable();
        this.element = ring.restoreElement(repr.obj().get("elem"));
    }

    /**
     * Returns the ring element represented by this plain text.
     */
    public RingElement getRingElement() {
        return element;
    }

    @Override
    public Representation getRepresentation() {
        ObjectRepresentation result = new ObjectRepresentation();
        result.put("elem", element.getRepresentation());
        result.put("ring", new RepresentableRepresentation(element.getStructure()));
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(element);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RingElementPlainText other = (RingElementPlainText) obj;
        return Objects.equals(element, other.element);
    }

    @Override
    public String toString() {
        return element.toString();
    }

    @Override
    public ByteAccumulator updateAccumulator(ByteAccumulator accumulator) {
        return element.updateAccumulator(accumulator);
    }
}
