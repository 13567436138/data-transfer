package com.youben.batch.batchMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.youben.batch.entity.Message;
import org.springframework.jdbc.core.RowMapper;


/*
*hxp(hxpwangyi@126.com)
*
*/
public class MessageRowMapper implements RowMapper<Message> {

	public Message mapRow(ResultSet rs, int i) throws SQLException {
		Message message=new Message();
		message.setContent(rs.getString("content"));
		message.setMessageId(rs.getLong("messageId"));
		message.setReceiver(rs.getString("receiver"));
		message.setReceiveTime(new Date(rs.getLong("receiveTime")));
		message.setType(rs.getInt("type"));
		return message;
	}

}
