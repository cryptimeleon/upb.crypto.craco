package de.upb.crypto.craco.ser.standalone;

import de.upb.crypto.craco.kem.StreamingHybridEncryptionScheme;
import de.upb.crypto.craco.secretsharing.shamir.ShamirSecretSharingSchemeProvider;
import de.upb.crypto.craco.ser.standalone.params.*;
import de.upb.crypto.math.serialization.Representation;
import de.upb.crypto.math.serialization.StandaloneRepresentable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(value = Parameterized.class)
public class StandaloneTest {

    private Class<? extends StandaloneRepresentable> toTest;
    private Object instance;

    public StandaloneTest(StandaloneTestParams params) {
        this.toTest = params.toTest;
        this.instance = params.instance;
    }

    @Test
    public void testForConstructor() {
        System.out.println("Testing if a correct constructor exists for " + toTest.getCanonicalName());
        // Test for constructor
        try {
            // tries to get the constructor that has Representation as class
            // parameters
            Constructor<? extends StandaloneRepresentable> c = toTest.getConstructor(Representation.class);
            assertNotNull(c);
        } catch (NoSuchMethodException | SecurityException e) {
            // no constructor given or constructor not visible
            fail();
        }
    }

    @Test
    public void checkForOverrideEquals() {
        System.out.println("Testing if " + toTest.getCanonicalName() + " overwrites equals");
        // checks if all classes overwrite the equals method
        try {
            Method equals = toTest.getMethod("equals", Object.class);
            // this is maybe not enough since it only asserts that any super
            // class overwrites equals
            assertNotEquals(equals.getDeclaringClass(), Object.class);
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }

    @Test
    public void checkForOverrideHashCode() {
        System.out.println("Testing if " + toTest.getCanonicalName() + " overwrites hashCode");
        try {
            Method hashCode = toTest.getMethod("hashCode");
            // this is maybe not enough since it only asserts that any super
            // class overwrites hashcode
            assertNotEquals(hashCode.getDeclaringClass(), Object.class);
        } catch (NoSuchMethodException | SecurityException e) {
            fail();
        }
    }

    @Test
    public void testRecreateRepresentable()
            throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        System.out.println("Testing if serialization / deserialization is correct for " + toTest.getCanonicalName());
        // tests whether the deserialization of the serialized object equals the
        // original object
        if (instance == null) {
            fail("Failed to test " + toTest.getName());
        } else {
            Constructor<? extends StandaloneRepresentable> c = toTest.getConstructor(Representation.class);
            assertNotNull(c);
            Representation repr = (Representation) toTest.getMethod("getRepresentation").invoke(instance);
            assertEquals(instance, c.newInstance(repr));
        }
    }

    @Parameters(name = "{index}: {0}")
    public static Collection<StandaloneTestParams> getStandaloneClasses() {
        Reflections reflection = new Reflections("de.upb.crypto.craco");
        // get all classes that are subtypes of standalone representable
        Set<Class<? extends StandaloneRepresentable>> classes = reflection.getSubTypesOf(StandaloneRepresentable.class);
        ArrayList<StandaloneTestParams> toReturn = new ArrayList<>();
        // add de.upb.crypto.groupsig.params here
        System.out.println("Creating objects that will be serialized...");
        toReturn.add(SetOfAttributeParams.get());
        toReturn.add(ThresholdTreeSecretSharingParams.get());
        toReturn.add(StringAttributeParams.get());
        toReturn.add(BigIntegerAttributeParams.get());
        toReturn.add(ElgamalEncryptionParams.get());
        toReturn.add(StreamingGCMAESParams.get());
        toReturn.add(StreamingCBCAESParams.get());
        toReturn.add(StreamingGCMAESPacketModeParams.get());
        toReturn.add(RingElementAttributeParams.get());
        toReturn.add(SnParams.get());
        toReturn.addAll(ElgamalKEMParams.get());
        toReturn.addAll(PSTestParams.get());
        toReturn.addAll(SPSEQParams.get());
        toReturn.add(BooleanPolicyParams.get());
        toReturn.add(ByteArrayImplementationParams.get());
        toReturn.add(HashBasedKDFParams.get());
        toReturn.addAll(BBSBParams.get());
        toReturn.add(AesPseudorandomFunction.get());
        toReturn.add(new StandaloneTestParams(ShamirSecretSharingSchemeProvider.class, new
                ShamirSecretSharingSchemeProvider()));
        toReturn.add(ThresholdPolicyParams.get());
        toReturn.addAll(PS18ROMSignatureParams.get());
        toReturn.addAll(PS18SignatureParams.get());
        toReturn.addAll(PS18SignaturePrecParams.get());

        // clarc
        toReturn.addAll(CommitmentSchemeParams.get());
        toReturn.addAll(PSSignatureParams.get());
        toReturn.addAll(AccumulatorParams.get());

        System.out.println("Finished creating objects...");
        // remove all provided de.upb.crypto.groupsig.params
        for (StandaloneTestParams stp : toReturn) {
            classes.remove(stp.toTest);
        }
        // remove classes that are tested elsewhere
        classes.remove(StreamingHybridEncryptionScheme.class); // This is tested in upb.crypto.predenc since ABE is used
        // add remaining classes
        for (Class<? extends StandaloneRepresentable> c : classes) {
            if (!c.isInterface() && !Modifier.isAbstract(c.getModifiers()) && !(c.getPackage().toString().startsWith
                    ("package de.upb.crypto.math"))) {
                toReturn.add(new StandaloneTestParams(c, null));
            }
        }

        return toReturn;
    }
}