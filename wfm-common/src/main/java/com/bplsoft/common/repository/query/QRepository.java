package com.bplsoft.common.repository.query;


import com.bplsoft.common.model.Model;

import java.io.Serializable;
import java.util.List;


public interface QRepository<T extends Model<PK>, PK extends Serializable> {

    T findById(PK id);

    List<T> findAll();

    List<T> findAll(Integer index, Integer numberOfRecords);

    Long count();

}
