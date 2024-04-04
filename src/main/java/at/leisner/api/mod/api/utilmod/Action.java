package at.leisner.api.mod.api.utilmod;

public class Action {
    public static final class TYPE {
        public static final ActionType RUN_CMD = new ActionType("RUN_CMD");
        public static final ActionType SEND_CHAT_MSG = new ActionType("SEND_CHAT_MSG");
        public static final ActionType SEND_CUSTOM_MSG = new ActionType("SEND_CUSTOM_MSG");
        public static final ActionType NOTHING = new ActionType("NOTHING");
        public static final ActionType CLOSE_GUI = new ActionType("CLOSE_GUI");
        public static final ActionType CHANGE_FOKUS = new ActionType("CHANGE_FOKUS");
        public static final ActionType CHANGE_WIDGET = new ActionType("CHANGE_WIDGET");
    }
    public static final class TRIGGER {
        public static final ActionTrigger CLOSE_GUI = new ActionTrigger("CLOSE_GUI");
        public static final ActionTrigger ON_CLICK = new ActionTrigger("ON_CLICK");
        public static final ActionTrigger ON_WIDGET_CHANGE = new ActionTrigger("ON_WIDGET_CHANGE");
        public static final ActionTrigger NEVER = new ActionTrigger("NEVER");

    }

}
