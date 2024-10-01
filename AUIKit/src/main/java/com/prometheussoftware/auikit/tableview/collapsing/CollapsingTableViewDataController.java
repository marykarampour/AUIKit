package com.prometheussoftware.auikit.tableview.collapsing;

import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewDataController;
import com.prometheussoftware.auikit.uiview.UIControl;
import com.prometheussoftware.auikit.uiview.UISwitch;

public class CollapsingTableViewDataController extends UITableViewDataController implements UISwitch.SelectionDelegate {

    /** Default is true, if false only one section can be expanded at a time
     @apiNote You are responsible for updating/tapping etc. of the header, this does not
     result in header button press programmatically, only reloads the rows in sections */
    public boolean multiExpandedSectionEnabled = true;

    public CollapsingTableViewDataController() {
        super();
    }

    @Override public int numberOfRowsInSection(int section) {

        if (sections.size() <= section) return 0;
        TableObject.Section sectionObject = sections.get(section);

        if (!sectionObject.isExpanded || sections.get(section).rows == null) return 0;
        return sections.get(section).rows.getItems().size();
    }

    public int numberOfRowsInSection(int section, boolean isExpanded) {

        if (sections.size() <= section) return 0;
        TableObject.Section sectionObject = sections.get(section);

        if (sections.get(section).rows != null && (isExpanded || sectionObject.isExpanded))
            return sections.get(section).rows.getItems().size();
        return 0;
    }

    @Override public int heightForHeaderInSection(int section) {

        if (sections.size() <= section) return 0;
        TableObject.Section sectionObject = sections.get(section);

        return sectionObject.estimatedHeight();
    }

    @Override public boolean sectionIsCollapsable(int section) {

        if (sections.size() <= section) return false;
        TableObject.Section sectionObject = sections.get(section);

        return sectionObject.isCollapsible();
    }

    @Override public boolean sectionIsExpanded(int section) {

        if (sections.size() <= section) return false;
        TableObject.Section sectionObject = sections.get(section);

        return !sectionIsCollapsable(section) || sectionObject.isExpanded;
    }

    @Override public void buttonViewSetSelected(UIControl view, boolean selected) { }

}
