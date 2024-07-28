package com.finance.anubis.report.common;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvURLDataSet;
import org.springframework.core.io.Resource;

/**
 * 单元测试数据集 - CSV格式支持
 *
 * @author baocheng
 * @date 2022/12/8 10:25
 */
public class CsvDataSetLoader extends AbstractDataSetLoader {

    @Override
    protected IDataSet createDataSet(Resource resource) throws Exception {
        return new CsvURLDataSet(resource.getURL());
    }
}
