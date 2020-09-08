<!--<style>
    .mask {
        background-color: black;
        color: black;
        transition: background-color 0.5s color 0.5s;
    }
    .mask:hover {
        background-color: none;
        color: none;
    }
</style>-->

# Minecraft 2D
<img src="icon.png" alt="icon" style="zoom:500%;" />

Welcome to Minecraft 2D. This is a sandplay. **(Not sandbox game!!!)**  
欢迎来到Minecraft 2D的世界！这是一个**沙盘**游戏。**（不是沙盒游戏！！！）**

## 初代设计图（结构树） Primary design drawing (structure tree)
| Type  | Description  |
| ----- | ------------ |
| <\|-- | 继承         |
| \*--   | 组成         |
| o--   | 聚合         |
| -->   | 合作         |
| --    | 连接（实线） |
| ..>   | 依赖         |
| ..\|> | 实现         |
| ..    | 连接（虚线） |
```mermaid
classDiagram
Main --> Client: run
Client --> Server: start
Server --> ITickable: call onTick()
RenderThread --|> Client: call repaint()
ITickable <|.. AbstractBlock: implements
AbstractBlock <|-- Block: extends
Client --> AbstractBlock: call draw()
IWorld --> IDimension: storage
Overworld ..|> IDimension: implements
Dimensions --> Overworld: storage
IDimension --> IRegistrable: storage
Registry --> IRegistrable: register
ItemConvertible ..|> IRegistrable: implements
AbstractBlock ..|> ItemConvertible: implements
Blocks --> Block: storage
DeferredRegistry --> RegistryStorage: use
Registry ..> DeferredRegistry: storage
RegistryStorage --> Identifier: use

class IWorld {
    <<Interface>>
}
class Client {
    +paint(Graphics g)
}
class ITickable {
    <<Interface>>
    +onTick()*
}
class AbstractBlock {
    <<abstract>>
    +onTick()*
    +draw(Graphics g)*
}
class Registry {
    +DeferredRegistry~Block~ BLOCK
    +register(DeferredRegistry~T~ registry, Identifier id, T entry)$ ~T extends IRegistrable~
    +register(DeferredRegistry~T~ registry, String path, T entry)$ ~T extends IRegistrable~
}
class IRegistrable {
    <<Interface>>
}
class ItemConvertible {
    <<Interface>>
    +asItem()* Item
}
class DeferredRegistry~T extends IRegistrable~ {
    -Map~Identifier, T~ map
    +register(Identifier id, Supplier~T~ supplier) ~T~
}
class RegistryStorage {
    +Map<Identifier, Block> BLOCKS
}
class Block {
    +draw(Graphics g)
}
class Identifier {
    -String namespace
    -String path
}
```

## 贡献 Contributing
此项目仍处于初始阶段。我们希望您能为我们做出贡献，哪怕只是1个issue或1个PR。

See [CONTRIBUTING.md](CONTRIBUTING.md)

> Here is the Contribute Value Table.

```mermaid
pie title Contribute Value Table
"squid233": 1
```

## 其它 Other
<iframe src="https://discord.com/widget?id=751804389718753421&theme=dark" width="350" height="500" allowtransparency="true" frameborder="0" sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts"></iframe>

Join our Discord channel just time! [![discord](https://img.shields.io/discord/751804389718753421)](https://discord.gg/ydYzTKV)

现在就加入我们的QQ<span class="mask" title="你知道的太多了"><s>吹水</s></span>群！<a target="_blank" href="https://qm.qq.com/cgi-bin/qm/qr?k=efwa2cjVSs-S_UorWELGd45SPTJBTGV6&jump_from=webapi"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="Java 开发交流群" title="Java 开发交流群"></a>

