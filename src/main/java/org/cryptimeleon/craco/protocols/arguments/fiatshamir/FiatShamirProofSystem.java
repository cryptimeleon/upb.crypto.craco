package org.cryptimeleon.craco.protocols.arguments.fiatshamir;

import org.cryptimeleon.craco.protocols.CommonInput;
import org.cryptimeleon.craco.protocols.SecretInput;
import org.cryptimeleon.craco.protocols.arguments.sigma.*;
import org.cryptimeleon.math.hash.HashFunction;
import org.cryptimeleon.math.hash.impl.ByteArrayAccumulator;
import org.cryptimeleon.math.hash.impl.VariableOutputLengthHashFunction;
import org.cryptimeleon.math.serialization.Representation;

public class FiatShamirProofSystem {
    private final SigmaProtocol protocol;

    public FiatShamirProofSystem(SigmaProtocol protocol) {
        this.protocol = protocol;
    }

    public FiatShamirProof createProof(CommonInput commonInput, SecretInput secretInput, byte[] additionalData) {
        AnnouncementSecret announcementSecret = protocol.generateAnnouncementSecret(commonInput, secretInput);
        Announcement announcement = protocol.generateAnnouncement(commonInput, secretInput, announcementSecret);
        Challenge challenge = computeChallengeForAnnouncement(commonInput, announcement, additionalData);
        Response response = protocol.generateResponse(commonInput, secretInput, announcement, announcementSecret, challenge);

        return new FiatShamirProof(protocol.compressTranscript(commonInput, new SigmaProtocolTranscript(announcement, challenge, response)), challenge);
    }

    public FiatShamirProof createProof(CommonInput commonInput, SecretInput secretInput) {
        return createProof(commonInput, secretInput, new byte[0]);
    }

    public boolean checkProof(CommonInput commonInput, FiatShamirProof proof, byte[] additionalData) {
        SigmaProtocolTranscript transcript = protocol.decompressTranscript(commonInput, proof.challenge, proof.compressedTranscript);
        return computeChallengeForAnnouncement(commonInput, transcript.getAnnouncement(), additionalData).equals(transcript.getChallenge());
    }

    public boolean checkProof(CommonInput commonInput, FiatShamirProof proof) {
        return checkProof(commonInput, proof, new byte[0]);
    }

    private Challenge computeChallengeForAnnouncement(CommonInput commonInput, Announcement announcement, byte[] additionalData) {
        ByteArrayAccumulator acc = new ByteArrayAccumulator();
        acc.escapeAndSeparate(announcement);
        acc.append(additionalData);
        return protocol.getChallengeSpace(commonInput).hashIntoChallengeSpace(acc.extractBytes());
    }

    public FiatShamirProof restoreProof(CommonInput commonInput, Representation repr) {
        return new FiatShamirProof(repr.obj().get("transcript"), protocol.restoreChallenge(commonInput, repr.obj().get("challenge")));
    }
}
