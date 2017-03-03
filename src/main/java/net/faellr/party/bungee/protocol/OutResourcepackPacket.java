package net.faellr.party.bungee.protocol;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import io.netty.buffer.ByteBuf;
import net.faellr.party.api.exceptions.PartyException;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OutResourcepackPacket extends DefinedPacket {

    private String resourcepackURI;

    public OutResourcepackPacket(String resourcepackURI) {
        this.resourcepackURI = resourcepackURI;
    }

    @Override
    public void handle(AbstractPacketHandler abstractPacketHandler) throws Exception {
        // no idea what this is for
    }

    @Override
    public void read(ByteBuf buf) {
        resourcepackURI = readString(buf);
    }

    @Override
    public void write(ByteBuf buf) {
        writeString(getResourcepackURI(), buf);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(getResourcepackURI().getBytes("utf8"));
            byte[] digestBytes = digest.digest();

            String hashedString = DatatypeConverter.printHexBinary(digestBytes);
            writeString(hashedString, buf);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new PartyException("Exception while hashing. Please send this message to the developer", e);
        }
    }

    public String getResourcepackURI() {
        return resourcepackURI;
    }

    @Override
    public String toString() {
        return "OutResourcepackPacket{" +
                "resourcepackURI='" + resourcepackURI + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutResourcepackPacket that = (OutResourcepackPacket) o;

        return resourcepackURI.equals(that.resourcepackURI);
    }

    @Override
    public int hashCode() {
        return resourcepackURI.hashCode();
    }
}
