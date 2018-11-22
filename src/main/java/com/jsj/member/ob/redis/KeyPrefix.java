package com.jsj.member.ob.redis;

public interface KeyPrefix {

	public int expireSeconds();

	public String getPrefix();

}
