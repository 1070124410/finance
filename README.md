# 背景
本项目为实习做的对账平台
主要功能是提供实时和离线对账功能，将数据端的账单信息进行对账，保证订单价格的一致性，减少人力对账成本，便于及时发现系统问题

# 流程图
![微信图片_20240728193531](https://github.com/user-attachments/assets/b30b3a21-cfb6-40e2-9c48-6cb0a54cc9e8)



# 时序图
![image](https://github.com/user-attachments/assets/3aac31ae-2d43-467a-83fe-16251b9b07f9)

# 改动
由于原中间件依赖于公司，已初步替换为开源组件，暂时屏蔽了feign、sentinel等功能。

# 亮点
项目中采用了大量工厂+策略模式，便于扩展。
采用模板方法编排对账流程，保证结构的清晰。
大文件流式下载，分割小文件，通过磁盘归并排序整理文件，减少内存的使用。
采用MQ驱动对账流程的推进，支持分布式，采用分布式锁保证context中只更新本次执行结果。

# 未来
扩展支持对账文件类型
扩展对账数据获取方式
优化磁盘占用和内存使用
...
