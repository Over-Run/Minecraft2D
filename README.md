# Minecraft 2D
Welcome to Minecraft 2D. This is a sandplay. **(Not sandbox game!!!)**
欢迎来到Minecraft 2D的世界！这是一个**沙盘**游戏。**（不是沙盒游戏！！！）**

## 初代设计图（结构树） Primary design drawing (structure tree)

```mermaid
graph LR
Main-->|run|cl(Client)
ir(IRegistrable)-->ic(ItemConvertible)-->ab(AbstractBlock)-->b(Block)
cl-->|draw|ab
iw(IWorld)-->ow(Overworld)
w(Worlds)-->|stroage|ow
iw-->|stroage|ir
reg(Registry)-->|registry|ir
```

