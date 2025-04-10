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

    //accessory
    default Size Accessory_Size() { return Dimensions.size(Dimensions.Int_32()); }

    //table view
    default Size TableView_Accessory_Size() { return Dimensions.size(Dimensions.Int_28()); }
    default int Default_Row_Height() { return Dimensions.Int_44(); }
    default int TableView_Section_Header_Height() { return 0; }

    //strings
    String Generic_Success_Message();
    String Generic_Failure_Message();
}
