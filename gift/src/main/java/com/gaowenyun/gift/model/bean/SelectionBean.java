package com.gaowenyun.gift.model.bean;

import java.util.List;

/**
 *
 *
 */
public class SelectionBean {
    /**
     * code : 200
     * data : {"banners":[{"ad_monitors":[],"channel":"all","id":715,"image_url":"http://img01.liwushuo.com/image/160907/2e7nf9evv.jpg-w720","order":400,"status":0,"target_id":1045582,"target_type":"url","target_url":"liwushuo:///page?page_action=navigation&login=false&type=post&post_id=1045582","type":"post","webp_url":"http://img01.liwushuo.com/image/160907/2e7nf9evv.jpg?imageView2/2/w/720/q/85/format/webp"},{"ad_monitors":[],"channel":"all","id":712,"image_url":"http://img03.liwushuo.com/image/160912/w27nffhwn.jpg-w720","order":399,"status":0,"target_id":null,"target_url":"liwushuo:///page?type=shop_item&item_id=100895","type":"url","webp_url":"http://img03.liwushuo.com/image/160912/w27nffhwn.jpg?imageView2/2/w/720/q/85/format/webp"},{"ad_monitors":[],"channel":"all","id":718,"image_url":"http://img03.liwushuo.com/image/160912/oc9jytqbo.jpg-w720","order":392,"status":0,"target_id":1045671,"target_type":"url","target_url":"liwushuo:///page?page_action=navigation&login=false&type=post&post_id=1045671","type":"post","webp_url":"http://img03.liwushuo.com/image/160912/oc9jytqbo.jpg?imageView2/2/w/720/q/85/format/webp"},{"ad_monitors":[],"channel":"all","id":713,"image_url":"http://img01.liwushuo.com/image/160906/4aco2fhmd.jpg-w720","order":390,"status":0,"target_id":1045589,"target_type":"url","target_url":"liwushuo:///page?page_action=navigation&login=false&type=post&post_id=1045589","type":"post","webp_url":"http://img01.liwushuo.com/image/160906/4aco2fhmd.jpg?imageView2/2/w/720/q/85/format/webp"},{"ad_monitors":[],"channel":"all","id":716,"image_url":"http://img03.liwushuo.com/image/160908/a0h3m4p1p.jpg-w720","order":360,"status":0,"target":{"banner_image_url":"http://img01.liwushuo.com/image/160908/azurj77e8.jpg-w300","banner_webp_url":"http://img01.liwushuo.com/image/160908/azurj77e8.jpg?imageView2/2/w/300/q/85/format/webp","cover_image_url":"http://img02.liwushuo.com/image/160908/0d2ckm3rj.jpg-w720","cover_webp_url":"http://img02.liwushuo.com/image/160908/0d2ckm3rj.jpg?imageView2/2/w/720/q/85/format/webp","created_at":1473329032,"id":351,"posts_count":8,"status":1,"subtitle":"贴心宿舍神器出没，一起来\u201c变形记\u201d。","title":"贴心宿舍神器","updated_at":1473388134},"target_id":351,"target_type":"url","target_url":"liwushuo:///page?page_action=navigation&login=false&type=topic&topic_id=351","type":"collection","webp_url":"http://img03.liwushuo.com/image/160908/a0h3m4p1p.jpg?imageView2/2/w/720/q/85/format/webp"},{"ad_monitors":[],"channel":"all","id":705,"image_url":"http://img03.liwushuo.com/image/160901/2sm8iy4n4.jpg-w720","order":280,"status":0,"target_id":1045432,"target_type":"url","target_url":"liwushuo:///page?page_action=navigation&login=false&type=post&post_id=1045432","type":"post","webp_url":"http://img03.liwushuo.com/image/160901/2sm8iy4n4.jpg?imageView2/2/w/720/q/85/format/webp"},{"ad_monitors":[],"channel":"all","id":711,"image_url":"http://img01.liwushuo.com/image/160905/sfgmt79zc.jpg-w720","order":200,"status":0,"target_id":1045559,"target_type":"url","target_url":"liwushuo:///page?page_action=navigation&login=false&type=post&post_id=1045559","type":"post","webp_url":"http://img01.liwushuo.com/image/160905/sfgmt79zc.jpg?imageView2/2/w/720/q/85/format/webp"}]}
     * message : OK
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * ad_monitors : []
         * channel : all
         * id : 715
         * image_url : http://img01.liwushuo.com/image/160907/2e7nf9evv.jpg-w720
         * order : 400
         * status : 0
         * target_id : 1045582
         * target_type : url
         * target_url : liwushuo:///page?page_action=navigation&login=false&type=post&post_id=1045582
         * type : post
         * webp_url : http://img01.liwushuo.com/image/160907/2e7nf9evv.jpg?imageView2/2/w/720/q/85/format/webp
         */

        private List<BannersBean> banners;

        public List<BannersBean> getBanners() {
            return banners;
        }

        public void setBanners(List<BannersBean> banners) {
            this.banners = banners;
        }

        public static class BannersBean {
            private String channel;
            private int id;
            private String image_url;
            private int order;
            private int status;
            private int target_id;
            private String target_type;
            private String target_url;
            private String type;
            private String webp_url;
            private List<?> ad_monitors;

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getTarget_id() {
                return target_id;
            }

            public void setTarget_id(int target_id) {
                this.target_id = target_id;
            }

            public String getTarget_type() {
                return target_type;
            }

            public void setTarget_type(String target_type) {
                this.target_type = target_type;
            }

            public String getTarget_url() {
                return target_url;
            }

            public void setTarget_url(String target_url) {
                this.target_url = target_url;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getWebp_url() {
                return webp_url;
            }

            public void setWebp_url(String webp_url) {
                this.webp_url = webp_url;
            }

            public List<?> getAd_monitors() {
                return ad_monitors;
            }

            public void setAd_monitors(List<?> ad_monitors) {
                this.ad_monitors = ad_monitors;
            }
        }
    }
}
