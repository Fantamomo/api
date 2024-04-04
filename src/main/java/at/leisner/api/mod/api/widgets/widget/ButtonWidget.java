package at.leisner.api.mod.api.widgets.widget;

import at.leisner.api.mod.api.utilmod.ActionMeta;
import at.leisner.api.mod.api.utilmod.ActionTrigger;
import at.leisner.api.mod.api.utilmod.ActionType;
import at.leisner.api.mod.api.utilmod.Tooltip;
import at.leisner.api.mod.api.widgets.Widget;
import at.leisner.api.mod.api.widgets.builder.ButtonBuilder;

public class ButtonWidget extends Widget {


    public ButtonWidget(int ID, int x, int y, int width, int height, Tooltip tooltip, ActionType actionType, ActionTrigger actionTrigger, ActionMeta actionMeta) {
        super(ID, x, y, width, height, tooltip, "button", actionType, actionTrigger, actionMeta);
    }

    public static ButtonBuilder builder() {
        return new ButtonBuilder();
    }
}
