package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.model.Identifier;

public class UISwitch extends UIControl {

    protected boolean enabled;

    public interface SelectionDelegate {
        void buttonViewSetSelected (UIControl view, boolean selected);
    }

    private SelectionDelegate delegate;
//    public IndexPath indexPath;

    static {
        Identifier.Register(UISwitch.class);
    }

    public UISwitch() {
        super();
        setTarget(v -> switchOnOff());
    }

    protected void switchOnOff() {
        setSelected(!isSelected());
    }

    protected void customizeSetSelected(boolean selected) {
        if (delegate != null) delegate.buttonViewSetSelected(this, selected);
    }

    @Override public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override public void setSelected(boolean selected) {
        super.setSelected(selected);
        customizeSetSelected(selected);
    }

    public void setDelegate(SelectionDelegate delegate) {
        this.delegate = delegate;
    }
}
