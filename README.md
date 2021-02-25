# MW-Chat-Command
MWC命令插件。

# 命令集

（注：当末尾为X代表命令不完整；末尾为V代表完整，将会调用功能）

    注意：
    <identifier> - 标识符，必须符合Java命名规则（但不必规避Java关键字或保留字）
    <boolean> - 布尔值。true/false
    <command> - 有分支命令

    mwc - 总命令开头 X
      autoyo <boolean> - 玩家加入时自动欢迎 X
        true - 开启 V
        false - 关闭 V
      opme - 将自己设为OP V
      deop - 取消自己的OP V
      location <command> - 对坐标的操作 X
        add <identifier> - 将当前坐标标记为<identifier> V
        remove <identifier> - 删除<identifier>标记的坐标 V
        list - 显示所有的坐标 V
        goto <identifier> - 前往标记为<identifier>的坐标 V
