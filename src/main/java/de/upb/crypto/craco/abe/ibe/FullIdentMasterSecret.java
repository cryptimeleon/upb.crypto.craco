package de.upb.crypto.craco.abe.ibe;

import de.upb.crypto.craco.interfaces.pe.MasterSecret;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.annotations.v2.ReprUtil;
import de.upb.crypto.math.serialization.annotations.v2.Represented;
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
    @Represented(restorer = "zp")
    private ZpElement s;

    public FullIdentMasterSecret(ZpElement s) {
        this.s = s;
    }

    public FullIdentMasterSecret(Representation repr, FullIdentPublicParameters pp) {
        Zp zp = new Zp(pp.getGroupG1().size());
        new ReprUtil(this).register(zp, "zp").deserialize(repr);
    }

    @Override
    public Representation getRepresentation() {
        return ReprUtil.serialize(this);
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
