package dtnperf.client;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import it.unibo.dtn.JAL.Bundle;

class ClientDataMode extends ClientMode {

	private final byte[] payloadCache;
	
	private final long dataToSend;
	private long dataSent;
	
	protected ClientDataMode(Client client, DataMode dataMode) {
		super(client);
		
		this.dataToSend = dataMode.getMaxData();
		this.dataSent = 0;
		
		this.payloadCache = new byte[this.getClient().getPayloadSize()];
		Arrays.fill(this.payloadCache, StandardCharsets.UTF_8.encode("X").array()[0]);
	}

	@Override
	public byte[] getPayloadData() {
		return payloadCache;
	}

	@Override
	public boolean isTerminated() {
		return (this.dataSent + this.getClient().getPayloadSize() > this.dataToSend); // If dataSent + a new bundle (payloadSize) overflows dataToSend
	}

	@Override
	protected void bundleSent(Bundle bundle) {
		this.dataSent += bundle.getPayload().getData().length;
	}

	@Override
	public dtnperf.header.ClientMode getClientMode() {
		return dtnperf.header.ClientMode.Data;
	}

}
