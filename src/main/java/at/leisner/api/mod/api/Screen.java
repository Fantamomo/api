package at.leisner.api.mod.api;

import at.leisner.api.mod.api.widgets.Widget;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private List<Widget> widgets = new ArrayList<>();
    private boolean renderBackground = false;
    public static ScreenBuilder builder() {
        return new ScreenBuilder();
    }
}
