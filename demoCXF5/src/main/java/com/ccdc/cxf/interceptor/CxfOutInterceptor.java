package com.ccdc.cxf.interceptor;

import java.io.OutputStream;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.io.CachedOutputStreamCallback;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;

public class CxfOutInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger log = Logger.getLogger(CxfInInterceptor.class);
	
	private int limit = 102400;
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public CxfOutInterceptor() {
		super(Phase.PRE_STREAM);
	}
	
	@Override
	public void handleMessage(Message message) throws Fault {
		OutputStream os = (OutputStream) message.getContent(OutputStream.class);
		if (os == null) {
			return;
		}
		CacheAndWriteOutputStream newOut = new CacheAndWriteOutputStream(os);
		message.setContent(OutputStream.class, newOut);
		newOut.registerCallback(new LoggingCallback(message, os));
	}
	
	class LoggingCallback implements CachedOutputStreamCallback {
		private final Message message;
		private final OutputStream origStream;
 
		public LoggingCallback(Message msg, OutputStream os) {
			this.message = msg;
			this.origStream = os;
		}
 
		public void onFlush(CachedOutputStream cos) {
		}
 
		public void onClose(CachedOutputStream cos) {
			String id = (String) this.message.getExchange().get(LoggingMessage.ID_KEY);
			if (id == null) {
				id = LoggingMessage.nextId();
				this.message.getExchange().put(LoggingMessage.ID_KEY, id);
			}
			LoggingMessage buffer = new LoggingMessage("Outbound Message\n---------------------------", id);
 
			String encoding = (String) this.message.get(Message.ENCODING);
 
			if (encoding != null) {
				buffer.getEncoding().append(encoding);
			}
 
			String address = (String) this.message.get(Message.ENDPOINT_ADDRESS);
			if (address != null) {
				buffer.getAddress().append(address);
			}
			String ct = (String) this.message.get("Content-Type");
			if (ct != null) {
				buffer.getContentType().append(ct);
			}
			Object headers = this.message.get(Message.PROTOCOL_HEADERS);
			if (headers != null) {
				buffer.getHeader().append(headers);
			}
 
			if (cos.getTempFile() == null) {
				if (cos.size() > CxfOutInterceptor.this.limit)
					buffer.getMessage().append("(message truncated to " + CxfOutInterceptor.this.limit + " bytes)\n");
			} else {
				buffer.getMessage().append("Outbound Message (saved to tmp file):\n");
				buffer.getMessage().append("Filename: " + cos.getTempFile().getAbsolutePath() + "\n");
				if (cos.size() > CxfOutInterceptor.this.limit)
					buffer.getMessage().append("(message truncated to " + CxfOutInterceptor.this.limit + " bytes)\n");
			}
			try {
				cos.writeCacheTo(buffer.getPayload(), CxfOutInterceptor.this.limit);
			} catch (Exception ex) {
			}
			log.info("CxfOutInterceptor\n"+buffer.toString());
			try {
				cos.lockOutputStream();
				cos.resetOut(null, false);
			} catch (Exception ex) {
			}
			this.message.setContent(OutputStream.class, this.origStream);
		}
	}
}
