package com.jsj.member.ob.utils;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class CCPage<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //当前页
    private int pageNum;
    //每页的数量
    private int pageSize;
    //当前页的数量
    private int size;

    //由于startRow和endRow不常用，这里说个具体的用法
    //可以在页面中"显示startRow到endRow 共size条数据"


    //总记录数
    private long total;
    //总页数
    private int pages;
    //结果集
    private List<T> list;

    //前一页
    private int prePage;
    //下一页
    private int nextPage;

    private String prePageNav;
    private String nextPageNav;

    //是否为第一页
    private boolean isFirstPage = false;
    //是否为最后一页
    private boolean isLastPage = false;
    //是否有前一页
    private boolean hasPreviousPage = false;
    //是否有下一页
    private boolean hasNextPage = false;
    //导航页码数
    private int navigatePages;
    //所有导航页号
    private CCNav[] ccNavs;
    //导航条上的第一页
    private int navigateFirstPage;
    //导航条上的最后一页
    private int navigateLastPage;

    private HttpServletRequest servletRequest;

    public CCPage() {
    }

    /**
     * 包装Page对象
     *
     * @param list
     */
    public CCPage(Page list, int pageSize) {
        this(list, pageSize, 8);
    }

    /**
     * 包装Page对象
     *
     * @param list          page结果
     * @param navigatePages 页码数量
     */
    public CCPage(Page list, int pageSize, int navigatePages) {

        ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();

        if(request != null){
            this.servletRequest = request;
        }

        if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNum = page.getCurrent();
            this.pageSize = pageSize;

            this.pages = page.getPages();
            this.list = page.getRecords();
            this.size = page.getSize();
            this.total = page.getTotal();
            //由于结果是>startRow的，所以实际的需要+1

        }
        if (this.list instanceof Collection) {
            this.navigatePages = navigatePages;
            //计算导航页
            calcNavigatepageNums();
            //计算前后页，第一页，最后一页
            calcPage();
            //判断页面边界
            judgePageBoudary();
        }



    }


    /**
     * 计算导航页
     */
    private void calcNavigatepageNums() {
        //当总页数小于或等于导航页码数时
        if (pages <= navigatePages) {
            ccNavs = new CCNav[pages];
            for (int i = 0; i < pages; i++) {

                ccNavs[i] = new CCNav();
                ccNavs[i].setIndex( i + 1);
            }
        } else { //当总页数大于导航页码数时
            ccNavs = new CCNav[navigatePages];
            int startNum = pageNum - navigatePages / 2;
            int endNum = pageNum + navigatePages / 2;

            if (startNum < 1) {
                startNum = 1;
                //(最前navigatePages页
                for (int i = 0; i < navigatePages; i++) {
                    ccNavs[i] = new CCNav();
                    ccNavs[i].setIndex(startNum++);
                }
            } else if (endNum > pages) {
                endNum = pages;
                //最后navigatePages页
                for (int i = navigatePages - 1; i >= 0; i--) {
                    ccNavs[i] = new CCNav();
                    ccNavs[i].setIndex(endNum--);
                }
            } else {
                //所有中间页
                for (int i = 0; i < navigatePages; i++) {
                    ccNavs[i] = new CCNav();
                    ccNavs[i].setIndex(startNum++);
                }
            }
        }

        for(CCNav nav : ccNavs){
            nav.setNav(this.getNav(nav.getIndex()));
        }
    }

    private String getNav(int index){

        if(this.servletRequest == null){
            return "javascript:;";
        }

        List<String> query = new ArrayList<String>();
        Map<String, String[]> maps = this.servletRequest.getParameterMap();
        for (Map.Entry<String, String[]> entry : maps.entrySet()) {
            if (!entry.getKey().equals("page")) {
                query.add(entry.getKey() + "=" + entry.getValue()[0]);
                this.servletRequest.setAttribute(entry.getKey(), entry.getValue()[0]);
            }
        }

        if(query.size() > 0){
            String params = String.join("&", query);
            return String.format("?page=%d&%s", index, params);
        }

        return String.format("?page=%d", index);


    }

    /**
     * 计算前后页，第一页，最后一页
     */
    private void calcPage() {
        if (this.ccNavs != null && this.ccNavs.length > 0) {
            navigateFirstPage = this.ccNavs[0].getIndex();
            navigateLastPage = this.ccNavs[this.ccNavs.length - 1].getIndex();
            if (pageNum > 1) {
                prePage = pageNum - 1;
            }
            if (pageNum < pages) {
                nextPage = pageNum + 1;
            }
        }

        this.prePageNav = this.getNav(prePage);
        this.nextPageNav = this.getNav(nextPage);

    }

    /**
     * 判定页面边界
     */
    private void judgePageBoudary() {
        isFirstPage = pageNum == 1;
        isLastPage = pageNum == pages;
        hasPreviousPage = pageNum > 1;
        hasNextPage = pageNum < pages;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Deprecated
    // firstPage就是1, 此函数获取的是导航条上的第一页, 容易产生歧义
    public int getFirstPage() {
        return navigateFirstPage;
    }

    @Deprecated
    public void setFirstPage(int firstPage) {
        this.navigateFirstPage = firstPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    @Deprecated
    // 请用getPages()来获取最后一页, 此函数获取的是导航条上的最后一页, 容易产生歧义.
    public int getLastPage() {
        return navigateLastPage;
    }

    @Deprecated
    public void setLastPage(int lastPage) {
        this.navigateLastPage = lastPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public CCNav[] getCcNavs() {
        return ccNavs;
    }

    public void setCcNavs(CCNav[] ccNavs) {
        this.ccNavs = ccNavs;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }


    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public String getPrePageNav() {
        return prePageNav;
    }

    public void setPrePageNav(String prePageNav) {
        this.prePageNav = prePageNav;
    }

    public String getNextPageNav() {
        return nextPageNav;
    }

    public void setNextPageNav(String nextPageNav) {
        this.nextPageNav = nextPageNav;
    }
}

class CCNav{

   private int index;
   private String nav;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getNav() {
        return nav;
    }

    public void setNav(String nav) {
        this.nav = nav;
    }
}