package com.example.zpp.mydiary;
//日记类
public class Daily {
    private Integer id;//id
    //private String author;//作者
    private String title;//标题
    private String createtime;//创建时间
    private String content;//内容

    public Daily(){

    }

    public Daily(Integer id,String author,String title,String createtime,String content){
        this.id=id;
        //this.author=author;
        this.title=title;
        this.createtime=createtime;
        this.content=content;
    }

    public Integer getId() {
        return id;
    }

    //public String getAuthor() { return author;}

    public String getContent() {
        return content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //public void setAuthor(String author){this.author=author;}

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
