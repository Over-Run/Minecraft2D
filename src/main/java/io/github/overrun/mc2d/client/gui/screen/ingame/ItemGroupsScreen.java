/*
 * MIT License
 *
 * Copyright (c) 2020-2022 Overrun Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2d.client.gui.screen.ingame;

import io.github.overrun.mc2d.client.Keyboard;
import io.github.overrun.mc2d.client.gui.screen.Screen;
import io.github.overrun.mc2d.screen.ItemGroupsScreenHandler;
import io.github.overrun.mc2d.screen.inv.PlayerInventory;
import io.github.overrun.mc2d.screen.slot.Slot;
import io.github.overrun.mc2d.text.IText;
import io.github.overrun.mc2d.util.Identifier;
import io.github.overrun.mc2d.world.item.ItemStack;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 * @since 2021/01/23
 */
public final class ItemGroupsScreen extends HandledScreen<ItemGroupsScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/tab_items.png");
    private final Screen parent;
    private final ItemStack stack = ItemStack.ofEmpty();

    public ItemGroupsScreen(ItemGroupsScreenHandler handler, PlayerInventory playerInventory, Screen parent) {
        super(handler, playerInventory, IText.translatable("itemGroup.mc2d"));
        this.parent = parent;
    }

    @Override
    protected void onClickSlot(Slot slot, int button) {
        super.onClickSlot(slot, button);
        // click on items
        var invStack = slot.getStack();
        if (slot.id() >= Slot.CONTAINER_ID0) {
            if (!stack.isEmpty()) {
                if (stack.getItem() == invStack.getItem()) {
                    stack.increment();
                } else {
                    stack.setCount(0);
                }
                // todo: press shift -> ::transferSlot
            } else {
                stack.set(invStack);
                // ::transferSlot
                if (button == GLFW_MOUSE_BUTTON_MIDDLE || Keyboard.isKeyPress(GLFW_KEY_LEFT_SHIFT)) {
                    stack.setCount(stack.getMaxCount());
                }
            }
        }
        // click on hot-bar
        else {
            if (!stack.isEmpty()) {
                if (invStack.isEmpty()) {
                    if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                        slot.setStack(stack.copy(1));
                        stack.decrement();
                    } else {
                        slot.setStack(stack.copy());
                        stack.setCount(0);
                    }
                } else {
                    if (button == GLFW_MOUSE_BUTTON_LEFT) {
                        if (stack.getItem() == invStack.getItem()) {
                            // merge
                            int oldCount = invStack.getCount();
                            invStack.increment(stack.getCount());
                            stack.setCount(Math.max(0, oldCount + stack.getCount() - invStack.getMaxCount()));
                        } else {
                            // swap
                            var copyOldStack = invStack.copy();
                            playerInventory.setStack(slot.id(), stack.copy());
                            stack.set(copyOldStack);
                        }
                    } else if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                        if (invStack.getCount() < invStack.getMaxCount()) {
                            invStack.increment();
                            stack.decrement();
                        }
                    }
                }
            } else {
                if (!invStack.isEmpty()) {
                    if (button == GLFW_MOUSE_BUTTON_LEFT) {
                        // ::transferSlot
                        if (!Keyboard.isKeyPress(GLFW_KEY_LEFT_SHIFT)) {
                            stack.set(invStack);
                        }
                        playerInventory.removeStack(slot.id());
                    } else if (button == GLFW_MOUSE_BUTTON_MIDDLE) {
                        stack.setItem(invStack.getItem());
                        stack.setMaxCount(invStack.getMaxCount());
                        stack.setCount(stack.getMaxCount());
                    } else if (button == GLFW_MOUSE_BUTTON_RIGHT) {
                        if (invStack.getCount() == 1) {
                            stack.set(playerInventory.removeStack(slot.id(), 1));
                        } else {
                            int half = invStack.getCount() / 2;
                            stack.set(playerInventory.removeStack(slot.id(), half));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY) {
        client.getTextureManager().bindTexture(TEXTURE);
        drawTexture(x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        renderBackground();
        super.render(mouseX, mouseY, delta);
        if (!stack.isEmpty()) {
            client.itemRenderer.renderItemStack(textRenderer, stack, mouseX - 8, mouseY - 8);
        }
    }

    @Override
    public boolean keyPressed(int key, int scancode, int mods) {
        if (key == GLFW_KEY_E) {
            onClose();
        }
        return super.keyPressed(key, scancode, mods);
    }

    @Override
    public void onClose() {
        super.onClose();
        client.openScreen(parent);
    }
}
