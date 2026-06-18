package com.prometheussoftware.auikit.uiview.protocols;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;

import java.text.AttributedString;
import java.util.Set;

public interface ViewContentProtocol {

    interface TitleSubTitle {
        default String title() { return null; };
        default String subtitle() { return null; };
        default AttributedString attributedTitle() { return null; };
        default AttributedString attributedSubtitle() { return null; };
    }

    interface Placeholder extends TitleSubTitle {
        /** @brief The title of the view, section, etc. containing a list of objects of this type */
        default String titleForContainingList() { return null; };
        default String addItemTitle() { return null; };
    }

    interface Callback {
        default Placeholder item() { return null; }
    }

    interface Search extends Placeholder {
        default String searchPredicateKey() { return null; };
        /** @brief Used in constructing OR predicate in search.
        If this has count greater than 0, searchPredicateKey will be ignored. */
        default Set<String> searchPredicateKeys() { return null; };
        /** @brief Search property name, return nil if searching self. */
        default Set<String> searchPredicatePropertyNames() { return null; };
        default boolean hasDetail() { return false; };
        default int numberOfLines() { return 1; };
    }

    interface CellStyle {
        default UIFont cellTitleFont() { return null; };
        default UIColor cellTitleColor() { return null; };
        default UIFont cellSubtitleFont() { return null; };
        default UIColor cellSubtitleColor() { return null; };
    }

   //public interface RefreshViewControllerDelegate {
   //    default void performRefreshWithControl(RefreshControl sender) {}
   //}
}
