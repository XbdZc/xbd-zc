package com.ccc.util;

public class PageSupport {
    //获取SQL 开始位置 , limit a, b;  返回 a 的值
    public int getPageStart() {
        return (this.currentPageNo - 1) * this.pageSize;
    }

    //总数量（查表总数count）
    private int totalCount = 0;

    //每页显示的数量
    private int pageSize = 0;

    //总页数-totalCount/pageSize（+1）
    private int totalPageCount = 1;

    //当前页码-来自于用户输入
    private int currentPageNo = 1;

    public int getCurrentPageNo() {
        return currentPageNo;
    }

    public void setCurrentPageNo(int currentPageNo) {
        if (currentPageNo > 0) {
            this.currentPageNo = currentPageNo;
        }
    }

    //获取总数量
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        if (totalCount > 0) {
            this.totalCount = totalCount;
            //设置总页数
            this.setTotalPageCountByRs();
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    //总页数
    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }

    //最大页数约定 小于0 = 0 , 除尽 = 总数/每页数量 , 有余数 = (总数/每页数量)  +1 页
    public void setTotalPageCountByRs() {
        if (this.totalCount % this.pageSize == 0) {
            this.totalPageCount = this.totalCount / this.pageSize;
        } else if (this.totalCount % this.pageSize > 0) {
            this.totalPageCount = this.totalCount / this.pageSize + 1;
        } else {
            this.totalPageCount = 0;
        }
    }
//	public int getPageStart(int currentPageNo,int pageSize) {
////		return (currentPageNo-1)*this.pageSize;
//		int i = ((currentPageNo - 1) * pageSize);
//		return i ;
//	}

}