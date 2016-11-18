package prg2.connectfour.comlayer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class ComLayerConfig {
	
	private Charset usedCharset = StandardCharsets.US_ASCII;
	
	public void setUsedCharset(Charset usedCharset) {
		this.usedCharset = usedCharset;
	}

	public Charset getUsedCharset() {
		return this.usedCharset;
	}

}
