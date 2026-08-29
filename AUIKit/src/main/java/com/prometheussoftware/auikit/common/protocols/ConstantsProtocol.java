package com.prometheussoftware.auikit.common.protocols;

import android.util.Size;

import com.prometheussoftware.auikit.common.Dimensions;

public interface ConstantsProtocol {

    //environment and services
    default int Server_Environment() { return 0; }
    default String BaseURL() { return ""; }

    //app
    default String CopyRight_STR() { return ""; }
    String Version_Name_STR();
    long Version_Code_STR();

    //spinner
    default Size Spinner_Hud_Size() { return Dimensions.size(Dimensions.Int_52()); }
    default Size Small_Spinner_Hud_Size() { return Dimensions.size(Dimensions.Int_32()); }
    default Size Spinner_Frame_Size() { return Dimensions.size(Dimensions.Int_80()); }
    default int Spinner_Corner_Radius() { return Dimensions.Int_8(); }

    //nav bar
    default int Nav_Bar_Icon_Height() { return Dimensions.Int_32(); }
    default Size Nav_Bar_Icon_Size() { return Dimensions.size(Nav_Bar_Icon_Height()); }
    default int Nav_Bar_Shadow_Size() { return Dimensions.Int_2(); }
    default int Nav_Bar_Height() { return Dimensions.Int_64(); }
    default int Status_Bar_Height() { return Dimensions.Int_22(); }

    //nav bar
    default int Tab_Bar_Icon_Height() { return Dimensions.Int_32(); }
    default int Tab_Bar_Height() { return Dimensions.Int_64(); }

    //controls
    default int TextField_Height() { return Dimensions.Int_44(); }
    default int TextView_Title_Height() { return Dimensions.Int_44(); }
    default int TextView_Medium_Height() { return Dimensions.Int_120(); }
    default int Max_Value1Cell_Character_Count() { return 16; }
    default int NumericInput_TextField_Width() { return Dimensions.Int_200(); }
    default int Input_TextField_Width() { return Dimensions.Int_200(); }
    default int TableCell_Content_HorizontalMargin() { return Dimensions.Int_16(); }
    default int Control_Corner_Radius() { return 0; }
    default int Group_Corner_Radius() { return 0; }
    default int Border_Width() { return 0; }

    default int MaxTextViewCharacters() { return 400; }
    default int MaxTextViewCharactersLong() { return 1024; }

    //data
    default String File_Provider_Authority() { return null; }
    default int Max_Transition_Bitmap_Size() { return Dimensions.Int_512(); }

    //regex
    default int Max_Regex_Chars() { return Dimensions.Int_64(); }
    String Regex_Password();
    String Regex_Email();
    String Regex_Phone();

    //padding and size
    default int Min_TextView_Height() { return Dimensions.Int_44(); }
    default int Horizontal_Margin() { return Dimensions.Int_8(); }
    default int Vertical_Margin() { return Dimensions.Int_8(); }

    //accessory
    default Size Accessory_Size() { return Dimensions.size(Dimensions.Int_32()); }

    //table view
    default Size TableView_Accessory_Size() { return Dimensions.size(Dimensions.Int_28()); }
    default int Default_Row_Height() { return Dimensions.Int_48(); }
    default int Extended_Row_Height() { return Dimensions.Int_56(); }
    default int Table_Cell_Line_Height() { return Dimensions.Int_22(); }

    default int Table_Section_Header_Height() { return Dimensions.Int_32(); }
    default int Table_Section_Header_Medium_Height() { return Dimensions.Int_44(); }
    default int Table_Section_Header_Short_Height() { return Dimensions.Int_22(); }

    //strings
    String Generic_Success_Message();
    String Generic_Failure_Message();

    String Save_Successful_STR();
    String Update_Failed_Title_STR();
    String Add_New_Item_STR();
    String Delete_Failed_STR();
}
