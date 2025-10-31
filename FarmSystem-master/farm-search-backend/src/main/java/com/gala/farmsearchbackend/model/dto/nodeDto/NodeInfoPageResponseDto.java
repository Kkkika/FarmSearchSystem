package com.gala.farmsearchbackend.model.dto.nodeDto;

import com.gala.farmsearchbackend.model.domain.NodeInfo;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 节点企业分页响应DTO
 */
@Data
public class NodeInfoPageResponseDto {
    /**
     * 总记录数
     */
    private Integer totalRow;

    /**
     * 总页数
     */
    private Integer totalPageNum;

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 数据列表
     */
    private List<NodeInfoSimpleDto> data;

    public Integer getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(Integer totalRow) {
        this.totalRow = totalRow;
    }

    public Integer getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(Integer totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<NodeInfoSimpleDto> getData() {
        return data;
    }

    public void setData(List<NodeInfoSimpleDto> data) {
        this.data = data;
    }

}