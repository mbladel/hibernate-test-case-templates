package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "message_polling_queue" )
public class MessagePollingQueue {
	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE )
	private Long revision;

	@Column( insertable = false, updatable = false )
	private long messageId;

	@Column( insertable = false, updatable = false )
	private long channelId;

	@ManyToOne
	@JoinColumns( {
			@JoinColumn( name = "channelId", referencedColumnName = "channel_id" ),
			@JoinColumn( name = "messageId", referencedColumnName = "id" )
	} )
	private ChannelMessage message;

	public Long getRevision() {
		return revision;
	}

	public void setRevision(Long revision) {
		this.revision = revision;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public ChannelMessage getMessage() {
		return message;
	}

	public void setMessage(ChannelMessage message) {
		this.message = message;
	}
}
