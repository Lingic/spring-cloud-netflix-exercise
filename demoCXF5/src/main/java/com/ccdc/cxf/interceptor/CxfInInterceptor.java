package com.ccdc.cxf.interceptor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingMessage;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.log4j.Logger;

public class CxfInInterceptor extends AbstractPhaseInterceptor<Message> {

	private static final Logger log = Logger.getLogger(CxfInInterceptor.class);
	
	private int limit = 102400;
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public CxfInInterceptor() {
		super(Phase.RECEIVE);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		logging(message);
	}
	
	private void logging(Message message) {
		if (message.containsKey(LoggingMessage.ID_KEY)) {
			return;
		}
		String id = (String) message.getExchange().get(LoggingMessage.ID_KEY);
		if (id == null) {
			id = LoggingMessage.nextId();
			message.getExchange().put(LoggingMessage.ID_KEY, id);
		}
		message.put(LoggingMessage.ID_KEY, id);
		LoggingMessage buffer = new LoggingMessage("Inbound Message\n----------------------------", id);
 
		String encoding = (String) message.get(Message.ENCODING);
 
		if (encoding != null) {
			buffer.getEncoding().append(encoding);
		}
		String ct = (String) message.get("Content-Type");
		if (ct != null) {
			buffer.getContentType().append(ct);
		}
		Object headers = message.get(Message.PROTOCOL_HEADERS);
 
		if (headers != null) {
			buffer.getHeader().append(headers);
		}
		String uri = (String) message.get(Message.REQUEST_URI);
		if (uri != null) {
			buffer.getAddress().append(uri);
		}
 
		InputStream is = (InputStream) message.getContent(InputStream.class);
		if (is != null) {
			CachedOutputStream bos = new CachedOutputStream();
			try {
				IOUtils.copy(is, bos);
 
				bos.flush();
				is.close();
 
				message.setContent(InputStream.class, bos.getInputStream());
				if (bos.getTempFile() != null) {
					buffer.getMessage().append("\nMessage (saved to tmp file):\n");
					buffer.getMessage().append("Filename: " + bos.getTempFile().getAbsolutePath() + "\n");
				}
				if (bos.size() > this.limit) {
					buffer.getMessage().append("(message truncated to " + this.limit + " bytes)\n");
				}
				bos.writeCacheTo(buffer.getPayload(), this.limit);
 
				bos.close();
			} catch (IOException e) {
				throw new Fault(e);
			}
		}
		log.info("CxfInInterceptor\n"+buffer.toString());
	}
	
	public void handleFault(Message message) {
		Exception exeption = message.getContent(Exception.class);
		log.info(exeption.getMessage());
	}
}
