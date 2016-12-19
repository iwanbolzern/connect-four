package prg2.connectfour.utils;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Various utilities
 *
 * @author Iwan Bolzern {@literal <iwan.bolzern@ihomelab.ch>}
 */
public class Utils {
    /**
     * Serializes a given object into a byte array
     *
     * @param serializable Object
     * @return Bytes
     */
    public static byte[] getByteArrayFromObject(Serializable serializable) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(serializable);
            out.flush();
            return bos.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    /**
     * Deserializes a byte array into an object
     *
     * @param bytes Bytes
     * @return Object
     * @throws ClassNotFoundException Class is not known
     */
    public static Object getObjectFromByteArray(byte[] bytes) throws ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    /**
     * Generates a secure random token
     *
     * @return Secure token
     */
    public static String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
