package org.hibernate.bugs;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table( name = "messages" )
public class ChannelMessage {

	@Id
	@GeneratedValue( strategy = GenerationType.SEQUENCE )
	private long id;

	@Generated
	@Column( name = "channel_id", nullable = false, insertable = false, updatable = false )
	private long channelId;

	@ManyToOne( fetch = FetchType.LAZY )
	private ChatChannel channel;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public ChatChannel getChannel() {
		return channel;
	}

	public void setChannel(ChatChannel channel) {
		this.channel = channel;
	}
}
