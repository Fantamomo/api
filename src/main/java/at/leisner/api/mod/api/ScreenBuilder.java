package at.leisner.api.mod.api;

import at.leisner.api.mod.api.widgets.Widget;

import java.util.ArrayList;
import java.util.List;

public class ScreenBuilder {
    private List<Widget> widgets = new ArrayList<>();
    private String title = "";
    private boolean renderBackground = true;
    public ScreenBuilder addWidget(Widget widget) {
        widgets.add(widget);
        return this;
    }
    public ScreenBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    public ScreenBuilder renderBackground(boolean render) {
        this.renderBackground = render;
        return this;
    }
}
