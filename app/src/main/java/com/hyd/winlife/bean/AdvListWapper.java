package com.hyd.winlife.bean;

import java.util.List;

/**
 * banner广告
 */
public class AdvListWapper {
	public List<AdvList> advInfo;


	public class AdvList {
		public String advName;//	广告位位置名称
		public List<HomeAd> advertise;//	广告位位置名称

		public class HomeAd {
			public long id;//广告图id
			public String image_filename;//广告图片
			public String url;//广告链接
			public boolean is_acct;//是否需要开户
			public boolean is_login;//是否需要登录
			public boolean is_link_enabled;//是否可以点击
		}
	}
}
