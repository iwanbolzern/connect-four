package prg2.connectfour.comlayer;

import java.nio.charset.Charset;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

public abstract class PayLoad {
	
	public int CMD_ID = -1;
	
	public PayLoad parse(byte[] payLoad, Class<? extends PayLoad> clazz, Charset charset) {
		String stringPayLoad = new String(payLoad, charset);
		Gson gson = new Gson();
		return gson.fromJson(stringPayLoad, clazz);
	}

	public byte[] getBytes(Charset charset) {
		Gson gson = new Gson();
		return gson.toJson(this).getBytes(charset);
	}
}
