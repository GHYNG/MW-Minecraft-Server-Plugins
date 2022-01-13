# 奶路投票插件

奶路投票插件用于为服务器提供投票功能支持。

## 普通投票

普通投票指的是玩家可以围绕某个话题进行投票。
同时可以有多个投票（下称问卷）。
每个问卷有多个选项，玩家可以就这些选项进行选择并投票。

每个投票有若干个属性：

	vpp: 每个玩家可以为该问卷投出的总票数上限。
	vps: 每个玩家可以为该问卷的单个选项投出的票数上限。
	anon: 该问卷是否匿名。
	
举例：假设一个名为```color```的问卷，
有三个分别名为```red```、```green```、```blue```的选项，
```vpp```为```3```，```vps```为```2```，```anon```为```true```，
那么玩家总共可以投出```3```票，为每个选项最多可以投出```2```票，这个问卷是匿名的。

### 玩家使用的指令

使用```/vote-normal <vote_name> <selection_name> <count>```来投票。

其中```<vote_name>```为问卷名，```<selection_name>```为选项名，```<count>```为投出的票数（必须是整数）。
票数可以是负数，意味着收回对该选项的先前投票。

举例：为名为```color```的问卷的名为```red```的选项投出```2```票，使用指令：
```
/vote-normal color red 2
```

### 管理使用指令（玩家可以忽略这条）

管理使用```/vote-normal-manage```指令控制问卷系统。

注意：下文中的各种名称必须符合Java命名规则，但无视关键字。

#### 开始新问卷
```
/vote-normal-manage start <vote_name>
```

#### 调整问卷属性
```
/vote-normal-manage setting <property> <value>
```

比如，如果想要把匿名性调整为假，使用
```
/vote-normal-manage setting anon false
```

##### 默认属性

属性有各自的默认值：

	anon = true
	vpp = 0
	vps = 0

#### 查阅问卷信息

查看所有问卷简略信息：
```
/vote-normal-manage list
```

查看指定问卷信息：
```
/vote-normal-manage list <vote_name>
```

#### 结束指定问卷并唱片

```
/vote-normal-manage stop <vote_name>
```
	
## 投票ban人

还没有实际应用，先不写。
