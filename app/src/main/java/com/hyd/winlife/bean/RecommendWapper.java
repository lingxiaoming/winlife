package com.hyd.winlife.bean;

import java.util.List;

/**
 * 赢生活美食推荐
 */
public class RecommendWapper {

    public List<Recommend> list;

    public class Recommend {
        public String brand_name;//品牌名称
        public String brand_desc;//品牌介绍
        public String logo_url;//品牌logo
        public String h5_url;//h5跳转链接
    }
}
