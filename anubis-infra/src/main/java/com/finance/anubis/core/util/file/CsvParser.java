package com.finance.anubis.core.util.file;

import com.finance.anubis.enums.FileType;
import org.springframework.stereotype.Component;

/**
 * @Author yezhaoyang
 * @Date 2023/03/02 15:35
 * @Description
 **/
@Component
public class CsvParser extends FileParseUtil {
    public CsvParser() {
        super(FileType.CSV);
    }

    @Override
    public String parseLine(String line) {
        return line;
    }
}
