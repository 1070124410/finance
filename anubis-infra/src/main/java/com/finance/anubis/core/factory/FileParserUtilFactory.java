package com.finance.anubis.core.factory;

import com.finance.anubis.core.util.file.FileParseUtil;
import com.finance.anubis.enums.FileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class FileParserUtilFactory extends StageHandlerFactory {

    public final static Logger log= LoggerFactory.getLogger(FileParserUtilFactory.class);

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
