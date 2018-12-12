/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.hzkans.crm.modules.wechat.entity;

import com.hzkans.crm.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


/**
 * 微信公众号自动回复Entity
 * @author dtm
 * @version 2018-12-05
 */
public class WechatReplyNew extends DataEntity<WechatReplyNew> {

	private static final long serialVersionUID = 1L;
	private Integer wechatId;		// 微信ID
	private String ruleId;		// 规则ID
	private Integer keywordType;		// 关键词类型： 0-半匹配  1-全匹配
	private String keyword;		// 关键词
	private Integer status;		// 回复：0-关闭回复 1.开启回复
	private String ruleName;		// 规则名称
	private Integer ruleType;		// 类型：1-关键词回复 2-收到消息回复  3-被关注回复
	private Integer replyWay;		// 回复方式：1-全部回复 2-随机回复一条 关键字回复不能为空
	private String creator;		// 创建者
	private String updator;		// 修改者
	private Integer deleted;		// 0.存在1.删除
	private Integer contentType;		// 回复消息内容类型：0.文字回复1.图片 2-语音 3-视频 4-图文 5-自定义
	private String content;		// 回复消息内容

	List<WechatReplyContentDO> wechatReplyContentDOS;

	List<WechatReplyKeywordDO> wechatReplyKeywordDOS;

	public List<WechatReplyContentDO> getWechatReplyContentDOS() {
		return wechatReplyContentDOS;
	}

	public void setWechatReplyContentDOS(List<WechatReplyContentDO> wechatReplyContentDOS) {
		this.wechatReplyContentDOS = wechatReplyContentDOS;
	}

	public List<WechatReplyKeywordDO> getWechatReplyKeywordDOS() {
		return wechatReplyKeywordDOS;
	}

	public void setWechatReplyKeywordDOS(List<WechatReplyKeywordDO> wechatReplyKeywordDOS) {
		this.wechatReplyKeywordDOS = wechatReplyKeywordDOS;
	}

	public Integer getWechatId() {
		return wechatId;
	}

	public void setWechatId(Integer wechatId) {
		this.wechatId = wechatId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(Integer keywordType) {
		this.keywordType = keywordType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public Integer getReplyWay() {
		return replyWay;
	}

	public void setReplyWay(Integer replyWay) {
		this.replyWay = replyWay;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}