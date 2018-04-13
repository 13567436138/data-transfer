package com.youben.batch.processor;

import com.youben.batch.entity.Message;
import com.youben.batch.entity.ProcessedMessage;
import org.springframework.batch.item.ItemProcessor;
/*
*hxp(hxpwangyi@126.com)
*
*/
public class MessageItemProcessor implements ItemProcessor<Message,ProcessedMessage> {

	public ProcessedMessage process(Message message) throws Exception {
		ProcessedMessage pm=new ProcessedMessage();
		pm.setContent(message.getContent()+"processed");
		pm.setMessageId(message.getMessageId());
		pm.setReceiver(message.getReceiver());
		pm.setReceiveTime(message.getReceiveTime());
		pm.setType(2);
		return pm;
	}

}
