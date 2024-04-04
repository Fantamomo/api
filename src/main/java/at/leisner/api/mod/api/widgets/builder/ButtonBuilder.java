package at.leisner.api.mod.api.widgets.builder;

import at.leisner.api.mod.api.utilmod.*;
import at.leisner.api.mod.api.widgets.Builder;
import at.leisner.api.mod.api.widgets.widget.ButtonWidget;

public class ButtonBuilder extends Builder<ButtonBuilder> {
    private String text = "Button";
    private ActionType action = Action.TYPE.NOTHING;
    private ActionTrigger actionTrigger = Action.TRIGGER.NEVER;
    private ActionMeta actionMeta = new ActionMeta("");

    @Override
    public ButtonBuilder setTooltip(Tooltip tooltip) {
        super.setTooltip(tooltip);
        return this;
    }

    @Override
    public ButtonBuilder setID(int id) {
        this.ID = id;
        return this;
    }

    @Override
    public ButtonBuilder setX(int x) {
        X = x;
        return this;
    }

    @Override
    public ButtonBuilder setY(int y) {
        Y = y;
        return this;
    }

    public ButtonBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public ButtonBuilder setAction(ActionType actionType) {
        this.action = actionType;
        return this;
    }

    public ButtonBuilder setActionAndMeta(ActionType actionType, ActionMeta actionMeta) {
        this.action = actionType;
        this.actionMeta = actionMeta;
        return this;
    }

    public ButtonBuilder setActionMeta(ActionMeta actionMeta) {
        this.actionMeta = actionMeta;
        return this;
    }

    public ButtonBuilder setActionTrigger(ActionTrigger actionTrigger) {
        this.actionTrigger = actionTrigger;
        return this;
    }

    public ButtonWidget build() {
        return new ButtonWidget(ID, X, Y, width, height, tooltip, action, actionTrigger, actionMeta);
    }
}
