/**
 * Copyright (c) 2018, Mr.Wang (recallcode@aliyun.com) All rights reserved.
 */

package cn.recallcode.iot.mqtt.server.broker.protocol;

import cn.recallcode.iot.mqtt.server.broker.internal.InternalCommunication;
import cn.recallcode.iot.mqtt.server.common.auth.IAuthService;
import cn.recallcode.iot.mqtt.server.common.message.IDupPubRelMessageStoreService;
import cn.recallcode.iot.mqtt.server.common.message.IDupPublishMessageStoreService;
import cn.recallcode.iot.mqtt.server.common.message.IMessageIdService;
import cn.recallcode.iot.mqtt.server.common.message.IRetainMessageStoreService;
import cn.recallcode.iot.mqtt.server.common.session.ISessionStoreService;
import cn.recallcode.iot.mqtt.server.common.subscribe.ISubscribeStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 协议处理
 */
@Component
public class ProtocolProcess {

	@Autowired
	private ISessionStoreService sessionStoreService;

	@Autowired
	private ISubscribeStoreService subscribeStoreService;

	@Autowired
	private IAuthService authService;

	@Autowired
	private IMessageIdService messageIdService;

	@Autowired
	private IRetainMessageStoreService messageStoreService;

	@Autowired
	private IDupPublishMessageStoreService dupPublishMessageStoreService;

	@Autowired
	private IDupPubRelMessageStoreService dupPubRelMessageStoreService;

	@Autowired
	private InternalCommunication internalCommunication;

	private Connect connect;

	private Subscribe subscribe;

	private UnSubscribe unSubscribe;

	private Publish publish;

	private DisConnect disConnect;

	private PingReq pingReq;

	private PubRel pubRel;

	private PubAck pubAck;

	private PubRec pubRec;

	private PubComp pubComp;

	public Connect connect() {
		if (connect == null) {
			connect = new Connect(sessionStoreService, subscribeStoreService, dupPublishMessageStoreService, dupPubRelMessageStoreService, authService);
		}
		return connect;
	}

	public Subscribe subscribe() {
		if (subscribe == null) {
			subscribe = new Subscribe(subscribeStoreService, messageIdService, messageStoreService);
		}
		return subscribe;
	}

	public UnSubscribe unSubscribe() {
		if (unSubscribe == null) {
			unSubscribe = new UnSubscribe(subscribeStoreService);
		}
		return unSubscribe;
	}

	public Publish publish() {
		if (publish == null) {
			publish = new Publish(sessionStoreService, subscribeStoreService, messageIdService, messageStoreService, dupPublishMessageStoreService, internalCommunication);
		}
		return publish;
	}

	public DisConnect disConnect() {
		if (disConnect == null) {
			disConnect = new DisConnect(sessionStoreService, subscribeStoreService, dupPublishMessageStoreService, dupPubRelMessageStoreService);
		}
		return disConnect;
	}

	public PingReq pingReq() {
		if (pingReq == null) {
			pingReq = new PingReq();
		}
		return pingReq;
	}

	public PubRel pubRel() {
		if (pubRel == null) {
			pubRel = new PubRel();
		}
		return pubRel;
	}

	public PubAck pubAck() {
		if (pubAck == null) {
			pubAck = new PubAck(messageIdService, dupPublishMessageStoreService);
		}
		return pubAck;
	}

	public PubRec pubRec() {
		if (pubRec == null) {
			pubRec = new PubRec(dupPublishMessageStoreService, dupPubRelMessageStoreService);
		}
		return pubRec;
	}

	public PubComp pubComp() {
		if (pubComp == null) {
			pubComp = new PubComp(messageIdService, dupPubRelMessageStoreService);
		}
		return pubComp;
	}

	public ISessionStoreService getSessionStoreService() {
		return sessionStoreService;
	}

}
