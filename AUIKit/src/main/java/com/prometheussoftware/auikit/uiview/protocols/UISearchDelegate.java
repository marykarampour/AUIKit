package com.prometheussoftware.auikit.uiview.protocols;

public interface UISearchDelegate {

    /** Called when the user submits the query.
     * @return true if handled by the listener, false for default action. */
    default boolean searchBarDidSubmitText(String query) { return false; }

    /** Called when the query text is changed by the user.
     * @return true if handled by the listener, false for default action. */
    default boolean searchBarDidChangeText(String newText) { return false; }

    /** The user is attempting to close the SearchView.
     * @return true if handled by the listener, false for default action. */
    default boolean searchBarCloseButtonPressed() { return false; }

    /** Called when a suggestion was selected by navigating to it.
     * @return true if handled by the listener, false for default action. */
    default boolean searchBarDidSelectSuggestion(int position) { return false; }

    /** Called when a suggestion was clicked.
     * @return true if handled by the listener, false for default action. */
    default boolean searchBarDidPressSuggestion(int position) { return false; }
}