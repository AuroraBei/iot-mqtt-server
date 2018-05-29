/**
 * Copyright (c) 2018, Mr.Wang (recallcode@aliyun.com) All rights reserved.
 */

package cn.recallcode.iot.mqtt.server.broker.protocol;

import cn.recallcode.iot.mqtt.server.common.message.IDupPubRelMessageStoreService;
import cn.recallcode.iot.mqtt.server.common.message.IDupPublishMessageStoreService;
import cn.recallcode.iot.mqtt.server.common.session.ISessionStoreService;
import cn.recallcode.iot.mqtt.server.common.session.SessionStore;
import cn.recallcode.iot.mqtt.server.common.subscribe.ISubscribeStoreService;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DISCONNECT连接处理
 */
public class DisConnect {

	private static final Logger LOGGER = LoggerFactory.getLogger(DisConnect.class);

	private ISessionStoreService sessionStoreService;

	private ISubscribeStoreService subscribeStoreService;

	private IDupPublishMessageStoreService dupPublishMessageStoreService;

	private IDupPubRelMessageStoreService dupPubRelMessageStoreService;

	public DisConnect(ISessionStoreService sessionStoreService, ISubscribeStoreService subscribeStoreService, IDupPublishMessageStoreService dupPublishMessageStoreService, IDupPubRelMessageStoreService dupPubRelMessageStoreService) {
		this.sessionStoreService = sessionStoreService;
		this.subscribeStoreService = subscribeStoreService;
		this.dupPublishMessageStoreService = dupPublishMessageStoreService;
		this.dupPubRelMessageStoreService = dupPubRelMessageStoreService;
	}

	public void processDisConnect(Channel channel, MqttMessage msg) {
		String clientId = (String) channel.attr(AttributeKey.valueOf("clientId")).get();
		SessionStore sessionStore = sessionStoreService.get(clientId);
		if (sessionStore.isCleanSession()) {
			subscribeStoreService.removeForClient(clientId);
			dupPublishMessageStoreService.removeByClient(clientId);
			dupPubRelMessageStoreService.removeByClient(clientId);
		}
		LOGGER.debug("DISCONNECT - clientId: {}, cleanSession: {}", clientId, sessionStore.isCleanSession());
		sessionStoreService.remove(clientId);
		channel.close();
	}

}
