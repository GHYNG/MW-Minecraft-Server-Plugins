# MW-Minecraft-Server-Plugins
用于奶路服务器的插件。

## 如何下载：
在 https://github.com/GHYNG/MW-Minecraft-Server-Plugins/releases 页面中下载各个标题下的Jar文件，并将这些文件放入服务器目录下面的plugins文件夹中即可。如果服务器已经在运行，需要reload服务器。建议下载标题中带有“Nesest”字样的最新版本插件。

## 已有插件介绍

### MW-Plugin-Main
奶路主插件，主要是为各子插件提供支持。

### MW-Block-Protection
方块保护。主要是取消了爆炸对方块的伤害以及大多数生物对方块的影响。

这个插件未来将会做出更多改进，包括对不同的世界做出不同的反应。

#### 硬依赖：
MW-Plugin-Main

### MW-Chat
聊天插件。主要是在聊天栏中显示头衔，以及聊天时发出“叮”的一声。

如果同时存在MW-Color-Code插件，将会在MW-Color-Code插件之后加载。

### 存在的问题：
显示的头衔与LuckPerm不一致。

#### 硬依赖：
MW-Plugin-Main

#### 软依赖：
MW-Color-Code

### MW-Chat-Command
MWC命令插件，用于给服主运行各种命令。这些命令允许服主在不拥有OP权限时运行。

如果存在MW-Chat插件，将会在MW-Chat插件之后加载。

#### 硬依赖：
MW-Plugin-Main

#### 软依赖：
MW-Chat

### MW-Color-Code
字符颜色插件。给聊天栏，告示栏，书本，铁毡命名中的字符根据规则添加颜色或风格。

#### 硬依赖：
MW-Plugin-Main

### MW-Player-Death-Message
当玩家死亡时，服务器广播位置。

这个插件未来有可能会被取消，功能会被整合进其它插件。

#### 硬依赖：
MW-Plugin-Main

### MW-World-Management
多世界管理插件。注意这个插件只是辅助功能，管理不同世界的特性。本身并不是多时间插件。如果想要多时间插件，请另外寻找。

#### 硬依赖：
MW-Plugin-Main

## 未来插件展望：
以下这些插件，有些几乎制作完毕，有些还刚开始设计，而有些只是提出了概念

### MW-File-Management
负责管理服务器文件的插件。普通玩家不会与这个插件直接交互。这个插件将会给其它插件提供文件管理的方法支持。将会成为大多数插件的直属父插件。

### MW-Server-Interactive
服务器（娘）与玩家互动的插件。将会将主插件的部分功能转移给此插件（待定）。
