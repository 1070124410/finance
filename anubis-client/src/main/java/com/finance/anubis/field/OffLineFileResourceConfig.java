package com.finance.anubis.field;


import com.finance.anubis.constant.FileType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yezhaoyang
 * @Date 2023/02/21 15:08
 * @Description
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OffLineFileResourceConfig extends OffLineResourceConfig {

    /**
     * 从源文件中取出的key集合
     */
    private List<String> keyList;
    /**
     * 源文件的分隔符
     */
    private String fileFormatSplit;

    /**
     * 源文件类型
     */
    private FileType fileType;

    /**
     * 文件头跳过行数
     */
    private Integer skipHead;

    /**
     * 文件尾跳过行数
     */
    private Integer skipTail;





}
