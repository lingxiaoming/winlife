package com.hyd.winlife.bean;

import java.util.HashMap;
import java.util.List;

/**
 * 模块分类
 */
public class MoudleWapper {
	public HashMap<String, List<Modules>> moduleInfo;

	public class Modules {
		public int id; // app模块id
		public String title; // 标题
		public String icon; // 图标
		public String link; // 链接(link_type为1时提供h5的链接)
		public boolean is_login; // 是否需要用户登录(0 否 1是)
		public boolean is_open_account;//是否需要开户
	}

}
