package at.leisner.api.mod.api.widgets;

import at.leisner.api.mod.api.utilmod.Tooltip;

public abstract class Builder<T> {
    protected Tooltip tooltip = new Tooltip("");
    protected int ID = 0;
    protected int X = 10;
    protected int Y = 10;
    protected int height = 20;
    protected int width = 100;
    public T setTooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        return null;
    }
    public abstract T setID(int id);
    public abstract T setX(int x);
    public abstract T setY(int y);
}
