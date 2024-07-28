package com.finance.anubis.core.factory;

import com.finance.anubis.core.constants.enums.FileType;
import com.finance.anubis.core.util.file.FileParseUtil;
import lombok.CustomLog;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@CustomLog
public class FileParserUtilFactory extends StageHandlerFactory {

    private static final Map<FileType, FileParseUtil> FILE_PARSE_MAP = new EnumMap<>(FileType.class);


    public FileParseUtil getParseUtil(FileType type) {
        return FILE_PARSE_MAP.get(type);
    }


    @Override
    public void afterPropertiesSet() {
        appContext.getBeansOfType(FileParseUtil.class)
                .values()
                .forEach(util -> {
                    if (FILE_PARSE_MAP.containsKey(util.getFileType())) {

                        FileParseUtil parseUtil = FILE_PARSE_MAP.get(util.getFileType());
                        log.error("duplicate action handler {},{}", parseUtil.getClass().getName(), util.getClass().getName());
                        throw new RuntimeException("duplicate action handler");
                    }
                    FILE_PARSE_MAP.put(util.getFileType(), util);
                });
    }
}
