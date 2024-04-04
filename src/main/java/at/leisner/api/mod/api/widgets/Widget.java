package at.leisner.api.mod.api.widgets;

import at.leisner.api.mod.api.utilmod.*;

public abstract class Widget {
    protected int ID = 0;
    protected int x = 10;
    protected int y = 10;
    protected int width = 100;
    protected int height = 20;
    protected Tooltip tooltip = new Tooltip("");
    protected String type = "widget";
    protected ActionType actionType = Action.TYPE.NOTHING;
    protected ActionTrigger actionTrigger = Action.TRIGGER.NEVER;
    protected ActionMeta actionMeta = new ActionMeta("");

//    public Widget(int ID, int x, int y, Tooltip tooltip, String type, ActionType actionType, ActionTrigger actionTrigger, ActionMeta actionMeta) {
//        this.ID = ID;
//        this.x = x;
//        this.y = y;
//        this.tooltip = tooltip;
//        this.type = type;
//        this.actionType = actionType;
//        this.actionTrigger = actionTrigger;
//        this.actionMeta = actionMeta;
//    }

    public Widget(int ID, int x, int y, int width, int height, Tooltip tooltip, String type, ActionType actionType, ActionTrigger actionTrigger, ActionMeta actionMeta) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tooltip = tooltip;
        this.type = type;
        this.actionType = actionType;
        this.actionTrigger = actionTrigger;
        this.actionMeta = actionMeta;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getID() {
        return ID;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Tooltip getTooltip() {
        return tooltip;
    }
    public String getType() {
        return type;
    }
    public ActionType getActionType() {
        return actionType;
    }
    public ActionTrigger getActionTrigger() {
        return actionTrigger;
    }

    public ActionMeta getActionMeta() {
        return actionMeta;
    }

}
