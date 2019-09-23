package com.project.utils;

import java.security.SecureRandom;

public class UUIDUtils {
	// 区域标志位数
	private final static long regionIdBits = 3L;
	// 机器标识位数
	private final static long workerIdBits = 10L;
	// 序列号识位数
	private final static long sequenceBits = 10L;

	// 区域标志ID最大值
	private final static long maxRegionId = -1L ^ (-1L << regionIdBits);
	// 机器ID最大值
	private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
	// 序列号ID最大值
	private final static long sequenceMask = -1L ^ (-1L << sequenceBits);

	// 机器ID偏左移10位
	private final static long workerIdShift = sequenceBits;
	// 业务ID偏左移20位
	private final static long regionIdShift = sequenceBits + workerIdBits;
	// 时间毫秒左移23位
	private final static long timestampLeftShift = sequenceBits + workerIdBits + regionIdBits;

	private static long lastTimestamp = -1L;

	private static long sequence = 0L;

	public synchronized static String nextId() {
		return nextId(1,1,false,1);
	}
	public synchronized static String nextId(long workerId, long regionId,boolean isPadding, long busId) {
		// 如果超出范围就抛出异常
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException("机器Id不能大于最大机器Id或者小于0");
		}
		if (regionId > maxRegionId || regionId < 0) {
			throw new IllegalArgumentException("区域Id不能大于最大区域Id或者小于0");
		}
		long timestamp = System.currentTimeMillis();
		long paddingNum = regionId;
		if (isPadding) {
			paddingNum = busId;
		}
		if (timestamp < lastTimestamp) {
			try {
				throw new RuntimeException("时钟倒退，拒绝生成Id " + (lastTimestamp - timestamp)
						+ " 毫秒");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 如果上次生成时间和当前时间相同,在同一毫秒内
		if (lastTimestamp == timestamp) {
			// sequence自增，因为sequence只有10bit，所以和sequenceMask相与一下，去掉高位
			sequence = (sequence + 1) & sequenceMask;
			// 判断是否溢出,也就是每毫秒内超过1024，当为1024时，与sequenceMask相与，sequence就等于0
			if (sequence == 0) {
				// 自旋等待到下一毫秒
				timestamp = tailNextMillis(lastTimestamp);
			}
		} else {
			// 如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加,
			// 为了保证尾数随机性更大一些,最后一位设置一个随机数
			sequence = new SecureRandom().nextInt(10);
		}
		lastTimestamp = timestamp;
		// 基准时间
		long baseTime = 1288834974657L;
		return String.valueOf(((timestamp - baseTime) << timestampLeftShift) | (paddingNum << regionIdShift)
				| (workerId << workerIdShift) | sequence);
	}
	// 防止产生的时间比之前的时间还要小（由于NTP回拨等问题）,保持增量的趋势.
	private static long tailNextMillis(final long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}
}
