package de.upb.crypto.craco.abe.ibe;

import de.upb.crypto.craco.interfaces.pe.MasterSecret;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.annotations.AnnotatedRepresentationUtil;
import de.upb.crypto.math.serialization.annotations.Represented;
import de.upb.crypto.math.structures.zn.Zp;
import de.upb.crypto.math.structures.zn.Zp.ZpElement;

/**
 * The {@link MasterSecret} for the {@link FullIdent} generated by
 * {@link FullIdentSetup}.
 *
 * @author Mirko Jürgens
 */
public class FullIdentMasterSecret implements MasterSecret {


    // Uniformly random element in Z_{size(GroupG1)}*
    @Represented(structure = "zp", recoveryMethod = ZpElement.RECOVERY_METHOD)
    private ZpElement s;

    @SuppressWarnings("unused")
    private Zp zp;

    public FullIdentMasterSecret(ZpElement s) {
        this.s = s;
    }

    public FullIdentMasterSecret(Representation repr, FullIdentPublicParameters pp) {
        zp = new Zp(pp.getGroupG1().size());
        AnnotatedRepresentationUtil.restoreAnnotatedRepresentation(repr, this);
    }

    @Override
    public Representation getRepresentation() {
        return AnnotatedRepresentationUtil.putAnnotatedRepresentation(this);
    }

    public ZpElement getS() {
        return s;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((s == null) ? 0 : s.hashCode());
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
        FullIdentMasterSecret other = (FullIdentMasterSecret) obj;
        if (s == null) {
            if (other.s != null)
                return false;
        } else if (!s.equals(other.s))
            return false;
        return true;
    }

}