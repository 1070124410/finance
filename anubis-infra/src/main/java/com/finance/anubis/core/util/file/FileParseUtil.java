package com.finance.anubis.core.util.file;

import com.finance.anubis.enums.FileType;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @Author yezhaoyang
 * @Date 2023/03/02 15:00
 * @Description
 **/
@Component
public abstract class FileParseUtil {

    @Getter
    private final FileType fileType;

    public FileParseUtil(FileType fileType) {
        this.fileType = fileType;
    }


    public abstract String parseLine(String line);


}
