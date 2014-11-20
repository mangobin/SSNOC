package edu.cmu.sv.ws.ssnoc.data.po;

import java.util.Date;

public class MessagePO {

	private long messageId;
	
	private String content;
	
	private long author;
	
	private long target;
	
	private String messageType;
	
	private Date postedAt;

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getAuthor() {
		return author;
	}

	public void setAuthor(long author) {
		this.author = author;
	}

	public long getTarget() {
		return target;
	}

	public void setTarget(long target) {
		this.target = target;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}
	
//	@Override
//	public boolean equals(Object o){
//		// Return true if the objects are identical.
//	     // (This is just an optimization, not required for correctness.)
//	     if (this == o) {
//	       return true;
//	     }
//
//	     // Return false if the other object has the wrong type.
//	     // This type may be an interface depending on the interface's specification.
//	     if (!(o instanceof MessagePO)) {
//	       return false;
//	     }
//
//	     // Cast to the appropriate type.
//	     // This will succeed because of the instanceof, and lets us access private fields.
//	     MessagePO lhs = (MessagePO) o;
//	     
//	     return this.content.equals(lhs.getContent()) 
//	    		 && this.author == lhs.getAuthor()
//	    		 && this.messageType.equals(lhs.getMessageType())
//	    		 && this.postedAt.equals(lhs.getPostedAt())
//	    		 && this.target == lhs.getTarget();
//	}
	
}
