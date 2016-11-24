package prg2.connectfour.utils;

import java.awt.image.ImagingOpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Utils {

	public static <T> T[] joinArray(T[]... arrays) {
		int length = 0;
		for (T[] array : arrays) {
			length += array.length;
		}

		// T[] result = new T[length];
		final T[] result = (T[]) Array.newInstance(arrays[0].getClass().getComponentType(), length);

		int offset = 0;
		for (T[] array : arrays) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}

		return result;
	}
	
	public static byte[] joinArray(byte[]... arrays) {
		int length = 0;
		for (byte[] array : arrays) {
			length += array.length;
		}
		
		byte[] result = new byte[length];
		
		int offset = 0;
		for (byte[] array : arrays) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}

		return result;
	}

	public static byte[] getByteArrayFromObject(Serializable serializable) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(serializable);
			out.flush();
			return bos.toByteArray();
		} catch (IOException ex) {
			System.err.println("Could not get bytes of serializable");
		} finally {
			try {
				bos.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return null;
	}
	
	public static Object getObjectFromByteArray(byte[] bytes) throws ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		try {
		  in = new ObjectInputStream(bis);
		return in.readObject();
		} catch (IOException ex) {
			System.err.println("Could not get object of bytes");
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

	public static String generateSecureToken() {
		 SecureRandom random = new SecureRandom();
		 return new BigInteger(130, random).toString(32);
	}
}
