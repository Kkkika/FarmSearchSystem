package com.gala.farmsearchbackend.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.gala.farmsearchbackend.mapper.RetaBatchMapper;
import com.gala.farmsearchbackend.mapper.SlauBatchMapper;
import com.gala.farmsearchbackend.mapper.WholBatchMapper;
import com.gala.farmsearchbackend.model.domain.RetaBatch;
import com.gala.farmsearchbackend.model.domain.SlauBatch;
import com.gala.farmsearchbackend.model.domain.WholBatch;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UpdateStateService {
    @Resource
    private SlauBatchMapper slauBatchMapper;
    @Resource
    private WholBatchMapper wholBatchMapper;
    @Resource
    private RetaBatchMapper retaBatchMapper;

    public int updateSlauState(Integer sbId, Integer state) {
        return slauBatchMapper.update(null,
                new UpdateWrapper<SlauBatch>()
                        .eq("sb_id", sbId)
                        .set("state", state)
        );
    }

    public int updateWholState(Integer wbId, Integer state) {
        return wholBatchMapper.update(null,
                new UpdateWrapper<WholBatch>()
                        .eq("wb_id", wbId)
                        .set("state", state)
        );
    }

    public int updateRetaState(Integer rbId, Integer state) {
        return retaBatchMapper.update(null,
                new UpdateWrapper<RetaBatch>()
                        .eq("rb_id", rbId)
                        .set("state", state)
        );
    }
}
