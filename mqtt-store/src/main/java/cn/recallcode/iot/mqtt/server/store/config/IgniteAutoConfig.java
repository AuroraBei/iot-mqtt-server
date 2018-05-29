/**
 * Copyright (c) 2018, Mr.Wang (recallcode@aliyun.com) All rights reserved.
 */

package cn.recallcode.iot.mqtt.server.store.config;

import cn.hutool.core.util.StrUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.slf4j.Slf4jLogger;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置apache ignite
 */
@Configuration
@SuppressWarnings("unchecked")
public class IgniteAutoConfig {

	@Value("${spring.mqtt.broker.id}")
	private String instanceName;

	@Value("${spring.mqtt.broker.multicast-group}")
	private String multicastGroup;

	@Bean
	public IgniteProperties igniteProperties() {
		return new IgniteProperties();
	}

	@Bean
	public Ignite ignite() throws Exception {
		IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
		// Ignite实例名称
		igniteConfiguration.setIgniteInstanceName(instanceName);
		// Ignite日志
		Logger logger = LoggerFactory.getLogger("org.apache.ignite");
		igniteConfiguration.setGridLogger(new Slf4jLogger(logger));
		// 非持久化数据区域
		DataRegionConfiguration notPersistence = new DataRegionConfiguration().setPersistenceEnabled(false)
			.setInitialSize(igniteProperties().getNotPersistenceInitialSize() * 1024 * 1024)
			.setMaxSize(igniteProperties().getNotPersistenceMaxSize() * 1024 * 1024).setName("not-persistence-data-region");
		// 持久化数据区域
		DataRegionConfiguration persistence = new DataRegionConfiguration().setPersistenceEnabled(true)
			.setInitialSize(igniteProperties().getPersistenceInitialSize() * 1024 * 1024)
			.setMaxSize(igniteProperties().getPersistenceMaxSize() * 1024 * 1024).setName("persistence-data-region");
		DataStorageConfiguration dataStorageConfiguration = new DataStorageConfiguration().setDefaultDataRegionConfiguration(notPersistence)
			.setDataRegionConfigurations(persistence)
			.setWalArchivePath(StrUtil.isNotBlank(igniteProperties().getPersistenceStorePath()) ? igniteProperties().getPersistenceStorePath() : null)
			.setWalPath(StrUtil.isNotBlank(igniteProperties().getPersistenceStorePath()) ? igniteProperties().getPersistenceStorePath() : null)
			.setStoragePath(StrUtil.isNotBlank(igniteProperties().getPersistenceStorePath()) ? igniteProperties().getPersistenceStorePath() : null);
		igniteConfiguration.setDataStorageConfiguration(dataStorageConfiguration);
		// 集群, 基于组播配置
		TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
		TcpDiscoveryMulticastIpFinder tcpDiscoveryMulticastIpFinder = new TcpDiscoveryMulticastIpFinder();
		tcpDiscoveryMulticastIpFinder.setMulticastGroup(multicastGroup);
		tcpDiscoverySpi.setIpFinder(tcpDiscoveryMulticastIpFinder);
		igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi);
		Ignite ignite = Ignition.start(igniteConfiguration);
		ignite.cluster().active(true);
		return ignite;
	}

	@Bean
	public IgniteCache messageIdCache() throws Exception {
		CacheConfiguration cacheConfiguration = new CacheConfiguration().setDataRegionName("not-persistence-data-region")
			.setCacheMode(CacheMode.PARTITIONED).setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL).setName("messageIdCache");
		return ignite().getOrCreateCache(cacheConfiguration);
	}

	@Bean
	public IgniteCache retainMessageCache() throws Exception {
		CacheConfiguration cacheConfiguration = new CacheConfiguration().setDataRegionName("persistence-data-region")
			.setCacheMode(CacheMode.PARTITIONED).setName("retainMessageCache");
		return ignite().getOrCreateCache(cacheConfiguration);
	}

	@Bean
	public IgniteCache subscribeNotWildcardCache() throws Exception {
		CacheConfiguration cacheConfiguration = new CacheConfiguration().setDataRegionName("persistence-data-region")
			.setCacheMode(CacheMode.PARTITIONED).setName("subscribeNotWildcardCache");
		return ignite().getOrCreateCache(cacheConfiguration);
	}

	@Bean
	public IgniteCache subscribeWildcardCache() throws Exception {
		CacheConfiguration cacheConfiguration = new CacheConfiguration().setDataRegionName("persistence-data-region")
			.setCacheMode(CacheMode.PARTITIONED).setName("subscribeWildcardCache");
		return ignite().getOrCreateCache(cacheConfiguration);
	}

	@Bean
	public IgniteCache dupPublishMessageCache() throws Exception {
		CacheConfiguration cacheConfiguration = new CacheConfiguration().setDataRegionName("persistence-data-region")
			.setCacheMode(CacheMode.PARTITIONED).setName("dupPublishMessageCache");
		return ignite().getOrCreateCache(cacheConfiguration);
	}

	@Bean
	public IgniteCache dupPubRelMessageCache() throws Exception {
		CacheConfiguration cacheConfiguration = new CacheConfiguration().setDataRegionName("persistence-data-region")
			.setCacheMode(CacheMode.PARTITIONED).setName("dupPubRelMessageCache");
		return ignite().getOrCreateCache(cacheConfiguration);
	}

	@Bean
	public IgniteMessaging igniteMessaging() throws Exception {
		return ignite().message(ignite().cluster().forRemotes());
	}

}
