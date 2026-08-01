package com.prometheussoftware.auikit.uiview.protocols;

import android.text.SpannableStringBuilder;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.utility.StringFormatting;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.Set;

public interface ViewContentProtocol {

    interface TitleSubTitle {
        default String title() { return null; };
        default String subtitle() { return null; };
        default SpannableStringBuilder attributedTitle() { return null; };
        default SpannableStringBuilder attributedSubtitle() { return null; };

        default SpannableStringBuilder defaultAttributedTitle() {

            String title = title();
            String subtitle = subtitle();

            if (StringUtility.isEmpty(title) || StringUtility.isEmpty(subtitle)) return null;

            SpannableStringBuilder builder = new SpannableStringBuilder();

            ArrayList<StringFormatting.StringAttributes> attrs = new ArrayList<>();
            attrs.add(new StringFormatting.StringAttributes(
                    title,
                    App.theme().XX_Small_Bold_Font(),
                    App.theme().Black_Blue_Color()));
            attrs.add(new StringFormatting.StringAttributes(
                    subtitle,
                    App.theme().X_Small_Regular_Font(),
                    App.theme().Medium_Silver_Color()));

            SpannableStringBuilder titleBuilder = StringFormatting.attributedText(attrs, "\n");
            builder.append(titleBuilder);
            return builder;
        };
    }

    interface Placeholder extends TitleSubTitle {
        /** @brief The title of the view, section, etc. containing a list of objects of this type */
        default String titleForContainingList() { return null; };
        default String addItemTitle() { return null; };
    }

//    interface Callback <T extends Placeholder> {
//        default T item() { return null; }
//    }

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
