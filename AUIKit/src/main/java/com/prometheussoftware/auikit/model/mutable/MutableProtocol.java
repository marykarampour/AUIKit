package com.prometheussoftware.auikit.model.mutable;

import com.prometheussoftware.auikit.callback.ViewControllerCallback;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.ModelProtocol;
import com.prometheussoftware.auikit.uiview.UITextField;
import com.prometheussoftware.auikit.uiview.UITextView;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.uiviewcontroller.ItemsListProtocol;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.utility.DateUtility;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.io.Serializable;
import java.text.AttributedString;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MutableProtocol {

    public enum FIELD_TYPE {
        /** @brief Use for date cells */
        BLANK,
        /** @brief Same as SELECTION without the ability to select or indicator */
        LABEL,
        SELECTION,
        FIELD,
        TITLE_FIELD,
        VERTICAL_FIELD,
        CHECKBOX,
        CHECKBOX_BUTTON,
        CHECKBOX_COMMENT,
        SINGLE_CELL,
        COMMENT,
        STEPPER_FIELD,
        /**
         * @brief Implement and return the array using listItemsForListOfType
         * or declare the property for the list with protocol ArrayPropertyProtocol
         * and include the property name in propertyEnumDictionary
         * and retrun the corresponding section in sectionEnumDictionary.
         */
        LIST
    }

    public interface SaveCallback<E extends Error> {
        void onSuccess(Integer ID);
        void onFailure(E error);
    }

    public interface UpdateCallback<E extends Error> {
        void onSuccess(boolean result);
        void onFailure(E error);
    }

    public interface Field extends UITextView.TextViewDelegate, Serializable, Cloneable {

        /** @brief keys are field types and values are property names */
        Map<Integer, String> propertyEnumDictionary();
        /** @brief keys are field types and values are section types */
        Map<Integer, Integer> sectionEnumDictionary();
        /** @brief keys are section types and values are section titles */
        Map<Integer, String> titleEnumDictionary();
        boolean hasValueForObjectType(int type);
        boolean hasValueForSectionType(int section);
        boolean boolValueForObjectType(int type);
        boolean boolValueForSectionType(int section);
        Date dateValueForObjectType(int type);
        Date dateValueForSectionType(int section);
        DateUtility.FORMAT dateFormatForObjectType(int type);
        DateUtility.FORMAT dateFormatForSectionType(int section);
        StringUtility.TYPE textTypeForObjectType(int type);
        boolean shouldValidateWhenEditingObjectType(int type);
        Number numberValueForObjectType(int type);
        Number numberValueForSectionType(int section);
        Object valueForObjectType(int type);
        Set valuesForSectionType(int section);
        String titleForObjectType(int type);
        String titleForSectionType(int section);
        Set<Integer> typesForSection(int section);
        String stringValueForObjectType(int type);
        String stringValueForSectionType(int section);
        Set objectTypesForSectionType(int section);
        String badgeValueForSectionType(int section);
        /** @brief Default is 2. */
        int floatingDigits();
        /** @brief Uses the same format as dates being formatted by this class. */
        String localDateStringWithDate(Date date);
        /** @brief Uses the same format as dates being formatted by this class for object type. */
        String localDateStringWithDateForObjectType(Date date, int type);
        String localDateStringForObjectType(int type);
        void setValueForObjectType(Object value, int type);
        void setValueForSectionType(Object value, int section);
        void switchBoolValueForObjectType(int type);
        void switchBoolValueForSectionType(int section);
        boolean isLongValueForObjectType(int type);
        boolean isLongValueForSectionType(int section);
        boolean isEditableSectionType(int section);
        boolean isCommentSectionType(int section);
        boolean isUppercaseStringObjectType(int type);
        boolean isEmailSectionType(int section);
        boolean isPhoneSectionType(int section);
        String missingValueErrorMessage();
        String missingObjectErrorMessage();
        /** @brief Set to implement custom updates when a value is updated. Useful in cases custom
         * calculations require a view update. */
        default void setUpdateDelegate(Delegate obj) {}
        default Delegate UpdateDelegate() { return null; }


        /** @brief Returns an array for a property that conforms to protocol MKUArrayPropertyProtocol. */
        default <O extends ViewContentProtocol.Placeholder> List<O> arrayForObjectType (int type) { return null; }
        /** @brief Returns an array for a section corresponding to a property that conforms to protocol MKUArrayPropertyProtocol. */
        default <O extends ViewContentProtocol.Placeholder> List<O> arrayForSectionType (int type) { return null; }
    }

    public interface Mutable {
        String nameForOriginalObject();
        String nameForUpdatedObject();
        Class defaultClassForUpdatedObject();
        Class defaultClassForOriginalObject();
    }

    public interface Update extends Mutable {
        boolean isLongValueForSectionType(int section);
        boolean isEditableSectionType(int section);
        boolean hasValueForSectionType(int section);
        boolean isCommentSectionType(int section);
    }

    public interface Delegate<O extends FieldModel> {
        default void objectDidUpdateObjectType(O obj, Integer type) {}
        default void objectDidUpdateObjectType(O obj, Integer type, UITextField textField, boolean endEditing, IndexPath indexPath) {}
    }

    public interface ViewController <ObjectType extends BaseModel & MutableProtocol.Field, UpdateObjectType extends BaseModel & MutableProtocol.Field, O extends MutableUpdateObject<ObjectType, UpdateObjectType>> extends Delegate, ModelProtocol.Object<O>, ItemsListProtocol.ListVC {

        /**
         * @note Default checks:
         * @code object.OriginalObject.equals(object.UpdatedObject);
         * @endcode
         */
        default boolean canPerformSaveObject() { return !object().OriginalObject.equals(object().UpdatedObject); }
        /**
         * @brief If canPerformSaveObject returns NO, this method is called and save is aborted.
         * Use this method to perform cleanup or show information prompts to user.
         */
        default void handleAbortSaveObject() {};
        /**
         * @note Default checks:
         * @code prepareDataForUpdate();
         * <p>
         * if (performDefaultSavePressedAction()) {
         * dispatchDelegateForSaveDone();
         * return;
         * }
         * <p>
         * performSaveOrUpdateObjectWithCompletion(performSaveOrUpdateObjectCompletionHandler());
         * @endcode
         */
        default void handleSaveObject() {};
        default void updateObjectWithCompletion(UpdateCallback completion) {};
        default void saveObjectWithCompletion(SaveCallback completion) {};
        default boolean canUpdate() { return false; }
        /** @brief Last chance to process data for saving. */
        default void prepareDataForUpdate() {};
        default void didFinishUpdateWithResultID(Number ID) {};
        /**
         * @brief Sets the Save and Reset right bar button items.
         * @note viewControllerContainingNavigationBar must be set before calling this method.
         */
        default void setMutableNavBarItems() {
            //[self addButtonOfType:MKU_NAV_BAR_BUTTON_TYPE_SYSTEM_SAVE position:MKU_NAV_BAR_BUTTON_POSITION_RIGHT];
            //[self addButtonOfType:MKU_NAV_BAR_BUTTON_TYPE_RESET position:MKU_NAV_BAR_BUTTON_POSITION_RIGHT];
        }
        /**
         * @brief This is called in reset, setObject and setUpdatedObject methods, use this to update any single cells like segments, or other needed updates,
         * default does nothing. Call super if you have date cells, it will automatically set the values in date cells.
         */
        default void didResetUpdateObject(UpdateObjectType object) {}
        /**
         * @brief Called every time object is set. Use this as a replacement for overriding setObject: which is implemented in category UIViewController (MKUMutableObjectVC)
         * and should not be overriden.
         */
        default void didSetObject(O object) {}
        /**
         * @brief Only calls dispatchDelegateForSaveDone after end editing. Defalt returns NO.
         */
        default boolean performDefaultSavePressedAction() { return false; }
        default void dispatchDelegateForSaveDone() {}
        default Class classForObject() { return MutableUpdateObject.class; }
        default boolean showSaveSuccessAlert() { return true; }
        default <O extends Field> void updateObjectDidUpdateKey(O object, String key) {}
        /** @brief This is called when save is pressed as the completion of performSaveOrUpdateObjectWithCompletion.
        If nil, performSaveOrUpdateObjectWithCompletion will shows success failure error alerts, otherwise it will perform the completion with no alerts.
         @note Default completion is nil. Set to perform other actions. */
        public UpdateCallback performSaveOrUpdateObjectCompletionHandler();
        /** @brief It calls setObject: */
        public void setUpdatedObject (UpdateObjectType updatedObject);

        /** @brief Set initial isEditable state. Called in initBase. */
        default void initIsEditable() {}
        default boolean isHeaderSection (int section) { return false; };
        default UIView singleCellForRowAtIndexPath (IndexPath indexPath) { return new UIView(); };
        /** @brief Default is based on isEditable. */
        default boolean userInteractionEnabledForSingleCellAtIndexPath (IndexPath indexPath) { return false; };
        default FIELD_TYPE typeForSection (int section) { return FIELD_TYPE.BLANK; };

        /** @brief Implement in case of MKU_MUTABLE_OBJECT_FIELD_TYPE_SELECTION.
        If this returns NO, the accessory indicator will not show, it can be used as a simple subtitle cell.
        One of valueForSection: or subvalueForSection: should be non-empty to add as label.
        Return nil for placeholderTitleForSection: in case of label, and not nil in case of types. */
        default boolean hasTypesForSection (int section) { return false; }
        default boolean hasValueForSection (int section) { return false; }
        /** @brief Return if you want the disclosure indicator be present.
        Default checks if canSelectSection or hasTypesForSection and canTransitionToPresentingSelectionVCInSection
        return YES. */
        default boolean hasAccessoryForSection (int section) { return false; }
        /** @brief Default only handles email and phone calls. */
        default void didSelectSection (int section) {}
        default boolean canEditSection (int section) { return false; }
        /** @brief Return YES if you want to hide this section completely regardlss of other conditions.
        Default hides a section with no value or subvalue when isEditable is NO.
        Used for field and comment sections and headers. */
        boolean hideSection (int section);
        /** @brief Default hides a section with no value or subvalue or attributes or placeholder. */
        boolean shouldHideSelectionSection (int section);
        /** @brief By default it returns YES. Return NO if you want to disable transitions, the accessory indicator wiil not show. The main use case is
        when isEditable is NO and the selection VC is a list. */
        boolean canTransitionToPresentingSelectionVCInSection (int section);
        String titleForSection (int section);
        String subtitleForSection (int section);
        /** @brief Implement in case of MKU_MUTABLE_OBJECT_FIELD_TYPE_SELECTION */
        String placeholderTitleForSection (int section);
        String valueForSection (int section);
        /** @brief If placeholder or title or, subvalue and value are provided, this will be ignored */
        AttributedString attributedValueForSection (int section);
        /** @brief If placeholder or subtitle or, subvalue and value are provided, this will be ignored */
        AttributedString attributedSubvalueForSection (int section);
        /** @brief Used in MKU_MUTABLE_OBJECT_FIELD_TYPE_SELECTION and MKU_MUTABLE_OBJECT_FIELD_TYPE_LABEL. Default is kColonEmptyString */
        String labelDelimiterForSection (int section);
        String sublabelDelimiterForSection (int section);
        /** @brief Implement in case of MKU_MUTABLE_OBJECT_FIELD_TYPE_SELECTION */
        String subvalueForSection (int section);
        boolean boolValueForSection (int section);
        int rowForFieldAtIndexInSection (int index, int section);
        boolean isHiddenFieldAtIndexInSection (int index, int section);
        //StepperValueObject stepperValuesForSection (int section);
        /** @brief If the section contains a button such as in MKU_MUTABLE_OBJECT_FIELD_TYPE_CHECKBOX_BUTTON the action will be performed. */
        default UITargetDelegate.TouchUp actionForSection (int section) { return null; };
        /** @brief Used in MKU_MUTABLE_OBJECT_FIELD_TYPE_CHECKBOX_BUTTON. Default is  MKU_VIEW_POSITION_NONE constrained to sides. */
        UIView.ALIGNMENT checkboxButtonPositionForSection (int section);
        /** @brief Used in MKU_MUTABLE_OBJECT_FIELD_TYPE_CHECKBOX_BUTTON. Default is  48.0. */
        int checkboxButtonHeightForSection (int section);
        /** @brief Used in MKU_MUTABLE_OBJECT_FIELD_TYPE_CHECKBOX_BUTTON. Default is  [self checkboxButtonHeightForSection:section] + 2*VerticalMargin. */
        int checkboxButtonRowHeightForSection (int section);
        /** @brief Used in MKU_MUTABLE_OBJECT_FIELD_TYPE_CHECKBOX_BUTTON. Default is  (self.view.frame.size.width - 3*HorizontalMargin) / 2.0. */
        int checkboxButtonWidthForSection (int section);
        void switchBoolValueAtIndexPath (IndexPath indexPath);
        /** @brief Default is isEditableSectionType of the object. */
        boolean isEditableSectionType (int section);
        /** @brief By default reloads the corresonding section only. */
        void didSwitchBoolValueAtIndexPath (IndexPath indexPath);
        /** @brief It is called in tableView:didSelectRowAtIndexPath: when type is MKU_MUTABLE_OBJECT_FIELD_TYPE_SELECTION.
        By default calls createPresentingSelectionVCAtIndexPath:completion: */
        void handleSelectionAtIndexPath (IndexPath indexPath);
        /** @brief It is called in tableView:didSelectRowAtIndexPath: via handleSelectionAtIndexPath when type is MKU_MUTABLE_OBJECT_FIELD_TYPE_SELECTION.
        By default calls presentSelectionVC:atIndexPath: */
        default void createPresentingSelectionVCAtIndexPath (IndexPath indexPath, ViewControllerCallback completion) {}
        /** @brief It is called in tableView:didSelectRowAtIndexPath: via didSelectListItem:atIndexPath when type is MKU_MUTABLE_OBJECT_FIELD_TYPE_LIST.
        By default calls presentTransitioningViewControllerWithItem:atIndexPath: */

        /** @brief By default does [self.navigationController pushViewController:VC animated:YES]. In case of a container or popover
        for example you can override to provide other actions.*/
        void presentSelectionVCAtIndexPath (UIViewController VC, IndexPath indexPath);
        int heightForStandardSelectionCell();
        int heightForSingleCellRowAtIndexPath (IndexPath indexPath);
        int attributedHeightForRowAtIndexPath (IndexPath indexPath);
        UIImage buttonImageForFieldAtIndexPath (IndexPath indexPath);
        default UITargetDelegate.TouchUp actionForFieldButtonAtIndexPath (IndexPath indexPath) { return null; }
    }

    public interface TransitionDelegate {

        /** @brief Handles transitions, called in presentSelectionVC:atIndexPath.

         @note If transitionVCDelegate = nil, or if shouldHandlePresentSelectionVC:atIndexPath returns NO, it
         performa default transition:
         @code
         [self.navigationController pushViewController:VC animated:YES];
         @endcode
         */
        default void presentSelectionVCAtIndexPath (UIViewController VC, IndexPath indexPath) {};
        default boolean shouldHandlePresentSelectionVCAtIndexPath (UIViewController VC, IndexPath indexPath) { return false; };
    }
}
