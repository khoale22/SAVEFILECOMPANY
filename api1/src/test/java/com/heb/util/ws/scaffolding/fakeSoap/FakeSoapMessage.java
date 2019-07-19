package com.heb.util.ws.scaffolding.fakeSoap;

import javax.xml.soap.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * This class emulates a SOAPMessage to be able to test functions in the utl.com.heb.util.ws package
 *
 * Created by d116773 on 2/15/2016.
 */
public class FakeSoapMessage extends SOAPMessage {

	public static final String TEST_MESSGE = "THIS IS A TEST MESSAGE";

	@Override
	public void writeTo(OutputStream out) throws SOAPException, IOException {

		out.write(FakeSoapMessage.TEST_MESSGE.getBytes());

	}


	// everything past this is unimplemented

	@Override
	public void setContentDescription(String description) {

	}

	@Override
	public String getContentDescription() {
		return null;
	}

	@Override
	public SOAPPart getSOAPPart() {
		return null;
	}

	@Override
	public void removeAllAttachments() {

	}

	@Override
	public int countAttachments() {
		return 0;
	}

	@Override
	public Iterator getAttachments() {
		return null;
	}

	@Override
	public Iterator getAttachments(MimeHeaders headers) {
		return null;
	}

	@Override
	public void removeAttachments(MimeHeaders headers) {

	}

	@Override
	public AttachmentPart getAttachment(SOAPElement element) throws SOAPException {
		return null;
	}

	@Override
	public void addAttachmentPart(AttachmentPart AttachmentPart) {

	}

	@Override
	public AttachmentPart createAttachmentPart() {
		return null;
	}

	@Override
	public MimeHeaders getMimeHeaders() {
		return null;
	}

	@Override
	public void saveChanges() throws SOAPException {

	}

	@Override
	public boolean saveRequired() {
		return false;
	}


}
